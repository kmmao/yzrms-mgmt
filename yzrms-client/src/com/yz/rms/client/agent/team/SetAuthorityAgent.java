/*
 * SetAuthorityAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 14:09:23
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

/**
 * 为团队设置负责人Agent
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class SetAuthorityAgent extends AbstractAgent<TeamMemberWrap>{
    TeamMemberWrap member =null;
    private String teamId;
    private String memberId;
    private boolean authority;
    public void setParameter(String teamId,String memberId,boolean authority){
        this.teamId = teamId;
        this.memberId = memberId;
        this.authority = authority;
    }
    @Override
    protected TeamMemberWrap doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            member = FakeDataFactory.modifyMemberAuthority(teamId,memberId,authority);
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            member= service.modifyMemberAuthority(teamId,memberId,authority);
        }
        tf .sleepIfNecessary();
        return member;
    }
}
