/*
 * ClientContext.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-09-16 17:09:58
 */
package com.yz.rms.client;

import com.nazca.usm.common.PermissionConst;
import com.nazca.usm.model.USMSUser;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public class ClientContext {

    private static USMSUser user = null;
    private static String password = "";
    private static ClientMainFrame mainFrame = null;
    private static String usmsToken = null;

    public ClientContext() {
    }

    /**
     * 设置主窗口引用
     *
     * @param mainFrame
     */
    public static void setMainFrame(ClientMainFrame mainFrame) {
        ClientContext.mainFrame = mainFrame;
    }

    /**
     * 获取主窗口引用
     *
     * @return
     */
    public static ClientMainFrame getMainFrame() {
        return mainFrame;
    }

    public static URL getSysServerRPCURL() {
        URL url = null;
        try {
            url = new URL(ClientConfig.getSysServerRpcURL());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
    }

    public static URL getUsmsServerRPCURL() {
        URL url = null;
        try {
            url = new URL(ClientConfig.getUsmsServerRpcURL());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
    }

    public static void setUser(USMSUser user) {
        ClientContext.user = user;
    }

    public static USMSUser getUser() {
        return user;
    }

    public static String getUserId() {
        return user != null ? user.getId() : null;
    }

//    public static boolean hasPermission(PermissionConst permissionId) {
//        return user != null && user.hasPermission(ProjectConst.USMS_MODULE_ID, permissionId.name());
//    }

    /**
     * 拥有其中任意一个权限即返回true
     *
     * @param permissions
     * @return
     */
    public static boolean containPermissions(PermissionConst... permissions) {
        if (permissions != null) {
            for (PermissionConst p : permissions) {
//                if (user != null && user.hasPermission(ProjectConst.USMS_MODULE_ID, p.name())) {
//                    return true;
//                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static void setPassword(String password) {
        ClientContext.password = password;
    }

    public static boolean isPasswordCorrect(String password) {
        return ClientContext.password.equals(password);
    }

    public static String getUsmsToken() {
        return usmsToken;
    }

    public static void setUsmsToken(String usmsToken) {
        ClientContext.usmsToken = usmsToken;
    }
}
