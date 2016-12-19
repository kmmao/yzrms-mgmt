/*
 * TeamMemberWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-10 17:32:09
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * 团队成员的封装类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class TeamMemberWrap implements Serializable, Comparator<TeamMemberWrap> {

    private static final long serialVersionUID = -6830225555947407256L;
    /**
     * 成员ID
     */
    private String memberId;

    /**
     * 团队ID
     */
    private String teamId;

    /**
     * 团队成员工号
     */
    private String employeeNumber;
    /**
     * 团队成员名称
     */
    private String employeeName;
    /**
     * 成员职位
     */
    private String position;
    /**
     * 成员角色
     */
    private boolean authority;
    /**
     * 成员电话
     */
    private String phone;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isAuthority() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.memberId);
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
        final TeamMemberWrap other = (TeamMemberWrap) obj;
        if (!Objects.equals(this.memberId, other.memberId)) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(TeamMemberWrap o1, TeamMemberWrap o2) {
        String employeeNumber1 = o1.getEmployeeNumber();
        String employeeNumber2 = o2.getEmployeeNumber();
        //对工号字段进行升序，如果欲降序可采用before方法
        if (employeeNumber1.compareTo(employeeNumber2) > 0) {
            return 1;
        } else if (employeeNumber1.compareTo(employeeNumber2) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
