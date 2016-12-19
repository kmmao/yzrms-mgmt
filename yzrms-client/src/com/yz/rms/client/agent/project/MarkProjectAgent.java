/*
 * MarkProjectAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 09:43:07
 */
package com.yz.rms.client.agent.project;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.rpc.ProjectService;
import java.text.ParseException;

/**
 * 标记项目
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class MarkProjectAgent extends AbstractAgent<Project> {

    private String projectId;
    private ProjectEnums state;

    public void setPrama(String projectId, ProjectEnums state) {
	this.projectId = projectId;
	this.state = state;
    }

    @Override
    protected Project doExecute() throws HttpRPCException {
	if (FakeDataFactory.isFake()) {
	    try {
		return FakeDataFactory.changeProjectState(projectId, state);
	    } catch (ParseException ex) {
		ex.printStackTrace();
		throw new HttpRPCException("未知错误", 0);
	    }
	} else {
	    ProjectService service = HttpRPC.getService(ProjectService.class, ClientContext.
		    getSysServerRPCURL());
	    return service.changeProjectState(projectId, state);
	}
    }
}
