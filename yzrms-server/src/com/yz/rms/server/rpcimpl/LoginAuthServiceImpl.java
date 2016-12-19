/*
 * LoginAuthServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-07-22 10:30:35
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httpdao.HttpSessionTokenManager;
import com.nazca.io.httpdao.Token;
import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCInjection;
import com.nazca.io.httprpc.HttpRPCTokenVerifier;
import com.nazca.usm.client.connector.USMSRPCService;
import com.nazca.usm.common.SessionConst;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.consts.ProjectConst;
import com.yz.rms.common.rpc.LoginAuthService;
import com.yz.rms.common.util.ErrorCodeFormater;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 登录验证服务实现类
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
public class LoginAuthServiceImpl implements LoginAuthService {
    private static Log log = LogFactory.getLog(LoginAuthServiceImpl.class);

    @HttpRPCInjection
    private HttpSession session;

    /**
     * 登录认证
     *
     * @param userId
     * @param usmTokenId
     * @throws HttpRPCException
     */
    @Override
    public void auth(String userId, String usmTokenId) throws HttpRPCException {
        HttpRPCTokenVerifier verifier = null;
        try {
            verifier = HttpRPC.getService(HttpRPCTokenVerifier.class, new URL(USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getConfig().getUsmsServerAddr()));
        } catch (MalformedURLException ex) {
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.SERVER_ERROR), ErrorCode.SERVER_ERROR);
        }
        //用户通行证
        Token usmToken = verifier.verifyToken(usmTokenId);
        if (usmToken == null) {
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.LACK_OF_AUTH), ErrorCode.LACK_OF_AUTH);
        } else {
            //判断该用户登录认证
            if (!userId.equals(usmToken.get(SessionConst.KEY_USER_ID))) {
                throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.LACK_OF_AUTH), ErrorCode.LACK_OF_AUTH);
            }
            //缓存token,若接口上方有“@sessionToken”,只有缓存token才能调用该接口
            Token compToken = new Token();
            compToken.put(SessionConst.KEY_USER_ID, usmToken.get(SessionConst.KEY_USER_ID));
            compToken.put(SessionConst.KEY_USER_LOGINNAME, usmToken.get(SessionConst.KEY_USER_LOGINNAME));
            compToken.put(SessionConst.KEY_USER_NAME, usmToken.get(SessionConst.KEY_USER_NAME));
            //session缓存
            session.setAttribute(SessionConst.KEY_USER_ID, usmToken.get(SessionConst.KEY_USER_ID));
            session.setAttribute(SessionConst.KEY_USER_LOGINNAME, usmToken.get(SessionConst.KEY_USER_LOGINNAME));
            session.setAttribute(SessionConst.KEY_USER_NAME, usmToken.get(SessionConst.KEY_USER_NAME));
            //把token缓存到rpc，为了判断usm服务端、客户端、服务端的token是否一样
            HttpSessionTokenManager.getManager().giveToken(session, compToken);
        }
    }

    /**
     * 注销
     *
     * @throws HttpRPCException
     */
    @Override
    public void logout() throws HttpRPCException {
        //清空session
        HttpSessionTokenManager.getManager().confiscateToken(session);
        session.invalidate();
    }

}
