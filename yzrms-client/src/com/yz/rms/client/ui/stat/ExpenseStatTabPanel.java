/*
 * ExpenseStatTabPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 17:55:33
 */
package com.yz.rms.client.ui.stat;

import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.UIUtilities;
import com.nazca.ui.model.SimpleObjectListModel;
import com.nazca.ui.util.CardLayoutWrapper;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.stat.QueryTeamByPMAgent;
import com.yz.rms.client.agent.stat.QueyTeamStatAgent;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.expensestat.ExpenseTeamOrProjectStatTableModel;
import com.yz.rms.client.renderer.TeamCmBoxListRenderer;
import com.yz.rms.client.renderer.TeamMonthListRenderer;
import com.yz.rms.client.renderer.TeamOrProjectStatRenderer;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.consts.ProjectConst;
import com.yz.rms.common.enums.ExpenseRoleEnum;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.StatItemWrap;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import org.jdesktop.jdnc.JNTable;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.ColumnGroupTableHeader;

/**
 * 统计切换面板
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class ExpenseStatTabPanel extends javax.swing.JPanel {

    //团队统计
    List<Team> backResult;
    //项目统计
    private ProjectStatMgmtPanel projectStatMgmtPanel = null;
    private QueyTeamStatAgent queyTeamStatAgent = null;
    private final SimpleObjectListModel<Integer> yearComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<Team> teamComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<Integer> monthComboxModel = new SimpleObjectListModel<>();

    private QueryTeamByPMAgent queryTeamByPMAgent = null;
    private final JNTable leftTable = new JNTable();
    private final JNTable rightTable = new JNTable();
    private ExpenseTeamOrProjectStatTableModel tableModel = null;
    private TeamMonthListRenderer teamMonthListRenderer;
    private TeamCmBoxListRenderer teamCmBoxListRenderer;
    private CardLayoutWrapper card;
    private TeamOrProjectStatRenderer teamOrProjectStatRenderer;

    /**
     * Creates new form ExpenseStatTabPanel
     */
    public ExpenseStatTabPanel() {
        initComponents();
        initUI();
        projectStatMgmtPanel = new ProjectStatMgmtPanel();
        initAgentAndListener();
        initModelAndRenderer();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        refreshBtn = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jLabel1 = new javax.swing.JLabel();
        teamCmBox = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        yearCmBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        monthCmBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        contentPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        antialiasedLabel1 = new com.nazca.ui.AntialiasedLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nWaitingPanel1 = new com.nazca.ui.NWaitingPanel();
        nNavToolBar1 = new com.nazca.ui.NNavToolBar();
        teamStatBtn = new javax.swing.JToggleButton();
        projectStatBtn = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

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

        jLabel1.setText("团队选择：");
        jToolBar1.add(jLabel1);

        teamCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        teamCmBox.setMaximumSize(new java.awt.Dimension(65, 32767));
        teamCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                teamCmBoxItemStateChanged(evt);
            }
        });
        jToolBar1.add(teamCmBox);
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

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        contentPanel.setLayout(new java.awt.CardLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        antialiasedLabel1.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel1.setText("暂无报销信息");
        antialiasedLabel1.setFont(antialiasedLabel1.getFont().deriveFont(antialiasedLabel1.getFont().getStyle() | java.awt.Font.BOLD, 20));
        jPanel2.add(antialiasedLabel1, java.awt.BorderLayout.CENTER);

        contentPanel.add(jPanel2, "EMPTY");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPanel.add(jScrollPane1, "CONTENT");
        contentPanel.add(nWaitingPanel1, "WAIT");

        jPanel1.add(contentPanel, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout());

        nNavToolBar1.setRollover(true);

        buttonGroup1.add(teamStatBtn);
        teamStatBtn.setSelected(true);
        teamStatBtn.setText("团队统计");
        teamStatBtn.setFocusable(false);
        teamStatBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        teamStatBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        teamStatBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        teamStatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teamStatBtnActionPerformed(evt);
            }
        });
        nNavToolBar1.add(teamStatBtn);

        buttonGroup1.add(projectStatBtn);
        projectStatBtn.setText("项目统计");
        projectStatBtn.setFocusable(false);
        projectStatBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        projectStatBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        projectStatBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        projectStatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectStatBtnActionPerformed(evt);
            }
        });
        nNavToolBar1.add(projectStatBtn);

        add(nNavToolBar1, java.awt.BorderLayout.NORTH);
        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void teamStatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teamStatBtnActionPerformed
        jPanel5.removeAll();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(jPanel1, BorderLayout.CENTER);
        revalidate();
        repaint();
    }//GEN-LAST:event_teamStatBtnActionPerformed

    private void projectStatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectStatBtnActionPerformed
        jPanel5.removeAll();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(projectStatMgmtPanel, BorderLayout.CENTER);
        projectStatMgmtPanel.onInit();
        revalidate();
        repaint();
    }//GEN-LAST:event_projectStatBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        Integer year = yearComboxModel.getSelectedItem();
        Team team = null;
        int index = teamCmBox.getSelectedIndex();
        if (index < 0) {
            team = teamComboxModel.get(0);
        } else {
            team = teamComboxModel.get(index);
        }

        String id = null;
        if (null != team) {
            id = team.getTeamId();
        }
        Integer month = monthComboxModel.getSelectedItem();

        queyTeamStatAgent.setParameter(id, year, month);
        queyTeamStatAgent.start();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void setTeamCmBoxByAuthority() {
        //               判断权限
        USMSUser curUser = ClientContext.getUser();
        if (curUser.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name()) || curUser.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {//如果是总经理或HR
            queryTeamByPMAgent.setParameter(null);
            queryTeamByPMAgent.start();
        } else {
            queryTeamByPMAgent.setParameter(curUser.getId());
            queryTeamByPMAgent.start();
        }
    }

    private void teamCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_teamCmBoxItemStateChanged
        // TODO add your handling code here:
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_teamCmBoxItemStateChanged

    private void yearCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yearCmBoxItemStateChanged
        // TODO add your handling code here:
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_yearCmBoxItemStateChanged

    private void monthCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_monthCmBoxItemStateChanged
        // TODO add your handling code here:
        refreshBtnActionPerformed(null);
    }//GEN-LAST:event_monthCmBoxItemStateChanged

    private void initModelAndRenderer() {
        teamCmBox.setModel(teamComboxModel);
        yearComboxModel.setObjectList(ResourceUtil.getYears());
        yearCmBox.setModel(yearComboxModel);
        monthComboxModel.setObjectList(ResourceUtil.getMonths());
        monthCmBox.setModel(monthComboxModel);
        yearCmBox.setSelectedIndex(0);
        monthCmBox.setSelectedIndex(0);
        initTableHeader();
        teamMonthListRenderer = new TeamMonthListRenderer();
        monthCmBox.setRenderer(teamMonthListRenderer);
        teamCmBoxListRenderer = new TeamCmBoxListRenderer();
        teamCmBox.setRenderer(teamCmBoxListRenderer);
        teamOrProjectStatRenderer = new TeamOrProjectStatRenderer();
        rightTable.getTable().setDefaultRenderer(Object.class, teamOrProjectStatRenderer);
    }

    private void initAgentAndListener() {
        queryTeamByPMAgent = new QueryTeamByPMAgent();
        queryTeamByPMAgent.addListener(queryTeamByPMAgentLis);
        queyTeamStatAgent = new QueyTeamStatAgent();
        queyTeamStatAgent.addListener(queyTeamStatAgentLis);
    }

    private void initUI() {
        card = new CardLayoutWrapper(contentPanel);
    }

    private void setEnable(boolean flag) {
        refreshBtn.setEnabled(flag);
        teamCmBox.setEnabled(flag);
        yearCmBox.setEnabled(flag);
        monthCmBox.setEnabled(flag);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nazca.ui.AntialiasedLabel antialiasedLabel1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel contentPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox<String> monthCmBox;
    private com.nazca.ui.NNavToolBar nNavToolBar1;
    private com.nazca.ui.NWaitingPanel nWaitingPanel1;
    private javax.swing.JToggleButton projectStatBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JComboBox<String> teamCmBox;
    private javax.swing.JToggleButton teamStatBtn;
    private javax.swing.JComboBox<String> yearCmBox;
    // End of variables declaration//GEN-END:variables
   
    private final AgentListener<List<Team>> queryTeamByPMAgentLis = new AgentListener<List<Team>>() {
        @Override
        public void onStarted(long seq) {
        }

        @Override
        public void onSucceeded(List<Team> result, long seq) {
            backResult = result;
            teamCmBox.setEnabled(true);
            teamComboxModel.setObjectList(result);
            if (!result.isEmpty()) {
                teamCmBox.setSelectedIndex(0);
            } else {
                teamCmBox.setEnabled(false);
            }
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
        }
    };
    
    private long queryTeamStatSeq=0;
    private final AgentListener<List<StatItemWrap>> queyTeamStatAgentLis = new AgentListener<List<StatItemWrap>>() {
        @Override
        public void onStarted(long seq) {
            queryTeamStatSeq=seq;
            setEnable(false);
            nWaitingPanel1.setIndeterminate(true);
            nWaitingPanel1.showMsgMode("数据加载中，请稍后...", 0, nWaitingPanel1.MSG_MODE_INFO);
            nWaitingPanel1.showWaitingMode();
            card.show(CardLayoutWrapper.WAIT);
        }

        @Override
        public void onSucceeded(List<StatItemWrap> result, long seq) {
            if(queryTeamStatSeq!=seq){
                return;
            }
            int tpCount = 0;
            for (StatItemWrap statItemWrap : result) {
                tpCount += statItemWrap.getTpList().size();
            }
            if (backResult != null &&!backResult.isEmpty()) {
                if (tpCount == 0) {
                    setEnable(true);
                    card.show(CardLayoutWrapper.EMPTY);

                } else {
                    setEnable(true);
                    nWaitingPanel1.setIndeterminate(false);
                    if (result != null && result.get(0) != null && result.get(0).getTpList() != null) {
                        tableModel.setHeader(result.get(0).getTpList());
                    }
                    setTableHeader();
                    tableModel.setDataList(result);
                    card.show(CardLayoutWrapper.CONTENT);
                }
            } else {
                setEnable(false);
                teamCmBox.removeAllItems();
                refreshBtn.setEnabled(true);
                card.show(CardLayoutWrapper.EMPTY);
            }
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
             if(queryTeamStatSeq!=seq){
                return;
            }
            setEnable(true);
            nWaitingPanel1.setIndeterminate(false);
            nWaitingPanel1.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_INFO);
        }
    };

    public void onInit() {
        setTeamCmBoxByAuthority();
        teamStatBtn.doClick();
    }
}
