/*
 * WaitPayTableCellRender.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 18:06:33
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaTableDefaultCellRenderer;
import com.yz.rms.client.model.expenseform.WaitPayTableModel;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class WaitPayTableCellRender extends NazcaTableDefaultCellRenderer{
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == WaitPayTableModel.MONTH) {
            if((int)value == -1){
               label.setText( "总计");
            }
        }
        return this;
    }    
}
