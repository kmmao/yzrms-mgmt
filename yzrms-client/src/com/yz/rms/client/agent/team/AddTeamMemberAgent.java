/*
 * AddTeamMemberAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 12:17:40
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
import java.util.List;

/**
 * 添加团队成员Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class AddTeamMemberAgent extends AbstractAgent<List<Member>> {

    private List<Member> memberLis;
    private List<String> memberIdLis;
    private String teamId;

    public void setParameter(List<String> memberIdLis, String teamId) {
        this.memberIdLis = memberIdLis;
        this.teamId = teamId;
    }

    @Override
    protected List<Member> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            memberLis = FakeDataFactory.createMemberToTeam(memberIdLis, teamId);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            memberLis = service.createMemberToTeam(memberIdLis, teamId);
        }
        tf.sleepIfNecessary();
        return memberLis;
    }
}
