/*
 * QueyTeamAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 15:26:23
 */
package com.yz.rms.client.agent.stat;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.rpc.TeamService;
import java.util.List;

/**
 * 查询团队列表的agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class QueyTeamAgent extends AbstractAgent<List<Team>> {

    List<Team> allTeamResult = null;

    @Override
    protected List<Team> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            allTeamResult = FakeDataFactory.queryAllTeams();
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            allTeamResult = service.queryAllTeams();
        }
        tf.sleepIfNecessary();
        return allTeamResult;
    }
}
