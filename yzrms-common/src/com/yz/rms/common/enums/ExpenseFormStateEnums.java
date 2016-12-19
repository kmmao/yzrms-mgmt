/*
 * ExpenseFormEnums.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:30:08
 */
package com.yz.rms.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 报销单状态枚举类
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public enum ExpenseFormStateEnums {

    newForm() {
	@Override
	public String toString() {
	    return "新建";
	}

    },
    waitAuditForPm() {
	@Override
	public String toString() {
	    return "待审核";
	}

    },
    waitAuditForCeo() {
	@Override
	public String toString() {
	    return "待总经理审核";
	}

    },
    waitAuditForHr() {
	@Override
	public String toString() {
	    return "待行政审核";
	}

    },
    waitPay() {
	@Override
	public String toString() {
	    return "待发放";
	}

    },
    paid() {
	@Override
	public String toString() {
	    return "已发放";
	}

    },
     deny() {
	@Override
	public String toString() {
	    return "未通过";
	}

    };
    /**
     * 主管角色的状态
     * @return 
     */
    public static List<ExpenseFormStateEnums> getStateEnums5PM(){
        List<ExpenseFormStateEnums> list = new ArrayList<>();
        list.add(waitAuditForPm);
        list.add(waitAuditForCeo);
        list.add(waitAuditForHr);
        list.add(waitPay);
        list.add(paid);
        return list;
    };
    /**
     * 总经理角色的状态
     * @return 
     */
    public static List<ExpenseFormStateEnums> getStateEnums4CEO(){
        List<ExpenseFormStateEnums> list = new ArrayList<>();
        list.add(waitAuditForPm);
        list.add(waitAuditForHr);
        list.add(waitPay);
        list.add(paid);
        return list;
    };
    /**
     * 行政普通员工角色的状态
     * @return 
     */
    public static List<ExpenseFormStateEnums> getStateEnums3HR(){
        List<ExpenseFormStateEnums> list = new ArrayList<>();
        list.add(waitAuditForHr);
        list.add(waitPay);
        list.add(paid);
        return list;
    };

}
