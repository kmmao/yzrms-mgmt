/*
 * StatListWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 11:10:12
 */
package com.yz.rms.common.model.wrap;

import com.yz.rms.common.enums.ExpenseItemEnum;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 报销统计list
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class StatItemWrap implements Serializable {

    private static final long serialVersionUID = 2000886374889180702L;

    /**
     * 报销项目
     */
    private ExpenseItemEnum item;

    /**
     * 报销团队list或报销项目list
     */
    private List<StatTeamOrProjectWrap> tpList;

    public ExpenseItemEnum getItem() {
        return item;
    }

    public void setItem(ExpenseItemEnum item) {
        this.item = item;
    }

    public List<StatTeamOrProjectWrap> getTpList() {
        return tpList;
    }

    public void setTpList(List<StatTeamOrProjectWrap> tpList) {
        this.tpList = tpList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.item);
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
        final StatItemWrap other = (StatItemWrap) obj;
        if (this.item != other.item) {
            return false;
        }
        return true;
    }

}
