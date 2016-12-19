/*
 * DeleteOperationPanel.java
 * 
 * Copyright(c) 2007-2012 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2012-09-17 17:37:43
 */
package com.yz.rms.client.ui;

import com.nazca.ui.NInternalDiag;
import com.nazca.ui.NInternalDiagListener;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.listener.AgentListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Qiu Dongyue
 */
public class DeleteOperationPanel<T> extends javax.swing.JPanel {
    private static final String DELETE_SINGLE_TMPL =
            "<html>确认要执行删除$objType“$objName”的操作吗？"
            + "<br /><font color=\"#c47f2d\">提示：删除的$objType将不可恢复。</font></html>";
    private static final String DELETE_MANY_TMPL =
            "<html>确认要执行删除“$objName”等$objCount$objUnit$objType的操作吗？<br />"
            + "<font color=\"#c47f2d\">提示：删除的$objType将不可恢复。</font></html>";
    private static final String KEY_OBJ_TYPE = "$objType";
    private static final String KEY_OBJ_NAME = "$objName";
    private static final String KEY_OBJ_COUNT = "$objCount";
    private static final String KEY_OBJ_UNIT = "$objUnit";
    private AbstractAgent agent = null;

    public DeleteOperationPanel() {
        initComponents();
        oKCancelPane.addOKCancelListener(new OKCancelPanelListener() {
            @Override
            public void onOKClicked() {
                NInternalDiag diag = NInternalDiag.findNInternalDiag(DeleteOperationPanel.this);
                diag.hideDiag(true);
            }

            @Override
            public void onCancelClicked() {
                NInternalDiag diag = NInternalDiag.findNInternalDiag(DeleteOperationPanel.this);
                diag.hideDiag(false);
            }
        });

        oKCancelPane.toDeleteButtonStyle();
    }

    /** Creates new form DeleteOperationPanel */
    public DeleteOperationPanel(AbstractAgent<T> agt) {
        initComponents();
        this.agent = agt;
        this.agent.addListener(new AgentListener() {
            public void onStarted(long seq) {
                oKCancelPane.gotoWaitMode("正在执行...");
            }

            public void onSucceeded(final Object result, long seq) {
                oKCancelPane.gotoSuccessMode("执行成功");
                new Thread() {
                    public void run() {
                        new TimeFairy().sleepIfNecessary();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                NInternalDiag<T, JComponent> diag =
                                        NInternalDiag.findNInternalDiag(DeleteOperationPanel.this);
                                if (diag != null) {
                                    diag.hideDiag((T) result);
                                } else {
                                    System.out.println("diag is null");
                                }
                            }
                        });
                    }
                }.start();
            }

            public void onFailed(String errMsg, int errCode, long seq) {
                oKCancelPane.gotoErrorMode(errMsg, errCode);
            }
        });

        oKCancelPane.addOKCancelListener(new OKCancelPanelListener() {
            public void onOKClicked() {
                agent.start();
            }

            public void onCancelClicked() {
                NInternalDiag diag = NInternalDiag.findNInternalDiag(DeleteOperationPanel.this);
                diag.hideDiag();
            }
        });

        oKCancelPane.toDeleteButtonStyle();
    }

    /**
     * 单一删除，示例：确认要执行删除考生“张三”的操作吗？
     * @param objType 对象类型，如：考生
     * @param objName 对象名，如：张三
     */
    public void configSingleDelete(String objType, String objName) {
        String str = DELETE_SINGLE_TMPL.replace(KEY_OBJ_TYPE, objType).replace(KEY_OBJ_NAME, objName);
        deleteRecvPrompt.setText(str);
    }

    /**
     * 配置多对象删除信息，示例：确认要执行删除“张三”等3个考生的操作吗？
     * @param objType 对象类型，如：考生
     * @param objName 对象名，如：张山
     * @param objCount 对象数量
     * @param objUnit 对象量纲，如：人
     */
    public void configManyDeleteMsg(String objType, String objName, int objCount, String objUnit) {
        String str = DELETE_MANY_TMPL.replace(KEY_OBJ_TYPE, objType).
                replace(KEY_OBJ_NAME, objName).replace(KEY_OBJ_COUNT, Integer.toString(objCount)).replace(KEY_OBJ_UNIT,
                objUnit);
        deleteRecvPrompt.setText(str);
    }

    /**
     * 完全自定义删除文字
     * @param htmlString HTML文字信息，如：<html>确认要执行删除考生“张三”的操作吗？<br /><font color=\"#c47f2d\">提示：删除的考生将不可恢复。</font></html>
     */
    public void configDeleteMsg(String htmlString) {
        deleteRecvPrompt.setText(htmlString);
    }

    /**
     * 获取OK Cancel面板
     * @return 
     */
    public OKCancelPanel getOKCancelPanel(){
        return oKCancelPane;
    }
    
    /**
     * 显示对话框
     * @param parent
     * @param icon
     * @param title
     * @return 
     */
    public T showMe(JComponent parent, Icon icon, String title) {
        NInternalDiag<T, JComponent> diag = new NInternalDiag<T, JComponent>(title, icon, this);
        return diag.showInternalDiag(parent);
    }

    /**
     * 显示对话框
     * @param parent
     * @param icon
     * @param title
     * @param width
     * @param height
     * @return 
     */
    public T showMe(JComponent parent, Icon icon, String title, int width, int height) {
        NInternalDiag<T, JComponent> diag = new NInternalDiag<T, JComponent>(title, icon, this, width, height);
        diag.addNInternalDiagListener(new NInternalDiagListener() {
            public void onClosing(NInternalDiag diag) {
            }

            public void onClosed(NInternalDiag diag) {
            }

            public void onShowingDone(NInternalDiag arg0) {
                oKCancelPane.setDefaultOK();
            }
        });
        return diag.showInternalDiag(parent);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        deleteRecvPrompt = new javax.swing.JLabel();
        oKCancelPane = new com.yz.rms.client.ui.OKCancelPanel();

        setMinimumSize(new java.awt.Dimension(258, 85));
        setLayout(new java.awt.GridBagLayout());

        deleteRecvPrompt.setText("<html>确认要执行删除接收人“张三”的操作吗？<br /><font color=\"#c47f2d\">提示：删除的接收人将不可恢复。</font></html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 20);
        add(deleteRecvPrompt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(oKCancelPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel deleteRecvPrompt;
    private com.yz.rms.client.ui.OKCancelPanel oKCancelPane;
    // End of variables declaration//GEN-END:variables
}
