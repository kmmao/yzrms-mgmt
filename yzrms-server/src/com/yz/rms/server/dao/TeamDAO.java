/*
 * TeamDAO.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-23 13:43:28
 */
package com.yz.rms.server.dao;

import com.nazca.sql.JDBCUtil;
import com.yz.rms.common.consts.table.MemberTableConsts;
import com.yz.rms.common.consts.table.TeamTableConsts;
import com.yz.rms.common.enums.RecordState;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.model.Team;
import com.yz.rms.server.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 查询团队相关的DAO
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class TeamDAO {

    /**
     * 查询所有团队SQL
     */
    private static final String QUERY_ALL_TEAM_SQL = "SELECT * FROM  "
            + TeamTableConsts.TABLE_NAME.getName() + "  WHERE  " + TeamTableConsts.FIELD_DELETE_STATE.getName() + "  ='"
            + RecordState.normal.name() + "' ORDER BY "+TeamTableConsts.FIELD_CREATE_TIME.getName()+"  DESC";

    /**
     * 查询所有统计团队除去管理SQL
     */
    private static final String QUERY_ALL_STAT_TEAM_SQL = "SELECT * FROM  "
            + TeamTableConsts.TABLE_NAME.getName() + "  WHERE  " + TeamTableConsts.FIELD_DELETE_STATE.getName() + "  ='"
            + RecordState.normal.name() + "' ";

    /**
     * 查询项目经理管理的所有团队SQL
     */
    private static final String QUERY_ALL_TEAM_BY_PM_SQL = "SELECT tm.* FROM  "
            + TeamTableConsts.TABLE_NAME.getName() + "  tm  LEFT JOIN  " + MemberTableConsts.TABLE_NAME.getName()
            + "  mb  ON tm." + TeamTableConsts.FIELD_TEAM_ID.getName() + "= mb." + TeamTableConsts.FIELD_TEAM_ID.getName() + "  WHERE  mb."
            + MemberTableConsts.FIELD_MENBER_ID.getName() + "  = ?  AND  mb."
            + MemberTableConsts.FIELD_LEADER.getName() + " = ?  AND tm." + TeamTableConsts.FIELD_DELETE_STATE.getName() + "= ?";

    /**
     * 通过id查询团队SQL
     */
    private static final String QUERY_TEAM_SQL = "SELECT * FROM  "
            + TeamTableConsts.TABLE_NAME.getName() + "  WHERE  " + TeamTableConsts.FIELD_TEAM_ID.getName() + "=?";
    /**
     * 添加团队SQL
     */
    private static final String ADD_TEAM_SQL = "INSERT INTO " + TeamTableConsts.TABLE_NAME.getName() + "("
            + TeamTableConsts.FIELD_TEAM_ID.getName() + ","
            + TeamTableConsts.FIELD_TEAM_NAME.getName() + ","
            + TeamTableConsts.FIELD_DELETE_STATE.getName() + ","
            + TeamTableConsts.FIELD_CREATE_TIME.getName() + ","
            + TeamTableConsts.FIELD_CREATOR.getName() + ","
            + TeamTableConsts.FIELD_MODIFY_TIME.getName() + ","
            + TeamTableConsts.FIELD_MODIFIER.getName() + ")"
            + "values(?,?,?,?,?,?,?)";

    /**
     * 修改团队SQL
     */
    private static final String MODIFY_TEAM_SQL = "UPDATE  " + TeamTableConsts.TABLE_NAME.getName() + "  SET "
            + TeamTableConsts.FIELD_TEAM_NAME.getName() + "=?,"
            + TeamTableConsts.FIELD_MODIFY_TIME.getName() + "=?,"
            + TeamTableConsts.FIELD_MODIFIER.getName() + "=?"
            + "  where " + TeamTableConsts.FIELD_TEAM_ID.getName() + "=?";

    /**
     * 删除团队SQL
     */
    private static final String DELETE_TEAM_SQL = "UPDATE  " + TeamTableConsts.TABLE_NAME.getName() + "  SET  "
            + TeamTableConsts.FIELD_DELETE_STATE.getName() + "=?  "
            + "  where " + TeamTableConsts.FIELD_TEAM_ID.getName() + "=?";

    /**
     * 删除团队所有成员SQL
     */
    private static final String DELETE_TEAM_ALL_MEMBER_SQL = "DELETE FROM  " + MemberTableConsts.TABLE_NAME.getName()
            + "  WHERE " + MemberTableConsts.FIELD_TEAM_ID.getName() + "=? ";

    /**
     * 查询团队成员SQL
     */
    private static final String QUERY_TEAM_MEMBER_SQL = "SELECT * FROM  " + MemberTableConsts.TABLE_NAME.getName() + "  WHERE  "
            + TeamTableConsts.FIELD_TEAM_ID.getName() + "=?";

    /**
     * 查询团队成员SQL
     */
    private static final String QUERY_MANAGE_TEAM_MEMBER_SQL = "SELECT * FROM  " + MemberTableConsts.TABLE_NAME.getName() + "  WHERE  "
            + MemberTableConsts.FIELD_LEADER.getName() + "=?";

    /**
     * 查询成员表中的所有成员SQL
     */
    private static final String QUERY_MEMBER_TABLE_SQL = "SELECT * FROM  " + MemberTableConsts.TABLE_NAME.getName();

    /**
     * 增加团队成员SQL
     */
    private static final String ADD_TEAM_MEMBER_SQL = "INSERT INTO  " + MemberTableConsts.TABLE_NAME.getName() + "("
            + MemberTableConsts.FIELD_MENBER_ID.getName() + ","
            + MemberTableConsts.FIELD_TEAM_ID.getName() + ","
            + MemberTableConsts.FIELD_LEADER.getName() + ")"
            + "values(?,?,?)";
    /**
     * 删除团队成员SQL
     */
    private static final String DELETE_TEAM_MEMBER_SQL = "DELETE FROM  " + MemberTableConsts.TABLE_NAME.getName()
            + "  WHERE " + MemberTableConsts.FIELD_TEAM_ID.getName() + "=? " + "  AND  " + MemberTableConsts.FIELD_MENBER_ID.getName() + "=?";
    /**
     * 将团队成员设为负责人或组员SQL
     */
    private static final String MODIFY_MEMBER_AUTHORITY_SQL = "UPDATE  " + MemberTableConsts.TABLE_NAME.getName() + "  SET  "
            + MemberTableConsts.FIELD_LEADER.getName() + "=?"
            + "  where  "
            + MemberTableConsts.FIELD_TEAM_ID.getName() + "=?  AND  "
            + MemberTableConsts.FIELD_MENBER_ID.getName() + "=?";

    public List<Team> queryAllTeamsList() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Team data = null;
        List<Team> list = null;
        try {
            list = new ArrayList<>();
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_TEAM_SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Team();
                data.setTeamId(rs.getString(TeamTableConsts.FIELD_TEAM_ID.getName()));
                data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
                data.setDeleteState(RecordState.valueOf(rs.getString(TeamTableConsts.FIELD_DELETE_STATE.getName())));
                data.setCreateTime(new Date(rs.getTimestamp(TeamTableConsts.FIELD_CREATE_TIME.getName()).getTime()));
                data.setCreator(rs.getString(TeamTableConsts.FIELD_CREATOR.getName()));
                data.setModifyTime(rs.getTimestamp(TeamTableConsts.FIELD_MODIFY_TIME.getName()));
                data.setModifier(rs.getString(TeamTableConsts.FIELD_MODIFIER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return list;
    }

    public List<Team> queryAllTeamsByPMList(String memberId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Team data = null;
        List<Team> list = null;
        try {
            list = new ArrayList<>();
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_TEAM_BY_PM_SQL);
            ps.setString(1, memberId);
            ps.setBoolean(2, true);
            ps.setString(3, RecordState.normal.name());
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Team();
                data.setTeamId(rs.getString(TeamTableConsts.FIELD_TEAM_ID.getName()));
                data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
                data.setDeleteState(RecordState.valueOf(rs.getString(TeamTableConsts.FIELD_DELETE_STATE.getName())));
                data.setCreateTime(new Date(rs.getTimestamp(TeamTableConsts.FIELD_CREATE_TIME.getName()).getTime()));
                data.setCreator(rs.getString(TeamTableConsts.FIELD_CREATOR.getName()));
                data.setModifyTime(rs.getTimestamp(TeamTableConsts.FIELD_MODIFY_TIME.getName()));
                data.setModifier(rs.getString(TeamTableConsts.FIELD_MODIFIER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return list;
    }

    public Team createTeam(Team team) throws Exception {
        Date nowDate = new Date();
        team.setCreateTime(nowDate);
        team.setModifyTime(nowDate);
        team.setDeleteState(RecordState.normal);
        PreparedStatement ps = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            ps = conn.prepareStatement(ADD_TEAM_SQL);
            ps.setString(1, team.getTeamId());
            ps.setString(2, team.getTeamName());
            ps.setString(3, RecordState.normal.name());
            ps.setTimestamp(4, new Timestamp(team.getCreateTime().getTime()));
            ps.setString(5, team.getCreator());
            ps.setTimestamp(6, new Timestamp(team.getModifyTime().getTime()));
            ps.setString(7, team.getModifier());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
        return team;
    }

    public Team modifyTeam(Team team) throws Exception {
        PreparedStatement ps = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            Team oldTeam = queryTeamByTeamId(team.getTeamId());
            if (oldTeam.getModifyTime().equals(team.getModifyTime())) {
                ps = conn.prepareStatement(MODIFY_TEAM_SQL);
                ps.setString(1, team.getTeamName());
                ps.setTimestamp(2, new Timestamp(new Date().getTime()));
                ps.setString(3, team.getModifier());
                ps.setString(4, team.getTeamId());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
        return team;
    }

    public Team queryTeamByTeamId(String teamId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Team data = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            ps = conn.prepareStatement(QUERY_TEAM_SQL);
            ps.setString(1, teamId);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Team();
                data.setTeamId(rs.getString(TeamTableConsts.FIELD_TEAM_ID.getName()));
                data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
                data.setDeleteState(RecordState.valueOf(rs.getString(TeamTableConsts.FIELD_DELETE_STATE.getName())));
                data.setCreateTime(rs.getTimestamp(TeamTableConsts.FIELD_CREATE_TIME.getName()));
                data.setCreator(rs.getString(TeamTableConsts.FIELD_CREATOR.getName()));
                data.setModifyTime(rs.getTimestamp(TeamTableConsts.FIELD_MODIFY_TIME.getName()));
                data.setModifier(rs.getString(TeamTableConsts.FIELD_MODIFIER.getName()));
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, null);
        }
        return data;
    }

    public Team deleteTeam(String teamId) throws Exception {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            Team team = new Team();
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            team.setTeamId(teamId);
            team.setTeamName(Team.TEAM_NAME);
            ps = conn.prepareStatement(DELETE_TEAM_SQL);
            ps.setString(1, RecordState.deleted.name());
            ps.setString(2, teamId);
            ps.executeUpdate();
            ps = conn.prepareStatement(DELETE_TEAM_ALL_MEMBER_SQL);
            ps.setString(1, teamId);
            ps.executeUpdate();
            conn.commit();
            return team;
        } catch (SQLException ex) {
            conn.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
    }

    public List<Member> queryTeamMembersList(String teamId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member data;
        List<Member> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            ps = conn.prepareStatement(QUERY_TEAM_MEMBER_SQL);
            ps.setString(1, teamId);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Member();
                data.setMemberId(rs.getString(MemberTableConsts.FIELD_MENBER_ID.getName()));
                data.setTeamId(rs.getString(MemberTableConsts.FIELD_TEAM_ID.getName()));
                data.setLeader(rs.getBoolean(MemberTableConsts.FIELD_LEADER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, null);
        }
        return list;
    }

    public Member deleteTeamMember(String teamId, String memberId) throws Exception {
        PreparedStatement ps = null;
        Member member = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            member = new Member();
            ps = conn.prepareStatement(DELETE_TEAM_MEMBER_SQL);
            ps.setString(1, teamId);
            ps.setString(2, memberId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
        return member;
    }

    public List<Member> createTeamMember(List<String> memberIdList, String teamId) throws Exception {
        PreparedStatement ps = null;
        List<Member> memberLis = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(ADD_TEAM_MEMBER_SQL);
            for (int i = 0; i < memberIdList.size(); i++) {
                String memberId = memberIdList.get(i);
                Member member = new Member();
                member.setTeamId(teamId);
                member.setMemberId(memberId);
                member.setLeader(false);
                memberLis.add(member);
                ps.setString(1, memberId);
                ps.setString(2, teamId);
                ps.setBoolean(3, false);
                ps.addBatch();
                if (i == memberIdList.size() - 1) {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
        return memberLis;
    }

    public Member modifyMemberAuthority(String teamId, String userId, boolean authority) throws Exception {
        PreparedStatement ps = null;
        Member member = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            member = new Member();
            ps = conn.prepareStatement(MODIFY_MEMBER_AUTHORITY_SQL);
            ps.setBoolean(1, authority);
            ps.setString(2, teamId);
            ps.setString(3, userId);
            ps.executeUpdate();
            member.setTeamId(teamId);
            member.setMemberId(userId);
            member.setLeader(authority);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            JDBCUtil.closeConnection(null, ps, null);
        }
        return member;
    }

    public List<Team> queryAllStatTeamsList(String memberId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Team data = null;
        List<Team> list = null;
        try {
            list = new ArrayList<>();
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_STAT_TEAM_SQL);
            ps.setBoolean(1, false);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Team();
                data.setTeamId(rs.getString(TeamTableConsts.FIELD_TEAM_ID.getName()));
                data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
                data.setDeleteState(RecordState.valueOf(rs.getString(TeamTableConsts.FIELD_DELETE_STATE.getName())));
                data.setCreateTime(new Date(rs.getTimestamp(TeamTableConsts.FIELD_CREATE_TIME.getName()).getTime()));
                data.setCreator(rs.getString(TeamTableConsts.FIELD_CREATOR.getName()));
                data.setModifyTime(rs.getTimestamp(TeamTableConsts.FIELD_MODIFY_TIME.getName()));
                data.setModifier(rs.getString(TeamTableConsts.FIELD_MODIFIER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return list;
    }

    public List<Member> queryManageTeamMembersList() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member data;
        List<Member> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            ps = conn.prepareStatement(QUERY_MANAGE_TEAM_MEMBER_SQL);
            ps.setBoolean(1, true);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Member();
                data.setMemberId(rs.getString(MemberTableConsts.FIELD_MENBER_ID.getName()));
                data.setTeamId(rs.getString(MemberTableConsts.FIELD_TEAM_ID.getName()));
                data.setLeader(rs.getBoolean(MemberTableConsts.FIELD_LEADER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, null);
        }
        return list;
    }

    public List<String> queryMemberTableDate() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member data;
        List<String> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            ps = conn.prepareStatement(QUERY_MEMBER_TABLE_SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Member();
                list.add(rs.getString(MemberTableConsts.FIELD_MENBER_ID.getName()));
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, null);
        }
        return list;
    }

    public List<Team> queryAllTeamByCeoOrHr() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Team data;
        List<Team> list = null;
        try {
            list = new ArrayList<>();
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(QUERY_ALL_TEAM_SQL);
            rs = ps.executeQuery();
            Team manageTeam = new Team();
            manageTeam.setTeamId(Team.MANAGER_TEAM);
            manageTeam.setTeamName(Team.TEAM_NAME);
            list.add(null);
            list.add(manageTeam);
            while (rs.next()) {
                data = new Team();
                data.setTeamId(rs.getString(TeamTableConsts.FIELD_TEAM_ID.getName()));
                data.setTeamName(rs.getString(TeamTableConsts.FIELD_TEAM_NAME.getName()));
                data.setDeleteState(RecordState.valueOf(rs.getString(TeamTableConsts.FIELD_DELETE_STATE.getName())));
                data.setCreateTime(new Date(rs.getTimestamp(TeamTableConsts.FIELD_CREATE_TIME.getName()).getTime()));
                data.setCreator(rs.getString(TeamTableConsts.FIELD_CREATOR.getName()));
                data.setModifyTime(rs.getTimestamp(TeamTableConsts.FIELD_MODIFY_TIME.getName()));
                data.setModifier(rs.getString(TeamTableConsts.FIELD_MODIFIER.getName()));
                list.add(data);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            JDBCUtil.closeConnection(rs, ps, conn);
        }
        return list;
    }
}
