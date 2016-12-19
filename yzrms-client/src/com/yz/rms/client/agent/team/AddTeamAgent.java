/*
 * AddTeamAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-11 15:53:39
 */
package com.yz.rms.client.agent.team;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.rpc.TeamService;

/**
 * 添加团队Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class AddTeamAgent extends AbstractAgent<Team> {

    private Team team;

    public void setParameter(Team team) {
        this.team = team;
    }

    @Override
    protected Team doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            team = FakeDataFactory.createTeam(team);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            team = service.createTeam(team);
        }
        tf.sleepIfNecessary();
        return team;
    }

}
