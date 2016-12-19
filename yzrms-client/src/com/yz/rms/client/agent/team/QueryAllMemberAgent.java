/*
 * QueryAllMemberAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 18:10:25
 */
package com.yz.rms.client.agent.team;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.wrap.MemberWrap;
import com.yz.rms.common.rpc.TeamService;
import java.util.List;

/**
 * 查询所有成员
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class QueryAllMemberAgent extends AbstractAgent<List<MemberWrap>> {

    List<MemberWrap> allMembers = null;

    @Override
    protected List<MemberWrap> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            allMembers = FakeDataFactory.queryAllMembers();
        } else {
            TeamService service = HttpRPC.getService(TeamService.class, ClientContext.
                    getSysServerRPCURL());
            allMembers = service.queryAllMembers();
        }
        tf.sleepIfNecessary();
        return allMembers;
    }
}
