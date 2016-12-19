/*
 * USMSUserTool.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-16 10:58:49
 */
package com.yz.rms.client.util;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.usm.client.connector.USMSRPCService;
import com.nazca.usm.model.USMSUser;
import com.nazca.usm.service.rpc.LoginRPCService;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.common.rpc.USMSUserProxyService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * usms用户同步
 *
 * @author chenjianan
 */
public class USMSUserSyncTool {

    private final Log log = LogFactory.getLog(USMSUserSyncTool.class);
    private static final USMSUserSyncTool instance = new USMSUserSyncTool();

    /**
     * 缓存usms用户信息，只缓存id和用户名
     */
    private static Map<String, USMSUser> userMap = null;
    private static final int SYNC_USER_PAGE_SIZE = 200;

    public synchronized USMSUser getUserById(String userId) {
        if (userMap == null) {
            syncUsers();
        }
        USMSUser user = userMap.get(userId);
        if (user == null) {
            user = new USMSUser();
            user.setId(userId);
        }
        return user;
    }

    public synchronized String getUserNameById(String userId) {
        USMSUser user = getUserById(userId);
        return user.getName();
    }

    private USMSUserSyncTool() {
    }

    public static USMSUserSyncTool getInstance() {
        return instance;
    }

    /**
     * 休眠间隔：1000 ms
     */
    private static final int SLEEP_INTERVAL = 1000;
    /**
     * 监测间隔：5分钟
     */
    private volatile int sleepSec = 300;
    private volatile int countUp = sleepSec;
    private volatile boolean running = true;
    private SyncThread archiveThread = null;

    public synchronized void start() {
        if (archiveThread == null) {
            running = true;
            archiveThread = new SyncThread();
            archiveThread.start();
        }
    }

    public synchronized void stop() {
        if (archiveThread != null && archiveThread.isAlive()) {
            running = false;
            archiveThread = null;
        }
    }

    private synchronized void doSomething() {
        syncUsers();
    }

    private class SyncThread extends Thread {

        public SyncThread() {
            super("Sync-Thread");
        }

        @Override
        public void run() {
            log.info("*******同步线程已经启动*******");
            while (running) {
                if (countUp >= 0 && (countUp - sleepSec >= 0)) {
                    countUp = 0;
                    doSomething();
                }
                new TimeFairy(SLEEP_INTERVAL).sleepIfNecessary();
                countUp++;
            }
            log.info("=======同步线程已经停止=======");
        }
    };

    private static void syncUsers() {
        try {
            // 获取全部数量，分页获取用户，放到map中
            USMSUserProxyService usmsUserProxyService = HttpRPC.getService(
                    USMSUserProxyService.class, ClientContext.getSysServerRPCURL());
            int totalCount = usmsUserProxyService.getAllUserCount();
            int start = 0;
            Map<String, USMSUser> map = new HashMap<String, USMSUser>();
            if (totalCount > 0) {
                while (totalCount - start > 0) {
                    List<USMSUser> userList = usmsUserProxyService.getAllUsersInPage(start, SYNC_USER_PAGE_SIZE);
                    start += SYNC_USER_PAGE_SIZE;
                    if (userList == null) {
                        break;
                    }
                    for (USMSUser user : userList) {
                        USMSUser trunkedUser = new USMSUser();
                        trunkedUser.setId(user.getId());
                        trunkedUser.setName(user.getName());
                        trunkedUser.setLoginName(user.getLoginName());
                        map.put(user.getId(), trunkedUser);
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
