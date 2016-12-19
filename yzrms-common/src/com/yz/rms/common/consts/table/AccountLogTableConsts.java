/*
 * AccountLogTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-07-04 13:45:56
 */
package com.yz.rms.common.consts.table;

/**
 *
 * @author chenjianan
 */
public enum AccountLogTableConsts {
    TABLE_NAME("account_log", 0),
    /**
     * ID 唯一标识
     */
    FIELD_ID("id", 64),
    /**
     * 登录用户id
     */
    FIELD_LOGIN_USER_ID("login_user_id", 64),
    /**
     * 登录用户所属企业号
     */
    FIELD_COMPANY_NUM("company_num", 6),
    /**
     * 登录ip
     */
    FIELD_LOGIN_IP("login_ip", 15),
    /**
     * 日志类型
     */
    FIELD_ACTION_TYPE("action_type", 64),
//    /**
//     * 模块id
//     */
//    FIELD_MODULE_ID("module_id", 64),
//    /**
//     * 子模块id
//     */
//    FIELD_SUB_MODULE_ID("sub_module_id", 64),
    /**
     * 操作时间
     */
    FIELD_OPERATE_TIME("operate_time", 0),
    /**
     * 描述
     */
    FIELD_DESCRIPTION("description", 4000);

    private String name;
    private int length;

    AccountLogTableConsts(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getFieldName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}
