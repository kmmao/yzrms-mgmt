/*
 * ExpenseTableModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-16 15:51:21
 */
package com.yz.rms.client.model.expenseform;

import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.client.util.USMSUserSyncTool;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import java.text.SimpleDateFormat;

/**
 * 报销单列表Model类
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseTableModel extends AbstractSimpleObjectTableModel<ExpenseFormWrap> {

    public static final int EXPENSETIME = 0;
    public static final int EXPENSETOTAL = 1;
    public static final int EXPENSENAME = 2;
    public static final int PROJECTNAME = 3;
    public static final int TEAMNAME = 4;
    public static final int STATE = 5;    
    private static final String[] CLOS = new String[]{"报销时间", "报销金额（元）", "报销人员", "报销项目", "所在团队", "报销单状态"};

    public ExpenseTableModel() {
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
            case TEAMNAME:
                return data.getTeamName();
            case STATE:
		if(!data.getExpenseForm().isPassState()&&(data.getExpenseForm().getState().equals(ExpenseFormStateEnums.waitAuditForPm)||data.getExpenseForm().getState().equals(ExpenseFormStateEnums.waitAuditForHr)||data.getExpenseForm().getState().equals(ExpenseFormStateEnums.waitAuditForCeo))){
		return ExpenseFormStateEnums.deny;
		}
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
