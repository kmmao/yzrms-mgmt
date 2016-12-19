/*
 * AuthAgent.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-12-02 15:28:03
 */
package com.yz.rms.client.agent;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.yz.rms.client.ClientContext;
import com.yz.rms.common.rpc.LoginAuthService;

/**
 * 认证代理
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
public class AuthAgent extends AbstractAgent<Boolean> {

    @Override
    protected Boolean doExecute() throws HttpRPCException {
        LoginAuthService authService = HttpRPC.getService(LoginAuthService.class, ClientContext.getSysServerRPCURL(), true);
        authService.auth(ClientContext.getUser().getId(), ClientContext.getUsmsToken());
        return true;
    }
}
