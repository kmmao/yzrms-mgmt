/*
 * TeamTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 11:38:20
 */
package com.yz.rms.common.consts.table;

/**
 * 团队表枚举类
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public enum TeamTableConsts {
    TABLE_NAME("team", 0),
    /**
     * 团队ID
     */
    FIELD_TEAM_ID("team_id", 64),
    /**
     * 团队名称
     */
    FIELD_TEAM_NAME("team_name", 64),
    /**
     * 删除状态
     */
    FIELD_DELETE_STATE("delete_state", 16),
    /**
     * 创建时间
     */
    FIELD_CREATE_TIME("create_time", 0),
    /**
     * 创建人
     */
    FIELD_CREATOR("creator", 64),
    /**
     * 最近修改时间
     */
    FIELD_MODIFY_TIME("modify_time", 0),
    /**
     * 最近修改人
     */
    FIELD_MODIFIER("modifier", 64);
    private String name;
    private int length;

    private TeamTableConsts(String name, int length) {
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
