/*
 * QueryMyExpenseFormAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 09:56:22
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.sql.PageResult;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.rpc.ExpenseFormService;
import java.util.Date;

/**
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class QueryMyExpenseFormAgent extends AbstractAgent<PageResult<ExpenseFormWrap>> {

    PageResult<ExpenseFormWrap> pageResult = null;

    private ExpenseFormStateEnums state;
    private String projectId;
    private Date startTime;
    private Date endTime;
    private int curPage;
    private int pageSize;

    public void setParam(ExpenseFormStateEnums state, String projectId, Date startTime, Date endTime, int curPage, int pageSize) {
	this.state = state;
	this.projectId = projectId;
	this.startTime = startTime;
	this.endTime = endTime;
	this.curPage = curPage;
	this.pageSize = pageSize;
    }

    @Override
    protected PageResult<ExpenseFormWrap> doExecute() throws HttpRPCException {
	if (FakeDataFactory.isFake()) {
	    pageResult = FakeDataFactory.queryMyExpenseForm(state, projectId, startTime, endTime, curPage, pageSize);
	} else {
	    ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
		    getSysServerRPCURL());
	    pageResult = service.queryMyExpenseForm(state, projectId, startTime, endTime, curPage, pageSize);
	}
	new TimeFairy().sleepIfNecessary();
	return pageResult;
    }

}
