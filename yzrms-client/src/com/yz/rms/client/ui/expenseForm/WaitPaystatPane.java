/*
 * WaitPaystatPane.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 16:06:59
 */
package com.yz.rms.client.ui.expenseform;

import com.nazca.ui.NInternalDiag;
import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.model.SimpleObjectListModel;
import com.nazca.ui.util.CardLayoutWrapper;
import com.yz.rms.client.agent.expenseform.QueryWaitPayStatAgent;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.expenseform.WaitPayTableModel;
import com.yz.rms.client.renderer.WaitPayTableCellRender;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.WindowConstants;

/**
 *
 * @author Hu Qin
 */
public class WaitPaystatPane extends javax.swing.JPanel {

    private QueryWaitPayStatAgent queryWaitPayStat;
    private final SimpleObjectListModel<Integer> yearComboxModel
            = new SimpleObjectListModel<>();
    private WaitPayTableModel waitPayModel = new WaitPayTableModel();
    private CardLayoutWrapper contentLayout = null;
    private WaitPayTableCellRender waitPayTableCellRender
            = new WaitPayTableCellRender();
    private Map<Integer, List<WaitPayStatWrap>> map = null;
    private Map<Integer, List<WaitPayStatWrap>> refmap = null;

    /**
     * Creates new form WaitPaystatPane
     */
    public WaitPaystatPane() {
    }

    public WaitPaystatPane(Map<Integer, List<WaitPayStatWrap>> map) {
        this.map = map;
        initComponents();
        initModel();
        initAgentAndListener();
        initRenderer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initModel() {
        waitPayTable.setModel(waitPayModel);
        contentLayout = new CardLayoutWrapper(cardPane);
        yearComboxModel.setObjectList(ResourceUtil.getYears());
        chooseYearcbBox.setModel(yearComboxModel);
        chooseYearcbBox.setSelectedIndex(0);
    }

    private void initRenderer() {
        waitPayTable.setDefaultRenderer(Object.class, waitPayTableCellRender);
    }

    public List<WaitPayStatWrap> showMe(JComponent parent) {
        NInternalDiag<List<WaitPayStatWrap>, JComponent> diag
                = new NInternalDiag<>("待发放金额总计", ResourceUtil.buildImageIcon(
                                "32.png"), this);
        diag.setCloseButtonVisible(true);
        diag.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        return diag.showInternalDiag(parent,
                NInternalDiag.INIT_SIZE_MODE_PREFERED);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        refreshBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        chooseYearcbBox = new javax.swing.JComboBox();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        cardPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        waitPayTable = new javax.swing.JTable();
        waitPane = new com.nazca.ui.NWaitingPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);

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

        jLabel1.setText("年份选择：");
        jToolBar1.add(jLabel1);

        chooseYearcbBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        chooseYearcbBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chooseYearcbBoxItemStateChanged(evt);
            }
        });
        jToolBar1.add(chooseYearcbBox);
        jToolBar1.add(filler4);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        cardPane.setLayout(new java.awt.CardLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        waitPayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "月份", "条数(条)", "总额(元)"
            }
        ));
        jScrollPane1.setViewportView(waitPayTable);
        if (waitPayTable.getColumnModel().getColumnCount() > 0) {
            waitPayTable.getColumnModel().getColumn(0).setResizable(false);
            waitPayTable.getColumnModel().getColumn(1).setResizable(false);
            waitPayTable.getColumnModel().getColumn(2).setResizable(false);
        }

        cardPane.add(jScrollPane1, "CONTENT");
        cardPane.add(waitPane, "WAIT");

        add(cardPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void chooseYearcbBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chooseYearcbBoxItemStateChanged
       if(refmap != null && map != refmap){
           waitPayModel.setDatas(getvalues(refmap));
       }else{
           waitPayModel.setDatas(getvalues(map));
       }
        
    }//GEN-LAST:event_chooseYearcbBoxItemStateChanged

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        queryWaitPayStat = new QueryWaitPayStatAgent();
        queryWaitPayStat.addListener(queryWaitPayStatLis);
        queryWaitPayStat.start();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void initAgentAndListener() {
        queryWaitPayStat = new QueryWaitPayStatAgent();
        queryWaitPayStat.addListener(queryWaitPayStatLis);
        queryWaitPayStat.start();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardPane;
    private javax.swing.JComboBox chooseYearcbBox;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshBtn;
    private com.nazca.ui.NWaitingPanel waitPane;
    private javax.swing.JTable waitPayTable;
    // End of variables declaration//GEN-END:variables

    private AgentListener< Map<Integer, List<WaitPayStatWrap>>> queryWaitPayStatLis
            = new AgentListener<Map<Integer, List<WaitPayStatWrap>>>() {
                @Override
                public void onStarted(long seq) {
                    refreshBtn.setEnabled(false);
                    chooseYearcbBox.setEnabled(false);
                    waitPane.showWaitingMode("数据加载中，请稍候...");
                    contentLayout.show(CardLayoutWrapper.WAIT);
                }

                @Override
                public void onSucceeded(
                        Map<Integer, List<WaitPayStatWrap>> result, long seq) {
                            refreshBtn.setEnabled(true);
                            chooseYearcbBox.setEnabled(true);
                            waitPayModel.setDatas(getvalues(result));
                            refmap = result;
                            contentLayout.show(CardLayoutWrapper.CONTENT);
                        }

                @Override
                public void onFailed(String errMsg, int errCode,
                        long seq) {
                    waitPane.showMsgMode(errMsg, errCode,
                            NWaitingPanel.MSG_MODE_ERROR);
                    contentLayout.show(CardLayoutWrapper.FAIL);
                    refreshBtn.setEnabled(true);
                    chooseYearcbBox.setEnabled(true);
                }
            };

    private List<WaitPayStatWrap> getvalues(
            Map<Integer, List<WaitPayStatWrap>> result) {
        int sumCount = 0;
        Double sumAmount = 0.0;
        int n = 0;
        List<WaitPayStatWrap> lis = result.get(
                (int) chooseYearcbBox.getSelectedItem());
        List<WaitPayStatWrap> tmplis = new ArrayList();
        for (int i = 12; i > 0; i--) {
            if (lis != null) {
                if (i == lis.get(n).getMonth()) {
                    tmplis.add(lis.get(n));
                    if (n < lis.size() - 1) {
                        n++;
                    }
                } else {
                    newWaitPayWarp(0, 0.0, i, tmplis);
                }
            } else {
                newWaitPayWarp(0, 0.0, i, tmplis);
            }
        }
        if (lis != null) {
            for (WaitPayStatWrap li : lis) {
                sumCount = sumCount + li.getCount();
                sumAmount = sumAmount + li.getAmount();
            }
        }
        newWaitPayWarp(sumCount, sumAmount, TOTAL_MONTH, tmplis);
        return tmplis;
    }

    private void newWaitPayWarp(int count, Double Amount, int i,
            List<WaitPayStatWrap> tmplis) {
        WaitPayStatWrap wpw = new WaitPayStatWrap();
        wpw.setMonth(i);
        wpw.setCount(count);
        wpw.setAmount(Amount);
        tmplis.add(wpw);
    }
    private static final int TOTAL_MONTH = -1;
}
