/*
 * WaitPayStatWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 12:14:49
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.Objects;

/**
 * 待发放统计
 *
 * @author chenjianan
 */
public class WaitPayStatWrap implements Serializable {

    private static final long serialVersionUID = -7437095414185587351L;
    private int month;
    private int count;
    private Double amount;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.month);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WaitPayStatWrap other = (WaitPayStatWrap) obj;
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        return true;
    }

    

}
