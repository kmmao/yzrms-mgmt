/*
 * ExpenseFormAuditPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-10 12:03:45
 */
package com.yz.rms.client.ui.expenseform;

import com.nazca.sql.PageResult;
import com.nazca.ui.CellItem;
import com.nazca.ui.NComponentStyleTool;
import com.nazca.ui.NListSelectionEvent;
import com.nazca.ui.NListSelectionListener;
import com.nazca.ui.NTextHinter;
import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.UIUtilities;
import com.nazca.ui.laf.border.IconLabelBorder;
import com.nazca.ui.model.AbstractSimpleObjectTableModel;
import com.nazca.ui.model.SimpleObjectListModel;
import com.nazca.ui.pagination.PaginationListener;
import com.nazca.ui.pagination.TablePageSession;
import com.nazca.ui.util.CardLayoutWrapper;
import com.nazca.util.StringUtil;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.expenseform.markPaidAgent;
import com.yz.rms.client.agent.expenseform.QueryExpenseStateCountAgent;
import com.yz.rms.client.agent.expenseform.QueryExpenseFormAgent;
import com.yz.rms.client.agent.expenseform.QueryWaitPayStatAgent;
import com.yz.rms.client.agent.stat.QueyAllProjectAgent;
import com.yz.rms.client.agent.stat.QueyTeamAgent;
import com.yz.rms.client.ui.SimpleOperationPanel;
import com.yz.rms.client.ui.NGroupListNodePane;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.expenseform.ExpenseTableModelForPm;
import com.yz.rms.client.model.expenseform.ExpenseTableModel;
import com.yz.rms.client.renderer.ExpenseFormTabRender;
import com.yz.rms.client.renderer.TeamCmBoxListRenderer;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.client.util.USMSUserSyncTool;
import com.yz.rms.common.consts.ProjectConst;
import com.yz.rms.common.enums.ExpenseRoleEnum;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.ExpenseFormStateCountWrap;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseFormAuditPanel extends javax.swing.JPanel {

    private QueryExpenseStateCountAgent queryExpenseStateCountAgent = null;
    private QueryExpenseFormAgent queryExpenseformAgent = null;
    private long timeSeq = 0;
    private long timeSeqExpense = 0;
    private CardLayoutWrapper leftCard = null;
    private CardLayoutWrapper centerCard = null;
    private CardLayoutWrapper rightCard = null;
    private ExpenseFormWrap expenseFormWrap = null;
    private final SimpleObjectListModel<Project> projectComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<Team> teamComboxModel = new SimpleObjectListModel<>();
    private TeamCmBoxListRenderer teamCmBoxListRenderer;
    private QueyAllProjectAgent queyAllProjectAgent = null;
    private QueyTeamAgent queyTeamAgent = null;
    private markPaidAgent markPaidAgent = null;
    private TablePageSession pageSession;
    private ExpenseFormStateCountWrap curWrap = null;
    private Date startDate = null;
    private Date endDate = null;
    private Map<Integer, List<WaitPayStatWrap>> map = null;
    private QueryWaitPayStatAgent queryWaitPayStat;
    private boolean isFirst;
    private AbstractSimpleObjectTableModel<ExpenseFormWrap> model = null;
    private ExpenseFormTabRender expenseFormTabRender = new ExpenseFormTabRender();
    private String oldKeyWord;
    private int currLen = 0;

    /**
     * Creates new form ExpenseFormAuditTabPanel
     */
    public ExpenseFormAuditPanel() {
        initComponents();
        initUI();
        initModelAndrenderer();
        initAgentAndListener();
        startDate = getMinDateOfThisYear(new Date());
        endDate = new Date();
        dateRangePic.setStartDate(startDate);
        dateRangePic.setEndDate(endDate);
    }

    private void initUI() {
        statePane.setBorder(new IconLabelBorder(ResourceUtil.buildBufferedImage("32.png"), "状态列表"));
        espensePane.setBorder(new IconLabelBorder(ResourceUtil.buildBufferedImage("32.png"), "报销单列表"));
        detailPane.setBorder(new IconLabelBorder(ResourceUtil.buildBufferedImage("32.png"), "详细信息"));
        leftCard = new CardLayoutWrapper(jPanel2);
        centerCard = new CardLayoutWrapper(jPanel5);
        rightCard = new CardLayoutWrapper(detailPane);
        NTextHinter.attach("请输入姓名", searchTxFd);
        UIUtilities.attachSearchIcon(searchTxFd);
    }

    private void initModelAndrenderer() {
        if (ClientContext.getUser().hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name())
                || ClientContext.getUser().hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {
            model = new ExpenseTableModel();
        } else {
            model = new ExpenseTableModelForPm();
        }
        expenseTableComp.setModel(model);
        expenseTableComp.setDefaultRenderer(Object.class, expenseFormTabRender);
        teamCmBoxListRenderer = new TeamCmBoxListRenderer();
        projectCmBox.setRenderer(teamCmBoxListRenderer);
        projectCmBox.setModel(projectComboxModel);
        teamCmBox.setRenderer(teamCmBoxListRenderer);
        teamCmBox.setModel(teamComboxModel);
    }

    private void initAgentAndListener() {
        queryExpenseStateCountAgent = new QueryExpenseStateCountAgent();
        queryExpenseformAgent = new QueryExpenseFormAgent();
        queryExpenseStateCountAgent.addListener(queryExpenseStateCountAgentLis);
        queryExpenseStateCountAgent.start();
        queryExpenseformAgent.addListener(queryExpenseFormLis);
        queyAllProjectAgent = new QueyAllProjectAgent();
        queyAllProjectAgent.addListener(queryProjectAgentLis);
        queyAllProjectAgent.start();
        queyTeamAgent = new QueyTeamAgent();
        queyTeamAgent.addListener(queryTeamAgentLis);
        queyTeamAgent.start();
        queryWaitPayStat = new QueryWaitPayStatAgent();
        queryWaitPayStat.addListener(queryWaitPayStatLis);
        queryWaitPayStat.start();
        markPaidAgent = new markPaidAgent();
        nGroupList1.addListSelectionListener(new NListSelectionListener() {
            @Override
            public void valueChanged(NListSelectionEvent nlse) {
                CellItem newItem = nlse.getNewItem();
                Object t = newItem.getT();
                if (t instanceof ExpenseFormStateCountWrap) {
                    curWrap = (ExpenseFormStateCountWrap) t;
                    if (ClientContext.getUser().hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name())) {
                        switch (curWrap.getState()) {
                            case waitAuditForPm:
                                auditBtn.setVisible(true);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(true);
                                teamCmBox.setVisible(true);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(false);
                                dateRangePic.setVisible(false);
                                searchTxFd.setVisible(true);
                                break;
                            case waitAuditForHr:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(false);
                                dateRangePic.setVisible(false);
                                searchTxFd.setVisible(true);
                                break;
                            case waitPay:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(true);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(true);
                                break;
                            case paid:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(true);
                                break;
                        }
                    } else if (ClientContext.getUser().hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {
                        switch (curWrap.getState()) {
                            case waitAuditForPm:
                                auditBtn.setVisible(true);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(false);
                                dateRangePic.setVisible(false);
                                searchTxFd.setVisible(false);
                                break;
                            case waitAuditForCeo:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(false);
                                break;
                            case waitAuditForHr:
                                auditBtn.setVisible(true);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(true);
                                teamCmBox.setVisible(true);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(false);
                                dateRangePic.setVisible(false);
                                searchTxFd.setVisible(true);
                                break;
                            case waitPay:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(true);
                                waitPayBtn.setVisible(true);
                                jLabel7.setVisible(true);
                                teamCmBox.setVisible(true);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(true);
                                break;
                            case paid:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(true);
                                teamCmBox.setVisible(true);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(true);
                                break;
                        }
                    } else {
                        switch (curWrap.getState()) {
                            case waitAuditForPm:
                                auditBtn.setVisible(true);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(false);
                                dateRangePic.setVisible(false);
                                searchTxFd.setVisible(false);
                                break;
                            default:
                                auditBtn.setVisible(false);
                                markPaidBtn.setVisible(false);
                                waitPayBtn.setVisible(false);
                                jLabel7.setVisible(false);
                                teamCmBox.setVisible(false);
                                jLabel1.setVisible(true);
                                projectCmBox.setVisible(true);
                                jLabel8.setVisible(true);
                                dateRangePic.setVisible(true);
                                searchTxFd.setVisible(true);
                                break;
                        }
                    }
                    expenseRefreshBtnActionPerformed(null);
                }
            }
        }
        );
        expenseTableComp.getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e
                    ) {
                        if (!e.getValueIsAdjusting()) {
                            expenseFormWrap = new ExpenseFormWrap();
                            int a[] = expenseTableComp.getSelectedRows();
                            if (a.length > 1) {
                                auditBtn.setEnabled(false);
                                nWaitingPanel3.showWaitingMode("请选择一条信息");
                                rightCard.show(CardLayoutWrapper.WAIT);
                            } else {
                                auditBtn.setEnabled(true);
                                rightCard.show(CardLayoutWrapper.CONTENT);
                            }
                            int index = expenseTableComp.getSelectedRow();
                            if (index >= 0) {
                                index = expenseTableComp.convertRowIndexToModel(index);
                                expenseFormWrap = model.getData(index);
                                detailedInformationPanel1.setParama(expenseFormWrap);
                            }
                        }
                    }
                }
                );
        //分页
        pagePane.addPaginationListener(
                new PaginationListener() {
            @Override
            public void onPageChanged(TablePageSession tps
            ) {
                expenseRefreshBtnActionPerformed(null);
            }
        }
        );
        //时间范围搜索事件触发
        dateRangePic.addDateRangePickerListener(
                (Date startTime, Date endTime) -> {
                    startDate = startTime;
                    endDate = endTime;
                    queryExpenseformAgent.setStartTime(startTime);
                    queryExpenseformAgent.setEndTime(endTime);
                    queryExpenseformAgent.start();
                }
        );
    }

    private void setExpenseFormBtnEnabled(boolean flag) {
        auditBtn.setEnabled(flag);
        markPaidBtn.setEnabled(flag);
        waitPayBtn.setEnabled(flag);
        jLabel1.setEnabled(flag);
        projectCmBox.setEnabled(flag);
        jLabel7.setEnabled(flag);
        teamCmBox.setEnabled(flag);
        jLabel8.setEnabled(flag);
        dateRangePic.setEnabled(flag);
        searchTxFd.setEnabled(flag);
    }

    private Date getMinDateOfThisYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 3);
        return c.getTime();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        statePane = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        stateRefreshBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        nGroupList1 = new com.nazca.ui.NGroupList();
        nWaitingPanel1 = new com.nazca.ui.NWaitingPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        espensePane = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        projectCmBox = new javax.swing.JComboBox<>();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel7 = new javax.swing.JLabel();
        teamCmBox = new javax.swing.JComboBox<>();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel8 = new javax.swing.JLabel();
        dateRangePic = new com.nazca.ui.NDateRangePicker();
        jToolBar1 = new javax.swing.JToolBar();
        expenseRefreshBtn = new javax.swing.JButton();
        auditBtn = new javax.swing.JButton();
        markPaidBtn = new javax.swing.JButton();
        waitPayBtn = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(1767, 0));
        searchTxFd = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        expenseFormPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        expenseTableComp = new javax.swing.JTable();
        pagePane = new com.nazca.ui.pagination.PaginationPanel();
        nWaitingPanel2 = new com.nazca.ui.NWaitingPanel();
        antialiasedLabel1 = new com.nazca.ui.AntialiasedLabel();
        detailPane = new javax.swing.JPanel();
        detailedInformationPanel1 = new com.yz.rms.client.ui.DetailedInformationPanel();
        nWaitingPanel3 = new com.nazca.ui.NWaitingPanel();
        antialiasedLabel2 = new com.nazca.ui.AntialiasedLabel();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setResizeWeight(0.2);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(700, 79));

        statePane.setMinimumSize(new java.awt.Dimension(41, 27));
        statePane.setPreferredSize(new java.awt.Dimension(21, 37));
        statePane.setLayout(new java.awt.BorderLayout());

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        stateRefreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/refresh_16.png"))); // NOI18N
        stateRefreshBtn.setText("刷新");
        stateRefreshBtn.setFocusable(false);
        stateRefreshBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        stateRefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateRefreshBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(stateRefreshBtn);

        statePane.add(jToolBar2, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(nGroupList1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1, "CONTENT");
        jPanel2.add(nWaitingPanel1, "WAIT");

        statePane.add(jPanel2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(statePane);

        jSplitPane2.setResizeWeight(0.8);
        jSplitPane2.setPreferredSize(new java.awt.Dimension(10, 10));

        espensePane.setMinimumSize(new java.awt.Dimension(600, 75));
        espensePane.setPreferredSize(new java.awt.Dimension(665, 454));
        espensePane.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("项目：");
        jToolBar3.add(jLabel1);

        projectCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectCmBox.setMaximumSize(new java.awt.Dimension(267, 67));
        projectCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                projectCmBoxItemStateChanged(evt);
            }
        });
        jToolBar3.add(projectCmBox);
        jToolBar3.add(filler2);

        jLabel7.setText("团队：");
        jToolBar3.add(jLabel7);

        teamCmBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        teamCmBox.setMaximumSize(new java.awt.Dimension(267, 67));
        teamCmBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                teamCmBoxItemStateChanged(evt);
            }
        });
        jToolBar3.add(teamCmBox);
        jToolBar3.add(filler3);

        jLabel8.setText("报销时间：");
        jToolBar3.add(jLabel8);

        dateRangePic.setMaximumSize(new java.awt.Dimension(276, 76));
        jToolBar3.add(dateRangePic);

        jPanel3.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        expenseRefreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/refresh_16.png"))); // NOI18N
        expenseRefreshBtn.setText("刷新");
        expenseRefreshBtn.setFocusable(false);
        expenseRefreshBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        expenseRefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expenseRefreshBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(expenseRefreshBtn);

        auditBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/16.png"))); // NOI18N
        auditBtn.setText("审核");
        auditBtn.setFocusable(false);
        auditBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        auditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                auditBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(auditBtn);

        markPaidBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/16.png"))); // NOI18N
        markPaidBtn.setText("标记发放");
        markPaidBtn.setFocusable(false);
        markPaidBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        markPaidBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markPaidBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(markPaidBtn);

        waitPayBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/16.png"))); // NOI18N
        waitPayBtn.setText("待发放金额总计");
        waitPayBtn.setFocusable(false);
        waitPayBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        waitPayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waitPayBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(waitPayBtn);
        jToolBar1.add(filler4);

        searchTxFd.setMaximumSize(new java.awt.Dimension(247, 247));
        searchTxFd.setMinimumSize(new java.awt.Dimension(80, 21));
        searchTxFd.setPreferredSize(new java.awt.Dimension(100, 21));
        searchTxFd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxFdActionPerformed(evt);
            }
        });
        jToolBar1.add(searchTxFd);

        jPanel3.add(jToolBar1, java.awt.BorderLayout.NORTH);

        espensePane.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.CardLayout());

        expenseFormPane.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        expenseTableComp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(expenseTableComp);

        expenseFormPane.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        expenseFormPane.add(pagePane, java.awt.BorderLayout.PAGE_END);

        jPanel5.add(expenseFormPane, "CONTENT");
        jPanel5.add(nWaitingPanel2, "WAIT");

        antialiasedLabel1.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel1.setText("暂无报销单");
        antialiasedLabel1.setFont(antialiasedLabel1.getFont().deriveFont(antialiasedLabel1.getFont().getStyle() | java.awt.Font.BOLD, 20));
        jPanel5.add(antialiasedLabel1, "EMPTY");

        espensePane.add(jPanel5, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(espensePane);

        detailPane.setMinimumSize(new java.awt.Dimension(180, 22));
        detailPane.setPreferredSize(new java.awt.Dimension(384, 1044));
        detailPane.setLayout(new java.awt.CardLayout());
        detailPane.add(detailedInformationPanel1, "CONTENT");
        detailPane.add(nWaitingPanel3, "WAIT");

        antialiasedLabel2.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel2.setText("暂无详细信息");
        antialiasedLabel2.setFont(antialiasedLabel2.getFont().deriveFont(antialiasedLabel2.getFont().getStyle() | java.awt.Font.BOLD, antialiasedLabel2.getFont().getSize()+8));
        detailPane.add(antialiasedLabel2, "EMPTY");

        jSplitPane2.setRightComponent(detailPane);

        jSplitPane1.setRightComponent(jSplitPane2);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    private void auditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_auditBtnActionPerformed
        AuditExpenseFormPanel auditPanel = new AuditExpenseFormPanel();
        auditPanel.setExpenseFormWrap(expenseFormWrap);
        boolean b = auditPanel.showMe(this);
        if (b) {
            stateRefreshBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_auditBtnActionPerformed

    private void markPaidBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markPaidBtnActionPerformed
        String MARK_PAID_TMPL;
        int a[] = expenseTableComp.getSelectedRows();
        List<String> expenseFormIdList = new ArrayList<>();
        if (a.length > 0) {
            for (int i = 0; i < a.length; i++) {
                int index = a[i];
                index = expenseTableComp.convertRowIndexToModel(index);
                expenseFormIdList.add(model.getData(index).getExpenseForm().getExpenseId());
            }
            markPaidAgent.setExpenseFormIdList(expenseFormIdList);
        }
        SimpleOperationPanel<Boolean> simpleOperationPanel = new SimpleOperationPanel<>(markPaidAgent);
        if (a.length > 1) {
            MARK_PAID_TMPL
                    = "<html>确认将“$objName”等人的报销单标记为已发放吗？<br />"
                    + "<font color=\"#c47f2d\">提示：您已勾选$objCount条报销单。</font></html>";
        } else {
            MARK_PAID_TMPL
                    = "<html>确认将“$objName”的报销单标记为已发放吗？<br />"
                    + "<font color=\"#c47f2d\">提示：您已勾选$objCount条报销单。</font></html>";
        }
        String KEY_OBJ_NAME = "$objName";
        String KEY_OBJ_COUNT = "$objCount";
        String perId = model.getData(a[0]).getExpenseForm().getExpensePersonId();
        String objName = USMSUserSyncTool.getInstance().getUserNameById(perId);
        int objCount = a.length;
        if (objName == null) {
            objName = "";
        }
        String str = MARK_PAID_TMPL.replace(KEY_OBJ_NAME, objName).replace(KEY_OBJ_COUNT, Integer.toString(objCount));
        simpleOperationPanel.configDeteleMsg(str);
        NComponentStyleTool.goodNewsStyle(simpleOperationPanel.getOKCancelPanel().getOKButton());
        boolean b = simpleOperationPanel.showMe(this, ResourceUtil.buildImageIcon("32.png"), "标记已发放");

        if (b) {
            stateRefreshBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_markPaidBtnActionPerformed

    private void waitPayBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_waitPayBtnActionPerformed
        WaitPaystatPane waitPaystatPane = new WaitPaystatPane(map);
        waitPaystatPane.showMe(this);
    }//GEN-LAST:event_waitPayBtnActionPerformed

    private void stateRefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateRefreshBtnActionPerformed
        queryExpenseStateCountAgent.start();
    }//GEN-LAST:event_stateRefreshBtnActionPerformed

    private void expenseRefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expenseRefreshBtnActionPerformed
        pageSession = pagePane.getPageSession();
        if (projectCmBox.isVisible() && projectComboxModel.getSelectedItem() != null) {
            queryExpenseformAgent.setProjectId(projectComboxModel.getSelectedItem().getProjectId());
        } else {
            queryExpenseformAgent.setProjectId(null);
        }
        if (teamCmBox.isVisible() && teamComboxModel.getSelectedItem() != null) {
            queryExpenseformAgent.setTeamId(teamComboxModel.getSelectedItem().getTeamId());
        } else {
            queryExpenseformAgent.setTeamId(null);
        }
        if (dateRangePic.isVisible() && dateRangePic.getStartDate() != null) {
            queryExpenseformAgent.setStartTime(startDate);
        } else {
            queryExpenseformAgent.setStartTime(null);
        }
        if (dateRangePic.isVisible() && dateRangePic.getEndDate() != null) {
            queryExpenseformAgent.setEndTime(endDate);
        } else {
            queryExpenseformAgent.setEndTime(null);
        }
        queryExpenseformAgent.setState(curWrap.getState());
        queryExpenseformAgent.setCurPage(pageSession.getCurPageNum());
        queryExpenseformAgent.setPageSize(pageSession.getPageSize());
        queryExpenseformAgent.start();
//                    queryExpenseformAgent.setCurPage(1);
//                    queryExpenseformAgent.setPageSize(10);
    }//GEN-LAST:event_expenseRefreshBtnActionPerformed

    private void searchTxFdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxFdActionPerformed
        if (!StringUtil.isEmpty(searchTxFd.getText().trim())) {
            String keyWord = searchTxFd.getText().trim();
            List<ExpenseFormWrap> datas = null;
            List<Integer> idxs = new ArrayList<>();
            datas = model.getDatas();
            for (ExpenseFormWrap data : datas) {
                String perId = data.getExpenseForm().getExpensePersonId();
                String name = USMSUserSyncTool.getInstance().getUserNameById(perId);
                if (name != null && name.contains(keyWord)) {
                    int idx = model.getDataRow(data);
                    if (idx > -1) {
                        idx = expenseTableComp.convertRowIndexToView(idx);
                        idxs.add(idx);
                    }
                }
            }
            int idxsLen = idxs.size();
            int idx;
            if (idxsLen == 0) {
                expenseTableComp.setRowSelectionInterval(0, 0);
            } else {
                if (oldKeyWord != null && oldKeyWord.equals(keyWord)) {
                    currLen = currLen % idxsLen;
                    idx = idxs.get(currLen);
                } else {
                    currLen = 0;
                    idx = idxs.get(0);
                }
                expenseTableComp.setRowSelectionInterval(idx, idx);
                currLen++;
                oldKeyWord = keyWord;
            }
        }
    }//GEN-LAST:event_searchTxFdActionPerformed

    private void projectCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_projectCmBoxItemStateChanged
        if (projectComboxModel.getSelectedItem() != null) {
            queryExpenseformAgent.setProjectId(projectComboxModel.getSelectedItem().getProjectId());
        } else {
            queryExpenseformAgent.setProjectId(null);
        }
        queryExpenseformAgent.setState(curWrap.getState());
        pageSession = pagePane.getPageSession();
        queryExpenseformAgent.setCurPage(pageSession.getCurPageNum());
        queryExpenseformAgent.setPageSize(pageSession.getPageSize());
        queryExpenseformAgent.setStartTime(startDate);
        queryExpenseformAgent.setEndTime(endDate);
        queryExpenseformAgent.start();
    }//GEN-LAST:event_projectCmBoxItemStateChanged

    private void teamCmBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_teamCmBoxItemStateChanged
        if (teamComboxModel.getSelectedItem() != null) {
            queryExpenseformAgent.setTeamId(teamComboxModel.getSelectedItem().getTeamId());
        } else {
            queryExpenseformAgent.setTeamId(null);
        }
        queryExpenseformAgent.setState(curWrap.getState());
        pageSession = pagePane.getPageSession();
        queryExpenseformAgent.setCurPage(pageSession.getCurPageNum());
        queryExpenseformAgent.setPageSize(pageSession.getPageSize());
        queryExpenseformAgent.setStartTime(startDate);
        queryExpenseformAgent.setEndTime(endDate);
        queryExpenseformAgent.start();
    }//GEN-LAST:event_teamCmBoxItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nazca.ui.AntialiasedLabel antialiasedLabel1;
    private com.nazca.ui.AntialiasedLabel antialiasedLabel2;
    private javax.swing.JButton auditBtn;
    private com.nazca.ui.NDateRangePicker dateRangePic;
    private javax.swing.JPanel detailPane;
    private com.yz.rms.client.ui.DetailedInformationPanel detailedInformationPanel1;
    private javax.swing.JPanel espensePane;
    private javax.swing.JPanel expenseFormPane;
    private javax.swing.JButton expenseRefreshBtn;
    private javax.swing.JTable expenseTableComp;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JButton markPaidBtn;
    private com.nazca.ui.NGroupList nGroupList1;
    private com.nazca.ui.NWaitingPanel nWaitingPanel1;
    private com.nazca.ui.NWaitingPanel nWaitingPanel2;
    private com.nazca.ui.NWaitingPanel nWaitingPanel3;
    private com.nazca.ui.pagination.PaginationPanel pagePane;
    private javax.swing.JComboBox<String> projectCmBox;
    private javax.swing.JTextField searchTxFd;
    private javax.swing.JPanel statePane;
    private javax.swing.JButton stateRefreshBtn;
    private javax.swing.JComboBox<String> teamCmBox;
    private javax.swing.JButton waitPayBtn;
    // End of variables declaration//GEN-END:variables
    /**
     * 状态列表listener
     */
    private final AgentListener<List<ExpenseFormStateCountWrap>> queryExpenseStateCountAgentLis = new AgentListener<List<ExpenseFormStateCountWrap>>() {
        @Override
        public void onStarted(long seq) {
            timeSeq = seq;
            stateRefreshBtn.setEnabled(false);
            expenseRefreshBtn.setEnabled(false);
            setExpenseFormBtnEnabled(false);
            nWaitingPanel1.showWaitingMode("数据加载中，请稍后...");
            nWaitingPanel1.setIndeterminate(true);
            leftCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel2.showWaitingMode("数据加载中，请稍后...");
            nWaitingPanel2.setIndeterminate(true);
            centerCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel3.showWaitingMode("数据加载中，请稍后...");
            nWaitingPanel3.setIndeterminate(true);
            rightCard.show(CardLayoutWrapper.WAIT);
            nGroupList1.removeAll();
        }

        @Override
        public void onSucceeded(List<ExpenseFormStateCountWrap> result, long seq) {
            if (timeSeq == seq) {
                int idx = 0;
                nWaitingPanel1.setIndeterminate(false);
                stateRefreshBtn.setEnabled(true);
                expenseRefreshBtn.setEnabled(true);
                setExpenseFormBtnEnabled(true);
                if (result != null && !result.isEmpty()) {
                    for (ExpenseFormStateCountWrap wrap : result) {
                        NGroupListNodePane node = new NGroupListNodePane();
                        node.setLeftLabelText(wrap.getState().toString());
                        node.setRightLabelText(String.valueOf(wrap.getCount()));
                        nGroupList1.add(node);
                        CellItem<ExpenseFormStateCountWrap> cellItem = new CellItem(wrap);
                        nGroupList1.addGroup(cellItem, node, idx);
                        idx++;
                    }
                    nGroupList1.setSelectedGroupOrItem((CellItem) nGroupList1.getGroupList().get(0));
                    leftCard.show(CardLayoutWrapper.CONTENT);
                }
            }
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            stateRefreshBtn.setEnabled(true);
            expenseRefreshBtn.setEnabled(false);
            setExpenseFormBtnEnabled(false);
            leftCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel1.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_ERROR);
            centerCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel2.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_ERROR);
            rightCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel3.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_ERROR);
        }
    };

    /**
     * 报销单列表listener
     */
    private final AgentListener<PageResult<ExpenseFormWrap>> queryExpenseFormLis = new AgentListener<PageResult<ExpenseFormWrap>>() {
        @Override
        public void onStarted(long seq) {
            timeSeqExpense = seq;
            expenseRefreshBtn.setEnabled(false);
            setExpenseFormBtnEnabled(false);
            nWaitingPanel2.showWaitingMode("数据加载中，请稍后...");
            nWaitingPanel2.setIndeterminate(true);
            centerCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel3.showWaitingMode("数据加载中，请稍后...");
            nWaitingPanel3.setIndeterminate(true);
            rightCard.show(CardLayoutWrapper.WAIT);
        }

        @Override
        public void onSucceeded(PageResult<ExpenseFormWrap> result, long seq) {
            nWaitingPanel2.setIndeterminate(false);//关闭等待面板
            nWaitingPanel3.setIndeterminate(false);
            if (timeSeqExpense == seq) {
                expenseRefreshBtn.setEnabled(true);
                if (result != null && result.getTotalCount() > 0) {
                    setExpenseFormBtnEnabled(true);
                    List<ExpenseFormWrap> expenseList = result.getPageList();
                    int totalCount = result.getTotalCount();
                    int pageSize = result.getPageSize();
                    model.setDatas(expenseList);
                    centerCard.show(CardLayoutWrapper.CONTENT);
                    rightCard.show(CardLayoutWrapper.CONTENT);
                    expenseTableComp.getSelectionModel().setSelectionInterval(0, 0);
                    pagePane.initPageButKeepSession(totalCount, pageSize);
                } else {
                    setExpenseFormBtnEnabled(true);
                    auditBtn.setEnabled(false);
                    markPaidBtn.setEnabled(false);
                    waitPayBtn.setEnabled(false);
                    centerCard.show(CardLayoutWrapper.EMPTY);
                    rightCard.show(CardLayoutWrapper.EMPTY);
                }
            }
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            expenseRefreshBtn.setEnabled(true);
            centerCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel2.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_ERROR);
            rightCard.show(CardLayoutWrapper.WAIT);
            nWaitingPanel3.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_ERROR);
        }
    };

    /**
     * 项目下拉框listener
     */
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

    /**
     * 团队下拉框listener
     */
    private final AgentListener<List<Team>> queryTeamAgentLis = new AgentListener<List<Team>>() {
        @Override
        public void onStarted(long seq) {
        }

        @Override
        public void onSucceeded(List<Team> result, long seq) {
            result.add(null);
            Collections.reverse(result);
            teamComboxModel.setObjectList(result);
            teamCmBox.setSelectedIndex(0);
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
        }
    };

    private AgentListener< Map<Integer, List<WaitPayStatWrap>>> queryWaitPayStatLis
            = new AgentListener<Map<Integer, List<WaitPayStatWrap>>>() {
        @Override
        public void onStarted(long seq) {
        }

        @Override
        public void onSucceeded(
                Map<Integer, List<WaitPayStatWrap>> result, long seq) {
            map = result;
        }

        @Override
        public void onFailed(String errMsg, int errCode,
                long seq) {
        }
    };

}
