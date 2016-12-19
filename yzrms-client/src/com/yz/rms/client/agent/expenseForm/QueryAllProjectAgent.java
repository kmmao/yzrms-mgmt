/*
 * QueryAllProjectAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-25 14:02:08
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.rpc.ProjectService;
import java.util.List;

/**
 *
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class QueryAllProjectAgent extends AbstractAgent<List<Project>>
{
    List<Project> allProjectResult = null;
    @Override
    protected List<Project> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
            allProjectResult = FakeDataFactory.queryAllProjects();
        } else {
            ProjectService service = HttpRPC.getService(ProjectService.class, ClientContext.
                    getSysServerRPCURL());
            allProjectResult= service.queryAllProject(ProjectEnums.underway);
        }
        tf .sleepIfNecessary();
        return allProjectResult;
    }
    
}
