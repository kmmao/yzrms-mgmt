/*
 * TeamCmBoxListRenderer.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 12:18:14
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaListDefaultCellRenderer;
import java.awt.Component;
import javax.swing.JList;

/**
 * 团队统计下拉框的renderer
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class TeamCmBoxListRenderer extends NazcaListDefaultCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            value = "全部";
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
