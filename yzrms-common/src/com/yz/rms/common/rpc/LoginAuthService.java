/*
 * LoginAuthService.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-07-22 10:17:32
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;

/**
 * 登录验证服务接口
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING, identifier = "com.yz.rms.server.rpcimpl.LoginAuthServiceImpl")
public interface LoginAuthService {

    void auth(String userId, String token) throws HttpRPCException;

    void logout() throws HttpRPCException;
}
