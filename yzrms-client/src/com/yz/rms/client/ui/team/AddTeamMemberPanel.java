/*
 * AddTeamMemberPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-08 13:40:35
 */
package com.yz.rms.client.ui.team;

import com.nazca.ui.NInternalDiag;
import com.nazca.ui.NInternalDiagListener;
import com.nazca.ui.NWaitingPanel;
import com.nazca.ui.util.CardLayoutWrapper;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.agent.team.AddTeamMemberAgent;
import com.yz.rms.client.agent.team.QueryAllMemberAgent;
import com.yz.rms.client.ui.OKCancelPanelListener;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.model.team.MemberListModel;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.model.wrap.MemberWrap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * 添加团队成员面板
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class AddTeamMemberPanel extends javax.swing.JPanel {

    private QueryAllMemberAgent queryAllMemberAgent = null;
    private AddTeamMemberAgent addTeamMemberAgent = null;
    private MemberListModel listModel = null;
    private CardLayoutWrapper card = null;
    private List<MemberWrap> list = null;
    private String teamId;

    /**
     * Creates new form AddTeamMemberPanel
     */
    public AddTeamMemberPanel() {
        initComponents();
        card = new CardLayoutWrapper(jPanel1);
        initAgentAndModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jToolBar1 = new javax.swing.JToolBar();
        refreshBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        nWaitingPanel1 = new com.nazca.ui.NWaitingPanel();
        contentPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        triStateList1 = new com.nazca.ui.TriStateList();
        inverseSelectBtn = new com.nazca.ui.NLinkButton();
        allSelectBtn = new com.nazca.ui.NLinkButton();
        oKCancelPanel1 = new com.yz.rms.client.ui.OKCancelPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/yz/rms/client/res/refresh_16.png"))); // NOI18N
        refreshBtn.setText("刷新");
        refreshBtn.setFocusable(false);
        refreshBtn.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        refreshBtn.setMargin(new java.awt.Insets(2, 5, 2, 5));
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshBtn);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.CardLayout());
        jPanel1.add(nWaitingPanel1, "WAIT");

        contentPanel.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        triStateList1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setViewportView(triStateList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contentPanel.add(jScrollPane1, gridBagConstraints);

        inverseSelectBtn.setText("反选");
        inverseSelectBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inverseSelectBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inverseSelectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inverseSelectBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        contentPanel.add(inverseSelectBtn, gridBagConstraints);

        allSelectBtn.setText("全选");
        allSelectBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        allSelectBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        allSelectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allSelectBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        contentPanel.add(allSelectBtn, gridBagConstraints);

        jPanel1.add(contentPanel, "CONTENT");

        add(jPanel1, java.awt.BorderLayout.CENTER);

        oKCancelPanel1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                oKCancelPanel1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        add(oKCancelPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void initAgentAndModel() {
        listModel = new MemberListModel();
        triStateList1.setModel(listModel);
        queryAllMemberAgent = new QueryAllMemberAgent();
        queryAllMemberAgent.addListener(queryMenberAgentListener);
        addTeamMemberAgent = new AddTeamMemberAgent();
        addTeamMemberAgent.addListener(addMemberAgentListener);
        oKCancelPanel1.addOKCancelListener(new OKCancelPanelListener() {
            @Override
            public void onOKClicked() {
                if (validateIsSelected()) {
                    oKCancelPanel1.gotoNormalMode();
                    list = listModel.getAllSelectedNominals();
                    List<String> memberIdList = new ArrayList<>();
                    for (MemberWrap member : list) {
                        String menberId = member.getMemberId();
                        memberIdList.add(menberId);
                    }
                    addTeamMemberAgent.setParameter(memberIdList, teamId);
                    addTeamMemberAgent.start();
                }
            }

            @Override
            public void onCancelClicked() {
                NInternalDiag<List<Member>, JComponent> diag = NInternalDiag.findNInternalDiag(
                        AddTeamMemberPanel.this);
                diag.hideDiag();
            }
        });
    }
    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        queryAllMemberAgent.start();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void inverseSelectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inverseSelectBtnActionPerformed
        listModel.setReversSelected();
    }//GEN-LAST:event_inverseSelectBtnActionPerformed

    private void allSelectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allSelectBtnActionPerformed
        listModel.setAllSelected();
    }//GEN-LAST:event_allSelectBtnActionPerformed

    private void oKCancelPanel1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_oKCancelPanel1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_oKCancelPanel1AncestorAdded
    public List<Member> showMe(JComponent parent, String teamId) {
        this.teamId = teamId;
        NInternalDiag<List<Member>, JComponent> diag = new NInternalDiag<>("选择团队成员", ResourceUtil.buildImageIcon("32.png"), this, 270, 375);

        diag.addNInternalDiagListener(new NInternalDiagListener() {

            @Override
            public void onClosing(NInternalDiag nid) {
            }

            @Override
            public void onClosed(NInternalDiag nid) {
            }

            @Override
            public void onShowingDone(NInternalDiag nid) {
                refreshBtnActionPerformed(null);
            }
        });
        return diag.showInternalDiag(parent, NInternalDiag.INIT_SIZE_MODE_PREFERED);
    }

    private boolean validateIsSelected() {
        if (listModel.getAllSelectedNominals().isEmpty()) {
            oKCancelPanel1.gotoErrorMode("请至少选择一个团队成员!");
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nazca.ui.NLinkButton allSelectBtn;
    private javax.swing.JPanel contentPanel;
    private com.nazca.ui.NLinkButton inverseSelectBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private com.nazca.ui.NWaitingPanel nWaitingPanel1;
    private com.yz.rms.client.ui.OKCancelPanel oKCancelPanel1;
    private javax.swing.JButton refreshBtn;
    private com.nazca.ui.TriStateList triStateList1;
    // End of variables declaration//GEN-END:variables
    AgentListener<List<MemberWrap>> queryMenberAgentListener = new AgentListener<List<MemberWrap>>() {
        @Override
        public void onStarted(long seq) {
            refreshBtn.setEnabled(false);
            nWaitingPanel1.setIndeterminate(true);
            nWaitingPanel1.showMsgMode("数据加载中，请稍后...", 0, NWaitingPanel.MSG_MODE_INFO);
            nWaitingPanel1.showWaitingMode();
            card.show(CardLayoutWrapper.WAIT);
        }

        @Override
        public void onSucceeded(List<MemberWrap> result, long seq) {
            listModel.setNominalList(result);
            nWaitingPanel1.setIndeterminate(false);
            if (result != null && !result.isEmpty()) {
                card.show(CardLayoutWrapper.CONTENT);
            } else {//没有数据
                allSelectBtn.setEnabled(false);
                inverseSelectBtn.setEnabled(false);
                nWaitingPanel1.showMsgMode("暂无成员信息", 0, NWaitingPanel.MSG_MODE_INFO);
            }
            refreshBtn.setEnabled(true);
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            nWaitingPanel1.setIndeterminate(false);
            nWaitingPanel1.showMsgMode(errMsg, errCode, NWaitingPanel.MSG_MODE_INFO);
            refreshBtn.setEnabled(true);

        }
    };
    AgentListener<List<Member>> addMemberAgentListener = new AgentListener<List<Member>>() {
        @Override
        public void onStarted(long seq) {
            refreshBtn.setEnabled(false);
            allSelectBtn.setEnabled(false);
            inverseSelectBtn.setEnabled(false);
            triStateList1.setEnabled(false);
            oKCancelPanel1.gotoWaitMode("正在添加团队成员...");
        }

        @Override
        public void onSucceeded(List<Member> result, long seq) {
            oKCancelPanel1.gotoWaitMode("添加团队成员成功！");
            new Thread() {
                @Override
                public void run() {
                    new TimeFairy().sleepIfNecessary();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            NInternalDiag<List<Member>, JComponent> diag = NInternalDiag.findNInternalDiag(
                                    AddTeamMemberPanel.this);
                            diag.hideDiag(result);
                        }
                    });
                }
            }.start();
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            refreshBtn.setEnabled(true);
            allSelectBtn.setEnabled(true);
            inverseSelectBtn.setEnabled(true);
            triStateList1.setEnabled(true);
            oKCancelPanel1.gotoErrorMode(errMsg, errCode);
        }
    };
}
