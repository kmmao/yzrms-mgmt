package com.yz.rms.client.util;

/*
 * ClientUtils.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-09-16 15:38:02
 */


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 资源读取工具类
 * @author Qiu Dongyue
 */
public class ResourceUtil {

    /**
     * 返回BufferedImage
     *
     * @param imageName
     * @return
     */
    public static BufferedImage buildBufferedImage(String imageName) {
        try {
            return ImageIO.read(ResourceUtil.class.getResource(getImageURL(imageName)));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ImageIcon buildImageIcon(String imageName) {
        try {
            return new ImageIcon(ResourceUtil.class.getResource(getImageURL(imageName)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据图标名称获取该图标的地址
     *
     * @param imageName 图标的名称
     * @return 图标的地址
     */
    public static String getImageURL(String imageName) {
        return "/com/yz/rms/client/res/" + imageName;
    }
    
     /**
     * 获得年份的列表，从当前年到2000年
     * @return 
     */
     public static List<Integer> getYears() {
          List<Integer> yearList = new ArrayList<>();
          Calendar cal = Calendar.getInstance();
          int nowYear = cal.get(Calendar.YEAR);
          for(int i=nowYear;i>=2000;i--){
          yearList.add(nowYear--);
          }
        return yearList;
    }
     /**
      * 获取一年的十二个月及全年
      * @return 
      */
      public static List<Integer> getMonths() {
        List<Integer> months = new ArrayList<>();
        months.add(null);
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        return months;
    }
}
