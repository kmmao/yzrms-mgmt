/*
 * TeamOrProjectStatRenderer.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-09-02 14:40:12
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaTableDefaultCellRenderer;
import com.nazca.util.NazcaFormater;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class TeamOrProjectStatRenderer extends NazcaTableDefaultCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Double && (double) value != 0) {
            String s = NazcaFormater.getCashString((double) value);
            label.setText(s);
        }
        if ((double) value == 0) {
            label.setText("--");
        }
        return this;
    }
}
