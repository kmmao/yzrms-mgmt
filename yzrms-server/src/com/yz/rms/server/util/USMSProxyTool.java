/*
 * Copyright(c) 2007-2011 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2011-06-15 16:23:27
 */
package com.yz.rms.server.util;

import com.nazca.usm.client.connector.USMSRPCService;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ProjectConst;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fred
 */
public class USMSProxyTool {

    private static Map<String, USMSUser> userMap = null;
    private static final long SYNC_INTERVAL = 60 * 60 * 1000;
    private static long lastSyncTime = -1;
    private static final int SYNC_USER_PAGE_SIZE = 2000;

    public synchronized static USMSUser getWholeUser(USMSUser user) {
        return user != null ? getUserById(user.getId()) : null;
    }

    public synchronized static List<USMSUser> getAllUsers() {
        List<USMSUser> result = new ArrayList<USMSUser>();
        if (userMap == null) {
            syncUsers();
        } else if (System.currentTimeMillis() - lastSyncTime > SYNC_INTERVAL) {
            syncUsers();
            lastSyncTime = System.currentTimeMillis();
        }

        if (userMap != null) {
            result.addAll(userMap.values());
        }

        return result;
    }

    public synchronized static USMSUser getUserById(String userId) {
        if (userMap == null) {
            syncUsers();
        } else if (System.currentTimeMillis() - lastSyncTime > SYNC_INTERVAL) {
            syncUsers();
            lastSyncTime = System.currentTimeMillis();
        }
        USMSUser user = userMap.get(userId);
        if (user == null) {
            user = new USMSUser();
            user.setId(userId);
        }
        return user;
    }

    private static void syncUsers() {
        try {
            // 获取全部数量，分页获取用户，放到map中
            int totalCount = USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).getAllUserCount();
            int start = 0;
            Map<String, USMSUser> map = new HashMap<String, USMSUser>();
            if (totalCount > 0) {
                while (totalCount - start > 0) {
                    List<USMSUser> userList = USMSRPCService.getInstance(ProjectConst.USMS_MODULE_ID).
                            getAllUsersInPage(start,
                                    SYNC_USER_PAGE_SIZE);
                    start += SYNC_USER_PAGE_SIZE;

                    if (userList != null) {
                        for (USMSUser user : userList) {
                            USMSUser trunkedUser = new USMSUser();
                            trunkedUser.setId(user.getId());
                            trunkedUser.setName(user.getName());
                            trunkedUser.setLoginName(user.getLoginName());
                            map.put(user.getId(), trunkedUser);
                        }
                    }
                }
            }
            userMap = map;
        } catch (Exception ex) {
            ex.printStackTrace();
            userMap = new HashMap<String, USMSUser>();
        }
    }
}
