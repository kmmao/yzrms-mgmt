/*
 * ProjectEnums.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 09:28:08
 */
package com.yz.rms.common.enums;

/**
 * 项目枚举类
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public enum ProjectEnums {

    underway() {
	@Override
	public String toString() {
	    return "进行中";
	}

    },
    expired() {
	@Override
	public String toString() {
	    return "已验收";
	}

    }
}
