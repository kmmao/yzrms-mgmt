/*
 * MemberTableModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-10 17:29:03
 */
package com.yz.rms.client.model.team;

import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.yz.rms.common.model.wrap.TeamMemberWrap;

/**
 * 团队成员tableModel
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class MemberTableModel extends AbstractSimpleObjectTableModel<TeamMemberWrap> {

    public static final int USERNAME = 0;
    public static final int EMPLOYEENUM = 1;
    public static final int POSITION = 2;
    public static final int AUTHORITY = 3;
    public static final int PHONE = 4;
    private static final String[] COLS = new String[]{"姓名", "工号", "职务", "团队角色", "联系电话"};

    public MemberTableModel() {
        super(COLS);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TeamMemberWrap data = dataList.get(rowIndex);
        switch (columnIndex) {
            case USERNAME://姓名

                return data.getEmployeeName();

            case EMPLOYEENUM://工号

                return data.getEmployeeNumber();

            case POSITION://职务

                return data.getPosition();
            case AUTHORITY://职务

                return data.isAuthority();

            case PHONE://电话号码
                return data.getPhone();
            default:
                return "";
        }
    }
}
