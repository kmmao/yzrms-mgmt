/*
 * ExpenseFormTabRender.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-09-08 15:28:13
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaTableDefaultCellRenderer;
import com.yz.rms.client.model.expenseform.ExpenseTableModel;
import com.yz.rms.client.model.expenseform.ExpenseTableModelForPm;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseFormTabRender extends NazcaTableDefaultCellRenderer {

    private static final Double expenseTotalLimit = 2000.0;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == ExpenseTableModel.EXPENSETOTAL || column == ExpenseTableModelForPm.EXPENSETOTAL) {
            if ((Double) value >= expenseTotalLimit) {
                label.setForeground(Color.white);
                label.setBackground(Color.orange);
                label.setOpaque(true);
            } else {
                label.setOpaque(false);
            }
        } else {
            label.setOpaque(false);
        }
        return this;
    }

}
