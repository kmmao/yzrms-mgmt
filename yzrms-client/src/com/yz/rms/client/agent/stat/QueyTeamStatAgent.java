/*
 * QueyTeamStatAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 13:37:44
 */
package com.yz.rms.client.agent.stat;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.wrap.StatItemWrap;
import com.yz.rms.common.rpc.StatService;
import java.util.List;

/**
 * 查询报销统计Agent
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class QueyTeamStatAgent extends AbstractAgent<List<StatItemWrap>> {

    List<StatItemWrap> allTeamResult = null;
    private String teamId;
    private int year;
    private Integer month;

    public void setParameter(String teamId, int year, Integer month) {
        this.teamId = teamId;
        this.year = year;
        this.month = month;
    }

    @Override
    protected List<StatItemWrap> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            allTeamResult = FakeDataFactory.queryExpenseForTeamStat(teamId, year, month);
        } else {
            StatService service = HttpRPC.getService(StatService.class, ClientContext.
                    getSysServerRPCURL());
            allTeamResult = service.queryExpenseForTeamStat(teamId, year, month);
        }
        tf.sleepIfNecessary();
        return allTeamResult;
    }
}
