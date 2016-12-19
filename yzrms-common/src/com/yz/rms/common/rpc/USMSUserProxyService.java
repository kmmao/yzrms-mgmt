/*
 * USMSUserService.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-18 16:18:41
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;
import com.nazca.usm.model.USMSUser;
import java.util.List;

/**
 * usms用户接口
 *
 * @author chenjianan
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING,
        identifier = "com.yz.rms.server.rpcimpl.USMSUserProxyServiceImpl")
public interface USMSUserProxyService {

    int getAllUserCount() throws HttpRPCException;

    List<USMSUser> getAllUsersInPage(int start, int pageSize) throws HttpRPCException;
}
