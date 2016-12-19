/*
 * QueryProjectAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 15:33:21
 */
package com.yz.rms.client.agent.project;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.sql.PageResult;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.rpc.ProjectService;

/**
 * 查询项目
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class QueryProjectAgent extends AbstractAgent<PageResult<Project>> {

    private ProjectEnums state;
    private String keywords;
    private int curPage;
    private int pageSize;

    public void setParam(ProjectEnums state, String keywords, int curPage, int pageSize) {
	this.state = state;
	this.keywords = keywords;
	this.curPage = curPage;
	this.pageSize = pageSize;
    }

    @Override
    protected PageResult<Project> doExecute() throws HttpRPCException {
	PageResult<Project> pageResult = null;
	if (FakeDataFactory.isFake()) {
	    pageResult = FakeDataFactory.queryProject(state, keywords, curPage, pageSize);
	} else {
	    ProjectService service = HttpRPC.getService(ProjectService.class, ClientContext.
		    getSysServerRPCURL());
	    pageResult = service.queryProject(state, keywords, curPage, pageSize);
	}
	//new TimeFairy().sleepIfNecessary();
	return pageResult;
    }

}
