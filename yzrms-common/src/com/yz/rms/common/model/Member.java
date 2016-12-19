/*
 * Member.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:50:07
 */
package com.yz.rms.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 团队成员基础类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class Member implements Serializable {

    private static final long serialVersionUID = -8434743351894192300L;
    /**
     * 成员ID
     */
    private String memberId;
    /**
     * 团队ID
     */
    private String teamId;
    /**
     * 是否负责人
     */
    private boolean leader;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.memberId);
        hash = 47 * hash + Objects.hashCode(this.teamId);
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
        final Member other = (Member) obj;
        if (!Objects.equals(this.memberId, other.memberId)) {
            return false;
        }
        if (!Objects.equals(this.teamId, other.teamId)) {
            return false;
        }
        return true;
    }

}
