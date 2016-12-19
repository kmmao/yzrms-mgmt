/*
 * ExpenseFormStateRender.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 11:27:53
 */
package com.yz.rms.client.renderer;

import com.nazca.ui.laf.blueocean.NazcaListDefaultCellRenderer;
import java.awt.Component;
import javax.swing.JList;

/**
 * 报销单状态下拉框的renderer
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class ExpenseFormStateRender extends NazcaListDefaultCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	if (value == null) {
	    value = "全部";
	}
	return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
