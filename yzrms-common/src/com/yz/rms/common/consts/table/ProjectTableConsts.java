/*
 * ProjectTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:38:06
 */
package com.yz.rms.common.consts.table;

/**
 * 项目表枚举类
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public enum ProjectTableConsts {
    TABLE_NAME("project", 0),
    /**
     * 项目id
     */
    FIELD_PROJECT_ID("project_id", 64),
    /**
     * 项目名称
     */
    FIELD_PROJECT_NAME("project_name", 64),
    /**
     * 甲方
     */
    FIELD_CUSTOMER("customer", 64),
    /**
     * 金额
     */
    FIELD_AMOUNT("amount", 8),
    /**
     * 合同签订开始时间
     */
    FIELD_START_TIME("start_time", 0),
    /**
     * 合同签订结束时间
     */
    FIELD_END_TIME("end_time", 0),
    /**
     * 状态
     */
    FIELD_STATE("state", 16),
    /**
     * 删除状态
     */
    FIELD_DELETE_STATE("delete_state", 16),
    /**
     * 创建时间
     */
    FIELD_CREATE_TIME("create_time", 0),
    /**
     * 创建人
     */
    FIELD_CREATOR("creator", 64),
    /**
     * 最近修改时间
     */
    FIELD_MODIFY_TIME("modify_time", 0),
    /**
     * 最近修改人
     */
    FIELD_MODIFIER("modifier", 64);

    private String name;
    private int length;

    private ProjectTableConsts() {
    }

    private ProjectTableConsts(String name, int length) {
	this.name = name;
	this.length = length;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getLength() {
	return length;
    }

    public void setLength(int length) {
	this.length = length;
    }

}
