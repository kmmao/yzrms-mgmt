/*
 * ExpenseFormStateCountWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 11:45:51
 */
package com.yz.rms.common.model.wrap;

import com.yz.rms.common.enums.ExpenseFormStateEnums;
import java.io.Serializable;
import java.util.Objects;

/**
 * 状态未审核数量封装类
 *
 * @author chenjianan
 */
public class ExpenseFormStateCountWrap implements Serializable {

    private static final long serialVersionUID = -1077186731494389587L;
    private ExpenseFormStateEnums state;
    private int count;

    public ExpenseFormStateEnums getState() {
        return state;
    }

    public void setState(ExpenseFormStateEnums state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.state);
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
        final ExpenseFormStateCountWrap other = (ExpenseFormStateCountWrap) obj;
        if (this.state != other.state) {
            return false;
        }
        return true;
    }
    
    

}
