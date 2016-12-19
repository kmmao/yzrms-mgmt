/*
 * ProjectStatMgmtPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-11 11:43:12
 */
package com.yz.rms.client.ui.stat;

import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.UIUtilities;
import com.nazca.ui.model.SimpleObjectListModel;
import com.nazca.ui.util.CardLayoutWrapper;
import com.yz.rms.client.agent.stat.QueyAllProjectAgent;
import com.yz.rms.client.agent.stat.QueyProjectStatAgent;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.expensestat.ExpenseTeamOrProjectStatTableModel;
import com.yz.rms.client.renderer.TeamCmBoxListRenderer;
import com.yz.rms.client.renderer.TeamMonthListRenderer;
import com.yz.rms.client.renderer.TeamOrProjectStatRenderer;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.model.wrap.StatItemWrap;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import org.jdesktop.jdnc.JNTable;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.ColumnGroupTableHeader;

/**
 * 项目统计面板
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class ProjectStatMgmtPanel extends javax.swing.JPanel {

    private QueyAllProjectAgent queyAllProjectAgent = null;
    private QueyProjectStatAgent queyProjectStatAgent = null;
    private final JNTable leftTable = new JNTable();
    private final JNTable rightTable = new JNTable();
    private final SimpleObjectListModel<Project> projectComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<Integer> yearComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<Integer> monthComboxModel = new SimpleObjectListModel<>();
    private ExpenseTeamOrProjectStatTableModel tableModel = null;
    private TeamMonthListRenderer teamMonthListRenderer;
    private TeamCmBoxListRenderer teamCmBoxListRenderer;
    private CardLayoutWrapper card;
    private TeamOrProjectStatRenderer teamOrProjectStatRenderer;

    /**
     * Creates new form ProjectStatMgmtPanel
     */
    public ProjectStatMgmtPanel() {
        initComponents();
        initUI();
        initAgentAndListener();
        initModelAndRenderer();
        refreshBtn.doClick();
    }

    //初始化table
    private void initTableHeader() {
        tableModel = new ExpenseTeamOrProjectStatTableModel();
        rightTable.setModel(tableModel);
        rightTable.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftTable.setModel(tableModel);
        leftTable.getTable().setSelectionModel(rightTable.getTable().getSelectionModel());
    }

    private void setTableHeader() {
        //生成合成表头
        TableColumnModel columnModel = rightTable.getTable().getColumnModel();
        ColumnGroupTableHeader header = new ColumnGroupTableHeader(columnModel);
        for (int i = 0; i < tableModel.getGroupColumnCounts(); i++) {
            ColumnGroup group = new ColumnGroup(tableModel.getGroupColumnName(i));
            int subCount = tableModel.getGroupColumnSubCounts(i);
            for (int k = 0; k < subCount; k++) {
                group.add(columnModel.getColumn(tableModel.changeToTotalColumnIndex(i, k)));
            }
            header.addColumnGroup(group);
        }
        rightTable.getTable().setTableHeader(header);

        //锁定左侧列，只滚动右侧列
        TableColumnModel columnModel2 = leftTable.getTable().getColumnModel();
        for (int i = 0; i < tableModel.getSingleColumnCount(); i++) {//设置合并表格表头的宽度
            UIUtilities.setTableColumnFixedSize(leftTable.getTable(), i, tableModel.getColumnWidth(i));
        }
        for (int i = tableModel.getSingleColumnCount(); i < tableModel.getColumnCount() - 1; i++) {//设置中间列的宽度
            UIUtilities.setTableColumnFixedSize(rightTable.getTable(), i, tableModel.getColumnWidth(i));
        }
        for (int i = tableModel.getColumnCount() - 1; i < tableModel.getColumnCount(); i++) {//设置末尾合并列的宽度
            UIUtilities.setTableColumnFixedSize(rightTable.getTable(), i, tableModel.getColumnWidth(i));
        }
        for (int i = columnModel2.getColumnCount() - 1; i >= tableModel.getSingleColumnCount(); i--) {
            leftTable.getTable().getColumnModel().removeColumn(columnModel2.getColumn(i));
        }
        for (int i = tableModel.getSingleColumnCount() - 1; i >= 0; i--) {
            rightTable.getTable().getColumnModel().removeColumn(rightTable.getTable().getColumnModel().getColumn(i));
        }
        jScrollPane1.setRowHeaderView(leftTable.getTable());
        jScrollPane1.setCorner(JScrollPane.UPPER_LEFT_CORNER, leftTable.getTable().getTableHeader());
        jScrollPane1.setViewportView(rightTable.getTable());

        //设置表的一些属性
        rightTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rightTable.getTable().setSortable(false);
        rightTable.getTable().getTableHeader().setReorderingAllowed(false);
        rightTable.getTable().getTableHeader().setResizingAllowed(false);
        leftTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        leftTable.getTable().setSortable(false);
        leftTable.getTable().getTableHeader().setReorderingAllowed(false);
        leftTable.getTable().getTableHeader().setResizingAllowed(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        refreshBtn = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jLabel1 = new javax.swing.JLabel();
        projectCmBox = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        yearCmBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        monthCmBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        contentPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        antialiasedLabel1 = new com.nazca.ui.AntialiasedLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nWaitingPanel1 = new com.nazca.ui.NWaitingPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/refresh_16.png"))); // NOI18N
        refreshBtn.setText("刷新");
        refreshBtn.setFocusable(false);
        refreshBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshBtn);
        jToolBar1.add(filler3);

        jLabel1.setText("项目选择：");
        jToolBar1.add(jLabel1);

        projectCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectCmBox.setMaximumSize(new java.awt.Dimension(65, 32767));
        projectCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                projectCmBoxItemStateChanged(evt);
            }
        });
        jToolBar1.add(projectCmBox);
        jToolBar1.add(filler1);

        yearCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        yearCmBox.setMaximumSize(new java.awt.Dimension(65, 32767));
        yearCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                yearCmBoxItemStateChanged(evt);
            }
        });
        jToolBar1.add(yearCmBox);

        jLabel2.setText("年");
        jLabel2.setMaximumSize(new java.awt.Dimension(20, 15));
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 15));
        jLabel2.setPreferredSize(new java.awt.Dimension(20, 15));
        jToolBar1.add(jLabel2);
        jToolBar1.add(filler4);

        monthCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        monthCmBox.setMaximumSize(new java.awt.Dimension(65, 32767));
        monthCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                monthCmBoxItemStateChanged(evt);
            }
        });
        jToolBar1.add(monthCmBox);

        jLabel3.setText("月");
        jToolBar1.add(jLabel3);
        jToolBar1.add(filler2);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        contentPanel.setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        antialiasedLabel1.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel1.setText("暂无报销信息");
        antialiasedLabel1.setFont(antialiasedLabel1.getFont().deriveFont(antialiasedLabel1.getFont().getStyle() | java.awt.Font.BOLD, 20));
        jPanel1.add(antialiasedLabel1, java.awt.BorderLayout.CENTER);

        contentPanel.add(jPanel1, "EMPTY");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPanel.add(jScrollPane1, "CONTENT");
        contentPanel.add(nWaitingPanel1, "WAIT");

        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void projectCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_projectCmBoxItemStateChanged
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_projectCmBoxItemStateChanged

    private void yearCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yearCmBoxItemStateChanged
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_yearCmBoxItemStateChanged

    private void monthCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_monthCmBoxItemStateChanged
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_monthCmBoxItemStateChanged

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        Integer year = yearComboxModel.getSelectedItem();
        Project project = projectComboxModel.getSelectedItem();
        String id = null;
        if (null != project) {
            id = project.getProjectId();
        }
        Integer month = monthComboxModel.getSelectedItem();
        queyProjectStatAgent.setParameter(id, year, month);
        queyProjectStatAgent.start();
    }//GEN-LAST:event_refreshBtnActionPerformed
    private void initUI() {
        card = new CardLayoutWrapper(contentPanel);
    }

    private void initModelAndRenderer() {
        projectCmBox.setModel(projectComboxModel);
        yearComboxModel.setObjectList(ResourceUtil.getYears());
        yearCmBox.setModel(yearComboxModel);
        monthComboxModel.setObjectList(ResourceUtil.getMonths());
        monthCmBox.setModel(monthComboxModel);
        yearCmBox.setSelectedIndex(0);
        monthCmBox.setSelectedIndex(0);
        teamMonthListRenderer = new TeamMonthListRenderer();
        monthCmBox.setRenderer(teamMonthListRenderer);
        teamCmBoxListRenderer = new TeamCmBoxListRenderer();
        projectCmBox.setRenderer(teamCmBoxListRenderer);
        teamOrProjectStatRenderer = new TeamOrProjectStatRenderer();
        rightTable.getTable().setDefaultRenderer(Object.class, teamOrProjectStatRenderer);
        initTableHeader();
    }

    private void initAgentAndListener() {
        queyAllProjectAgent = new QueyAllProjectAgent();
        queyAllProjectAgent.addListener(queryProjectAgentLis);
        queyProjectStatAgent = new QueyProjectStatAgent();
        queyProjectStatAgent.addListener(queyProjectStatAgentLis);
    }

    private void setEnable(boolean flag) {
        refreshBtn.setEnabled(flag);
        projectCmBox.setEnabled(flag);
        yearCmBox.setEnabled(flag);
        monthCmBox.setEnabled(flag);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nazca.ui.AntialiasedLabel antialiasedLabel1;
    private javax.swing.JPanel contentPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox<String> monthCmBox;
    private com.nazca.ui.NWaitingPanel nWaitingPanel1;
    private javax.swing.JComboBox<String> projectCmBox;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JComboBox<String> yearCmBox;
    // End of variables declaration//GEN-END:variables
    private final AgentListener<List<Project>> queryProjectAgentLis = new AgentListener<List<Project>>() {
        @Override
        public void onStarted(long seq) {
        }

        @Override
        public void onSucceeded(List<Project> result, long seq) {
            result.add(null);
            Collections.reverse(result);
            projectComboxModel.setObjectList(result);
            projectCmBox.setSelectedIndex(0);
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
        }
    };
    private final AgentListener<List<StatItemWrap>> queyProjectStatAgentLis = new AgentListener<List<StatItemWrap>>() {
        @Override
        public void onStarted(long seq) {
            setEnable(false);
            nWaitingPanel1.setIndeterminate(true);
            nWaitingPanel1.showMsgMode("数据加载中，请稍后...", 0, nWaitingPanel1.MSG_MODE_INFO);
            nWaitingPanel1.showWaitingMode();
            card.show(CardLayoutWrapper.WAIT);
        }

        @Override
        public void onSucceeded(List<StatItemWrap> result, long seq) {
            int tpCount = 0;
            for (StatItemWrap statItemWrap : result) {
                tpCount += statItemWrap.getTpList().size();
            }
            if (tpCount > 0) {
                setEnable(true);
                nWaitingPanel1.setIndeterminate(false);
                if (result != null && result.get(0) != null && result.get(0).getTpList() != null) {
                    tableModel.setHeader(result.get(0).getTpList());
                }
                setTableHeader();
                tableModel.setDataList(result);
                card.show(CardLayoutWrapper.CONTENT);
            } else {
                setEnable(true);
                card.show(CardLayoutWrapper.EMPTY);
            }
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            setEnable(true);
            nWaitingPanel1.setIndeterminate(false);
            nWaitingPanel1.showMsgMode(errMsg, errCode, nWaitingPanel1.MSG_MODE_INFO);
        }
    };

    public void onInit() {
        queyAllProjectAgent.start();
    }
}
