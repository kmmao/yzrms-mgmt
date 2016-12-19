/*
 * MemberWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 11:43:24
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.Objects;

/**
 * 团队成员的wrap
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class MemberWrap implements Serializable {

    private static final long serialVersionUID = 5777769355575576195L;
    /**
     * 成员ID
     */
    private String memberId;
    /**
     * 成员的名字
     */
    private String memberName;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return memberName.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.memberId);
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
        final MemberWrap other = (MemberWrap) obj;
        if (!Objects.equals(this.memberId, other.memberId)) {
            return false;
        }
        return true;
    }

}
