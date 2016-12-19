/*
 * ErrorCodeUtil.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-03-24 15:52:24
 */
package com.yz.rms.common.util;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public final class ErrorCodeFormater {

    private ErrorCodeFormater() {
    }

    private static final String MAP_FILE = "com.yz.rms.common.consts.ErrorCode";
    private static DecimalFormat df = new DecimalFormat("000000");
    private static ResourceBundle errorBundle = null;

    static {
        errorBundle = ResourceBundle.getBundle(MAP_FILE);
    }

    /**
     * 解释错误代码，返回错误释义
     *
     * @param errorCode
     * @return
     */
    public static String explainErrorCode(int errorCode) {
        try {
            String codeString = df.format(errorCode);
            return errorBundle.getString(codeString);
        } catch (Exception ex) {
            return errorBundle.getString("unknown");
        }
    }

    /**
     * 格式化错误代码，直动添加错误释义
     *
     * @param errorCode
     * @return
     */
    public static String formate(int errorCode) {
        return formate(explainErrorCode(errorCode), errorCode);
    }

    /**
     * 格式化错误代码和错误释义
     *
     * @param msg
     * @param code
     * @return 返回结果样例为：用户名或密码错误(#000010)
     */
    public static String formate(String msg, int code) {
        if (code >= 0) {
            String codeString = df.format(code);
            return msg + "(#" + codeString + ")";
        } else {
            return msg + "(#" + Integer.toHexString(code) + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println(explainErrorCode(252001));
    }
}
