/*
 * ProjectDAO.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 14:11:41
 */
package com.yz.rms.server.dao;

import com.nazca.sql.JDBCUtil;
import com.nazca.util.StringUtil;
import com.yz.rms.common.consts.table.ProjectTableConsts;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.enums.RecordState;
import com.yz.rms.common.model.Project;
import com.yz.rms.server.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 项目持久层
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class ProjectDAO {

    /**
     * 项目个数常量
     */
    private static final String COUNT_PROJECT = "count_project";
    /**
     * 添加项目SQL
     */
    private static final String ADD_SQL = "INSERT INTO " + ProjectTableConsts.TABLE_NAME.getName() + "("
	    + ProjectTableConsts.FIELD_STATE.getName() + ","
	    + ProjectTableConsts.FIELD_AMOUNT.getName() + ","
	    + ProjectTableConsts.FIELD_CREATE_TIME.getName() + ","
	    + ProjectTableConsts.FIELD_CREATOR.getName() + ","
	    + ProjectTableConsts.FIELD_CUSTOMER.getName() + ","
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + ","
	    + ProjectTableConsts.FIELD_END_TIME.getName() + ","
	    + ProjectTableConsts.FIELD_MODIFIER.getName() + ","
	    + ProjectTableConsts.FIELD_MODIFY_TIME.getName() + ","
	    + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + ","
	    + ProjectTableConsts.FIELD_START_TIME.getName() + ","
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + ")"
	    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 删除项目SQL
     */
    private static final String DELETE_SQL = "UPDATE " + ProjectTableConsts.TABLE_NAME.getName() + " SET "
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFIER.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFY_TIME.getName() + " = ?"
	    + " WHERE " + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " = ?";
    /**
     * 修改项目SQL
     */
    private static final String MODIFY_SQL = "UPDATE " + ProjectTableConsts.TABLE_NAME.getName() + " SET "
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_AMOUNT.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_CREATE_TIME.getName() + " =?,"
	    + ProjectTableConsts.FIELD_CREATOR.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_CUSTOMER.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_END_TIME.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFIER.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFY_TIME.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_START_TIME.getName() + " = ?"
	    + " WHERE " + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " = ?";
    /**
     * 标记项目状态SQL
     */
    private static final String MARK_SQL = "UPDATE " + ProjectTableConsts.TABLE_NAME.getName() + " SET "
	    + ProjectTableConsts.FIELD_STATE.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFIER.getName() + " = ?,"
	    + ProjectTableConsts.FIELD_MODIFY_TIME.getName() + " = ?"
	    + " WHERE " + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " = ?";
    /**
     * 根据id查询项目SQL,不带分页
     */
    private static final String QUERY_PROJECT_SQL_BY_ID = "SELECT * FROM "
	    + ProjectTableConsts.TABLE_NAME.getName() + " WHERE "
	    + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?" + " AND "
	    + ProjectTableConsts.FIELD_PROJECT_ID.getName() + " = ? ";

    public int queryProjectTotalCount(ProjectEnums state, String keywords) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0;
	StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS " + COUNT_PROJECT + " FROM "
		+ ProjectTableConsts.TABLE_NAME.getName() + " WHERE 1=1");
	if (state != null) {
	    sql.append(" AND " + ProjectTableConsts.FIELD_STATE.getName() + " = ?");
	}
	if (!StringUtil.isEmpty(keywords)) {
	    sql.append(" AND "
		    + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + " LIKE ? OR "
		    + ProjectTableConsts.FIELD_CUSTOMER.getName() + " LIKE ?");
	}
	sql.append(" AND " + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    int idx = 0;
	    if (state != null) {
		ps.setString(++idx, state.name());
	    }
	    if (!StringUtil.isEmpty(keywords)) {
		ps.setString(++idx, "%" + keywords + "%");
		ps.setString(++idx, "%" + keywords + "%");
	    }
	    ps.setString(++idx, RecordState.normal.name());
	    rs = ps.executeQuery();
	    if (rs.next()) {
		count = rs.getInt(COUNT_PROJECT);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return count;
    }

    public List<Project> queryProjectList(ProjectEnums state, String keywords, int start, int pageSize) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	Project data = null;
	List<Project> list = new ArrayList<>();
	StringBuilder sql = new StringBuilder("SELECT * FROM " + ProjectTableConsts.TABLE_NAME.getName() + " WHERE 1=1");
	if (state != null) {
	    sql.append(" AND " + ProjectTableConsts.FIELD_STATE.getName() + " = ?");
	}
	if (!StringUtil.isEmpty(keywords)) {
	    sql.append(" AND "
		    + ProjectTableConsts.FIELD_PROJECT_NAME.getName() + " LIKE ? OR "
		    + ProjectTableConsts.FIELD_CUSTOMER.getName() + " LIKE ?");
	}
	sql.append(" AND " + ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?" + " ORDER BY "
		+ ProjectTableConsts.FIELD_CREATE_TIME.getName() + " DESC LIMIT ?,?");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    int idx = 0;
	    if (state != null) {
		ps.setString(++idx, state.name());
	    }
	    if (!StringUtil.isEmpty(keywords)) {
		ps.setString(++idx, "%" + keywords + "%");
		ps.setString(++idx, "%" + keywords + "%");
	    }
	    ps.setString(++idx,RecordState.normal.name());
	    ps.setInt(++idx, start);
	    ps.setInt(++idx, pageSize);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		data = buildProjectWrap(rs);
		list.add(data);
	    }
	} catch (SQLException ex) {
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    private Project buildProjectWrap(ResultSet rs) throws SQLException {
	Project data = new Project();
	data.setState(ProjectEnums.valueOf(rs.getString(ProjectTableConsts.FIELD_STATE.getName())));
	data.setProjectId(rs.getString(ProjectTableConsts.FIELD_PROJECT_ID.getName()));
	data.setProjectName(rs.getString(ProjectTableConsts.FIELD_PROJECT_NAME.getName()));
	data.setAmount(rs.getDouble(ProjectTableConsts.FIELD_AMOUNT.getName()));
	data.setCustomer(rs.getString(ProjectTableConsts.FIELD_CUSTOMER.getName()));
	data.setStartTime(rs.getTimestamp(ProjectTableConsts.FIELD_START_TIME.getName()));
	data.setEndTime(rs.getTimestamp(ProjectTableConsts.FIELD_END_TIME.getName()));
	data.setCreator(rs.getString(ProjectTableConsts.FIELD_CREATOR.getName()));
	data.setCreateTime(rs.getTimestamp(ProjectTableConsts.FIELD_CREATE_TIME.getName()));
	data.setModifier(rs.getString(ProjectTableConsts.FIELD_MODIFIER.getName()));
	data.setModifyTime(rs.getTimestamp(ProjectTableConsts.FIELD_MODIFY_TIME.getName()));
	data.setDeleteState(RecordState.valueOf(rs.getString(ProjectTableConsts.FIELD_DELETE_STATE.getName())));
	return data;
    }

    public Project createProject(Project project) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(ADD_SQL);
	    ps.setString(1, project.getState() != null ? project.getState().name() : null);
	    ps.setDouble(2, project.getAmount());
	    //创建时间
	    ps.setTimestamp(3, project.getCreateTime() != null ? new Timestamp(project.getCreateTime().getTime()) : null);
	    ps.setString(4, project.getCreator());
	    ps.setString(5, project.getCustomer());
	    ps.setString(6, RecordState.normal.name());
	    ps.setTimestamp(7, project.getEndTime() != null ? new Timestamp(project.getEndTime().getTime()) : null);
	    ps.setString(8, project.getModifier());
	    //最近修改时间
	    ps.setTimestamp(9, project.getModifyTime() != null ? new Timestamp(project.getModifyTime().getTime()) : null);
	    ps.setString(10, project.getProjectName());
	    ps.setTimestamp(11, project.getStartTime() != null ? new Timestamp(project.getStartTime().getTime()) : null);
	    ps.setString(12, UUID.randomUUID().toString());
	    ps.executeUpdate();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return project;
    }

    public void deleteProject(String projectId, String modifier) throws Exception {
	PreparedStatement ps = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(DELETE_SQL);
	    ps.setString(1, RecordState.deleted.name());
	    ps.setString(2, modifier);
	    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    ps.setString(4, projectId);
	    ps.executeUpdate();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(null, ps, null);
	}
    }

    public Project modifyProject(Project project) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    Project oldProject = queryProjectById(project.getProjectId());
	    if(oldProject!=null){
	    if (oldProject.getModifyTime() != null) {
		    if (oldProject.getModifyTime().equals(project.getModifyTime())) {
			project.setModifyTime(new java.util.Date());
			//不应该用Date而是Timestamp
			ps = conn.prepareStatement(MODIFY_SQL);
			ps.setString(1, project.getState().name());
			ps.setDouble(2, project.getAmount());
			//创建时间
			ps.setTimestamp(3, new Timestamp(project.getCreateTime().getTime()));
			ps.setString(4, project.getCreator());
			ps.setString(5, project.getCustomer());
			ps.setString(6, project.getDeleteState().name());
			//结束时间
			ps.setTimestamp(7, new Timestamp(project.getEndTime().getTime()));
			//修改人
			ps.setString(8, project.getModifier());
			//修改时间
			ps.setTimestamp(9, new Timestamp(project.getModifyTime().getTime()));
			ps.setString(10, project.getProjectName());
			//开始时间
			ps.setTimestamp(11, new Timestamp(project.getStartTime().getTime()));
			ps.setString(12, project.getProjectId());
			ps.executeUpdate();
		    }
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	}
	return project;
    }

    public void changeProjectState(String projectId, ProjectEnums state, String modifier) throws Exception {
	PreparedStatement ps = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    conn.setAutoCommit(false);
	    ps = conn.prepareStatement(MARK_SQL);
	    ps.setString(1, state.name());
	    ps.setString(2, modifier);
	    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    ps.setString(4, projectId);
	    ps.executeUpdate();
	    conn.commit();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw ex;
	}
    }

    public List<Project> queryProjectList(ProjectEnums state) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<Project> list = new ArrayList();
	StringBuilder sql = new StringBuilder("SELECT * FROM " + ProjectTableConsts.TABLE_NAME.getName() + " WHERE "
		+ ProjectTableConsts.FIELD_DELETE_STATE.getName() + " = ?");
	if (state != null) {
	    sql.append(" AND " + ProjectTableConsts.FIELD_STATE.getName() + " = ? ");
	}
	sql.append(" ORDER BY " + ProjectTableConsts.FIELD_CREATE_TIME.getName() + " DESC");
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(sql.toString());
	    ps.setString(1, RecordState.normal.name());
	    if (state != null) {
		ps.setString(2, state.name());
	    }
	    rs = ps.executeQuery();
	    while (rs.next()) {
		Project project = buildProject(rs);
		list.add(project);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return list;
    }

    public Project queryProjectById(String projectId) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	Project project = null;
	try (Connection conn = ConnectionFactory.getConnection()) {
	    ps = conn.prepareStatement(QUERY_PROJECT_SQL_BY_ID);
	    ps.setString(1, RecordState.normal.name());
	    if (projectId != null) {
		ps.setString(2, projectId);
	    }
	    rs = ps.executeQuery();
	    if (rs.next()) {
		project = buildProject(rs);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw ex;
	} finally {
	    JDBCUtil.closeConnection(rs, ps, null);
	}
	return project;
    }

    private Project buildProject(ResultSet rs) throws SQLException {
	Project project = new Project();
	project.setProjectId(rs.getString(ProjectTableConsts.FIELD_PROJECT_ID.getName()));
	project.setProjectName(rs.getString(ProjectTableConsts.FIELD_PROJECT_NAME.getName()));
	project.setAmount(rs.getDouble(ProjectTableConsts.FIELD_AMOUNT.getName()));
	project.setCustomer(rs.getString(ProjectTableConsts.FIELD_CUSTOMER.getName()));
	project.setStartTime(rs.getTimestamp(ProjectTableConsts.FIELD_START_TIME.getName()));
	project.setEndTime(rs.getTimestamp(ProjectTableConsts.FIELD_END_TIME.getName()));
	project.setState(ProjectEnums.valueOf(rs.getString(ProjectTableConsts.FIELD_STATE.getName())));
	project.setCreateTime(rs.getTimestamp(ProjectTableConsts.FIELD_CREATE_TIME.getName()));
	project.setCreator(rs.getString(ProjectTableConsts.FIELD_CREATOR.getName()));
	project.setModifier(rs.getString(ProjectTableConsts.FIELD_MODIFIER.getName()));
	project.setModifyTime(rs.getTimestamp(ProjectTableConsts.FIELD_MODIFY_TIME.getName()));
	project.setDeleteState(RecordState.valueOf(rs.getString(ProjectTableConsts.FIELD_DELETE_STATE.getName())));
	return project;
    }
}
