/*
 * NActionPaneLayout.java
 * 
 * Copyright(c) 2007-2011 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2011-10-14 17:08:21
 */
package com.yz.rms.client.ui;

import com.nazca.ui.NProcessingPanel;
import com.nazca.ui.UIUtilities;
import com.nazca.ui.laf.NazcaLAFTool;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

/**
 *
 * @author chen jianan
 */
public class NActionPaneLayout2 implements LayoutManager2 {

    private List<JComponent> leftBts = null;
    private List<JComponent> rightBts = null;
    private int buttonDistns = 10;
    private Insets insets = new Insets(8, 20, 8, 20);
    public static final String WAITING_COMP = "WAITING_COMP";
    public static final String MESSAGE_COMP = "MESSAGE_COMP";
    private NProcessingPanel waitingComp = null;
    private JLabel messageComp = null;
    private Container parent = null;
    private static int preferedHeight = (int) (40 * 1);

    public void setButtonDistns(int buttonDistns) {
        this.buttonDistns = buttonDistns;
    }

    public int getButtonDistns() {
        return buttonDistns;
    }

    public NActionPaneLayout2() {
        this.leftBts = new ArrayList<JComponent>();
        this.rightBts = new ArrayList<JComponent>();
    }

    public void addLayoutComponent(String name, Component comp) {
        //addLayoutComponent(comp, name);
    }

    public void removeLayoutComponent(Component comp) {
        leftBts.remove(comp);
        rightBts.remove(comp);
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(1, preferedHeight);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(1, preferedHeight);
    }

    public void layoutContainer(Container parent) {
        this.parent = parent;
        int widthL = insets.left;
        for (JComponent ab : leftBts) {
            if (!ab.isVisible()) {
                continue;
            }
            Dimension size = ab.getPreferredSize();
            ab.setBounds(widthL, insets.top, size.width, parent.getHeight() - insets.bottom - insets.top);
            widthL += size.width;
            widthL += buttonDistns;
        }
        int widthR = parent.getSize().width - insets.right;
        for (JComponent ab : rightBts) {
            if (!ab.isVisible()) {
                continue;
            }
            Dimension size = ab.getPreferredSize();
            ab.setBounds(widthR - size.width, insets.top, ab.getPreferredSize().width, parent.getSize().height - insets.bottom - insets.top);
            widthR -= size.width;
            widthR -= buttonDistns;
        }
        if (waitingComp != null && waitingComp.isVisible()) {
            int waitingW = parent.getHeight() - insets.top - insets.bottom - 4;
            waitingComp.setBounds(widthR - waitingW, insets.top + 2, waitingW, waitingW);
            widthR -= waitingW;
            widthR -= buttonDistns;
        }
        if (messageComp != null) {
            int msgW = widthR - widthL;
            int msgH = messageComp.getPreferredSize().height;
            messageComp.setBounds(widthL, (parent.getHeight() - msgH) / 2 + 1, msgW, msgH);
        }
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        if ((comp instanceof JButton) || (comp instanceof JToggleButton)) {
            AbstractButton ab = (AbstractButton) comp;
            ab.setMargin(new Insets(ab.getMargin().top, ab.getMargin().left + 4, ab.getMargin().bottom, ab.getMargin().right + 4));
        }
        if (constraints == null && comp instanceof JComponent) {
            rightBts.add((JComponent) comp);
            return;
        }
        if ((new Float(JComponent.LEFT_ALIGNMENT)).equals(constraints)) {
            leftBts.add((JComponent) comp);
        } else if ((new Float(JComponent.RIGHT_ALIGNMENT)).equals(constraints)) {
            rightBts.add((JComponent) comp);
        }
        if (constraints.equals(WAITING_COMP)) {
            waitingComp = (NProcessingPanel) comp;
        } else if (constraints.equals(MESSAGE_COMP)) {
            messageComp = (JLabel) comp;
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, 46);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    public void invalidateLayout(Container target) {
    }

    public void setCompPosition(JComponent c, float alianment, int position) {
        synchronized (c.getTreeLock()) {
            if (alianment == JComponent.LEFT_ALIGNMENT) {
                rightBts.remove(c);
                if (position < 0) {
                    position = 0;
                } else if (position > leftBts.size()) {
                    position = leftBts.size() - 1;
                }
                if (leftBts.contains(c)) {
                    leftBts.remove(c);
                }
                leftBts.add(position, c);
            } else {
                leftBts.remove(c);
                if (position > rightBts.size()) {
                    position = rightBts.size() - 1;
                }
                if (rightBts.contains(c)) {
                    rightBts.remove(c);
                }
                rightBts.add(position, c);
            }
        }
    }
}
