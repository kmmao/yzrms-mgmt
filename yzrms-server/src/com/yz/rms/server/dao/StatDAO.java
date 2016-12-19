/*
 * StatDAO.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-24 15:36:23
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
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.ExpenseTIdTNamePIdPNameWrap;
import com.yz.rms.server.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 报销统计的DAO
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class StatDAO {

    private static final String NORMAL_TEAM = "normalTeam";
    /**
     * 查询全部团队
     */
    private static final String QUERY_ALL_EXPENSE_FORM_SQL = "SELECT  ef.* ,mb." + TeamTableConsts.FIELD_TEAM_ID.getName() + " AS " + TeamTableConsts.FIELD_TEAM_ID.getName() + " ,tm." + TeamTableConsts.FIELD_TEAM_NAME.getName() + "  AS team_name  FROM  " + ExpenseFormTableConsts.TABLE_NAME.getName()
            + "  ef LEFT JOIN  " + MemberTableConsts.TABLE_NAME.getName() + " mb ON  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + "= mb."
            + MemberTableConsts.FIELD_MENBER_ID.getName() + "  INNER JOIN  " + TeamTableConsts.TABLE_NAME.getName() + " tm  ON  mb." + MemberTableConsts.FIELD_TEAM_ID.getName() + "  = tm." + TeamTableConsts.FIELD_TEAM_ID.getName() + "  WHERE  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName()
            + "    BETWEEN  ?  AND  ?  AND ef." + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + "=?  AND  (ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  OR ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  )";

    /**
     * 查询指定团队
     */
    private static final String QUERY_EXPENSE_FORM_BY_TEAM_SQL = "SELECT  ef.* ,mb." + TeamTableConsts.FIELD_TEAM_ID.getName() + " AS " + TeamTableConsts.FIELD_TEAM_ID.getName() + " ,tm." + TeamTableConsts.FIELD_TEAM_NAME.getName() + " AS team_name  FROM  " + ExpenseFormTableConsts.TABLE_NAME.getName()
            + "  ef LEFT JOIN  " + MemberTableConsts.TABLE_NAME.getName() + " mb ON  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + "= mb."
            + MemberTableConsts.FIELD_MENBER_ID.getName() + "  INNER JOIN  " + TeamTableConsts.TABLE_NAME.getName() + " tm  ON  mb." + MemberTableConsts.FIELD_TEAM_ID.getName() + "  = tm." + TeamTableConsts.FIELD_TEAM_ID.getName() + "  WHERE  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName()
            + "  BETWEEN  ?  AND  ? AND  mb." + MemberTableConsts.FIELD_TEAM_ID.getName() + " = ?  AND  ef." + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + "=?  AND  (ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  OR ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  )";

    /**
     * 查询管理团队
     */
    private static final String QUERY_EXPENSE_BY_MANAGE_TEAM_SQL = "SELECT  ef.*   FROM  " + ExpenseFormTableConsts.TABLE_NAME.getName()
            + "  ef LEFT JOIN  " + MemberTableConsts.TABLE_NAME.getName() + " mb ON  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName() + "= mb."
            + MemberTableConsts.FIELD_MENBER_ID.getName() + "  WHERE  ef." + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName()
            + "  BETWEEN  ?  AND  ?   AND  ef." + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + "=?  AND mb." + MemberTableConsts.FIELD_LEADER.getName() + "=?  AND  (ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  OR ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  )";

    /**
     * 查询全部项目
     */
    private static final String QUERY_ALL_PROJECT_EXPENSE_FORM_SQL = "SELECT  ef.* ,pj." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + "  AS project_name  FROM  " + ExpenseFormTableConsts.TABLE_NAME.getName() + " ef  LEFT JOIN  " + ProjectTableConsts.TABLE_NAME.getName() + "  pj  ON  pj." + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " =  ef." + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + "  WHERE  ef."
            + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName() + "  BETWEEN  ? AND  ?  AND  pj." + ProjectTableConsts.FIELD_STATE.getName() + "=?  AND ef." + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + "=?  AND  (ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  OR ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  )";

    /**
     * 查询指定项目
     */
    private static final String QUERY_EXPENSE_FORM_BY_PROJECT_SQL = "SELECT  ef.* ,pj." + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + "  AS project_name  FROM  " + ExpenseFormTableConsts.TABLE_NAME.getName() + " ef  LEFT JOIN  " + ProjectTableConsts.TABLE_NAME.getName() + "  pj  ON  pj." + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " =  ef." + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + "   WHERE  ef."
            + ExpenseFormTableConsts.FIELD_EXPENSE_TIME.getName() + "  BETWEEN  ? AND  ?   AND  ef." + ExpenseFormTableConsts.FIELD_PROJECT_ID.getName() + " =?  AND ef." + ExpenseFormTableConsts.FIELD_DELETE_STATE.getName() + "=?  AND  (ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  OR ef." + ExpenseFormTableConsts.FIELD_STATE.getName() + "=?  )";

    public HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> queryAllTeamStatDatas(Date start, Date end) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        //把teamID作为key，团队ID，团队名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> teamMap = new LinkedHashMap<>();
        try {
            //全部团队SQL
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_EXPENSE_FORM_SQL);
            ps.setTimestamp(1, new Timestamp(start.getTime()));
            ps.setTimestamp(2, new Timestamp(end.getTime()));
            ps.setString(3, RecordState.normal.name());
            ps.setString(4, ExpenseFormStateEnums.waitPay.name());
            ps.setString(5, ExpenseFormStateEnums.paid.name());
            rs = ps.executeQuery();
            //遍历查询的基本信息集，放入teamMap中
            teamMap = getTeamMap(NORMAL_TEAM, rs, teamMap);

        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return teamMap;
    }

    public HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> queryStatDatasByTeam(String teamId, Date start, Date end) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        //把teamID作为key，团队ID，团队名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> teamMap = new LinkedHashMap<>();
        try {
            //查询指定团队
            conn = ConnectionFactory.getConnection();
            if (teamId.equals(Team.MANAGER_TEAM)) {
                ps = conn.prepareStatement(QUERY_EXPENSE_BY_MANAGE_TEAM_SQL);
                ps.setTimestamp(1, new Timestamp(start.getTime()));
                ps.setTimestamp(2, new Timestamp(end.getTime()));
                ps.setString(3, RecordState.normal.name());
                ps.setBoolean(4, true);
                ps.setString(5, ExpenseFormStateEnums.waitPay.name());
                ps.setString(6, ExpenseFormStateEnums.paid.name());
                rs = ps.executeQuery();
                //遍历查询的基本信息集，放入teamMap中
                teamMap = getTeamMap(Team.MANAGER_TEAM, rs, teamMap);
            } else {
                ps = conn.prepareStatement(QUERY_EXPENSE_FORM_BY_TEAM_SQL);
                ps.setTimestamp(1, new Timestamp(start.getTime()));
                ps.setTimestamp(2, new Timestamp(end.getTime()));
                ps.setString(3, teamId);
                ps.setString(4, RecordState.normal.name());
                ps.setString(5, ExpenseFormStateEnums.waitPay.name());
                ps.setString(6, ExpenseFormStateEnums.paid.name());
                rs = ps.executeQuery();
                //遍历查询的基本信息集，放入teamMap中
                teamMap = getTeamMap(NORMAL_TEAM, rs, teamMap);
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return teamMap;
    }

    public HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> queryAllProjectStatDatas(Date start, Date end) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        //把teamID作为key，项目ID，项目名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap = new LinkedHashMap<>();
        try {
            //全部项目SQL
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_PROJECT_EXPENSE_FORM_SQL);
            ps.setTimestamp(1, new Timestamp(start.getTime()));
            ps.setTimestamp(2, new Timestamp(end.getTime()));
            ps.setString(3, ProjectEnums.underway.name());
            ps.setString(4, RecordState.normal.name());
            ps.setString(5, ExpenseFormStateEnums.waitPay.name());
            ps.setString(6, ExpenseFormStateEnums.paid.name());
            rs = ps.executeQuery();
            //遍历查询的基本信息集，放入teamMap中
            projectMap = getProjectMap(rs, projectMap);

        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return projectMap;
    }

    public HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> queryStatDatasByProjct(String projectId, Date start, Date end) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        //把teamID作为key，项目ID，项目名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap = new LinkedHashMap<>();
        try {
            //查询指定项目
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_EXPENSE_FORM_BY_PROJECT_SQL);
            ps.setTimestamp(1, new Timestamp(start.getTime()));
            ps.setTimestamp(2, new Timestamp(end.getTime()));
            ps.setString(3, projectId);
            ps.setString(4, RecordState.normal.name());
            ps.setString(5, ExpenseFormStateEnums.waitPay.name());
            ps.setString(6, ExpenseFormStateEnums.paid.name());
            rs = ps.executeQuery();
            //遍历查询的基本信息集，放入teamMap中
            projectMap = getProjectMap(rs, projectMap);
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return projectMap;
    }

    private HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> getTeamMap(String isManageTeam, ResultSet rs, HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> teamMap) throws SQLException {

        HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> teamAndExpenseIdMap = new LinkedHashMap<>();
        //进行遍历封装
        while (rs.next()) {
            ExpenseTIdTNamePIdPNameWrap expenseTeamdIdTeamNameWrap = null;

            //使teamId和报销人ID同时唯一确定key
            ExpenseTIdTNamePIdPNameWrap teamIdAndExpensePersonIdWrap = new ExpenseTIdTNamePIdPNameWrap();

            //判断是否是管理团队
            if (isManageTeam.equals(Team.MANAGER_TEAM)) {
                expenseTeamdIdTeamNameWrap = getExpenseTeamIdTeamNameWrap(Team.MANAGER_TEAM, rs);
                //使teamId和报销人ID同时唯一确定key
                teamIdAndExpensePersonIdWrap = new ExpenseTIdTNamePIdPNameWrap();
                teamIdAndExpensePersonIdWrap.setTeamId(Team.MANAGER_TEAM);
                teamIdAndExpensePersonIdWrap.setExpensePersonId(expenseTeamdIdTeamNameWrap.getExpensePersonId());
                teamIdAndExpensePersonIdWrap.setTeamName(Team.TEAM_NAME);
            } else {
                //不是管理团队的情况
                expenseTeamdIdTeamNameWrap = getExpenseTeamIdTeamNameWrap(NORMAL_TEAM, rs);
                teamIdAndExpensePersonIdWrap.setTeamId(expenseTeamdIdTeamNameWrap.getTeamId());
                teamIdAndExpensePersonIdWrap.setExpensePersonId(expenseTeamdIdTeamNameWrap.getExpensePersonId());
                teamIdAndExpensePersonIdWrap.setTeamName(expenseTeamdIdTeamNameWrap.getTeamName());
            }

            if (teamAndExpenseIdMap.get(teamIdAndExpensePersonIdWrap) != null) {
                //当通过key取得的value值不为空时，把teamId和ExpenseId相同的报销单循环想加
                teamAndExpenseIdMap = getTeamAndExpenseIdKeyNoNullMap(teamAndExpenseIdMap, teamIdAndExpensePersonIdWrap, expenseTeamdIdTeamNameWrap);
            } else {
                //当key取得的value为空时，创建一个对象放入value中
                teamAndExpenseIdMap = getTeamAndExpenseIdkeyNullMap(teamAndExpenseIdMap, teamIdAndExpensePersonIdWrap, expenseTeamdIdTeamNameWrap);
            }
        }

        //把 HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap>转成HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>>
        for (ExpenseTIdTNamePIdPNameWrap key : teamAndExpenseIdMap.keySet()) {
            if (teamMap.get(teamAndExpenseIdMap.get(key).getTeamId()) != null) {
                teamMap.get(teamAndExpenseIdMap.get(key).getTeamId()).add(teamAndExpenseIdMap.get(key));
            } else {
                List<ExpenseTIdTNamePIdPNameWrap> expenseFormLis = new ArrayList<>();
                teamMap.put(teamAndExpenseIdMap.get(key).getTeamId(), expenseFormLis);
                teamMap.get(teamAndExpenseIdMap.get(key).getTeamId()).add(teamAndExpenseIdMap.get(key));
            }
        }
        return teamMap;
    }

    private HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> getProjectMap(ResultSet rs, HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap) throws SQLException {

        HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> projectAndProjectIdMap = new LinkedHashMap<>();
        while (rs.next()) {
            //进行遍历封装
            ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap = getExpenseProjectIdProjectNameWrap(rs);

            //使projectId和报销人ID同时唯一确定key
            ExpenseTIdTNamePIdPNameWrap projectIdAndExpensePersonIdWrap = new ExpenseTIdTNamePIdPNameWrap();
            projectIdAndExpensePersonIdWrap.setProjectId(expenseProjectIdProjectNameWrap.getProjectId());
            projectIdAndExpensePersonIdWrap.setExpensePersonId(expenseProjectIdProjectNameWrap.getExpensePersonId());
            projectIdAndExpensePersonIdWrap.setProjectName(expenseProjectIdProjectNameWrap.getProjectName());

            if (projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap) != null) {
                //当通过key取得的value不为时，把相同projectId和相同ExpenseId的报销单循环想加
                projectAndProjectIdMap = getProjectAndProjectIdkeyNoNullMap(projectAndProjectIdMap, projectIdAndExpensePersonIdWrap, expenseProjectIdProjectNameWrap);
            } else {
                projectAndProjectIdMap = getProjectAndProjectIdKeyNullMap(projectAndProjectIdMap, projectIdAndExpensePersonIdWrap, expenseProjectIdProjectNameWrap);
            }
        }

        //把 HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap>转成HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>>
        for (ExpenseTIdTNamePIdPNameWrap key : projectAndProjectIdMap.keySet()) {
            if (projectMap.get(projectAndProjectIdMap.get(key).getProjectId()) != null) {
                projectMap.get(projectAndProjectIdMap.get(key).getProjectId()).add(projectAndProjectIdMap.get(key));
            } else {
                List<ExpenseTIdTNamePIdPNameWrap> expenseFormLis = new ArrayList<>();
                projectMap.put(projectAndProjectIdMap.get(key).getProjectId(), expenseFormLis);
                projectMap.get(projectAndProjectIdMap.get(key).getProjectId()).add(projectAndProjectIdMap.get(key));
            }
        }
        return projectMap;
    }

    private ExpenseTIdTNamePIdPNameWrap getExpenseProjectIdProjectNameWrap(ResultSet rs) throws SQLException {
        //遍历sql查询的结果集，把结果集封装成ExpenseTIdTNamePIdPNameWrap的list
        ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap = new ExpenseTIdTNamePIdPNameWrap();
        expenseProjectIdProjectNameWrap = setBasicExpenseInfo(expenseProjectIdProjectNameWrap, rs);
        expenseProjectIdProjectNameWrap.setProjectId(rs.getString(ExpenseFormTableConsts.FIELD_PROJECT_ID.getName()));
        expenseProjectIdProjectNameWrap.setProjectName(rs.getString(ProjectTableConsts.FIELD_PROJECT_NAME.getName()));
        return expenseProjectIdProjectNameWrap;
    }

    private ExpenseTIdTNamePIdPNameWrap getExpenseTeamIdTeamNameWrap(String isManageTeam, ResultSet rs) throws SQLException {
        //遍历sql查询的结果集，把结果集封装成ExpenseTIdTNamePIdPNameWrap的list
        ExpenseTIdTNamePIdPNameWrap expenseTeamdIdTeamNameWrap = new ExpenseTIdTNamePIdPNameWrap();
        expenseTeamdIdTeamNameWrap = setBasicExpenseInfo(expenseTeamdIdTeamNameWrap, rs);

        //判断传过来的是否是管理团队，对团队id和团队名进行不同的处理
        if (isManageTeam.equals(Team.MANAGER_TEAM)) {
            expenseTeamdIdTeamNameWrap.setTeamId(Team.MANAGER_TEAM);
            expenseTeamdIdTeamNameWrap.setTeamName(Team.TEAM_NAME);
        } else {
            expenseTeamdIdTeamNameWrap.setTeamId(rs.getString(MemberTableConsts.FIELD_TEAM_ID.getName()));
            expenseTeamdIdTeamNameWrap.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
        }
        return expenseTeamdIdTeamNameWrap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> getProjectAndProjectIdKeyNullMap(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> projectAndProjectIdMap, ExpenseTIdTNamePIdPNameWrap projectIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap) {
        //当通过key取得的对象为空时，新建一个对象放入value，然后对这个对象进行装值
        ExpenseTIdTNamePIdPNameWrap expenseForm = new ExpenseTIdTNamePIdPNameWrap();
        projectAndProjectIdMap.put(projectIdAndExpensePersonIdWrap, expenseForm);
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setProjectId(projectIdAndExpensePersonIdWrap.getProjectId());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setProjectName(projectIdAndExpensePersonIdWrap.getProjectName());
        projectAndProjectIdMap = setBasicProjectOrTeamExpensKeyNullInfo(projectAndProjectIdMap, projectIdAndExpensePersonIdWrap, expenseProjectIdProjectNameWrap);
        return projectAndProjectIdMap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> getTeamAndExpenseIdkeyNullMap(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> teamAndExpenseIdMap, ExpenseTIdTNamePIdPNameWrap teamIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseTeamdIdTeamNameWrap) {
        //当通过key取得的对象为空时，新建一个对象放入value，然后对这个对象进行装值
        ExpenseTIdTNamePIdPNameWrap expenseForm = new ExpenseTIdTNamePIdPNameWrap();
        teamAndExpenseIdMap.put(teamIdAndExpensePersonIdWrap, expenseForm);
        teamAndExpenseIdMap.get(teamIdAndExpensePersonIdWrap).setTeamId(teamIdAndExpensePersonIdWrap.getTeamId());
        teamAndExpenseIdMap.get(teamIdAndExpensePersonIdWrap).setTeamName(teamIdAndExpensePersonIdWrap.getTeamName());
        teamAndExpenseIdMap = setBasicProjectOrTeamExpensKeyNullInfo(teamAndExpenseIdMap, teamIdAndExpensePersonIdWrap, expenseTeamdIdTeamNameWrap);
        return teamAndExpenseIdMap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> getProjectAndProjectIdkeyNoNullMap(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> projectAndProjectIdMap, ExpenseTIdTNamePIdPNameWrap projectIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap) {
        //当通过key取得的对象不为空时，循环遍历想加
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setProjectId(projectIdAndExpensePersonIdWrap.getProjectId());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setProjectName(projectIdAndExpensePersonIdWrap.getProjectName());
        projectAndProjectIdMap = setBasicProjectOrTeamExpensKeyNoNullInfo(projectAndProjectIdMap, projectIdAndExpensePersonIdWrap, expenseProjectIdProjectNameWrap);
        return projectAndProjectIdMap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> getTeamAndExpenseIdKeyNoNullMap(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> teamAndExpenseIdMap, ExpenseTIdTNamePIdPNameWrap teamIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseTeamdIdTeamNameWrap) {
        //当通过key取得的对象不为空时，循环遍历相加
        teamAndExpenseIdMap.get(teamIdAndExpensePersonIdWrap).setTeamId(teamIdAndExpensePersonIdWrap.getTeamId());
        teamAndExpenseIdMap.get(teamIdAndExpensePersonIdWrap).setTeamName(teamIdAndExpensePersonIdWrap.getTeamName());
        teamAndExpenseIdMap = setBasicProjectOrTeamExpensKeyNoNullInfo(teamAndExpenseIdMap, teamIdAndExpensePersonIdWrap, expenseTeamdIdTeamNameWrap);
        return teamAndExpenseIdMap;
    }

    private ExpenseTIdTNamePIdPNameWrap setBasicExpenseInfo(ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap, ResultSet rs) throws SQLException {
        //遍历sql查询的结果集，把结果集封装成ExpenseTIdTNamePIdPNameWrap的list（团队和项目公共部分）
        expenseProjectIdProjectNameWrap.setExpenseId(rs.getString(ExpenseFormTableConsts.FIELD_EXPENSE_ID.getName()));
        expenseProjectIdProjectNameWrap.setExpensePersonId(rs.getString(ExpenseFormTableConsts.FIELD_EXPENSE_PERSON_ID.getName()));
        expenseProjectIdProjectNameWrap.setCityTraffic(rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_TRAFFIC.getName()));
        expenseProjectIdProjectNameWrap.setBooksMaterials(rs.getDouble(ExpenseFormTableConsts.FIELD_BOOKS_MATERIALS.getName()));
        expenseProjectIdProjectNameWrap.setCityGasoline(rs.getDouble(ExpenseFormTableConsts.FIELD_CITY_GASOLINE.getName()));
        expenseProjectIdProjectNameWrap.setCopyBind(rs.getDouble(ExpenseFormTableConsts.FIELD_COPY_BIND.getName()));
        expenseProjectIdProjectNameWrap.setEntertain(rs.getDouble(ExpenseFormTableConsts.FIELD_ENTERTAIN.getName()));
        expenseProjectIdProjectNameWrap.setMaterial(rs.getDouble(ExpenseFormTableConsts.FIELD_MATERIAL.getName()));
        expenseProjectIdProjectNameWrap.setTrain(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAIN.getName()));
        expenseProjectIdProjectNameWrap.setOfficeSupplies(rs.getDouble(ExpenseFormTableConsts.FIELD_OFFICE_SUPPLIES.getName()));
        expenseProjectIdProjectNameWrap.setTelephoneBill(rs.getDouble(ExpenseFormTableConsts.FIELD_TELEPHONE_BILL.getName()));
        expenseProjectIdProjectNameWrap.setPostage(rs.getDouble(ExpenseFormTableConsts.FIELD_POSTAGE.getName()));
        expenseProjectIdProjectNameWrap.setTravelAccommodation(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ACCOMMODATION.getName()));
        expenseProjectIdProjectNameWrap.setTravelTraffic(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_TRAFFIC.getName()));
        expenseProjectIdProjectNameWrap.setTravelMeals(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_MEALS.getName()));
        expenseProjectIdProjectNameWrap.setTravelAllowance(rs.getDouble(ExpenseFormTableConsts.FIELD_TRAVEL_ALLOWANCE.getName()));
        expenseProjectIdProjectNameWrap.setSpacePage(rs.getDouble(ExpenseFormTableConsts.FIELD_SPACE_PAGE.getName()));
        expenseProjectIdProjectNameWrap.setConferences(rs.getDouble(ExpenseFormTableConsts.FIELD_CONFERENCES.getName()));
        expenseProjectIdProjectNameWrap.setFieldOperation(rs.getDouble(ExpenseFormTableConsts.FIELD_FIELD_OPERATION.getName()));
        return expenseProjectIdProjectNameWrap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> setBasicProjectOrTeamExpensKeyNullInfo(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> projectAndProjectIdMap, ExpenseTIdTNamePIdPNameWrap projectIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap) {
        //当通过key取得的对象为空时，新建一个对象放入value，然后对这个对象进行装值(团队和项目共同的部分)
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setExpensePersonId(projectIdAndExpensePersonIdWrap.getExpensePersonId());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCityTraffic(expenseProjectIdProjectNameWrap.getCityTraffic());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelMeals(expenseProjectIdProjectNameWrap.getTravelMeals());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setBooksMaterials(expenseProjectIdProjectNameWrap.getBooksMaterials());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelAllowance(expenseProjectIdProjectNameWrap.getTravelAllowance());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCityGasoline(expenseProjectIdProjectNameWrap.getCityGasoline());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelAccommodation(expenseProjectIdProjectNameWrap.getTravelAccommodation());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCopyBind(expenseProjectIdProjectNameWrap.getCopyBind());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelTraffic(expenseProjectIdProjectNameWrap.getTravelTraffic());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setEntertain(expenseProjectIdProjectNameWrap.getEntertain());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setSpacePage(expenseProjectIdProjectNameWrap.getSpacePage());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setMaterial(expenseProjectIdProjectNameWrap.getMaterial());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setConferences(expenseProjectIdProjectNameWrap.getConferences());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTrain(expenseProjectIdProjectNameWrap.getTrain());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setFieldOperation(expenseProjectIdProjectNameWrap.getFieldOperation());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setOfficeSupplies(expenseProjectIdProjectNameWrap.getOfficeSupplies());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTelephoneBill(expenseProjectIdProjectNameWrap.getTelephoneBill());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setPostage(expenseProjectIdProjectNameWrap.getPostage());
        return projectAndProjectIdMap;
    }

    private HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> setBasicProjectOrTeamExpensKeyNoNullInfo(HashMap<ExpenseTIdTNamePIdPNameWrap, ExpenseTIdTNamePIdPNameWrap> projectAndProjectIdMap, ExpenseTIdTNamePIdPNameWrap projectIdAndExpensePersonIdWrap, ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap) {
        //当通过key取得的对象不为空时，循环遍历想加(团队和项目公共部分)
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setExpensePersonId(projectIdAndExpensePersonIdWrap.getExpensePersonId());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCityTraffic(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getCityTraffic() + expenseProjectIdProjectNameWrap.getCityTraffic());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelMeals(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTravelMeals() + expenseProjectIdProjectNameWrap.getTravelMeals());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setBooksMaterials(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getBooksMaterials() + expenseProjectIdProjectNameWrap.getBooksMaterials());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelAllowance(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTravelAllowance() + expenseProjectIdProjectNameWrap.getTravelAllowance());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCityGasoline(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getCityGasoline() + expenseProjectIdProjectNameWrap.getCityGasoline());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelAccommodation(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTravelAccommodation() + expenseProjectIdProjectNameWrap.getTravelAccommodation());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setCopyBind(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getCopyBind() + expenseProjectIdProjectNameWrap.getCopyBind());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTravelTraffic(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTravelTraffic() + expenseProjectIdProjectNameWrap.getTravelTraffic());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setEntertain(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getEntertain() + expenseProjectIdProjectNameWrap.getEntertain());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setSpacePage(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getSpacePage() + expenseProjectIdProjectNameWrap.getSpacePage());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setMaterial(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getMaterial() + expenseProjectIdProjectNameWrap.getMaterial());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setConferences(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getConferences() + expenseProjectIdProjectNameWrap.getConferences());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTrain(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTrain() + expenseProjectIdProjectNameWrap.getTrain());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setFieldOperation(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getFieldOperation() + expenseProjectIdProjectNameWrap.getFieldOperation());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setOfficeSupplies(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getOfficeSupplies() + expenseProjectIdProjectNameWrap.getOfficeSupplies());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setTelephoneBill(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getTelephoneBill() + expenseProjectIdProjectNameWrap.getTelephoneBill());
        projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).setPostage(projectAndProjectIdMap.get(projectIdAndExpensePersonIdWrap).getPostage() + expenseProjectIdProjectNameWrap.getPostage());
        return projectAndProjectIdMap;
    }
}
