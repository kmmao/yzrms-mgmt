/*
 * ExpenseFormTableConsts.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:52:18
 */
package com.yz.rms.common.consts.table;

/**
 * 报销单表枚举类
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public enum ExpenseFormTableConsts {
    TABLE_NAME("expense_form", 0),
    /**
     * 报销单ID
     */
    FIELD_EXPENSE_ID("expense_id", 64),
    /**
     * 项目ID
     */
    FIELD_PROJECT_ID("project_id", 64),
    /**
     * 报销人ID
     */
    FIELD_EXPENSE_PERSON_ID("expense_person_id", 64),
    /**
     * 报销数
     */
    FIELD_ATTACHMENT_COUNT("attachment_count", 8),
    /**
     * 报销时间
     */
    FIELD_EXPENSE_TIME("expense_time", 0),
    /**
     * 状态
     */
    FIELD_STATE("state", 16),
    /**
     * 通过状态
     */
    FIELD_PASS_STATE("pass_state", 1),
    /**
    * 备注信息
    */
    FIELD_MEMO("memo",256),
    /**
     * 删除状态
     */
    FIELD_DELETE_STATE("delete_state", 16),
    /**
     * 报销总金额
     */
    FIELD_EXPENSE_TOTAL("expense_total", 8),
    /**
     * 市内交通
     */
    FIELD_CITY_TRAFFIC("city_traffic", 8),
    /**
     * 出差餐费
     */
    FIELD_TRAVEL_MEALS("travel_meals", 8),
    /**
     * 图书资料
     */
    FIELD_BOOKS_MATERIALS("books_materials", 8),
    /**
     * 出差补助
     */
    FIELD_TRAVEL_ALLOWANCE("travel_allowance", 8),
    /**
     * 市内汽油费
     */
    FIELD_CITY_GASOLINE("city_gasoline", 8),
    /**
     * 出差住宿费
     */
    FIELD_TRAVEL_ACCOMMODATION("travel_accommodation", 8),
    /**
     * 复印装订费
     */
    FIELD_COPY_BIND("copy_bind", 8),
    /**
     * 出差交通费
     */
    FIELD_TRAVEL_TRAFFIC("travel_traffic", 8),
    /**
     * 室内招待费
     */
    FIELD_ENTERTAIN("entertain", 8),
    /**
     * 版面费
     */
    FIELD_SPACE_PAGE("space_page", 8),
    /**
     * 材料费
     */
    FIELD_MATERIAL("material", 8),
    /**
     * 会议费
     */
    FIELD_CONFERENCES("conferences", 8),
    /**
     * 培训费
     */
    FIELD_TRAIN("train", 8),
    /**
     * 外勤费
     */
    FIELD_FIELD_OPERATION("FIELD_operation", 8),
    /**
     * 办公用品费
     */
    FIELD_OFFICE_SUPPLIES("office_supplies", 8),
    /**
     * 电话费
     */
    FIELD_TELEPHONE_BILL("telephone_bill", 8),
    /**
     * 邮费
     */
    FIELD_POSTAGE("postage", 8),
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

    private ExpenseFormTableConsts() {
    }

    private ExpenseFormTableConsts(String name, int length) {
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
