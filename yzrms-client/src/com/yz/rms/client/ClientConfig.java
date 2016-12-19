/*
 * ClientConfig.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-09-16 13:56:19
 */
package com.yz.rms.client;

import com.nazca.util.PropertyTool;
import com.nazca.util.StringUtil;
import com.yz.rms.common.consts.ProjectConst;
import java.io.File;
import java.util.Properties;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public class ClientConfig {
    public static final String CONFIG_FILE_NAME = "sys_config.cfg";
    public static final String HTTPRPC_USM_SERVER = "http://172.16.100.83:8090/usms-web/rpc";
    public static final String HTTPRPC_SYS_SERVER = "http://172.16.100.42:8080/yzrms-server/rpc";
    public static final String KEY_USER_NAME = "loginName";
    public static final String KEY_USER_PWD = "password";
    public static final String VAL_AUTOLOGIN_TRUE = "true";
    public static final String VAL_AUTOLOGIN_FALSE = "false";
    
    /**
     * 用于检测新版本的SVN信息
     */
    public static final String INSTALL_SVN_OA = "http://172.16.100.42.8080/svn/yzrmsclient";
    public static final String INSTALL_SVN_UPDATE = "http://172.16.100.42.8080/svn/yzrmsupdate";
    public static final String INSTALL_USERNAME = "admin";
    public static final String INSTALL_PWD = "admin#123";
    /**
     * 用于检测新版本的客户端和更新器唯一标识
     */
    public static final String OA_COMP_ID = "OA_COMP_ID";
    public static final String UPD_COMP_ID = "UPD_COMP_ID";
    
    private static final String MY_APP = "sys.client.myapp=*(lkgf904523f@#$*(*%^#ds*^";
    private static Properties prop = new Properties();
    private static String usmsServerRpcURL;
    private static String sysServerRpcURL;
    public static String HTTPRPC_USM_SERVER_LIST = "http://172.16.100.83:8090/usms-web/rpc";
    public static String HTTPRPC_SYS_SERVER_LIST = "http://172.16.100.42:8080/yzrms-server/rpc;"
            + "http://localhost:8080/yzrms-server/rpc";

    static {
        try {
            prop = PropertyTool.loadProperty(new File(ProjectConst.CONFIG_DIR_PATH),
                    ProjectConst.CLIENT_PRJ_ID, CONFIG_FILE_NAME);
        } catch (Exception ex) {
            try {
                Properties p = new Properties();
                p.put(KEY_USER_NAME, "");
                PropertyTool.saveProperty(p, new File(ProjectConst.CONFIG_DIR_PATH), 
                        ProjectConst.CLIENT_PRJ_ID, CONFIG_FILE_NAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    private static void saveConfig() {
        try {
            PropertyTool.saveProperty(prop, new File(ProjectConst.CONFIG_DIR_PATH),
                    ProjectConst.CLIENT_PRJ_ID, CONFIG_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserId(String userId) {
        prop.setProperty(KEY_USER_NAME, userId);
        prop.remove(KEY_USER_PWD);
        saveConfig();
    }

    public static void saveUserIdAndPassword(String userId, String password) {
        prop.setProperty(KEY_USER_NAME, userId);
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(MY_APP);
        String myEncryptedText = textEncryptor.encrypt(password);
        prop.setProperty(KEY_USER_PWD, myEncryptedText);
        saveConfig();
    }

    public static void setPassword(String password) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(MY_APP);
        String myEncryptedText = textEncryptor.encrypt(password);
        prop.setProperty(KEY_USER_PWD, myEncryptedText);
        saveConfig();
    }

    public static void removeUserIdAndPassword() {
        prop.remove(KEY_USER_NAME);
        prop.remove(KEY_USER_PWD);
        saveConfig();
    }

    public static String getUserId() {
        return prop.getProperty(KEY_USER_NAME);
    }

    public static String getPassword() {
        String encryptedPass = prop.getProperty(KEY_USER_PWD);
        if (StringUtil.isEmpty(encryptedPass)) {
            return "";
        } else {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(MY_APP);
            return textEncryptor.decrypt(encryptedPass);
        }
    }

    public static String getUsmsServerRpcURL() {
        return usmsServerRpcURL;
    }

    public static void setUsmsServerRpcURL(String usmsServerRpcURL) {
        ClientConfig.usmsServerRpcURL = usmsServerRpcURL;
    }

    public static String getSysServerRpcURL() {
        return sysServerRpcURL;
    }

    public static void setSysServerRpcURL(String sysServerRpcURL) {
        ClientConfig.sysServerRpcURL = sysServerRpcURL;
    }
}
