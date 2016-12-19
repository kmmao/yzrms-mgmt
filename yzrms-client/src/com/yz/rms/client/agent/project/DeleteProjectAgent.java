/*
 * DeleteProjectAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 18:09:45
 */
package com.yz.rms.client.agent.project;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.rpc.ProjectService;
import java.text.ParseException;

/**
 * 删除项目
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class DeleteProjectAgent extends AbstractAgent<String> {

    private String projectId;

    public void setParam(String projectId) {
	this.projectId = projectId;
    }

    @Override
    protected String doExecute() throws HttpRPCException {

	if (FakeDataFactory.isFake()) {
	    try {
		return FakeDataFactory.deleteProject(projectId);
	    } catch (ParseException ex) {
		ex.printStackTrace();
		throw new HttpRPCException("未知错误", 0);
	    }
	} else {
	    ProjectService service = HttpRPC.getService(ProjectService.class, ClientContext.
		    getSysServerRPCURL());
	    service.deleteProject(projectId);
            return projectId;
	}
    }
}
