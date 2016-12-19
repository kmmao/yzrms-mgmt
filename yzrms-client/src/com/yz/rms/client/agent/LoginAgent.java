/*
 * LoginAgent.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-03-26 16:33:09
 */
package com.yz.rms.client.agent;

import com.nazca.io.httpdao.HttpClientContext;
import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.usm.client.ConfigManager;
import com.nazca.usm.client.ServiceConfig;
import com.nazca.usm.client.UsmsClientContext;
import com.nazca.usm.client.service.async.agent.ClientClockAgent;
import com.nazca.usm.common.LoginException;
import com.nazca.usm.common.LoginResult;
import com.nazca.usm.model.USMSUser;
import com.nazca.usm.service.rpc.LoginRPCService;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.consts.ErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 登录代理
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public class LoginAgent extends AbstractAgent<USMSUser> {

    private Log log = LogFactory.getLog(LoginAgent.class);
    private String loginName;
    private String password;
    private static final int SYNC_USER_PAGE_SIZE = 2000;
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected USMSUser doExecute() throws HttpRPCException {
        if (FakeDataFactory.isFake()) {
            USMSUser user = new USMSUser("sys");
            user.setName("管理员");
            new TimeFairy().sleepIfNecessary();
            return user;
        }

        log.info(ClientContext.getUsmsServerRPCURL());
        try {
            LoginRPCService usmLoginServ = HttpRPC.getService(
                    LoginRPCService.class, ClientContext.getUsmsServerRPCURL(), true);
            LoginResult result = usmLoginServ.login(loginName, password);
            USMSUser usmUser = result.getUser();
            if (usmUser.getRoleSet().size() == 0 && usmUser.getPermissionSet().size() == 0) {
                throw new HttpRPCException("您没有任何权限，不能登录", ErrorCode.LACK_OF_ROLES);
            }
            HttpClientContext usmContext = HttpRPC.getClientContext(ClientContext.getUsmsServerRPCURL());
            USMSessionKeeperAgent.getAgent().startKeepSession();
            UsmsClientContext.setUsmsServerAddr(ClientContext.getUsmsServerRPCURL());
            UsmsClientContext.setCurrUser(usmUser);
            UsmsClientContext.setCurOrg(usmUser.getOrg());
            ConfigManager.putConfig(ConfigManager.KEY_SELF_MGMT, ConfigManager.VALUE_TRUE);
            ConfigManager.putConfig(ConfigManager.KEY_HAS_ORG, ConfigManager.VALUE_TRUE);
            ClientClockAgent.getInstance().startSyncTime();
            ServiceConfig.setUsmsServerURL(ClientContext.getUsmsServerRPCURL());
            
            ClientContext.setUser(usmUser);
            ClientContext.setPassword(password);
            ClientContext.setUsmsToken(usmContext.getUserToken());
            log.info("-----usm token = " + usmContext.getUserToken());
            return usmUser;
        } catch (LoginException ex) {
            ex.printStackTrace();
            throw new HttpRPCException(ex.getMessage(), ex.getCode() + ErrorCode.USMS_ERROR_CODE_START);
        }
    }
}
