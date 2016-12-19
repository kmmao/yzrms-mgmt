/*
 * DeletTeamMemberAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 13:47:42
 */
package com.yz.rms.client.agent.team;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.rpc.TeamService;

/**
 * 移除团队成员Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class DeletTeamMemberAgent extends AbstractAgent<Member> {

    Member member = null;
    private String teamId;
    private String memberId;

    public void setParameter(String teamId, String memberId) {
        this.teamId = teamId;
        this.memberId = memberId;
    }

    @Override
    protected Member doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            member = FakeDataFactory.deleteMemberFromTeam(teamId, memberId);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            member = service.deleteMemberFromTeam(teamId, memberId);
        }
        tf.sleepIfNecessary();
        return member;
    }

}
