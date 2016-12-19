/*
 * NActionPane.java
 * 
 * Copyright(c) 2007-2011 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2011-10-14 13:45:33
 */
package com.yz.rms.client.ui;

import com.nazca.ui.NProcessingPanel;
import com.nazca.ui.laf.NDialogBottomPanelUI;
import com.nazca.ui.layout.NActionPaneLayout;
import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author chen jianan
 */
public class NActionPane2 extends JPanel {

    public NActionPane2() {
        initActionPanel();
    }
    private NActionPaneLayout2 layout = new NActionPaneLayout2();
    protected NProcessingPanel waitingProcess = null;
    protected JLabel msgLabel = null;

    private void initActionPanel() {
        setUI(new NDialogBottomPanelUI());
        setLayout(layout);
    }

    public void setAllButtonsEnable(boolean flag) {
        for (Component c : this.getComponents()) {
            if (c instanceof AbstractButton) {
                c.setEnabled(flag);
            }
        }
    }

    private void initWaitingBar() {
        if (waitingProcess == null) {
            this.waitingProcess = new NProcessingPanel();
            this.add(waitingProcess, NActionPaneLayout.WAITING_COMP);
            this.waitingProcess.setVisible(false);
        }
    }

    private void initMsgLabel() {
        if (this.msgLabel == null) {
            this.msgLabel = new JLabel();
            this.msgLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            this.add(msgLabel, NActionPaneLayout.MESSAGE_COMP);
            this.msgLabel.setVisible(false);
        }
    }

    public void setCompPositoin(JComponent c, float alianment, int position) {
        if (this != c.getParent()) {
            throw new IllegalStateException("not in this container:" + c);
        }
        invalidate();
        layout.setCompPosition(c, alianment, position);
    }

    public void changeButtonDistns(int buttonDistns) {
        layout.setButtonDistns(buttonDistns);
        validate();
        repaint();
    }

    /**
     * @return the msgLabel
     */
    public JLabel getMsgLabel() {
        initMsgLabel();
        return msgLabel;
    }

    /**
     * @return the waitingProcess
     */
    public NProcessingPanel getWaitingProcess() {
        initWaitingBar();
        return waitingProcess;
    }
}
