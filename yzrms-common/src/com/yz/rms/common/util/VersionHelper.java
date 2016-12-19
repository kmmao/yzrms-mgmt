/*
 * TmapVersionHelper.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-07-22 10:31:18
 */
package com.yz.rms.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 版本工具
 * @author Qiu Dongyue
 */
public final class VersionHelper {
    private static final String BUILD_VERSION_PATH = "build-version.txt";
    private static final String RELEASE_VERSION_PATH = "release-version.txt";
    private static String totalVersion;
    private static String releaseVersion;
    private static String buildVersion;
    private VersionHelper(){}
    static {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(VersionHelper.class.getResourceAsStream(RELEASE_VERSION_PATH)));
            releaseVersion = reader.readLine();
        } catch (Throwable ex) {
            System.out.println("can't get release version");
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        releaseVersion = releaseVersion == null ? "1.0.0" : releaseVersion;

        try {
            reader = new BufferedReader(new InputStreamReader(VersionHelper.class.getResourceAsStream(BUILD_VERSION_PATH)));
            buildVersion = reader.readLine();
        } catch (Throwable ex) {
            System.out.println("can't get build version");
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        buildVersion = buildVersion == null ? "-b0" : ("-b" + buildVersion);

        totalVersion = releaseVersion + buildVersion;
    }
    /**
     * 完整版本号
     * 样例：1.2.0.200907021123
     * 说明：完整版本号 = 发布号 + 构建号，详见#getReleaseVersion()和#getBuildVersion()
     * @return
     */
    public static final String getTotalVersion(){
        return totalVersion;
    }

    /**
     * 发布号
     * 样例：1.2.0
     * 说明：第1位为大版本，在发生重大变动时增加（一般不改变）；第2位为次版本，在发生模块功能变动时增加；第3位为小版本，在发生小功能变动或bug修正时增加
     * @return
     */
    public static final String getReleaseVersion(){
        return releaseVersion;
    }

    /**
     * 构建号
     * 样例：Build 200907021123
     * 说明：前8位是Build日期，后几位是SVN版本号
     * @return
     */
    public static final String getBuildVersion(){
        return buildVersion;
    }
}
