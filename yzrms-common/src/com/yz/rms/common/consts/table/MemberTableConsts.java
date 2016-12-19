/*
 * MemberTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 11:49:31
 */
package com.yz.rms.common.consts.table;

/**
 * 团队成员表枚举类
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public enum MemberTableConsts {
    TABLE_NAME("member", 0),
    /**
     * 成员ID
     */
    FIELD_MENBER_ID("member_id", 64),
    /**
     * 团队ID
     */
    FIELD_TEAM_ID("team_id", 64),
    /**
     * 是否负责人
     */
    FIELD_LEADER("leader", 0);
    private String name;
    private int length;

    private MemberTableConsts(String name, int length) {
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
