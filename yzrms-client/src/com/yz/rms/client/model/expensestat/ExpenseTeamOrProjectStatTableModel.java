/*
 * ExpenseTeamOrProjectStatTableModel.java
 * 
 * Copyright(c) 2007-2012 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2012-11-01 15:22:12
 */
package com.yz.rms.client.model.expensestat;

import com.yz.rms.common.model.wrap.StatExpensePersonWrap;
import com.yz.rms.common.model.wrap.StatItemWrap;
import com.yz.rms.common.model.wrap.StatTeamOrProjectWrap;
import java.util.ArrayList;
import java.util.List;

/**
 * 报销统计model
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class ExpenseTeamOrProjectStatTableModel extends AbstractStatTableModel {

    private final List<String> columns = new ArrayList<>();
    private final List<String> singleColumnsBefore = new ArrayList<>();
    private final List<String> singleColumnsAfter = new ArrayList<>();
    private final List<String> allColumns = new ArrayList<>();
    private final List<Integer> groupChildSize = new ArrayList<>();
    private final List<StatItemWrap> list = new ArrayList<>();

    @Override
    public int getColumnWidth(int columnIndex) {

        if (columnIndex == 0) {
            return 100;
        } else if (columnIndex == allColumns.size() - 1) {
            return 100;
        } else {
            return 80;
        }
    }

    public void setHeader(List<StatTeamOrProjectWrap> list) {
        columns.clear();
        singleColumnsBefore.clear();
        singleColumnsAfter.clear();
        allColumns.clear();
        groupChildSize.clear();

        singleColumnsBefore.add("项目");
        allColumns.add("项目");

        for (StatTeamOrProjectWrap c : list) {
            if (!c.getTpId().equals(StatTeamOrProjectWrap.TOTAL_ID)) {
                columns.add(c.getTpName());
                List<StatExpensePersonWrap> personList = c.getPersonList();
                for (StatExpensePersonWrap statExpensePersonWrap : personList) {
                    allColumns.add(statExpensePersonWrap.getpName());
                }
                groupChildSize.add(personList.size());
            } else {
                singleColumnsAfter.add("合计");
                allColumns.add("合计");

            }
        }
        fireTableStructureChanged();
    }

    public void setDataList(List<StatItemWrap> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        fillData();
        fireTableDataChanged();
    }

    /**
     * 填充数据
     */
    private void fillData() {
        dataMesh = new Object[list.size()][allColumns.size()];
        for (int i = 0; i < list.size(); i++) {
            StatItemWrap rowData = list.get(i);
            int littleColumnCount = 0;
            for (int j = 0; j < allColumns.size(); j++) {
                if (j == 0) {
                    dataMesh[i][j] = rowData.getItem().toString();
                } else {
                    for (int k = 0; k < rowData.getTpList().size(); k++) {
                        StatTeamOrProjectWrap a = rowData.getTpList().get(k);
                        if (k != 0) {
                            littleColumnCount += rowData.getTpList().get(k - 1).getPersonList().size();
                        }
                        List<StatExpensePersonWrap> personList = a.getPersonList();
                        for (int m = 1; m <= personList.size(); m++) {
                            StatExpensePersonWrap a1 = personList.get(m - 1);
                            j = m + littleColumnCount;
                            dataMesh[i][j] = a1.getMoney();
                        }
                    }
                }
            }
        }
    }

    public void reFillData() {
        fillData();
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return allColumns.get(column);
    }

    @Override
    public int getGroupColumnCounts() {
        return columns.size();
    }

    @Override
    public String getGroupColumnName(int idx) {
        return columns.get(idx);
    }

    @Override
    public int getGroupColumnSubCounts(int column) {
        return groupChildSize.get(column);
    }

    @Override
    public int getSingleColumnCount() {
        return singleColumnsBefore.size();
    }

    @Override
    public int getSuffixSingleColumnCount() {
        return 0;
    }

    @Override
    public int changeToTotalColumnIndex(int groupIndex, int index) {
        int k = 0;
        for (int i = 0; i < groupIndex; i++) {
            k += groupChildSize.get(i);
        }
        return singleColumnsBefore.size() + k + index;
    }

    @Override
    public int getColumnCount() {
        return allColumns.size();
    }

    public StatItemWrap getData(int idx) {
        return list.get(idx);
    }

    public void addData(int idx, StatItemWrap data) {
        list.add(idx, data);
        fillData();
        fireTableRowsInserted(idx, idx);
    }

    public void updateData(StatItemWrap data) {
        int idx = list.indexOf(data);
        list.set(idx, data);
        fillData();
        fireTableRowsUpdated(idx, idx);
    }

    public void deleteData(StatItemWrap data) {
        int idx = list.indexOf(data);
        list.remove(idx);
        fillData();
        fireTableRowsDeleted(idx, idx);
    }

    public List<StatItemWrap> getList() {
        return list;
    }

    @Override
    public int getSingleColumnAfterCount() {
        return singleColumnsAfter.size();
    }
}
