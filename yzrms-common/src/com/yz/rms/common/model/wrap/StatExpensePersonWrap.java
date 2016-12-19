/*
 * StatExpensePersonWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 10:34:17
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * 人员+金额wrap
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class StatExpensePersonWrap implements Serializable, Comparator<StatExpensePersonWrap> {

    private static final long serialVersionUID = 704522345565864225L;
    public static String TOTAL_ID = "PERSON_AMOUNT";
    public static final String TOTAL_NAME = "合计";
    /**
     * 报销人员ID
     */
    private String pId;

    /**
     * 报销人员名字
     */
    private String pName;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 报销人员的报销金额
     */
    private double money;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String EmployeeNumber) {
        this.employeeNumber = EmployeeNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pId);
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
        final StatExpensePersonWrap other = (StatExpensePersonWrap) obj;
        if (!Objects.equals(this.pId, other.pId)) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(StatExpensePersonWrap o1, StatExpensePersonWrap o2) {
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
