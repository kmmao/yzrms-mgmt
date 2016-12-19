/*
 * WaitPayTableModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-17 09:37:41
 */
package com.yz.rms.client.model.expenseform;

import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;

/**
 *
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class WaitPayTableModel extends AbstractSimpleObjectTableModel<WaitPayStatWrap>{

    public static final int MONTH = 0;
    public static final int COUNT = 1;
    public static final int AMOUNT = 2;
    private static final String[] COLS = new String[]{"月份", "条数（条）", "总额（元）"};
    public WaitPayTableModel() {
        super(COLS);
    }

     @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WaitPayStatWrap data = dataList.get(rowIndex);
        switch (columnIndex) {
            case MONTH://月份
                return data.getMonth();
            case COUNT://条数
                return data.getCount();
            case AMOUNT://总额
                return data.getAmount();
            default:
                return "";
        }
            
    }
         
}
