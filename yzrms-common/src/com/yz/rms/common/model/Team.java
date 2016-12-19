/*
 * Team.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:36:42
 */
package com.yz.rms.common.model;

import com.yz.rms.common.enums.RecordState;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 团队基础类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class Team implements Serializable {

    private static final long serialVersionUID = -2867382977209021884L;
    /**
     * 管理团队
     */
    public static final String MANAGER_TEAM = "managerTeam";
    /**
     * 管理团队名
     */
    public static final String TEAM_NAME = "管理团队";
    /**
     * 团队ID
     */
    private String teamId;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 删除转态
     */
    private RecordState deleteState;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 最近修改时间
     */
    private Date modifyTime;
    /**
     * 最近修改人
     */
    private String modifier;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public RecordState getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(RecordState deleteState) {
        this.deleteState = deleteState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.teamId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Team other = (Team) obj;
        if (!Objects.equals(this.teamId, other.teamId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return teamName; //To change body of generated methods, choose Tools | Templates.
    }

}
