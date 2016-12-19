/*
 * RecordState.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 11:16:15
 */
package com.yz.rms.common.enums;

/**
 * 删除状态enum
 * @author chenjianan
 */
public enum RecordState {
    normal() {
	@Override
	public String toString() {
	    return "正常";
	}
    },
    deleted() {
	@Override
	public String toString() {
	    return "已删除";
	}
    }
}
