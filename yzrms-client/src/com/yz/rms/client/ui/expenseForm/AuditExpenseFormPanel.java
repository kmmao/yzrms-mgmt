/*
 * AuditExpenseFormPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-10 15:42:08
 */
package com.yz.rms.client.ui.expenseform;

import com.nazca.ui.NComponentStyleTool;
import com.nazca.ui.NInternalDiag;
import com.nazca.ui.NInternalDiagListener;
import com.nazca.ui.NLabelMessageTool;
import com.nazca.ui.model.TextFieldLimiter;
import com.nazca.usm.client.util.JTextFieldLimit;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.agent.expenseform.AuditExpenseFromAgent;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.client.ui.OKCancelPanel;
import com.yz.rms.client.ui.team.AddOrUpdateTeamPanel;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.util.ErrorCodeFormater;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class AuditExpenseFormPanel extends javax.swing.JPanel {

    private AuditExpenseFromAgent auditExpenseFromAgent = null;
    private ExpenseFormWrap expenseFormWrap;

    public void setExpenseFormWrap(ExpenseFormWrap expenseFormWrap) {
        this.expenseFormWrap = expenseFormWrap;
    }

    /**
     * Creates new form AuditExpenseFormPanel
     */
    public AuditExpenseFormPanel() {
        initComponents();
        initUI();
        initAgentAndListener();
    }

    private void initUI() {
        NComponentStyleTool.goodNewsStyle(passBtn);
        NComponentStyleTool.errorStyle(denyBtn);
    }

    private void initAgentAndListener() {
        auditExpenseFromAgent = new AuditExpenseFromAgent();
        auditExpenseFromAgent.addListener(auditExpenseFromAgentListener);
        memoTxArea.setDocument(new TextFieldLimiter(250));
        memoTxArea.setLineWrap(true); 
        memoTxArea.setWrapStyleWord(true); 
        memoTxArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (memoTxArea.getText().length() >= 250) {
                    NLabelMessageTool.
                            errorMessage(nActionPane1.getMsgLabel(), "最多可输入250个字符！");
                    nActionPane1.getMsgLabel().setVisible(true);
                    NComponentStyleTool.errorStyle(memoTxArea);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        memoLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        memoTxArea = new javax.swing.JTextArea();
        nActionPane1 = new com.nazca.ui.NActionPane();
        cancelBtn = new javax.swing.JButton();
        denyBtn = new javax.swing.JButton();
        passBtn = new javax.swing.JButton();

        memoLabel.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        memoLabel.setText("备注：");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        memoTxArea.setColumns(20);
        memoTxArea.setRows(5);
        jScrollPane1.setViewportView(memoTxArea);

        cancelBtn.setText("取消");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        nActionPane1.add(cancelBtn);

        denyBtn.setText("不通过");
        denyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denyBtnActionPerformed(evt);
            }
        });
        nActionPane1.add(denyBtn);

        passBtn.setText("通过");
        passBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passBtnActionPerformed(evt);
            }
        });
        nActionPane1.add(passBtn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nActionPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(memoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(memoLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nActionPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        NInternalDiag<Boolean, JComponent> diag = NInternalDiag.findNInternalDiag(AuditExpenseFormPanel.this);
        diag.hideDiag(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void passBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passBtnActionPerformed
        auditExpenseFromAgent.setExpenseId(expenseFormWrap.getExpenseForm().getExpenseId());
        auditExpenseFromAgent.setMemo(memoTxArea.getText().trim());
        auditExpenseFromAgent.setIsPass(true);
        auditExpenseFromAgent.start();
    }//GEN-LAST:event_passBtnActionPerformed

    private void denyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_denyBtnActionPerformed
        auditExpenseFromAgent.setExpenseId(expenseFormWrap.getExpenseForm().getExpenseId());
        auditExpenseFromAgent.setMemo(memoTxArea.getText().trim());
        auditExpenseFromAgent.setIsPass(false);
        auditExpenseFromAgent.start();
    }//GEN-LAST:event_denyBtnActionPerformed
    public boolean showMe(JComponent parent) {
        NInternalDiag<Boolean, JComponent> diag = null;
        diag = new NInternalDiag<>("审核报销单", ResourceUtil.buildImageIcon("32.png"), this);
        diag.addNInternalDiagListener(new NInternalDiagListener() {

            @Override
            public void onClosing(NInternalDiag nid) {
            }

            @Override
            public void onClosed(NInternalDiag nid) {
            }

            @Override
            public void onShowingDone(NInternalDiag nid) {
                memoTxArea.requestFocusInWindow();
            }
        });
        return diag.showInternalDiag(parent, NInternalDiag.INIT_SIZE_MODE_PREFERED);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton denyBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel memoLabel;
    private javax.swing.JTextArea memoTxArea;
    private com.nazca.ui.NActionPane nActionPane1;
    private javax.swing.JButton passBtn;
    // End of variables declaration//GEN-END:variables
    AgentListener<Boolean> auditExpenseFromAgentListener = new AgentListener<Boolean>() {
        @Override
        public void onStarted(long seq) {
            memoTxArea.setEnabled(false);
            gotoWaitMode("正在执行");
        }

        @Override
        public void onSucceeded(Boolean result, long seq) {
            memoTxArea.setEnabled(true);
            gotoSuccessMode("执行成功");
            new Thread() {
                @Override
                public void run() {
                    new TimeFairy().sleepIfNecessary();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            NInternalDiag<Boolean, JComponent> diag = NInternalDiag.findNInternalDiag(
                                    AuditExpenseFormPanel.this);
                            diag.hideDiag(result);
                        }
                    });
                }
            }.start();
        }

        @Override
        public void onFailed(String errMsg, int errCode, long seq) {
            memoTxArea.setEnabled(true);
            gotoErrorMode(errMsg, errCode);
        }
    };

    /**
     * 等待模式
     *
     * @param waitMsg
     */
    public void gotoWaitMode(String waitMsg) {
        nActionPane1.getWaitingProcess().setVisible(true);
        nActionPane1.getWaitingProcess().setIndeterminate(true);
        NLabelMessageTool.plainMessage(nActionPane1.getMsgLabel(), waitMsg);
        nActionPane1.getMsgLabel().setVisible(true);
        cancelBtn.setEnabled(false);
        denyBtn.setEnabled(false);
        passBtn.setEnabled(false);
    }

    /**
     * 成功模式
     *
     * @param msg
     */
    public void gotoSuccessMode(String msg) {
        nActionPane1.getWaitingProcess().setVisible(false);
        nActionPane1.getWaitingProcess().setIndeterminate(false);
        NLabelMessageTool.goodNewsMessage(nActionPane1.getMsgLabel(), msg);
        nActionPane1.getMsgLabel().setVisible(true);
        cancelBtn.setEnabled(false);
        denyBtn.setEnabled(false);
        passBtn.setEnabled(false);
    }

    /**
     * 错误模式
     *
     * @param errMsg
     * @param errorCode
     */
    public void gotoErrorMode(String errMsg, int errorCode) {
        nActionPane1.getWaitingProcess().setVisible(false);
        nActionPane1.getWaitingProcess().setIndeterminate(false);
        NLabelMessageTool.errorMessage(nActionPane1.getMsgLabel(), ErrorCodeFormater.formate(errMsg, errorCode));
        nActionPane1.getMsgLabel().setVisible(true);
        cancelBtn.setEnabled(true);
        denyBtn.setEnabled(true);
        passBtn.setEnabled(true);
    }
}
