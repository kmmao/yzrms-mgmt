/*
 * TeamMgmtServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 19:24:46
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.usm.client.connector.USMSRPCServiceException;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.MemberWrap;
import com.yz.rms.common.model.wrap.TeamMemberWrap;
import com.yz.rms.common.rpc.TeamService;
import com.yz.rms.common.util.ErrorCodeFormater;
import com.yz.rms.server.dao.TeamDAO;
import com.yz.rms.server.util.USMSServiceUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 团队管理实现类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class TeamServiceImpl implements TeamService {

    private static final Log log = LogFactory.getLog(TeamServiceImpl.class);
    TeamDAO dao = new TeamDAO();
    List<USMSUser> uSMSuserList;
    public TeamServiceImpl() throws USMSRPCServiceException {
        uSMSuserList = USMSServiceUtils.getAllUsers();
    }

    @Override
    public List<Team> queryAllTeams() throws HttpRPCException {
        List<Team> list = null;
        try {
            list = dao.queryAllTeamsList();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query team failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return list;
    }

    @Override
    public Team createTeam(Team team) throws HttpRPCException {
        Team cTeam = null;
        try {
            cTeam = dao.createTeam(team);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("create team failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return cTeam;
    }

    @Override
    public Team modifyTeam(Team team) throws HttpRPCException {
        Team mTeam = null;
        try {
            mTeam = dao.modifyTeam(team);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("modify team failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return mTeam;
    }

    @Override
    public Team deleteTeam(String teamId) throws HttpRPCException {
        try {
            return dao.deleteTeam(teamId);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("delete team failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
    }

    @Override
    public List<TeamMemberWrap> queryMembersInTeam(String teamId) throws HttpRPCException {
        List<Member> list;
        List<TeamMemberWrap> teamMemberWrapLis = new ArrayList<>();
        try {
            if (!teamId.equals(Team.MANAGER_TEAM)) {
                list = dao.queryTeamMembersList(teamId);
            } else {
                list = dao.queryManageTeamMembersList();
            }
            for (Member member : list) {
                for (USMSUser uSMSUser : uSMSuserList) {
                    if (member.getMemberId().equals(uSMSUser.getId())) {
                        TeamMemberWrap teamMemberWrap = new TeamMemberWrap();
                        teamMemberWrap.setMemberId(member.getMemberId());
                        teamMemberWrap.setTeamId(teamId);
                        teamMemberWrap.setAuthority(member.isLeader());
                        teamMemberWrap.setEmployeeNumber(uSMSUser.getEmployeeNumber());
                        teamMemberWrap.setEmployeeName(uSMSUser.getName());
                        teamMemberWrap.setPosition(uSMSUser.getJobPosition());
                        teamMemberWrap.setPhone(uSMSUser.getMobile());
                        teamMemberWrapLis.add(teamMemberWrap);
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query team members failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return teamMemberWrapLis;
    }

    @Override
    public List<Member> createMemberToTeam(List<String> memberIdList, String teamId) throws HttpRPCException {
        List<Member> cMemberLis = null;
        try {
            cMemberLis = dao.createTeamMember(memberIdList, teamId);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("create team members failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return cMemberLis;
    }

    @Override
    public Member deleteMemberFromTeam(String teamId, String memberId) throws HttpRPCException {
        Member dMember = null;
        try {
            dMember = dao.deleteTeamMember(teamId, memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("delete team member failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return dMember;
    }

    @Override
    public TeamMemberWrap modifyMemberAuthority(String teamId, String userId, boolean authority) throws HttpRPCException {
        TeamMemberWrap teamMemberWrap = null;
        Member mMember;
        try {
            teamMemberWrap = new TeamMemberWrap();
            mMember = dao.modifyMemberAuthority(teamId, userId, authority);
            USMSUser uSMSUser = USMSServiceUtils.getUserBasicInfoById(mMember.getMemberId());
            teamMemberWrap.setMemberId(userId);
            if (uSMSUser.getEmployeeNumber() != null) {
                teamMemberWrap.setEmployeeNumber(uSMSUser.getEmployeeNumber());
            }
            if (uSMSUser.getName() != null) {
                teamMemberWrap.setEmployeeName(uSMSUser.getName());
            }
            if (uSMSUser.getJobPosition() != null) {
                teamMemberWrap.setPosition(uSMSUser.getJobPosition());
            }
            teamMemberWrap.setAuthority(authority);
            if (uSMSUser.getMobile() != null) {
                teamMemberWrap.setPhone(uSMSUser.getMobile());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("modify member authority failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return teamMemberWrap;
    }

    @Override
    public List<MemberWrap> queryAllMembers() throws HttpRPCException {
        List<MemberWrap> list = new ArrayList<>();
        //查询member表中的的所有人的memberid存为list
        List<String> memberIdList;
        try {
            memberIdList = dao.queryMemberTableDate();
            for (USMSUser uSMSUser : uSMSuserList) {
                if (!memberIdList.contains(uSMSUser.getId()) && !uSMSUser.getId().equals("sys")) {
                    MemberWrap memberWrap = new MemberWrap();
                    memberWrap.setMemberId(uSMSUser.getId());
                    memberWrap.setMemberName(uSMSUser.getName());
                    list.add(memberWrap);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query all members failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return list;
    }

    @Override
    public List<Team> queryAllTeamsByPM(String memberId) throws HttpRPCException {
        List<Team> list = null;
        try {
            if (memberId != null) {
                list = dao.queryAllTeamsByPMList(memberId);
            } else {
                list = dao.queryAllTeamByCeoOrHr();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query team failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return list;
    }

}
