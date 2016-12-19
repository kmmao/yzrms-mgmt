/*
 * StatTeamOrProjectWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 10:46:17
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 团队或项目人员wrap
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class StatTeamOrProjectWrap implements Serializable {

    private static final long serialVersionUID = -137362180317298180L;
    public static final String TOTAL_ID = "TP_AMOUNT";
    public static final String TOTAL_NAME = "合计";
    /**
     * 团队或项目的ID
     */
    private String tpId;

    /**
     * 团队或项目的名称
     */
    private String tpName;

    /**
     * 报销人员list
     */
    private List<StatExpensePersonWrap> personList;

    public String getTpId() {
        return tpId;
    }

    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public List<StatExpensePersonWrap> getPersonList() {
        return personList;
    }

    public void setPersonList(List<StatExpensePersonWrap> personList) {
        this.personList = personList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.tpId);
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
        final StatTeamOrProjectWrap other = (StatTeamOrProjectWrap) obj;
        if (!Objects.equals(this.tpId, other.tpId)) {
            return false;
        }
        return true;
    }

}
