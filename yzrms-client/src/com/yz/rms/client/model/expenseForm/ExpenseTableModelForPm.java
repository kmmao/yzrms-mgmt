/*
 * ExpenseTableModelForPm.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-09-01 18:42:53
 */
package com.yz.rms.client.model.expenseform;

import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.client.util.USMSUserSyncTool;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import java.text.SimpleDateFormat;

/**
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseTableModelForPm extends AbstractSimpleObjectTableModel<ExpenseFormWrap>{
    
    public static final int EXPENSETIME = 0;
    public static final int EXPENSETOTAL = 1;
    public static final int EXPENSENAME = 2;
    public static final int PROJECTNAME = 3;
    public static final int STATE = 4;    
    private static final String[] CLOS = new String[]{"报销时间", "报销金额（元）", "报销人员", "报销项目", "报销单状态"};


    public ExpenseTableModelForPm() {
        super(CLOS);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseFormWrap data = dataList.get(rowIndex);
        switch (columnIndex) {
            case EXPENSETIME:
                return new SimpleDateFormat("yyyy-MM-dd").format(data.getExpenseForm().getExpenseTime());
            case EXPENSETOTAL:
                return data.getExpenseForm().getExpenseTotal();
            case EXPENSENAME:
                USMSUser user = gerUserNameById(data.getExpenseForm().getExpensePersonId());
                return user.getName();
            case PROJECTNAME:
                return data.getProjectName();
            case STATE:
                return data.getExpenseForm().getState();
            default:
                return "";
        }
    }

    public USMSUser gerUserNameById(String userId) {
        USMSUser user = USMSUserSyncTool.getInstance().getUserById(userId);
        return user;
    }
}
