/*
 * USMSUserProxyServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-18 16:28:06
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.usm.client.connector.USMSRPCService;
import com.nazca.usm.client.connector.USMSRPCServiceException;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ProjectConst;
import com.yz.rms.common.rpc.USMSUserProxyService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chenjianan
 */
public class USMSUserProxyServiceImpl implements USMSUserProxyService {

    @Override
    public int getAllUserCount() throws HttpRPCException {
        try {
            return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getAllUserCount();
        } catch (USMSRPCServiceException ex) {
            throw new HttpRPCException(ex.getMessage(), ex.getErrcode(), ex);
        }
    }

    @Override
    public List<USMSUser> getAllUsersInPage(int start, int pageSize) throws HttpRPCException {
        try {
            return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getAllUsersInPage(start,pageSize);
        } catch (USMSRPCServiceException ex) {
            throw new HttpRPCException(ex.getMessage(), ex.getErrcode(), ex);
        }
    }

}
