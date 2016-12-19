/*
 * ExpenseFormWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-16 15:35:28
 */
package com.yz.rms.common.model.wrap;

import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.model.ExpenseForm;
import java.util.Date;
import java.io.Serializable;
import java.util.Objects;
/**
 * 报销单列表的封装类
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseFormWrap implements Serializable{
    private static final long serialVersionUID = -6275026591692839455L;
    /**
     * 报销单
     */
    private ExpenseForm expenseForm;
    /**
     * 报销项目
     */
    private String projectName;
    /**
     * 所在团队
     */
    private String teamName;

    public ExpenseForm getExpenseForm() {
        return expenseForm;
    }

    public void setExpenseForm(ExpenseForm expenseForm) {
        this.expenseForm = expenseForm;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final ExpenseFormWrap other = (ExpenseFormWrap) obj;
        if (!Objects.equals(this.expenseForm, other.expenseForm)) {
            return false;
        }
        return true;
    }
    
    
}
