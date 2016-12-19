/*
 * ExpenseFormModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 09:33:33
 */
package com.yz.rms.common.model;

import com.nazca.usm.common.BindingState;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.enums.RecordState;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 报销单
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseForm implements Serializable {

    private static final long serialVersionUID = -2260733722949564530L;
    /**
     * 报销单ID
     */
    private String expenseId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 报销人ID
     */
    private String expensePersonId;
    /**
     * 附件数
     */
    private int attachmentCount;
    /**
     * 报销时间
     */
    private Date expenseTime;
    /**
     * 状态
     */
    private ExpenseFormStateEnums state;
    /**
     * 通过状态
     */
    private boolean passState;
    /**
     * 备注
     */
    private String memo = "";
    /**
     * 删除状态
     */
    private RecordState deleteState = RecordState.normal;
    /**
     * 报销总金额
     */
    private Double expenseTotal;
    /**
     * 市内交通
     */
    private Double cityTraffic;
    /**
     * 出差餐费
     */
    private Double travelMeals;
    /**
     * 图书资料
     */
    private Double booksMaterials;
    /**
     * 出差补助
     */
    private Double travelAllowance;
    /**
     * 市内汽油费
     */
    private Double cityGasoline;
    /**
     * 出差住宿费
     */
    private Double travelAccommodation;
    /**
     * 复印装订费
     */
    private Double copyBind;
    /**
     * 出差交通费
     */
    private Double travelTraffic;
    /**
     * 市内招待费
     */
    private Double entertain;
    /**
     * 版面费
     */
    private Double spacePage;
    /**
     * 材料费
     */
    private Double material;
    /**
     * 会议费
     */
    private Double conferences;
    /**
     * 培训费
     */
    private Double train;
    /**
     * 外勤费
     */
    private Double fieldOperation;
    /**
     * 办公用品费
     */
    private Double officeSupplies;
    /**
     * 电话费
     */
    private Double telephoneBill;
    /**
     * 邮费
     */
    private Double postage;
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

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getExpensePersonId() {
        return expensePersonId;
    }

    public void setExpensePersonId(String expensePersonId) {
        this.expensePersonId = expensePersonId;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public Date getExpenseTime() {
        return expenseTime;
    }

    public void setExpenseTime(Date expenseTime) {
        this.expenseTime = expenseTime;
    }

    public ExpenseFormStateEnums getState() {
        return state;
    }

     public void setState(ExpenseFormStateEnums state) {
        this.state = state;
    }

    public boolean isPassState() {
        return passState;
    }

    public void setPassState(boolean passState) {
        this.passState = passState;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    public RecordState getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(RecordState deleteState) {
        this.deleteState = deleteState;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public Double getCityTraffic() {
        return cityTraffic;
    }

    public void setCityTraffic(Double cityTraffic) {
        this.cityTraffic = cityTraffic;
    }

    public Double getTravelMeals() {
        return travelMeals;
    }

    public void setTravelMeals(Double travelMeals) {
        this.travelMeals = travelMeals;
    }

    public Double getBooksMaterials() {
        return booksMaterials;
    }

    public void setBooksMaterials(Double booksMaterials) {
        this.booksMaterials = booksMaterials;
    }

    public Double getTravelAllowance() {
        return travelAllowance;
    }

    public void setTravelAllowance(Double travelAllowance) {
        this.travelAllowance = travelAllowance;
    }

    public Double getCityGasoline() {
        return cityGasoline;
    }

    public void setCityGasoline(Double cityGasoline) {
        this.cityGasoline = cityGasoline;
    }

    public Double getTravelAccommodation() {
        return travelAccommodation;
    }

    public void setTravelAccommodation(Double travelAccommodation) {
        this.travelAccommodation = travelAccommodation;
    }

    public Double getCopyBind() {
        return copyBind;
    }

    public void setCopyBind(Double copyBind) {
        this.copyBind = copyBind;
    }

    public Double getTravelTraffic() {
        return travelTraffic;
    }

    public void setTravelTraffic(Double travelTraffic) {
        this.travelTraffic = travelTraffic;
    }

    public Double getEntertain() {
        return entertain;
    }

    public void setEntertain(Double entertain) {
        this.entertain = entertain;
    }

    public Double getSpacePage() {
        return spacePage;
    }

    public void setSpacePage(Double spacePage) {
        this.spacePage = spacePage;
    }

    public Double getMaterial() {
        return material;
    }

    public void setMaterial(Double material) {
        this.material = material;
    }

    public Double getConferences() {
        return conferences;
    }

    public void setConferences(Double conferences) {
        this.conferences = conferences;
    }

    public Double getTrain() {
        return train;
    }

    public void setTrain(Double train) {
        this.train = train;
    }

    public Double getFieldOperation() {
        return fieldOperation;
    }

    public void setFieldOperation(Double fieldOperation) {
        this.fieldOperation = fieldOperation;
    }

    public Double getOfficeSupplies() {
        return officeSupplies;
    }

    public void setOfficeSupplies(Double officeSupplies) {
        this.officeSupplies = officeSupplies;
    }

    public Double getTelephoneBill() {
        return telephoneBill;
    }

    public void setTelephoneBill(Double telephoneBill) {
        this.telephoneBill = telephoneBill;
    }

    public Double getPostage() {
        return postage;
    }

    public void setPostage(Double postage) {
        this.postage = postage;
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
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.expenseId);
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
        final ExpenseForm other = (ExpenseForm) obj;
        if (!Objects.equals(this.expenseId, other.expenseId)) {
            return false;
        }
        return true;
    }

}
