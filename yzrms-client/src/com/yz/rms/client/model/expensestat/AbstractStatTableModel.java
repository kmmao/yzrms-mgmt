/*
 * AbstractStatTableModel.java
 * 
 * Copyright(c) 2007-2012 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2012-11-01 15:23:56
 */
package com.yz.rms.client.model.expensestat;

import javax.swing.table.AbstractTableModel;

/**
 * 抽象多表头TableModel
 *
 * @author Qiu Dongyue
 */
public abstract class AbstractStatTableModel extends AbstractTableModel {

    protected Object[][] dataMesh = new Object[0][0];

    public abstract int getGroupColumnCounts();

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getRowCount() {
        return dataMesh.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = dataMesh[rowIndex][columnIndex];
        return obj;
    }

    /**
     * 获取组名
     *
     * @param idx 组索引，不是列索引
     * @return
     */
    public abstract String getGroupColumnName(int idx);

    /**
     * 获取某一个合成表头的子列个数
     *
     * @param column 合成表头列索引(非整个表中的列索引)
     * @return
     */
    public abstract int getGroupColumnSubCounts(int column);

    /**
     * 获取表格前面的简单表头列数
     *
     * @return
     */
    public abstract int getSingleColumnCount();

    /**
     * 获取表格结尾处简单表头列数
     *
     */
    public abstract int getSuffixSingleColumnCount();

    /**
     * 根据组编号，组内索引，转换成对应的表格基础列索引
     *
     * @param groupIndex
     * @param index
     * @return
     */
    public abstract int changeToTotalColumnIndex(int groupIndex, int index);

    public abstract int getColumnWidth(int columnIndex);

    public abstract int getSingleColumnAfterCount();
}
