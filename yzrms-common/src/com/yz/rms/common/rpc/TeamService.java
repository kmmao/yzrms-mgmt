/*
 * TeamService.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 15:10:23
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCSessionTokenRequired;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.MemberWrap;
import com.yz.rms.common.model.wrap.TeamMemberWrap;
import java.util.List;

/**
 * 团队管理的接口类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING, identifier = "com.yz.rms.server.rpcimpl.TeamServiceImpl")
public interface TeamService {

    /**
     * 获取团队信息
     *
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<Team> queryAllTeams() throws HttpRPCException;

    /**
     * 添加团队信息
     *
     * @param team
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Team createTeam(Team team) throws HttpRPCException;

    /**
     * 修改团队信息
     *
     * @param team
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Team modifyTeam(Team team) throws HttpRPCException;

    /**
     * 删除团队信息
     *
     * @param teamId
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Team deleteTeam(String teamId) throws HttpRPCException;

    /**
     * 获取团队成员
     *
     * @param teamId
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<TeamMemberWrap> queryMembersInTeam(String teamId) throws HttpRPCException;

    /**
     * 添加团队成员
     *
     * @param memberIdList
     * @param teamId
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<Member> createMemberToTeam(List<String> memberIdList, String teamId) throws HttpRPCException;

    /**
     * 删除团队成员
     *
     * @param memberId
     * @param teamId
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Member deleteMemberFromTeam(String teamId, String memberId) throws HttpRPCException;

    /**
     * 将团队的某个人设为负责人或组员
     *
     * @param teamId
     * @param userId
     * @param authority
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    TeamMemberWrap modifyMemberAuthority(String teamId, String userId, boolean authority) throws HttpRPCException;

    /**
     * 查询所有成员
     *
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<MemberWrap> queryAllMembers() throws HttpRPCException;

    /**
     * 查询项目经理所管理的团队
     *
     * @param memberId
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<Team> queryAllTeamsByPM(String memberId) throws HttpRPCException;

}
