/*
 * MemberTableRenderer.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 13:54:56
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaTableDefaultCellRenderer;
import com.yz.rms.client.model.team.MemberTableModel;
import java.awt.Component;
import javax.swing.JTable;

/**
 * 团队成员Renderer
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class MemberTableRenderer extends NazcaTableDefaultCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == MemberTableModel.AUTHORITY) {
            if ((boolean) value == true) {
                label.setText("负责人");
            } else {
                label.setText("组员");
            }
        }
        return this;
    }
}
