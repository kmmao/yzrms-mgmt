/*
 * ModifyTeamAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 09:32:28
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
 * 修改团队Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class ModifyTeamAgent extends AbstractAgent<Team> {

    private Team team;

    public void setParameter(Team team) {
        this.team = team;
    }

    @Override
    protected Team doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            team = FakeDataFactory.modifyTeam(team);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            team = service.modifyTeam(team);
        }
        tf.sleepIfNecessary();
        return team;
    }
}
