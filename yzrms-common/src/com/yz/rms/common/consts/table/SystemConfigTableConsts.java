/*
 * SystemConfigTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 12:12:54
 */
package com.yz.rms.common.consts.table;

/**
 * 系统配置表枚举类
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public enum SystemConfigTableConsts {
    TABLE_NAME("system_config", 0),
    /**
     * 属性ID
     */
    FIELD_CONFIG_ID("config_id", 64),
    /**
     * 属性值
     */
    FIELD_CONFIG_VALUE("config_value", 8);
    private String name;
    private int length;

    private SystemConfigTableConsts(String name, int length) {
        this.name = name;
        this.length = length;
    }
    
    public String getName() {
        return name;
    }
    public int getLength() {
        return length;
    }
}
