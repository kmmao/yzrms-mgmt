/*
 * ExpenseTIdTNamePIdPNameWrap.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-25 18:11:16
 */
package com.yz.rms.common.model.wrap;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class ExpenseTIdTNamePIdPNameWrap implements Serializable {

    private static final long serialVersionUID = -1315996031503086435L;
    /**
     * 报销单ID
     */
    private String expenseId;//每个人的报销单ID不同
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 报销人ID
     */
    private String expensePersonId;

    /**
     * 项目名称
     */
    private String projectName;

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
     * 团队id
     */
    private String teamId;

    /**
     * 团队名
     */
    private String teamName;

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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.expenseId);
        hash = 71 * hash + Objects.hashCode(this.projectId);
        hash = 71 * hash + Objects.hashCode(this.expensePersonId);
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
        final ExpenseTIdTNamePIdPNameWrap other = (ExpenseTIdTNamePIdPNameWrap) obj;
        if (!Objects.equals(this.expenseId, other.expenseId)) {
            return false;
        }
        if (!Objects.equals(this.projectId, other.projectId)) {
            return false;
        }
        if (!Objects.equals(this.expensePersonId, other.expensePersonId)) {
            return false;
        }
        return true;
    }
}
