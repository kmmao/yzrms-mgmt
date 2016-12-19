/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yz.rms.server.util;

import com.nazca.usm.client.connector.USMSRPCService;
import com.nazca.usm.client.connector.USMSRPCServiceException;
import com.nazca.usm.model.USMSOrganization;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ProjectConst;
import java.util.List;

/**
 * usms工具类
 *
 * @author chen jianan
 */
public class USMSServiceUtils {
   
    public static USMSUser getUserBasicInfoById(String userID) throws USMSRPCServiceException {
        return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getUserByIdAndProperties(userID, USMSUser.PropertyType.ORG_INFO);
    }
    
   public static USMSUser getUserInfoByLoginName(String loginName) throws USMSRPCServiceException {
        return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getUserByLoginName(loginName);
    }

    public static void setUserEnable(String loginName, boolean enable) throws USMSRPCServiceException {
        USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).setUserEnable(loginName, enable);
    }

    public static USMSOrganization getOrgByOrgNumber(String orgNum) throws USMSRPCServiceException {
        return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getOrgByOrgNumber(orgNum);
    }

    public static List<USMSUser> getUsersByOrgId(String orgId) throws USMSRPCServiceException {
        return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getUserByOrg(orgId);
    }
    
    public static List<USMSUser> getAllUsers() throws USMSRPCServiceException {
        return USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getAllUsersInPage(0, 100);
    }

}
