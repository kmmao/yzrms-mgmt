/*
 * ProjectTableModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-16 14:33:22
 */
package com.yz.rms.client.model.project;

import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import java.text.SimpleDateFormat;

/**
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class ProjectTableModel extends AbstractSimpleObjectTableModel<Project> {

    public static final int STATE = 0;
    public static final int STARTTIME = 1;
    public static final int ENDTIME = 2;
    public static final int PROJECTNAME = 3;
    public static final int CUSTOMER = 4;
    public static final int AMOUNT = 5;
    private static final String[] COLS = new String[]{"当前状态", "合同签订时间", "合同结束时间", "项目名称", "甲方", "金额"};

    public ProjectTableModel() {
	super(COLS);
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
	Project pw = dataList.get(rowIndex);
	switch (colIndex) {
	    case STATE:
		if (pw != null) {
		    if (pw.getState() == ProjectEnums.expired) {
			return "已验收";
		    } else if (pw.getState() == ProjectEnums.underway) {
			return "进行中";
		    } else {
			return "全部";
		    }
		}
	    case STARTTIME:
		return pw.getStartTime() != null ? new SimpleDateFormat("yyyy-MM-dd").format(pw.getStartTime()) : null;
	    case ENDTIME:
		return pw.getEndTime() != null ? new SimpleDateFormat("yyyy-MM-dd").format(pw.getEndTime()) : null;
	    case PROJECTNAME:
		return pw.getProjectName() != null ? pw.getProjectName() : null;
	    case CUSTOMER:
		return pw.getCustomer() != null ? pw.getCustomer() : null;
	    case AMOUNT:
		return pw.getAmount()+"" !=null? pw.getAmount():null;
	    default:
		return "";
	}
    }

}
