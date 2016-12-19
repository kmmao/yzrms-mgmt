/*
 * PrintExpenseFormPanel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 18:00:18
 */
package com.yz.rms.client.ui.expenseform;

import com.nazca.ui.NInternalDiag;
import com.nazca.usm.model.USMSUser;
import com.nazca.util.NazcaFormater;
import com.yz.rms.client.util.ResourceUtil;
import com.yz.rms.client.util.USMSUserSyncTool;
import com.yz.rms.common.model.ExpenseForm;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.JRDocxSaveContributor;
import net.sf.jasperreports.view.save.JRHtmlSaveContributor;
import net.sf.jasperreports.view.save.JROdtSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;

/**
 *
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class PrintExpenseFormPanel extends javax.swing.JPanel {
    public static final String STUDENTNUM_EXAMINEE_FORM
            = "/com/yz/rms/client/model/expenseform/PrintExpenseForm.jasper";
    private List<ExpenseForm> expenseForms;
    private Map<String, Object> map;

    /**
     * Creates new form PrintExpenseFormPanel
     */
    public PrintExpenseFormPanel() {
        initComponents();
    }

    public ExpenseForm showMe(JComponent parent, List<ExpenseForm> expenseForms,
            Map<String, Object> map) {
        this.expenseForms = expenseForms;
        this.map = map;
        ExpenseForm ex = expenseForms.get(0);
        map.put("attachmentCount", ex.getAttachmentCount() == 0 ? "" : ex.
                getAttachmentCount());
        map.put("booksMaterials", ex.getBooksMaterials() == null ? "" : ex.
                getBooksMaterials().toString());
        map.put("cityGasoline", ex.getCityGasoline() == null ? "" : ex.
                getCityGasoline().toString());
        map.put("cityTraffic", ex.getCityTraffic() == null ? "" : ex.
                getCityTraffic().toString());
        map.put("conferences", ex.getConferences() == null ? "" : ex.
                getConferences().toString());
        map.put("copyBind", ex.getCopyBind() == null ? "" : ex.getCopyBind().
                toString());
        map.put("entertain", ex.getEntertain() == null ? "" : ex.getEntertain().
                toString());
        map.put("fieldOperation", ex.getFieldOperation() == null ? "" : ex.
                getFieldOperation().toString());
        map.put("material", ex.getMaterial() == null ? "" : ex.getMaterial().
                toString());
        map.put("officeSupplies", ex.getOfficeSupplies() == null ? "" : ex.
                getOfficeSupplies().toString());
        map.put("postage", ex.getPostage() == null ? "" : ex.getPostage().
                toString());
        map.put("spacePage", ex.getSpacePage() == null ? "" : ex.getSpacePage().
                toString());
        map.put("telephoneBill", ex.getTelephoneBill() == null ? "" : ex.
                getTelephoneBill().toString());
        map.put("train", ex.getTrain() == null ? "" : ex.getTrain().toString());
        map.put("travelAccommodation", ex.getTravelAccommodation() == null ? ""
                : ex.getTravelAccommodation().toString());
        map.put("travelAllowance", ex.getTravelAllowance() == null ? "" : ex.
                getTravelAllowance().toString());
        map.put("travelMeals", ex.getTravelMeals() == null ? "" : ex.
                getTravelMeals().toString());
        map.put("travelTraffic", ex.getTravelTraffic() == null ? "" : ex.
                getTravelTraffic().toString());
        map.put("expenseTotal", ex.getExpenseTotal().toString() + " ￥");
        map.put("expenseTime", NazcaFormater.getChineseDateString(ex.
                getExpenseTime()));
        BigDecimal bd1 = new BigDecimal(ex.getExpenseTotal());
        map.put("expenseTotal1", number2CNMontrayUnit(bd1));
        map.put("expenseName", getUserNameById(ex.getExpensePersonId()).
                getName());
        map.put("attachmentCount", ex.getAttachmentCount() + " 张");
        NInternalDiag<ExpenseForm, JComponent> diag = null;
        diag = new NInternalDiag<>("打印预览", ResourceUtil.buildImageIcon(
                "32.png"), this);
        diag.setCloseButtonVisible(true);
        diag.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        previewListInfo(expenseForms, map, STUDENTNUM_EXAMINEE_FORM);
        return diag.showInternalDiag(parent,
                NInternalDiag.INIT_SIZE_MODE_PREFERED);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(880, 700));
        setPreferredSize(new java.awt.Dimension(880, 700));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void previewListInfo(Collection collection, Map<String, Object> map,
            String formType) {
        try {
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(
                    collection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(getClass().
                    getResourceAsStream(formType), map,
                    source);
            JRViewer viewer = new JRViewer(jasperPrint, null);
            JRViewerToolbar bar = (JRViewerToolbar) viewer.getComponent(0);
            JRSaveContributor[] contributor = new JRSaveContributor[4];
            for (JRSaveContributor jrsc : bar.getSaveContributors()) {
                if (jrsc instanceof JRDocxSaveContributor) {
                    contributor[0] = jrsc;
                } else if (jrsc instanceof JRPdfSaveContributor) {
                    contributor[1] = jrsc;
                } else if (jrsc instanceof JRHtmlSaveContributor) {
                    contributor[2] = jrsc;
                } else if (jrsc instanceof JROdtSaveContributor) {
                    contributor[3] = jrsc;
                }
            }
            bar.setSaveContributors(contributor);
            for (int i = 0; i < bar.getComponentCount(); i++) {
                Component c = bar.getComponent(i);
                switch (i) {
                    case 0:
                        JButton btn0 = (JButton) c;
                        btn0.setMaximumSize(new Dimension(60, 23));
                        btn0.setPreferredSize(new Dimension(60, 23));
                        btn0.setText("保存");
                        btn0.setToolTipText(null);
                        break;
                    case 1:
                        JButton btn1 = (JButton) c;
                        btn1.setMaximumSize(new Dimension(60, 23));
                        btn1.setPreferredSize(new Dimension(60, 23));
                        btn1.setText("打印");
                        btn1.setToolTipText(null);
                        break;
                    case 2:
                        JButton btn2 = (JButton) c;
                        btn2.setMaximumSize(new Dimension(80, 23));
                        btn2.setPreferredSize(new Dimension(80, 23));
                        btn2.setText("重新载入");
                        btn2.setToolTipText(null);
                        break;
                    case 4:
                        JButton btn4 = (JButton) c;
                        btn4.setToolTipText("首页");
                        break;
                    case 5:
                        JButton btn5 = (JButton) c;
                        btn5.setToolTipText("上一页");
                        break;
                    case 6:
                        JButton btn6 = (JButton) c;
                        btn6.setToolTipText("下一页");
                        break;
                    case 7:
                        JButton btn7 = (JButton) c;
                        btn7.setToolTipText("尾页");
                        break;
                    case 10:
                        JToggleButton btn10 = (JToggleButton) c;
                        btn10.setToolTipText("实际尺寸");
                        break;
                    case 11:
                        JToggleButton btn11 = (JToggleButton) c;
                        btn11.setToolTipText("最适高度尺寸");
                        break;
                    case 12:
                        JToggleButton btn12 = (JToggleButton) c;
                        btn12.setToolTipText("最适宽度尺寸");
                        break;
                    case 14:
                        JButton btn14 = (JButton) c;
                        btn14.setMaximumSize(new Dimension(60, 23));
                        btn14.setPreferredSize(new Dimension(60, 23));
                        btn14.setText("放大");
                        btn14.setToolTipText(null);
                        break;
                    case 15:
                        JButton btn15 = (JButton) c;
                        btn15.setMaximumSize(new Dimension(60, 23));
                        btn15.setPreferredSize(new Dimension(60, 23));
                        btn15.setText("缩小");
                        btn15.setToolTipText(null);
                        break;
                    case 16:
                        JComboBox cbox = (JComboBox) c;
                        cbox.setToolTipText("显示比例");
                        break;
                    default:
                        break;
                }
            }
            PrintExpenseFormPanel.this.add(viewer, BorderLayout.CENTER);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
        "伍", "陆", "柒", "捌", "玖"};
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元", "拾",
        "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    private static final String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final int MONEY_PRECISION = 2;
    private static final String CN_ZEOR_FULL = "零元整";

    private static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();

        int signum = numberOfMoney.signum();
        if (signum == 0) {
            return "零元整";
        }
        long number = numberOfMoney.movePointRight(2).setScale(0, 4).abs().
                longValue();

        long scale = number % 100L;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        if (scale <= 0L) {
            numIndex = 2;
            number /= 100L;
            getZero = true;
        }
        if ((scale > 0L) && (scale % 10L <= 0L)) {
            numIndex = 1;
            number /= 10L;
            getZero = true;
        }
        int zeroSize = 0;
        while (number > 0L) {
            numUnit = (int) (number % 10L);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                zeroSize++;
                if (!getZero) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0L) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000L > 0L)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            number /= 10L;
            numIndex++;
        }
        if (signum == -1) {
            sb.insert(0, "负");
        }
        if (scale <= 0L) {
            sb.append("整");
        }
        return sb.toString();
    }

    private USMSUser getUserNameById(String userId) {
        USMSUser user = USMSUserSyncTool.getInstance().getUserById(userId);
        return user;
    }

}
