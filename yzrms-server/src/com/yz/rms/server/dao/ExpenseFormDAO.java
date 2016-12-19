/*
 * ExpenseFormDAO.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 16:52:59
 */
package com.yz.rms.server.dao;

import com.nazca.sql.JDBCUtil;
import com.yz.rms.common.consts.table.ExpenseFormTableConsts;
import com.yz.rms.common.consts.table.MemberTableConsts;
import com.yz.rms.common.consts.table.ProjectTableConsts;
import com.yz.rms.common.consts.table.TeamTableConsts;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.enums.RecordState;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import com.yz.rms.server.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报销单持久层
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseFormDAO {

    private static final String YEAR = "year";
    private static final String COUNT = "count";
    private static final String AMOUNT = "amount";

    /**
     * 获取所有待发放统计信息SQL
     */
    private static final String QUERY_WAITPAY_SQL = "SELECT DATE_FORMAT ("
	    + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName() + ", '%Y%m') AS "
	    + YEAR
	    + ", COUNT(*) AS " + COUNT + ", "
	    + "SUM(" + ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName()
	    + ") AS " + AMOUNT + " FROM  "
	    + ExpenseFormTableConsts.TABLE_NAME.getName()
	    + " WHERE " + ExpenseFormTableConsts.FIELD_STATE.getName() + " = '"
	    + ExpenseFormStateEnums.waitPay.name() + "' " + "AND "
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = '" + RecordState.normal.name() + "' "
	    + " GROUP BY " + YEAR + " ORDER BY " + YEAR + " DESC";

    /**
     * 查询团队teamId
     */
    private static final String QUERY_TEAM_ID = " SELECT "
	    + MemberTableConsts.FIELD_TEAM_ID.getName()
	    + " FROM " + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " = ? ";

    /**
     * 根据报销单Id查询报销单
     */
    private static final String QUERTY_EXPENESEBYID_SQL = "SELECT * FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " WHERE "
	    + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName() + " = ? ";

    /**
     * 添加报销单SQL
     */
    private static final String ADD_SQL = "INSERT INTO "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + "("
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_ATTACHMENT_COUNT.getName() + ","
	    + ExpenseFormTableConsts.FIELD_BOOKS_MATERIALS.getName() + ","
	    + ExpenseFormTableConsts.FIELD_CITY_GASOLINE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_CITY_TRAFFIC.getName() + ","
	    + ExpenseFormTableConsts.FIELD_CONFERENCES.getName() + ","
	    + ExpenseFormTableConsts.FIELD_COPY_BIND.getName() + ","
	    + ExpenseFormTableConsts.FIELD_CREATE_TIME.getName() + ","
	    + ExpenseFormTableConsts.FIELD_CREATOR.getName() + ","
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_ENTERTAIN.getName() + ","
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + ","
	    + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName() + ","
	    + ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName() + ","
	    + ExpenseFormTableConsts.FIELD_FIELD_OPERATION.getName() + ","
	    + ExpenseFormTableConsts.FIELD_MATERIAL.getName() + ","
	    + ExpenseFormTableConsts.FIELD_OFFICE_SUPPLIES.getName() + ","
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_POSTAGE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_SPACE_PAGE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TELEPHONE_BILL.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TRAIN.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TRAVEL_ACCOMMODATION.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TRAVEL_ALLOWANCE.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TRAVEL_MEALS.getName() + ","
	    + ExpenseFormTableConsts.FIELD_TRAVEL_TRAFFIC.getName() + ","
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + ","
	    + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName() + ","
	    + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + ","
	    + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName() + ")"
	    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 修改报销单SQL
     */
    private static final String MODIFY_SQL = "UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " SET "
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?, "
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + "=1,"
	    + ExpenseFormTableConsts.FIELD_ATTACHMENT_COUNT.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_BOOKS_MATERIALS.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_CITY_GASOLINE.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_CITY_TRAFFIC.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_CONFERENCES.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_COPY_BIND.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_ENTERTAIN.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_FIELD_OPERATION.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_MATERIAL.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_OFFICE_SUPPLIES.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_POSTAGE.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_SPACE_PAGE.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_TELEPHONE_BILL.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_TRAIN.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_TRAVEL_ACCOMMODATION.getName()
	    + "=?,"
	    + ExpenseFormTableConsts.FIELD_TRAVEL_ALLOWANCE.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_TRAVEL_MEALS.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_TRAVEL_TRAFFIC.getName() + "=?,"
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + "=?"
	    + " WHERE " + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()
	    + "=?";
    /**
     * sg我的报销单个数常量
     */
    private static final String COUNT_MYEXPENSE_FORM = "count_myexpense_form";

    /**
     * sg删除报销单SQL
     */
    private static final String DELETE_SQL = "UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " SET "
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ?,"
	    + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + " = ?,"
	    + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName() + " =?"
	    + " WHERE " + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()
	    + " = ?";
    /**
     * sg提交报销单SQL
     */
    private static final String SUBMIT_SQL = "UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " SET "
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ?,"
	    + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + " = ?,"
	    + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName() + " =?"
	    + " WHERE " + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()
	    + " = ?";

    /**
     * 审核报销单通过SQL
     */
    private static final String AUDIT_EXPENSE_FORM_PASS = " UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName()
	    + " SET " + ExpenseFormTableConsts.FIELD_MEMO.getName() + " = ? ,"
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ,"
	    + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + " = ? WHERE "
	    + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()
	    + " = ?";

    /**
     * 审核报销单不通过SQL
     */
    private static final String AUDIT_EXPENSE_FORM_DENY = " UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName()
	    + " SET " + ExpenseFormTableConsts.FIELD_MEMO.getName() + " = ? ,"
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_MODIFIER.getName() + " = ? WHERE "
	    + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName() + " = ?";
    /**
     * 标记发放报销单SQL
     */
    private static final String MARK_PAID_EXPENSE_FORM = " UPDATE "
	    + ExpenseFormTableConsts.TABLE_NAME.getName()
	    + " SET " + ExpenseFormTableConsts.FIELD_STATE.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName()
	    + " = ? ," + ExpenseFormTableConsts.FIELD_MODIFIER.getName()
	    + " = ? WHERE " + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()
	    + " = ? ";
    /**
     * 查询报销单(主管) teamId leader = 0 state
     */
    private static final String QUERY_WAIT_AUDIT_EXPENSE_FORM
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? AND "
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 查询报销单总数
     */
    private static final String QUERY_WAIT_AUDIT_EXPENSE_FORM_COUNT
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? AND "
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 总经理团队筛选框-筛选非管理团队报销单 teamId state1 = waitAuditForPm state2 =waitAuditForCeo
     */
    private static final String QUERY_TEAM_EXPENSE_FORM_FOR_CEO
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? ) AND ( c."
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? OR a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";
    /**
     * 总经理团队筛选框-筛选非管理团队报销单总数SQL
     */
    private static final String QUERY_TEAM_EXPENSE_FORM_COUNT_FOR_CEO
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? ) AND ( c."
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? OR a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 行政--待行政审核、待发放和已发放报销单查询 state
     */
    private static final String QUERY_WAIT_PAY_AND_PAID_EXPENSE_FORM
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 行政--待行政审核、待发放和已发放报销单数量
     */
    private static final String QUERY_WAIT_PAY_AND_PAID_EXPENSE_FORM_COUNT
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 查询总经理角色下的待审核报销单 leader = 1 state1 = waitAuditForPm state2 = waitAuditForCeo
     */
    private static final String QUERY_WAIT_AUDIT_EXPENSE_FORM_FOR_CEO
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? OR a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";
    /**
     * 总经理角色下的待审核报销单总数SQL
     */
    private static final String QUERY_WAIT_AUDIT_EXPENSE_FORM_COUNT_FOR_CEO
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? OR a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 查询总经理待行政审核、待发放和已发放报销单 leader = 1 state
     */
    private static final String QUERY_EXPENSE_FORM_FOR_CEO
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE c."
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 查询总经理待行政审核、待发放和已发放报销单总数SQL
     */
    private static final String QUERY_EXPENSE_FORM_COUNT_FOR_CEO
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE c."
	    + MemberTableConsts.FIELD_LEADER.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 行政团队筛选框-筛选团队所有成员当前状态下的报销单 teamId state
     */
    private static final String QUERY_TEAM_EXPENSE_FORM
	    = "SELECT a.*,b." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
	    + TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 行政团队筛选框-筛选团队所有成员当前状态下的报销单总数
     */
    private static final String QUERY_TEAM_EXPENSE_FORM_COUNT
	    = "SELECT COUNT(*) AS " + COUNT_MYEXPENSE_FORM + " FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " a,"
	    + ProjectTableConsts.TABLE_NAME.getName() + " b,"
	    + MemberTableConsts.TABLE_NAME.getName() + " c,"
	    + TeamTableConsts.TABLE_NAME.getName() + " d WHERE ( a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " IN ( SELECT "
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " FROM "
	    + MemberTableConsts.TABLE_NAME.getName() + " WHERE "
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_STATE.getName() + " = ? ) AND a."
	    + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + " = c."
	    + MemberTableConsts.FIELD_MENBER_ID.getName() + " AND c."
	    + MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " AND a."
	    + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " = b."
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " AND d."
	    + TeamTableConsts.FIELD_TEAM_ID.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " = ? AND a."
	    + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ? AND b."
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ? AND d."
	    + TeamTableConsts.FIELD_DELETE_STATE.getName() + " = ? ";

    /**
     * 根据报销单的ExpenseId查询报销单所有信息
     */
    private static final String QUERY_EXPENSE_FORM = "SELECT * FROM "
	    + ExpenseFormTableConsts.TABLE_NAME.getName() + " WHERE "
	    + ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName() + " = ? ";

    /**
     * 审核报销单方法
     *
     * @param state
     * @param expenseId
     * @param memo
     * @param isPass
     * @param modifier
     * @throws Exception
     */
    public void auditExpenseForm(ExpenseFormStateEnums state,
	    String expenseId, String memo, boolean isPass, String modifier)
	    throws Exception {
	Connection conn = null;
	PreparedStatement ps = null;
	try {
	    if (isPass) {
		conn = ConnectionFactory.getConnection();
		ps = conn.prepareStatement(AUDIT_EXPENSE_FORM_PASS);
		ps.setString(1, memo);
		ps.setBoolean(2, isPass);
		ps.setString(3, state.name());
		ps.setTimestamp(4, new Timestamp(new Date().getTime()));
		ps.setString(5, modifier);
		ps.setString(6, expenseId);
		ps.executeUpdate();
	    } else {
		conn = ConnectionFactory.getConnection();
		ps = conn.prepareStatement(AUDIT_EXPENSE_FORM_DENY);
		ps.setString(1, memo);
		ps.setBoolean(2, isPass);
		ps.setTimestamp(3, new Timestamp(new Date().getTime()));
		ps.setString(4, modifier);
		ps.setString(5, expenseId);
		ps.executeUpdate();
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(null, ps, conn);
	}
    }

    /**
     * 标记发放报销单
     *
     * @param expenseFormIdList
     * @param modifier
     * @throws Exception
     */
    public void markPaid(List<String> expenseFormIdList, String modifier) throws Exception {
	Connection conn = null;
	PreparedStatement ps = null;
	try {
	    conn = ConnectionFactory.getConnection();
	    for (String expenseId : expenseFormIdList) {
		ps = conn.prepareStatement(MARK_PAID_EXPENSE_FORM);
		ps.setString(1, ExpenseFormStateEnums.paid.name());
		ps.setTimestamp(2, new Timestamp(new Date().getTime()));
		ps.setString(3, modifier);
		ps.setString(4, expenseId);
		ps.executeUpdate();
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(null, ps, conn);
	}
    }

    /**
     * 添加报销单
     *
     * @param expenseForm
     * @return
     * @throws Exception
     */
    public ExpenseForm createExpenseForm(ExpenseForm expenseForm) throws
	    Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(ADD_SQL);
	    ps.setString(1, expenseForm.getState().name());
	    ps.setInt(2, expenseForm.getAttachmentCount());
	    ps.setDouble(3, expenseForm.getBooksMaterials() != null
		    ? expenseForm.getBooksMaterials() : 0);
	    ps.setDouble(4, expenseForm.getCityGasoline() != null ? expenseForm.
		    getCityGasoline() : 0);
	    ps.setDouble(5, expenseForm.getCityTraffic() != null ? expenseForm.
		    getCityTraffic() : 0);
	    ps.setDouble(6, expenseForm.getConferences() != null ? expenseForm.
		    getConferences() : 0);
	    ps.setDouble(7, expenseForm.getCopyBind() != null ? expenseForm.
		    getCopyBind() : 0);
	    ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
	    ps.setString(9, expenseForm.getCreator());
	    ps.setString(10, expenseForm.getDeleteState().name());
	    ps.setDouble(11, expenseForm.getEntertain() != null ? expenseForm.
		    getEntertain() : 0);
	    ps.setString(12, expenseForm.getExpensePersonId());
	    ps.setDate(13, new java.sql.Date(expenseForm.getExpenseTime().
		    getTime()));
	    ps.setDouble(14, expenseForm.getExpenseTotal() != null
		    ? expenseForm.getExpenseTotal() : 0);
	    ps.setDouble(15, expenseForm.getFieldOperation() != null
		    ? expenseForm.getFieldOperation() : 0);
	    ps.setDouble(16, expenseForm.getMaterial() != null ? expenseForm.
		    getMaterial() : 0);
	    ps.setDouble(17, expenseForm.getOfficeSupplies() != null
		    ? expenseForm.getOfficeSupplies() : 0);
	    ps.setBoolean(18, true);
	    ps.setDouble(19, expenseForm.getPostage() != null ? expenseForm.
		    getPostage() : 0);
	    ps.setDouble(20, expenseForm.getSpacePage() != null ? expenseForm.
		    getSpacePage() : 0);
	    ps.setDouble(21, expenseForm.getTelephoneBill() != null
		    ? expenseForm.getTelephoneBill() : 0);
	    ps.setDouble(22, expenseForm.getTrain() != null ? expenseForm.
		    getTrain() : 0);
	    ps.setDouble(23, expenseForm.getTravelAccommodation() != null
		    ? expenseForm.getTravelAccommodation() : 0);
	    ps.setDouble(24, expenseForm.getTravelAllowance() != null
		    ? expenseForm.getTravelAllowance() : 0);
	    ps.setDouble(25, expenseForm.getTravelMeals() != null ? expenseForm.
		    getTravelMeals() : 0);
	    ps.setDouble(26, expenseForm.getTravelTraffic() != null
		    ? expenseForm.getTravelTraffic() : 0);
	    ps.setString(27, expenseForm.getProjectId());
	    ps.setTimestamp(28, new Timestamp(System.currentTimeMillis()));
	    ps.setString(29, expenseForm.getModifier());
	    ps.setString(30, expenseForm.getExpenseId());
	    ps.executeUpdate();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return expenseForm;
    }

    /**
     * 修改报销单
     *
     * @param expenseForm
     * @return
     * @throws Exception
     */
    public ExpenseForm modifyExpenseForm(ExpenseForm expenseForm) throws
	    Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(MODIFY_SQL);
	    ps.setString(1, expenseForm.getState().name());
	    ps.setInt(2, expenseForm.getAttachmentCount());
	    ps.setDouble(3, expenseForm.getBooksMaterials() != null
		    ? expenseForm.getBooksMaterials() : 0);
	    ps.setDouble(4, expenseForm.getCityGasoline() != null
		    ? expenseForm.getCityGasoline() : 0);
	    ps.setDouble(5, expenseForm.getCityTraffic() != null
		    ? expenseForm.getCityTraffic() : 0);
	    ps.setDouble(6, expenseForm.getConferences() != null
		    ? expenseForm.getConferences() : 0);
	    ps.setDouble(7, expenseForm.getCopyBind() != null ? expenseForm.
		    getCopyBind() : 0);
	    ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
	    ps.setString(9, expenseForm.getModifier());
	    ps.setDouble(10, expenseForm.getEntertain() != null
		    ? expenseForm.getEntertain() : 0);
	    ps.setDate(11, new java.sql.Date(expenseForm.getExpenseTime().
		    getTime()));
	    ps.setDouble(12, expenseForm.getExpenseTotal() != null
		    ? expenseForm.getExpenseTotal() : 0);
	    ps.setDouble(13, expenseForm.getFieldOperation() != null
		    ? expenseForm.getFieldOperation() : 0);
	    ps.setDouble(14, expenseForm.getMaterial() != null
		    ? expenseForm.getMaterial() : 0);
	    ps.setDouble(15, expenseForm.getOfficeSupplies() != null
		    ? expenseForm.getOfficeSupplies() : 0);
	    ps.setDouble(16, expenseForm.getPostage() != null ? expenseForm.
		    getPostage() : 0);
	    ps.setDouble(17, expenseForm.getSpacePage() != null
		    ? expenseForm.getSpacePage() : 0);
	    ps.setDouble(18, expenseForm.getTelephoneBill() != null
		    ? expenseForm.getTelephoneBill() : 0);
	    ps.setDouble(19, expenseForm.getTrain() != null ? expenseForm.
		    getTrain() : 0);
	    ps.setDouble(20, expenseForm.getTravelAccommodation() != null
		    ? expenseForm.getTravelAccommodation() : 0);
	    ps.setDouble(21, expenseForm.getTravelAllowance() != null
		    ? expenseForm.getTravelAllowance() : 0);
	    ps.setDouble(22, expenseForm.getTravelMeals() != null
		    ? expenseForm.getTravelMeals() : 0);
	    ps.setDouble(23, expenseForm.getTravelTraffic() != null
		    ? expenseForm.getTravelTraffic() : 0);
	    ps.setString(24, expenseForm.getProjectId());
	    ps.setString(25, expenseForm.getExpenseId());
	    ps.executeUpdate();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return expenseForm;
    }

    /**
     * 通过ID查询报销单
     *
     * @param expenseID
     * @return
     * @throws Exception
     */
    private ExpenseForm getExpenseFormByID(String expenseID) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ExpenseForm expenseForm = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(QUERTY_EXPENESEBYID_SQL);
	    ps.setString(1, expenseID);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		expenseForm = new ExpenseForm();
		expenseForm.setExpenseId(rs.getString(
			ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()));
		expenseForm.setModifyTime(rs.getTimestamp(
			ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName()));
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return expenseForm;
    }

    public int queryMyExpenseTotalCount(ExpenseFormStateEnums state,
	    String projectId, Date startTime, Date endTime, String expensePersonId) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS "
		+ COUNT_MYEXPENSE_FORM + " FROM "
		+ ExpenseFormTableConsts.TABLE_NAME.getName() + " WHERE 1=1");

	if (state != null) {
	    if (state == ExpenseFormStateEnums.newForm) {
		sql.append(" AND " + ExpenseFormTableConsts.FIELD_STATE.getName()
			+ " = ?");
	    } else if (state == ExpenseFormStateEnums.waitAuditForPm || state == ExpenseFormStateEnums.waitAuditForHr || state == ExpenseFormStateEnums.waitAuditForCeo || state == ExpenseFormStateEnums.paid || state == ExpenseFormStateEnums.waitPay) {
		sql.append(" AND " + ExpenseFormTableConsts.FIELD_STATE.getName()
			+ " = ?" + " AND " + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " =1"
		);
	    } 
	    else {
		sql.append(" AND " + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " =0"
		);
	    }
	}
	if (projectId != null) {
	    sql.append(" AND " + ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName() + " = ?");
	}
	if (startTime != null) {
	    sql.append(" AND " + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName() + " >= ?");
	}
	if (endTime != null) {
	    sql.append(" AND " + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName() + " <= ?");
	}
	sql.append(" AND " + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName()
		+ " = ?" + " AND " + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName()
		+ " = ?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    int idx = 0;
	    if (state != null) {
		if (!state.equals(ExpenseFormStateEnums.deny)) {
		    ps.setString(++idx, state.name());
		}
	    }
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setString(++idx, RecordState.normal.name());
	    ps.setString(++idx, expensePersonId);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    public List<ExpenseFormWrap> queryMyExpenseForm(ExpenseFormStateEnums state,
	    String projectId, Date startTime, Date endTime, int start,
	    int pageSize, String expensePersonId) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	ExpenseFormWrap data = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder("SELECT a.*,b."
		+ ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ",d."
		+ TeamTableConsts.FIELD_TEAM_NAME.getName() + " FROM "
		+ ExpenseFormTableConsts.TABLE_NAME.getName()
		+ " a LEFT JOIN " + ProjectTableConsts.TABLE_NAME.getName()
		+ " b ON a." + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName()
		+ " = b."
		+ ProjectTableConsts.FIELD_PROJECT_ID.getName() + " LEFT JOIN "
		+ MemberTableConsts.TABLE_NAME.getName() + " c ON a."
		+ ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName()
		+ " = c." + MemberTableConsts.FIELD_MENBER_ID.getName()
		+ " LEFT JOIN "
		+ TeamTableConsts.TABLE_NAME.getName() + " d ON c."
		+ MemberTableConsts.FIELD_TEAM_ID.getName() + " = d."
		+ TeamTableConsts.FIELD_TEAM_ID.getName() + " WHERE 1=1");
	if (state != null) {
	    if (state == ExpenseFormStateEnums.newForm) {
		sql.append(" AND a." + ExpenseFormTableConsts.FIELD_STATE.getName()
			+ " = ?");
	    } else if (state == ExpenseFormStateEnums.waitAuditForPm || state == ExpenseFormStateEnums.waitAuditForHr || state == ExpenseFormStateEnums.waitAuditForCeo || state == ExpenseFormStateEnums.paid || state == ExpenseFormStateEnums.waitPay) {
		sql.append(" AND a." + ExpenseFormTableConsts.FIELD_STATE.getName()
			+ " = ?" + " AND a." + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " =1"
		);
	    } 
	    else {
		sql.append(" AND a." + ExpenseFormTableConsts.FIELD_PASS_STATE.getName() + " =0"
		);
	    }
	}
	if (projectId != null) {
	    sql.append(" AND a." + ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName() + " = ?");
	}

	if (startTime != null) {
	    sql.append(" AND a." + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName() + " >= ?");
	}
	if (endTime != null) {
	    sql.append(" AND a." + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName() + " <= ?");
	}

	sql.append(" AND a." + ExpenseFormTableConsts.FIELD_DELETE_STATE.
		getName() + " =?"
		+ " AND a." + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.
		getName() + " =?"
		+ " ORDER BY a." + ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName() + " DESC LIMIT ?,?");

	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    int idx = 0;
	    if (state != null) {
		if (!state.equals(ExpenseFormStateEnums.deny)) {
		    ps.setString(++idx, state.name());
		} 
	    }
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setString(++idx, RecordState.normal.name());
	    ps.setString(++idx, expensePersonId);
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (SQLException ex) {
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    public ExpenseForm deleteExpenseForm(String expenseId, String modifier) throws Exception {
	PreparedStatement ps = null;
	ExpenseForm expenseForm = new ExpenseForm();
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(DELETE_SQL);
	    ps.setString(1, RecordState.deleted.name());
	    ps.setString(2, modifier);
	    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    ps.setString(4, expenseId);
	    ps.executeUpdate();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	}
	return expenseForm;
    }

    public ExpenseForm submitExpenseForm(String expenseId, String modifier) throws Exception {
	PreparedStatement ps = null;
	ExpenseForm expenseForm = new ExpenseForm();
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(SUBMIT_SQL);
	    ps.setString(1, ExpenseFormStateEnums.waitAuditForPm.name());
	    ps.setString(2, modifier);
	    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    ps.setString(4, expenseId);
	    ps.executeUpdate();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	}
	return expenseForm;
    }

    /**
     * 获取团队teamId
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public String queryTeamId(String userId) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	String teamId = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(QUERY_TEAM_ID);
	    ps.setString(1, userId);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		teamId = rs.getString(MemberTableConsts.FIELD_TEAM_ID.getName());
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return teamId;
    }

    /**
     * 查询未发放报销统计
     *
     * @return
     * @throws Exception
     */
    public Map<Integer, List<WaitPayStatWrap>> queryWaitPayStat() throws
	    Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	Map<Integer, List<WaitPayStatWrap>> result = new HashMap<>();
	List<WaitPayStatWrap> waitPayStatWraps = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    System.out.println(QUERY_WAITPAY_SQL);
	    ps = conn.prepareStatement(QUERY_WAITPAY_SQL);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		WaitPayStatWrap waitPayStatWrap;
		if (!result.containsKey(rs.getInt(YEAR) / 100)) {
		    waitPayStatWraps = new ArrayList();
		    waitPayStatWrap = new WaitPayStatWrap();
		    waitPayStatWrap.setMonth(rs.getInt(YEAR) % 100);
		    waitPayStatWrap.setAmount(rs.getDouble(AMOUNT));
		    waitPayStatWrap.setCount(rs.getInt(COUNT));
		    waitPayStatWraps.add(waitPayStatWrap);
		    result.put((rs.getInt(YEAR) / 100), waitPayStatWraps);
		} else {
		    waitPayStatWrap = new WaitPayStatWrap();
		    waitPayStatWrap.setMonth(rs.getInt(YEAR) % 100);
		    waitPayStatWrap.setAmount(rs.getDouble(AMOUNT));
		    waitPayStatWrap.setCount(rs.getInt(COUNT));
		    waitPayStatWraps.add(waitPayStatWrap);
		}
		result.put((rs.getInt(YEAR) / 100), waitPayStatWraps);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return result;
    }

    /**
     * 查询报销单
     *
     * @param state
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryExpenseFormByState(
	    ExpenseFormStateEnums state,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_WAIT_AUDIT_EXPENSE_FORM);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setBoolean(2, false);
	    ps.setString(3, state.name());
	    ps.setString(4, teamId);
	    ps.setBoolean(5, true);
	    ps.setString(6, RecordState.normal.name());
	    ps.setString(7, ProjectEnums.underway.name());
	    ps.setString(8, RecordState.normal.name());
	    ps.setString(9, RecordState.normal.name());
	    int idx = 9;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 查询报销单总数
     *
     * @param state
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryExpenseFormByStateCount(
	    ExpenseFormStateEnums state,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder(QUERY_WAIT_AUDIT_EXPENSE_FORM_COUNT);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setBoolean(2, false);
	    ps.setString(3, state.name());
	    ps.setString(4, teamId);
	    ps.setBoolean(5, true);
	    ps.setString(6, RecordState.normal.name());
	    ps.setString(7, ProjectEnums.underway.name());
	    ps.setString(8, RecordState.normal.name());
	    ps.setString(9, RecordState.normal.name());
	    int idx = 9;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    /**
     * 总经理团队筛选框-筛选非管理团队报销单
     *
     * @param state1
     * @param state2
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryTeamExpenseFormForCeo(
	    ExpenseFormStateEnums state1, ExpenseFormStateEnums state2,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_TEAM_EXPENSE_FORM_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setBoolean(2, true);
	    ps.setString(3, state1.name());
	    ps.setString(4, state2.name());
	    ps.setString(5, teamId);
	    ps.setBoolean(6, true);
	    ps.setString(7, RecordState.normal.name());
	    ps.setString(8, ProjectEnums.underway.name());
	    ps.setString(9, RecordState.normal.name());
	    ps.setString(10, RecordState.normal.name());
	    int idx = 10;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 总经理团队筛选框-筛选非管理团队报销单总数
     *
     * @param state1
     * @param state2
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryTeamExpenseFormCountForCeo(
	    ExpenseFormStateEnums state1, ExpenseFormStateEnums state2,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder(QUERY_TEAM_EXPENSE_FORM_COUNT_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setBoolean(2, true);
	    ps.setString(3, state1.name());
	    ps.setString(4, state2.name());
	    ps.setString(5, teamId);
	    ps.setBoolean(6, true);
	    ps.setString(7, RecordState.normal.name());
	    ps.setString(8, ProjectEnums.underway.name());
	    ps.setString(9, RecordState.normal.name());
	    ps.setString(10, RecordState.normal.name());
	    int idx = 10;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    /**
     * 行政--待行政审核、待发放和已发放报销单查询
     *
     * @param state
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryWaitPayAndPaidExpenseForm(
	    ExpenseFormStateEnums state, String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_WAIT_PAY_AND_PAID_EXPENSE_FORM);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, state.name());
	    ps.setBoolean(2, true);
	    ps.setString(3, RecordState.normal.name());
	    ps.setString(4, ProjectEnums.underway.name());
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, RecordState.normal.name());
	    int idx = 6;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 行政--待行政审核、待发放和已发放报销单数量
     *
     * @param state
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryWaitPayAndPaidExpenseFormCount(
	    ExpenseFormStateEnums state, String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder(QUERY_WAIT_PAY_AND_PAID_EXPENSE_FORM_COUNT);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, state.name());
	    ps.setBoolean(2, true);
	    ps.setString(3, RecordState.normal.name());
	    ps.setString(4, ProjectEnums.underway.name());
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, RecordState.normal.name());
	    int idx = 6;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    /**
     * 审核报销单时，根据报销单的ExpenseId查询报销单总金额
     *
     * @param expenseId
     * @return
     * @throws Exception
     */
    public ExpenseForm queryExpenseForm(String expenseId) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(QUERY_EXPENSE_FORM);
	    ps.setString(1, expenseId);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		ExpenseForm expenseForm = new ExpenseForm();
		expenseForm.setExpenseTotal(rs.getDouble(ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName()));
		expenseForm.setState(ExpenseFormStateEnums.valueOf(rs.getString(ExpenseFormTableConsts.FIELD_STATE.getName())));
		return expenseForm;
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return null;
    }

    private ExpenseFormWrap buildExpenseFormWrap(ResultSet rs) throws
	    SQLException {
	ExpenseFormWrap data = new ExpenseFormWrap();
	ExpenseForm ef = new ExpenseForm();
	ef.setExpenseId(rs.getString(ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()));
	ef.setExpensePersonId(rs.getString(ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName()));
	ef.setExpenseTime(rs.getDate(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName()));
	ef.setProjectId(rs.getString(ExpenseFormTableConsts.FIELD_PROJECT_ID.getName()));
	ef.setAttachmentCount(rs.getInt(ExpenseFormTableConsts.FIELD_ATTACHMENT_COUNT.getName()));
	ef.setState(ExpenseFormStateEnums.valueOf(rs.getString(ExpenseFormTableConsts.FIELD_STATE.getName())));
	ef.setPassState(rs.getInt(ExpenseFormTableConsts.FIELD_PASS_STATE.getName()) == 1 ? true : false);
	ef.setConferences(rs.getDouble(ExpenseFormTableConsts.FIELD_CONFERENCES.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_CONFERENCES.getName()));
	ef.setEntertain(rs.getDouble(ExpenseFormTableConsts.FIELD_ENTERTAIN.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_ENTERTAIN.getName()));
	ef.setFieldOperation(rs.getDouble(ExpenseFormTableConsts.FIELD_FIELD_OPERATION.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_FIELD_OPERATION.getName()));
	ef.setMaterial(rs.getDouble(ExpenseFormTableConsts.FIELD_MATERIAL.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_MATERIAL.getName()));
	ef.setCityTraffic(rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_TRAFFIC.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_TRAFFIC.getName()));
	ef.setBooksMaterials(rs.getDouble(ExpenseFormTableConsts.FIELD_BOOKS_MATERIALS.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_BOOKS_MATERIALS.getName()));
	ef.setCityGasoline(rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_GASOLINE.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_GASOLINE.getName()));
	ef.setCopyBind(rs.getDouble(ExpenseFormTableConsts.FIELD_COPY_BIND.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_COPY_BIND.getName()));
	ef.setExpenseTotal(rs.getDouble(ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_EXPENSE_TOTAL.getName()));
	ef.setOfficeSupplies(rs.getDouble(ExpenseFormTableConsts.FIELD_OFFICE_SUPPLIES.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_OFFICE_SUPPLIES.getName()));
	ef.setPostage(rs.getDouble(ExpenseFormTableConsts.FIELD_POSTAGE.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_POSTAGE.getName()));
	ef.setSpacePage(rs.getDouble(ExpenseFormTableConsts.FIELD_SPACE_PAGE.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_SPACE_PAGE.getName()));
	ef.setTelephoneBill(rs.getDouble(ExpenseFormTableConsts.FIELD_TELEPHONE_BILL.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TELEPHONE_BILL.getName()));
	ef.setTravelAccommodation(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ACCOMMODATION.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ACCOMMODATION.getName()));
	ef.setTrain(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAIN.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TRAIN.getName()));
	ef.setTravelAllowance(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ALLOWANCE.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ALLOWANCE.getName()));
	ef.setTravelMeals(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_MEALS.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_MEALS.getName()));
	ef.setTravelTraffic(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_TRAFFIC.getName()) == 0.0
		? null : rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_TRAFFIC.getName()));
	ef.setMemo(rs.getString(ExpenseFormTableConsts.FIELD_MEMO.getName()));
	ef.setCreator(rs.getString(ExpenseFormTableConsts.FIELD_CREATOR.getName()).isEmpty() ? null : rs.getString(ExpenseFormTableConsts.FIELD_CREATOR.getName()));
	ef.setCreateTime(rs.getTimestamp(ExpenseFormTableConsts.FIELD_CREATE_TIME.getName()));
	ef.setModifier(rs.getString(ExpenseFormTableConsts.FIELD_MODIFIER.getName()).isEmpty() ? null : rs.getString(ExpenseFormTableConsts.FIELD_MODIFIER.getName()));
	ef.setModifyTime(rs.getTimestamp(ExpenseFormTableConsts.FIELD_MODIFY_TIME.getName()));
	data.setExpenseForm(ef);
	data.setProjectName(rs.getString(ProjectTableConsts.FIELD_PROJECT_NAME.getName()));
	data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
	return data;
    }

    /**
     * 查询总经理待审核报销单
     *
     * @param state1
     * @param state2
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryWaitAuditExpenseFormForCeo(
	    ExpenseFormStateEnums state1, ExpenseFormStateEnums state2,
	    String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_WAIT_AUDIT_EXPENSE_FORM_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setBoolean(1, true);
	    ps.setString(2, state1.name());
	    ps.setString(3, state2.name());
	    ps.setBoolean(4, true);
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, ProjectEnums.underway.name());
	    ps.setString(7, RecordState.normal.name());
	    int idx = 7;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 总经理待审核报销单总数
     *
     * @param state1
     * @param state2
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryWaitAuditExpenseFormCountForCeo(
	    ExpenseFormStateEnums state1, ExpenseFormStateEnums state2,
	    String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_WAIT_AUDIT_EXPENSE_FORM_COUNT_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setBoolean(1, true);
	    ps.setString(2, state1.name());
	    ps.setString(3, state2.name());
	    ps.setBoolean(4, true);
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, ProjectEnums.underway.name());
	    ps.setString(7, RecordState.normal.name());
	    int idx = 7;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    /**
     * 查询总经理待行政审核、待发放和已发放报销单
     *
     * @param state
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryExpenseFormForCeo(
	    ExpenseFormStateEnums state,
	    String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_EXPENSE_FORM_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setBoolean(1, true);
	    ps.setString(2, state.name());
	    ps.setBoolean(3, true);
	    ps.setString(4, RecordState.normal.name());
	    ps.setString(5, ProjectEnums.underway.name());
	    ps.setString(6, RecordState.normal.name());
	    int idx = 6;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 总经理待行政审核、待发放和已发放报销单总数
     *
     * @param state
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryExpenseFormCountForCeo(
	    ExpenseFormStateEnums state,
	    String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_EXPENSE_FORM_COUNT_FOR_CEO);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setBoolean(1, true);
	    ps.setString(2, state.name());
	    ps.setBoolean(3, true);
	    ps.setString(4, RecordState.normal.name());
	    ps.setString(5, ProjectEnums.underway.name());
	    ps.setString(6, RecordState.normal.name());
	    int idx = 6;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    /**
     * 查询团队筛选框筛选报销单
     *
     * @param state
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ExpenseFormWrap> queryTeamExpenseForm(
	    ExpenseFormStateEnums state,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime, int start,
	    int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<ExpenseFormWrap> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder(QUERY_TEAM_EXPENSE_FORM);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	sql.append(" ORDER BY a.").append(ExpenseFormTableConsts.FIELD_CREATE_TIME.
		getName()).append(" DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setString(2, state.name());
	    ps.setString(3, teamId);
	    ps.setBoolean(4, true);
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, ProjectEnums.underway.name());
	    ps.setString(7, RecordState.normal.name());
	    ps.setString(8, RecordState.normal.name());
	    int idx = 8;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		ExpenseFormWrap data = buildExpenseFormWrap(rs);
		list.add(data);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    /**
     * 查询团队筛选框筛选报销单总数
     *
     * @param state
     * @param teamId
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public int queryTeamExpenseFormCount(ExpenseFormStateEnums state,
	    String teamId, String projectId, String keywords,
	    Date startTime, Date endTime) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder(QUERY_TEAM_EXPENSE_FORM_COUNT);
	if (projectId != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_PROJECT_ID.
		    getName()).append(" = ?");
	}
	if (startTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" >= ? ");
	}
	if (endTime != null) {
	    sql.append(" AND a.").append(ExpenseFormTableConsts.FIELD_EXPENSE_TIME.
		    getName()).append(" <= ? ");
	}
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, teamId);
	    ps.setString(2, state.name());
	    ps.setString(3, teamId);
	    ps.setBoolean(4, true);
	    ps.setString(5, RecordState.normal.name());
	    ps.setString(6, ProjectEnums.underway.name());
	    ps.setString(7, RecordState.normal.name());
	    ps.setString(8, RecordState.normal.name());
	    int idx = 8;
	    if (projectId != null) {
		ps.setString(++idx, projectId);
	    }
	    if (startTime != null) {
		ps.setTimestamp(++idx, new Timestamp(startTime.getTime()));
	    }
	    if (endTime != null) {
		ps.setTimestamp(++idx, new Timestamp(endTime.getTime()));
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		count = rs.getInt(COUNT_MYEXPENSE_FORM);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }
}
