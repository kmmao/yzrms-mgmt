/*
 * QueryTeamMemberAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-10 16:56:55
 */
package com.yz.rms.client.agent.team;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.wrap.TeamMemberWrap;
import com.yz.rms.common.rpc.TeamService;
import java.util.List;

/**
 * 查询团队成员的Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class QueryTeamMemberAgent extends AbstractAgent<List<TeamMemberWrap>> {

    List<TeamMemberWrap> allTeamResult = null;
    private String teamId;

    public void setParameter(String teamId) {
        this.teamId = teamId;
    }

    @Override
    protected List<TeamMemberWrap> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            allTeamResult = FakeDataFactory.queryMembersInTeam(teamId);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            allTeamResult = service.queryMembersInTeam(teamId);
        }
        tf.sleepIfNecessary();
        return allTeamResult;
    }
}
