/*
 * ProjectConst.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-09-16 17:12:53
 */
package com.yz.rms.common.consts;

import java.io.File;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public interface ProjectConst {
    String USMS_MODULE_ID = "rms-module";
    String CONFIG_DIR_PATH = System.getProperty("user.home") + File.separator + ".yzrms";
    String CLIENT_PRJ_ID = "client";
    String SERVER_PRJ_ID = "server";
}
