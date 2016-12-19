/*
 * ExpenseFormTabPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 13:31:56
 */
package com.yz.rms.client.ui.expenseform;

import com.nazca.sql.PageResult;
import com.nazca.ui.NDateRangePickerListener;
import com.nazca.ui.NProcessFlowNode;
import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.laf.border.IconLabelBorder;
import com.nazca.ui.model.SimpleObjectListModel;
import com.nazca.ui.pagination.PaginationListener;
import com.nazca.ui.pagination.TablePageSession;
import com.nazca.ui.util.CardLayoutWrapper;
import com.yz.rms.client.agent.expenseform.QueryAllProjectAgent;
import com.yz.rms.client.agent.expenseform.DeleteExpenseFormAgent;
import com.yz.rms.client.agent.expenseform.QueryExpenseStateCountAgent;
import com.yz.rms.client.agent.expenseform.QueryMyExpenseFormAgent;
import com.yz.rms.client.agent.expenseform.SubmitExpenseFormAgent;
import com.yz.rms.client.agent.project.QueryProjectAgent;
import com.yz.rms.client.agent.stat.QueyAllProjectAgent;
import com.yz.rms.client.ui.DeleteOperationPanel;
import com.yz.rms.client.ui.SimpleOperationPanel;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.expenseform.ExpenseTableModel;
import com.yz.rms.client.renderer.ExpenseFormStateRender;
import com.yz.rms.client.renderer.TeamCmBoxListRenderer;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 我的报销单
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class ExpenseFormTabPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = -7273993156880268785L;

    private IconLabelBorder leftBorder = null;
    private IconLabelBorder rightBorder = null;
    private CardLayoutWrapper contentLayout = null;
    private CardLayoutWrapper infoLayout = null;
    private QueryMyExpenseFormAgent queryMyExpenseFormAgent = null;
    private DeleteExpenseFormAgent deleteExpenseFormAgent = null;
    private SubmitExpenseFormAgent submitExpenseFormAgent = null;
    private QueyAllProjectAgent queyAllProjectAgent = null;
    private long timeSeq = 0;
    private ExpenseTableModel expenseTableModel = new ExpenseTableModel();
    private ExpenseFormWrap curExpenseForm;
    QueryExpenseStateCountAgent queryExpenseStateCountAgent = null;
    QueryProjectAgent queryProjectAgent = null;
    ExpenseFormStateEnums expenseFormStateEnums = null;
    private final SimpleObjectListModel<Project> projectComboxModel = new SimpleObjectListModel<>();
    private final SimpleObjectListModel<ExpenseFormStateEnums> stateListModel = new SimpleObjectListModel<>();
    private TeamCmBoxListRenderer teamCmBoxListRenderer;
    private ExpenseFormStateRender expenseFormStateRender;
    private TablePageSession pageSession;
    private QueryAllProjectAgent queryAllProjectAgent = null;
    private final SimpleObjectListModel<Project> projectComboxModelforAddP = new SimpleObjectListModel<>();
    private static final Double expenseTotalLimit = 2000.0;
    private boolean isAdd = false;

    /**
     * Creates new form ExpenseFormTabPanel
     */
    public ExpenseFormTabPanel() {
	initComponents();
	initUI();
	initModel();
	initRenderer();
	initAgentAndListener();
	onInit();
    }

    private void initUI() {
	leftBorder = new IconLabelBorder(getClass().getResource(
		"/com/yz/rms/client/res/32.png"), "报销单列表");
	rightBorder = new IconLabelBorder(getClass().getResource(
		"/com/yz/rms/client/res/32.png"), "详细信息");
	leftPane.setBorder(leftBorder);
	rightPane.setBorder(rightBorder);
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.MONTH, -2);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	dateRangePic.setDateRange(calendar.getTime(), new Date());
	nProcessFlowPanel1.setNodeBaseline(0.5f);
	jScrollPane2.setBorder(null);
    }

    private void initModel() {
	expenseFormTable.setModel(expenseTableModel);
	projectcbBox.setModel(projectComboxModel);
	statecbBox.setModel(stateListModel);
	List<ExpenseFormStateEnums> list = new ArrayList<>();
	list.add(0, null);
	list.addAll(Arrays.asList(ExpenseFormStateEnums.values()));
	stateListModel.setObjectList(list);
	statecbBox.setRenderer(new TeamCmBoxListRenderer());
    }

    private void initRenderer() {
	teamCmBoxListRenderer = new TeamCmBoxListRenderer();
	projectcbBox.setRenderer(teamCmBoxListRenderer);
    }

    private void initAgentAndListener() {
	queryMyExpenseFormAgent = new QueryMyExpenseFormAgent();
	deleteExpenseFormAgent = new DeleteExpenseFormAgent();
	submitExpenseFormAgent = new SubmitExpenseFormAgent();
	queyAllProjectAgent = new QueyAllProjectAgent();
	queryMyExpenseFormAgent.addListener(queryMyExpenseAgentListener);
	queyAllProjectAgent.addListener(queryProjectAgentLis);
	queryAllProjectAgent = new QueryAllProjectAgent();
	queryAllProjectAgent.addListener(queyProjectAgentLis);
	queryAllProjectAgent.start();
	contentLayout = new CardLayoutWrapper(cardPanel);
	infoLayout = new CardLayoutWrapper(rightPane);

	expenseFormTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent lse) {
		if (!lse.getValueIsAdjusting()) {
		    int index = expenseFormTable.getSelectedRow();
		    if (index >= 0) {
			index = expenseFormTable.convertRowIndexToModel(index);
			curExpenseForm = expenseTableModel.getData(index);
			curExpenseForm = selectingExpenseForm();
			detailedInformationPanel1.setParama(curExpenseForm);
			addNode();
		    } else {
			detailedInformationPanel1.setParama(null);
			nProcessFlowPanel1.removeAll();
		    }
		}
	    }
	});

	//时间范围搜索事件触发
	dateRangePic.addDateRangePickerListener(new NDateRangePickerListener() {
	    @Override
	    public void onDateRangeSelected(Date startDate, Date endDate) {
		Project project = projectComboxModel.getSelectedItem();
		String projectId = null;
		if (project != null) {
		    projectId = project.getProjectId();
		}
		pageSession = paginationPanel1.getPageSession();
		queryMyExpenseFormAgent.setParam(stateListModel.getSelectedItem(), projectId, startDate, endDate, pageSession.getCurPageNum(), pageSession.getPageSize());
		queryMyExpenseFormAgent.start();
	    }
	});

	paginationPanel1.addPaginationListener(new PaginationListener() {
	    @Override
	    public void onPageChanged(TablePageSession tps) {
//		refreshBtn.doClick();
		refreshBtnActionPerformed(null);
	    }
	});
    }

    void onInit() {
	statecbBox.setSelectedIndex(0);
	//获取分页信息
	pageSession = this.paginationPanel1.getPageSession();
	queryMyExpenseFormAgent.setParam(stateListModel.getSelectedItem(), projectComboxModel.getSelectedItem() != null ? projectComboxModel.getSelectedItem().getProjectId() : null, dateRangePic.getStartDate(), dateRangePic.getEndDate(), pageSession.getCurPageNum(), pageSession.getPageSize());
	//查询项目列表
	queryMyExpenseFormAgent.start();
	queyAllProjectAgent.start();
	//单行
	expenseFormTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setButtonsEnabled(boolean flag) {
	createBtn.setEnabled(flag);
	deleteBtn.setEnabled(flag);
	refreshBtn.setEnabled(flag);
	modifyBtn.setEnabled(flag);
	printBtn.setEnabled(flag);
	commitBtn.setEnabled(flag);
	stateLabel.setEnabled(flag);
	statecbBox.setEnabled(flag);
	projectLabel.setEnabled(flag);
	projectcbBox.setEnabled(flag);
	expenseTimeLabel.setEnabled(flag);
	dateRangePic.setEnabled(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoPanel = new javax.swing.JPanel();
        cityTrafficLabel = new javax.swing.JLabel();
        cityTrafficTextField = new javax.swing.JTextField();
        travelMealsLabel = new javax.swing.JLabel();
        travelMealsTextField = new javax.swing.JTextField();
        booksMaterialsLabel = new javax.swing.JLabel();
        booksMaterialsTextField = new javax.swing.JTextField();
        travelAllowanceTextField = new javax.swing.JTextField();
        travelAllowanceLabel = new javax.swing.JLabel();
        cityGasolineLabel = new javax.swing.JLabel();
        cityGasolineTextField = new javax.swing.JTextField();
        travelAccommodationLabel = new javax.swing.JLabel();
        travelAccommodationTextField = new javax.swing.JTextField();
        copyBindLabel = new javax.swing.JLabel();
        copyBindTextField = new javax.swing.JTextField();
        travelTrafficLabel = new javax.swing.JLabel();
        travelTrafficTextField = new javax.swing.JTextField();
        entertainLabel = new javax.swing.JLabel();
        entertainTextField = new javax.swing.JTextField();
        spacePageLabel = new javax.swing.JLabel();
        spacePageTextField = new javax.swing.JTextField();
        materialTextField = new javax.swing.JTextField();
        materialLabel = new javax.swing.JLabel();
        conferencesTextField = new javax.swing.JTextField();
        conferencesLabel = new javax.swing.JLabel();
        trainTextField = new javax.swing.JTextField();
        trainLabel = new javax.swing.JLabel();
        fieldOperationTextField = new javax.swing.JTextField();
        fieldOperationLabel = new javax.swing.JLabel();
        officeSuppliesLabel = new javax.swing.JLabel();
        officeSuppliesTextField = new javax.swing.JTextField();
        telephoneBillLabel = new javax.swing.JLabel();
        telephoneBillTextField = new javax.swing.JTextField();
        postageLabel = new javax.swing.JLabel();
        postageTextField = new javax.swing.JTextField();
        totalLabel = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        nProcessFlowPanel2 = new com.nazca.ui.NProcessFlowPanel();
        jToolBar2 = new javax.swing.JToolBar();
        refreshBtn = new javax.swing.JButton();
        createBtn = new javax.swing.JButton();
        modifyBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        commitBtn = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        stateLabel = new javax.swing.JLabel();
        statecbBox = new javax.swing.JComboBox();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        projectLabel = new javax.swing.JLabel();
        projectcbBox = new javax.swing.JComboBox();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        expenseTimeLabel = new javax.swing.JLabel();
        dateRangePic = new com.nazca.ui.NDateRangePicker();
        jSplitPane1 = new javax.swing.JSplitPane();
        leftPane = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        jSplitPaneContent = new javax.swing.JSplitPane();
        upPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        expenseFormTable = new javax.swing.JTable();
        paginationPanel1 = new com.nazca.ui.pagination.PaginationPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        nProcessFlowPanel1 = new com.nazca.ui.NProcessFlowPanel();
        waitPanel1 = new com.nazca.ui.NWaitingPanel();
        noContentPanelLeft = new javax.swing.JPanel();
        antialiasedLabel1 = new com.nazca.ui.AntialiasedLabel();
        rightPane = new javax.swing.JPanel();
        waitPane12 = new com.nazca.ui.NWaitingPanel();
        detailedInformationPanel1 = new com.yz.rms.client.ui.DetailedInformationPanel();
        noContentPanelRight = new javax.swing.JPanel();
        antialiasedLabel2 = new com.nazca.ui.AntialiasedLabel();

        cityTrafficLabel.setText("市内交通费(元)：");

        cityTrafficTextField.setEditable(false);

        travelMealsLabel.setText("出差餐费(元)：");

        travelMealsTextField.setEditable(false);

        booksMaterialsLabel.setText("图书资料费(元)：");

        booksMaterialsTextField.setEditable(false);

        travelAllowanceTextField.setEditable(false);

        travelAllowanceLabel.setText("出差补助费(元)：");

        cityGasolineLabel.setText("市内汽油费(元)：");

        cityGasolineTextField.setEditable(false);

        travelAccommodationLabel.setText("出差住宿费(元)：");

        travelAccommodationTextField.setEditable(false);

        copyBindLabel.setText("复印装订费(元)：");

        copyBindTextField.setEditable(false);

        travelTrafficLabel.setText("出差交通费(元)：");

        travelTrafficTextField.setEditable(false);

        entertainLabel.setText("室内招待费(元)：");

        entertainTextField.setEditable(false);

        spacePageLabel.setText("版面费(元)：");

        spacePageTextField.setEditable(false);

        materialTextField.setEditable(false);

        materialLabel.setText("材料费(元)：");

        conferencesTextField.setEditable(false);

        conferencesLabel.setText("会议费(元)：");

        trainTextField.setEditable(false);

        trainLabel.setText("培训费(元)：");

        fieldOperationTextField.setEditable(false);

        fieldOperationLabel.setText("外勤费(元)：");

        officeSuppliesLabel.setText("办公用品费(元)：");

        officeSuppliesTextField.setEditable(false);

        telephoneBillLabel.setText("电话费(元)：");

        telephoneBillTextField.setEditable(false);

        postageLabel.setText("邮费(元)：");

        postageTextField.setEditable(false);

        totalLabel.setText("合计(元)：");

        totalTextField.setEditable(false);

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalLabel)
                    .addComponent(postageLabel)
                    .addComponent(telephoneBillLabel)
                    .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cityTrafficLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(travelMealsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(booksMaterialsLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(travelAllowanceLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cityGasolineLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(travelAccommodationLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(copyBindLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(travelTrafficLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(entertainLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spacePageLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(materialLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(conferencesLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(trainLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(officeSuppliesLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(fieldOperationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cityTrafficTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(travelMealsTextField)
                    .addComponent(booksMaterialsTextField)
                    .addComponent(travelAllowanceTextField)
                    .addComponent(cityGasolineTextField)
                    .addComponent(travelAccommodationTextField)
                    .addComponent(copyBindTextField)
                    .addComponent(travelTrafficTextField)
                    .addComponent(entertainTextField)
                    .addComponent(spacePageTextField)
                    .addComponent(materialTextField)
                    .addComponent(conferencesTextField)
                    .addComponent(trainTextField)
                    .addComponent(fieldOperationTextField)
                    .addComponent(officeSuppliesTextField)
                    .addComponent(telephoneBillTextField)
                    .addComponent(postageTextField)
                    .addComponent(totalTextField))
                .addContainerGap())
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityTrafficLabel)
                    .addComponent(cityTrafficTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(travelMealsLabel)
                    .addComponent(travelMealsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(booksMaterialsLabel)
                    .addComponent(booksMaterialsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(travelAllowanceLabel)
                    .addComponent(travelAllowanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityGasolineLabel)
                    .addComponent(cityGasolineTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(travelAccommodationLabel)
                    .addComponent(travelAccommodationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyBindLabel)
                    .addComponent(copyBindTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(travelTrafficLabel)
                    .addComponent(travelTrafficTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(entertainLabel)
                    .addComponent(entertainTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spacePageLabel)
                    .addComponent(spacePageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(materialLabel)
                    .addComponent(materialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conferencesLabel)
                    .addComponent(conferencesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trainLabel)
                    .addComponent(trainTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldOperationLabel)
                    .addComponent(fieldOperationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(officeSuppliesLabel)
                    .addComponent(officeSuppliesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telephoneBillTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telephoneBillLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postageLabel)
                    .addComponent(postageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        nProcessFlowPanel2.setPreferredSize(new java.awt.Dimension(0, 0));
        jScrollPane3.setViewportView(nProcessFlowPanel2);

        setMinimumSize(new java.awt.Dimension(1000, 31));
        setPreferredSize(new java.awt.Dimension(1000, 31));
        setLayout(new java.awt.BorderLayout());

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/refresh_16.png"))); // NOI18N
        refreshBtn.setText("刷新");
        refreshBtn.setFocusable(false);
        refreshBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(refreshBtn);

        createBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/add_16.png"))); // NOI18N
        createBtn.setText("添加");
        createBtn.setFocusable(false);
        createBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(createBtn);

        modifyBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/update-16.png"))); // NOI18N
        modifyBtn.setText("修改");
        modifyBtn.setFocusable(false);
        modifyBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        modifyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(modifyBtn);

        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/delete_16.png"))); // NOI18N
        deleteBtn.setText("删除");
        deleteBtn.setFocusable(false);
        deleteBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(deleteBtn);

        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/16.png"))); // NOI18N
        printBtn.setText("打印");
        printBtn.setFocusable(false);
        printBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(printBtn);

        commitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/16.png"))); // NOI18N
        commitBtn.setText("提交");
        commitBtn.setFocusable(false);
        commitBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        commitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(commitBtn);
        jToolBar2.add(filler7);
        jToolBar2.add(filler1);

        stateLabel.setText("状态：");
        jToolBar2.add(stateLabel);

        statecbBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        statecbBox.setMaximumSize(new java.awt.Dimension(135, 25));
        statecbBox.setMinimumSize(new java.awt.Dimension(135, 25));
        statecbBox.setName(""); // NOI18N
        statecbBox.setPreferredSize(new java.awt.Dimension(135, 25));
        statecbBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                statecbBoxItemStateChanged(evt);
            }
        });
        statecbBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statecbBoxActionPerformed(evt);
            }
        });
        jToolBar2.add(statecbBox);
        jToolBar2.add(filler2);

        projectLabel.setText("项目：");
        jToolBar2.add(projectLabel);

        projectcbBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectcbBox.setMaximumSize(new java.awt.Dimension(160, 25));
        projectcbBox.setMinimumSize(new java.awt.Dimension(160, 25));
        projectcbBox.setPreferredSize(new java.awt.Dimension(160, 25));
        projectcbBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                projectcbBoxItemStateChanged(evt);
            }
        });
        jToolBar2.add(projectcbBox);
        jToolBar2.add(filler3);

        expenseTimeLabel.setText("报销时间：");
        jToolBar2.add(expenseTimeLabel);

        dateRangePic.setMaximumSize(new java.awt.Dimension(260, 25));
        dateRangePic.setMinimumSize(new java.awt.Dimension(260, 25));
        dateRangePic.setPreferredSize(new java.awt.Dimension(260, 25));
        jToolBar2.add(dateRangePic);

        add(jToolBar2, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerSize(7);
        jSplitPane1.setResizeWeight(0.7);
        jSplitPane1.setMaximumSize(new java.awt.Dimension(0, 0));
        jSplitPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        leftPane.setAlignmentX(0.7F);
        leftPane.setAlignmentY(0.7F);
        leftPane.setMinimumSize(new java.awt.Dimension(0, 0));
        leftPane.setPreferredSize(new java.awt.Dimension(0, 0));
        leftPane.setLayout(new java.awt.BorderLayout());

        cardPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        cardPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        cardPanel.setLayout(new java.awt.CardLayout());

        jSplitPaneContent.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneContent.setResizeWeight(0.8);
        jSplitPaneContent.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        upPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        upPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        upPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));

        expenseFormTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        expenseFormTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "报销时间", "报销金额", "报销项目", "当前状态"
            }
        ));
        expenseFormTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        expenseFormTable.setMinimumSize(new java.awt.Dimension(0, 0));
        expenseFormTable.setName(""); // NOI18N
        expenseFormTable.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(expenseFormTable);

        upPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        upPanel.add(paginationPanel1, java.awt.BorderLayout.PAGE_END);

        jSplitPaneContent.setTopComponent(upPanel);

        jScrollPane2.setViewportView(nProcessFlowPanel1);

        jSplitPaneContent.setBottomComponent(jScrollPane2);

        cardPanel.add(jSplitPaneContent, "CONTENT");

        waitPanel1.setPreferredSize(new java.awt.Dimension(0, 0));
        cardPanel.add(waitPanel1, "WAIT");

        noContentPanelLeft.setMinimumSize(new java.awt.Dimension(0, 0));
        noContentPanelLeft.setPreferredSize(new java.awt.Dimension(0, 0));
        noContentPanelLeft.setLayout(new java.awt.BorderLayout());

        antialiasedLabel1.setBackground(new java.awt.Color(255, 0, 51));
        antialiasedLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 50, 50, 50));
        antialiasedLabel1.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel1.setText("暂无报销单信息");
        antialiasedLabel1.setFont(antialiasedLabel1.getFont().deriveFont(antialiasedLabel1.getFont().getStyle() | java.awt.Font.BOLD, antialiasedLabel1.getFont().getSize()+8));
        noContentPanelLeft.add(antialiasedLabel1, java.awt.BorderLayout.CENTER);

        cardPanel.add(noContentPanelLeft, "EMPTY");

        leftPane.add(cardPanel, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(leftPane);

        rightPane.setAlignmentX(0.3F);
        rightPane.setAlignmentY(0.3F);
        rightPane.setAutoscrolls(true);
        rightPane.setEnabled(false);
        rightPane.setMaximumSize(new java.awt.Dimension(0, 0));
        rightPane.setLayout(new java.awt.CardLayout());

        waitPane12.setPreferredSize(new java.awt.Dimension(0, 0));
        rightPane.add(waitPane12, "WAIT");

        detailedInformationPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        detailedInformationPanel1.setPreferredSize(new java.awt.Dimension(0, 0));
        rightPane.add(detailedInformationPanel1, "CONTENT");

        noContentPanelRight.setMinimumSize(new java.awt.Dimension(0, 0));
        noContentPanelRight.setPreferredSize(new java.awt.Dimension(0, 0));
        noContentPanelRight.setLayout(new java.awt.BorderLayout());

        antialiasedLabel2.setBackground(new java.awt.Color(255, 0, 51));
        antialiasedLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 50, 50, 50));
        antialiasedLabel2.setForeground(new java.awt.Color(153, 153, 153));
        antialiasedLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        antialiasedLabel2.setText("暂无详细信息");
        antialiasedLabel2.setFont(antialiasedLabel2.getFont().deriveFont(antialiasedLabel2.getFont().getStyle() | java.awt.Font.BOLD, antialiasedLabel2.getFont().getSize()+8));
        noContentPanelRight.add(antialiasedLabel2, java.awt.BorderLayout.CENTER);

        rightPane.add(noContentPanelRight, "EMPTY");

        jSplitPane1.setRightComponent(rightPane);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 添加我的报销单
     *
     * @param evt
     */
    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        AddOrModifyExpenseFormPanel addExpenseFormPane = new AddOrModifyExpenseFormPanel(projectComboxModelforAddP);
	addExpenseFormPane.setAddMode(true);
	ExpenseForm expenseForm = addExpenseFormPane.showMe(this);
	if (expenseForm != null) {
	    refreshBtnActionPerformed(evt);
	}
	modifyBtn.setEnabled(true);
	deleteBtn.setEnabled(true);
	printBtn.setEnabled(true);
	commitBtn.setEnabled(true);
    }//GEN-LAST:event_createBtnActionPerformed

    /**
     * 修改我的报销单
     *
     * @param evt
     */
    private void modifyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyBtnActionPerformed
        AddOrModifyExpenseFormPanel addExpenseFormPane = new AddOrModifyExpenseFormPanel(projectComboxModelforAddP);
	addExpenseFormPane.initPanelContent(curExpenseForm);
	addExpenseFormPane.setAddMode(false);
	ExpenseForm expenseForm = addExpenseFormPane.showMe(this);
	if (expenseForm != null) {
	    Project project = null;
	    for (Project proj : projectComboxModelforAddP.getObjectList()) {
		if (proj.getProjectId().equals(curExpenseForm.getExpenseForm().
			getProjectId())) {
		    project = proj;
		    break;
		}
	    }
	    curExpenseForm.setProjectName(project.getProjectName());
	    expenseTableModel.updateData(curExpenseForm);
	    detailedInformationPanel1.setParama(curExpenseForm);
	    addNode(); 
	    curExpenseForm=selectingExpenseForm();
            if(curExpenseForm.getExpenseForm().getState().toString().equals(expenseFormStateEnums.newForm.toString())){
                deleteBtn.setEnabled(true);
                commitBtn.setEnabled(true);
            }else if(curExpenseForm.getExpenseForm().getState().toString().equals(expenseFormStateEnums.waitAuditForPm.toString())||curExpenseForm.getExpenseForm().getState().toString().equals(expenseFormStateEnums.waitAuditForHr.toString())||curExpenseForm.getExpenseForm().getState().toString().equals(expenseFormStateEnums.waitAuditForCeo.toString())){
                deleteBtn.setEnabled(false);
                commitBtn.setEnabled(false);
		modifyBtn.setEnabled(false);
            }
	}
    }//GEN-LAST:event_modifyBtnActionPerformed

    /**
     * 刷新我的报销单
     *
     * @param evt
     */
    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
	Project project = projectComboxModel.getSelectedItem();
	String projectId = null;
	if (project != null) {
	    projectId = project.getProjectId();
	}
	pageSession = paginationPanel1.getPageSession();
	queryMyExpenseFormAgent.setParam(stateListModel.getSelectedItem(), projectId, dateRangePic.getStartDate(), dateRangePic.getEndDate(), pageSession.getCurPageNum(), pageSession.getPageSize());
	queryMyExpenseFormAgent.start();
    }//GEN-LAST:event_refreshBtnActionPerformed
    /**
     * 删除我的报销单
     *
     * @param evt
     */
    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed

	deleteExpenseFormAgent.setPrama(curExpenseForm.getExpenseForm().getExpenseId());
	DeleteOperationPanel<ExpenseForm> deletePane = new DeleteOperationPanel<>(deleteExpenseFormAgent);
	deletePane.configSingleDelete("报销单", curExpenseForm.getProjectName());
	ExpenseForm expenseForm = deletePane.showMe(deleteBtn, ResourceUtil.buildImageIcon("32.png"), "删除报销单", 400, 150);
	if (expenseForm != null) {
	    refreshBtnActionPerformed(null);
	}
    }//GEN-LAST:event_deleteBtnActionPerformed
    //选定某行数据作为当前数据
    ExpenseFormWrap selectingExpenseForm() {
	int index = expenseFormTable.getSelectedRow();
	if (index >= 0) {
	    index = expenseFormTable.convertRowIndexToModel(index);
	    curExpenseForm = expenseTableModel.getData(index);
	    if (curExpenseForm!=null&&curExpenseForm.getExpenseForm()!=null) {
		if (curExpenseForm.getExpenseForm().getState().toString().equals(expenseFormStateEnums.newForm.toString())) {
		    setButtonsEnabled(true);
		} else if(!curExpenseForm.getExpenseForm().isPassState()){
		    modifyBtn.setEnabled(true);
		    deleteBtn.setEnabled(false);
		    commitBtn.setEnabled(false);
		}else{
		  deleteBtn.setEnabled(false);
		  commitBtn.setEnabled(false);
		  modifyBtn.setEnabled(false);
		}
	    }
	} else {
	    setButtonsEnabled(false);
	}
	return curExpenseForm;
    }

    //添加node节点
    void addNode() {
	if (curExpenseForm != null && curExpenseForm.getExpenseForm() != null && curExpenseForm.getExpenseForm().getState() != null) {
	    switch (curExpenseForm.getExpenseForm().getState()) {
		case newForm: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    break;
		}
		case waitAuditForPm: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    NProcessFlowNode node2 = new NProcessFlowNode();
		    node2.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node2.setNodeTxt("待审核");
		    nProcessFlowPanel1.add(node2);
		    if (!curExpenseForm.getExpenseForm().isPassState()) {
			NProcessFlowNode nodeps = new NProcessFlowNode();
			nodeps.setNodeStatus(NProcessFlowNode.PNodeStatus.UNREACHABLE);
			nodeps.setNodeTxt("未通过");
			nProcessFlowPanel1.add(nodeps);
		    }
		    break;
		}
		case waitAuditForCeo: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    NProcessFlowNode node2 = new NProcessFlowNode();
		    node2.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node2.setNodeTxt("待审核");
		    nProcessFlowPanel1.add(node2);
		    NProcessFlowNode node3 = new NProcessFlowNode();
		    node3.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node3.setNodeTxt("待总经理审核");
		    nProcessFlowPanel1.add(node3);
		    if (!curExpenseForm.getExpenseForm().isPassState()) {
			NProcessFlowNode nodeps = new NProcessFlowNode();
			nodeps.setNodeStatus(NProcessFlowNode.PNodeStatus.UNREACHABLE);
			nodeps.setNodeTxt("未通过");
			nProcessFlowPanel1.add(nodeps);
		    }
		    break;
		}
		case waitAuditForHr: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    NProcessFlowNode node2 = new NProcessFlowNode();
		    node2.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node2.setNodeTxt("待审核");
		    nProcessFlowPanel1.add(node2);
		    if (curExpenseForm.getExpenseForm() != null && curExpenseForm.getExpenseForm().getExpenseTotal() >= expenseTotalLimit) {
			NProcessFlowNode node3 = new NProcessFlowNode();
			node3.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
			node3.setNodeTxt("待总经理审核");
			nProcessFlowPanel1.add(node3);
		    }
		    NProcessFlowNode node4 = new NProcessFlowNode();
		    node4.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node4.setNodeTxt("待行政审核");
		    nProcessFlowPanel1.add(node4);
		    if (!curExpenseForm.getExpenseForm().isPassState()) {
			NProcessFlowNode nodeps = new NProcessFlowNode();
			nodeps.setNodeStatus(NProcessFlowNode.PNodeStatus.UNREACHABLE);
			nodeps.setNodeTxt("未通过");
			nProcessFlowPanel1.add(nodeps);
		    }
		    break;
		}
		case waitPay: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    NProcessFlowNode node2 = new NProcessFlowNode();
		    node2.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node2.setNodeTxt("待审核");
		    nProcessFlowPanel1.add(node2);
		    if (curExpenseForm.getExpenseForm() != null && curExpenseForm.getExpenseForm().getExpenseTotal() >= 1000) {
			NProcessFlowNode node3 = new NProcessFlowNode();
			node3.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
			node3.setNodeTxt("待总经理审核");
			nProcessFlowPanel1.add(node3);
		    }
		    NProcessFlowNode node4 = new NProcessFlowNode();
		    node4.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node4.setNodeTxt("待行政审核");
		    nProcessFlowPanel1.add(node4);
		    NProcessFlowNode node5 = new NProcessFlowNode();
		    node5.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node5.setNodeTxt("待发放");
		    nProcessFlowPanel1.add(node5);
		    break;
		}
		case paid: {
		    nProcessFlowPanel1.clear();
		    NProcessFlowNode node1 = new NProcessFlowNode();
		    node1.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node1.setNodeTxt("新建");
		    nProcessFlowPanel1.add(node1);
		    NProcessFlowNode node2 = new NProcessFlowNode();
		    node2.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node2.setNodeTxt("待审核");
		    nProcessFlowPanel1.add(node2);
		    if (curExpenseForm.getExpenseForm() != null && curExpenseForm.getExpenseForm().getExpenseTotal() >= 1000) {
			NProcessFlowNode node3 = new NProcessFlowNode();
			node3.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
			node3.setNodeTxt("待总经理审核");
			nProcessFlowPanel1.add(node3);
		    }
		    NProcessFlowNode node4 = new NProcessFlowNode();
		    node4.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node4.setNodeTxt("待行政审核");
		    nProcessFlowPanel1.add(node4);
		    NProcessFlowNode node5 = new NProcessFlowNode();
		    node5.setNodeStatus(NProcessFlowNode.PNodeStatus.PASSED);
		    node5.setNodeTxt("待发放");
		    nProcessFlowPanel1.add(node5);
		    NProcessFlowNode node6 = new NProcessFlowNode();
		    node6.setNodeStatus(NProcessFlowNode.PNodeStatus.RUNNING);
		    node6.setNodeTxt("已发放");
		    nProcessFlowPanel1.add(node6);
		    break;
		}
	    }
	    nProcessFlowPanel1.validate();
	    nProcessFlowPanel1.repaint();
	}
    }

    /**
     * 提交我的报销单
     *
     * @param evt
     */
    private void commitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitBtnActionPerformed

	SimpleOperationPanel<ExpenseForm> commitPane = new SimpleOperationPanel<>(submitExpenseFormAgent);
	ExpenseForm expenseForm = null;
	if (curExpenseForm != null && curExpenseForm.getExpenseForm() != null && curExpenseForm.getExpenseForm().getState() != null) {
	    if ((curExpenseForm.getExpenseForm().getState().toString()).equals(ExpenseFormStateEnums.newForm.toString())) {
		commitPane.configDeteleMsg("<html>确认要执行提交当前新建报销单的操作吗？</html>");
		submitExpenseFormAgent.setPrama(curExpenseForm.getExpenseForm().getExpenseId());
		expenseForm = commitPane.showMe(commitBtn, ResourceUtil.buildImageIcon("32.png"), "提交报销单", 400, 100);
	    } else {
		commitBtn.setEnabled(false);
	    }
	    submitExpenseFormAgent.start();
	}
	if (expenseForm != null) {
	    curExpenseForm.getExpenseForm().setState(expenseFormStateEnums.waitAuditForPm);
	    expenseTableModel.updateData(curExpenseForm);
            deleteBtn.setEnabled(false);
            commitBtn.setEnabled(false);
	    addNode();
	}
    }//GEN-LAST:event_commitBtnActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
	PrintExpenseFormPanel addExpenseFormPane = new PrintExpenseFormPanel();
	List<ExpenseForm> expenseForms = new ArrayList<>();
	expenseForms.add(curExpenseForm.getExpenseForm());
	Map<String, Object> map = new HashMap();
	map.put("ProjectName", curExpenseForm.getProjectName());
	ExpenseForm expenseForm = addExpenseFormPane.showMe(this, expenseForms, map);
	if (expenseForm != null) {
	    refreshBtnActionPerformed(evt);
	}
    }//GEN-LAST:event_printBtnActionPerformed

    private void statecbBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_statecbBoxItemStateChanged
	pageSession = paginationPanel1.getPageSession();
	queryMyExpenseFormAgent.setParam(stateListModel.getSelectedItem(), projectComboxModel.getSelectedItem() != null ? projectComboxModel.getSelectedItem().getProjectId() : null, dateRangePic.getStartDate(), dateRangePic.getEndDate(), pageSession.getCurPageNum(), pageSession.getPageSize());
	queryMyExpenseFormAgent.start();
    }//GEN-LAST:event_statecbBoxItemStateChanged

    private void projectcbBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_projectcbBoxItemStateChanged
	pageSession = paginationPanel1.getPageSession();
	queryMyExpenseFormAgent.setParam(stateListModel.getSelectedItem(), projectComboxModel.getSelectedItem() != null ? projectComboxModel.getSelectedItem().getProjectId() : null, dateRangePic.getStartDate(), dateRangePic.getEndDate(), pageSession.getCurPageNum(), pageSession.getPageSize());
	queryMyExpenseFormAgent.start();
    }//GEN-LAST:event_projectcbBoxItemStateChanged

    private void statecbBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statecbBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statecbBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nazca.ui.AntialiasedLabel antialiasedLabel1;
    private com.nazca.ui.AntialiasedLabel antialiasedLabel2;
    private javax.swing.JLabel booksMaterialsLabel;
    private javax.swing.JTextField booksMaterialsTextField;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel cityGasolineLabel;
    private javax.swing.JTextField cityGasolineTextField;
    private javax.swing.JLabel cityTrafficLabel;
    private javax.swing.JTextField cityTrafficTextField;
    private javax.swing.JButton commitBtn;
    private javax.swing.JLabel conferencesLabel;
    private javax.swing.JTextField conferencesTextField;
    private javax.swing.JLabel copyBindLabel;
    private javax.swing.JTextField copyBindTextField;
    private javax.swing.JButton createBtn;
    private com.nazca.ui.NDateRangePicker dateRangePic;
    private javax.swing.JButton deleteBtn;
    private com.yz.rms.client.ui.DetailedInformationPanel detailedInformationPanel1;
    private javax.swing.JLabel entertainLabel;
    private javax.swing.JTextField entertainTextField;
    private javax.swing.JTable expenseFormTable;
    private javax.swing.JLabel expenseTimeLabel;
    private javax.swing.JLabel fieldOperationLabel;
    private javax.swing.JTextField fieldOperationTextField;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPaneContent;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel leftPane;
    private javax.swing.JLabel materialLabel;
    private javax.swing.JTextField materialTextField;
    private javax.swing.JButton modifyBtn;
    private com.nazca.ui.NProcessFlowPanel nProcessFlowPanel1;
    private com.nazca.ui.NProcessFlowPanel nProcessFlowPanel2;
    private javax.swing.JPanel noContentPanelLeft;
    private javax.swing.JPanel noContentPanelRight;
    private javax.swing.JLabel officeSuppliesLabel;
    private javax.swing.JTextField officeSuppliesTextField;
    private com.nazca.ui.pagination.PaginationPanel paginationPanel1;
    private javax.swing.JLabel postageLabel;
    private javax.swing.JTextField postageTextField;
    private javax.swing.JButton printBtn;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JComboBox projectcbBox;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JPanel rightPane;
    private javax.swing.JLabel spacePageLabel;
    private javax.swing.JTextField spacePageTextField;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JComboBox statecbBox;
    private javax.swing.JLabel telephoneBillLabel;
    private javax.swing.JTextField telephoneBillTextField;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalTextField;
    private javax.swing.JLabel trainLabel;
    private javax.swing.JTextField trainTextField;
    private javax.swing.JLabel travelAccommodationLabel;
    private javax.swing.JTextField travelAccommodationTextField;
    private javax.swing.JLabel travelAllowanceLabel;
    private javax.swing.JTextField travelAllowanceTextField;
    private javax.swing.JLabel travelMealsLabel;
    private javax.swing.JTextField travelMealsTextField;
    private javax.swing.JLabel travelTrafficLabel;
    private javax.swing.JTextField travelTrafficTextField;
    private javax.swing.JPanel upPanel;
    private com.nazca.ui.NWaitingPanel waitPane12;
    private com.nazca.ui.NWaitingPanel waitPanel1;
    // End of variables declaration//GEN-END:variables
 //查询我的报销单监听器
    private final AgentListener<PageResult<ExpenseFormWrap>> queryMyExpenseAgentListener = new AgentListener<PageResult<ExpenseFormWrap>>() {
	@Override
	public void onStarted(long seq) {
	    timeSeq = seq;
	    setButtonsEnabled(false);
	    waitPanel1.setIndeterminate(true);
	    waitPanel1.showWaitingMode("数据加载中，请稍候...");
	    contentLayout.show(CardLayoutWrapper.WAIT);
	    waitPane12.setIndeterminate(true);
	    waitPane12.showWaitingMode("数据加载中，请稍候...");
	    infoLayout.show(CardLayoutWrapper.WAIT);
	}

	@Override
	public void onSucceeded(PageResult<ExpenseFormWrap> result, long seq) {
	    setButtonsEnabled(true);
	    waitPanel1.setIndeterminate(false);
	    waitPane12.setIndeterminate(false);
	    expenseTableModel.clear();
	    if (result != null && result.getTotalCount() > 0) {
		List<ExpenseFormWrap> expenseFormList = result.getPageList();
		int totalCount = result.getTotalCount();
		int pageSize = result.getPageSize();
		expenseTableModel.setDatas(expenseFormList);
		contentLayout.show(CardLayoutWrapper.CONTENT);
		infoLayout.show(CardLayoutWrapper.CONTENT);
		setButtonsEnabled(true);
		expenseFormTable.getSelectionModel().setSelectionInterval(0, 0);
		paginationPanel1.initPageButKeepSession(totalCount, pageSize);
	    } else {
		detailedInformationPanel1.setParama(null);
		modifyBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		printBtn.setEnabled(false);
		commitBtn.setEnabled(false);
		contentLayout.show(CardLayoutWrapper.EMPTY);
		infoLayout.show(CardLayoutWrapper.EMPTY);
	    }
	}

	@Override
	public void onFailed(String errMsg, int errCode, long seq) {
	    if (seq == timeSeq) {
		waitPanel1.setIndeterminate(false);
		waitPanel1.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_INFO);
		waitPane12.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_INFO);
		contentLayout.show(CardLayoutWrapper.FAIL);
		infoLayout.show(CardLayoutWrapper.FAIL);
		setButtonsEnabled(false);
		refreshBtn.setEnabled(true);
	    }
	}
    };

    //项目列表监听器
    private final AgentListener<List<Project>> queryProjectAgentLis = new AgentListener<List<Project>>() {
	@Override
	public void onStarted(long seq) {
	}

	@Override
	public void onSucceeded(List<Project> result, long seq) {
	    result.add(null);
	    Collections.reverse(result);
	    projectComboxModel.setObjectList(result);
	    projectcbBox.setSelectedIndex(0);
	}

	@Override
	public void onFailed(String errMsg, int errCode, long seq) {
	}
    };

    private final AgentListener<List<Project>> queyProjectAgentLis
	    = new AgentListener<List<Project>>() {
	@Override
	public void onStarted(long seq) {
	}

	@Override
	public void onSucceeded(List<Project> result, long seq) {
	    Collections.reverse(result);
	    projectComboxModelforAddP.setObjectList(result);
	}

	@Override
	public void onFailed(String errMsg, int errCode, long seq) {
	}
    };
}
