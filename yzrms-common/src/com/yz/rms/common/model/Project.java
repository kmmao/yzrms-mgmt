/*
 * ProjectModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 09:33:33
 */
package com.yz.rms.common.model;

import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.enums.RecordState;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Project实体类
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class Project implements Serializable {

    private static final long serialVersionUID = -4682110467427826064L;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 甲方
     */
    private String customer;
    /**
     * 金额
     */
    private double  amount;
    /**
     * 合同签订开始时间
     */
    private Date startTime;
    /**
     * 合同签订结束时间
     */
    private Date endTime;
    /**
     * 状态
     */
    private ProjectEnums state = ProjectEnums.underway;
    /**
     * 删除状态
     */
    private RecordState deleteState = RecordState.normal;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 最近修改时间
     */
    private Date modifyTime;
    /**
     * 最近修改人
     */
    private String modifier;

    public String getProjectId() {
	return projectId;
    }

    public void setProjectId(String projectId) {
	this.projectId = projectId;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public String getCustomer() {
	return customer;
    }

    public void setCustomer(String customer) {
	this.customer = customer;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public ProjectEnums getState() {
	return state;
    }

    public void setState(ProjectEnums state) {
	this.state = state;
    }

    public RecordState getDeleteState() {
	return deleteState;
    }

    public void setDeleteState(RecordState deleteState) {
	this.deleteState = deleteState;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public String getCreator() {
	return creator;
    }

    public void setCreator(String creator) {
	this.creator = creator;
    }

    public Date getModifyTime() {
	return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
	this.modifyTime = modifyTime;
    }

    public String getModifier() {
	return modifier;
    }

    public void setModifier(String modifier) {
	this.modifier = modifier;
    }

    @Override
    public String toString() {
        return projectName;
    }

    
    @Override
    public int hashCode() {
	int hash = 5;
	hash = 37 * hash + Objects.hashCode(this.projectId);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Project other = (Project) obj;
	if (!Objects.equals(this.projectId, other.projectId)) {
	    return false;
	}
	return true;
    }

}
