
/*
 * SignupOrgCodeTextFieldBorder.java
 * 
 * Copyright(c) 2007-2011 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2011-09-29 15:30:24
 */
package com.vsd.client.util;

import com.nazca.ui.GraphicsTool;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author zhoue
 */
public class RightDefaultValueTextFieldBorder implements Border {

    private static final int LEFT_MARGIN = 0;
    private static final int RIGHT_MARGIN = 5;
    private String code = "";

    public void setDistrictCode(String code) {
        this.code = code;
    }

    public String getDistrictCode() {
        return code;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {
        FontMetrics fm = g.getFontMetrics();
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        JTextField txfd = (JTextField) c;
        if (txfd.isEnabled()) {
            g.setColor(txfd.getForeground());
        } else {
            g.setColor(txfd.getDisabledTextColor());
        }
        Graphics2D g2d = (Graphics2D) g;
        GraphicsTool.setQuanlifiedGraphics(g2d);
        g2d.drawString(code, c.getWidth() - RIGHT_MARGIN - fm.stringWidth(code), textY);
    }

    public Insets getBorderInsets(Component c) {
        FontMetrics fm = c.getFontMetrics(c.getFont());
        int width = fm.stringWidth(code);
        return new Insets(0, 0, 0, width + LEFT_MARGIN + RIGHT_MARGIN);
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
