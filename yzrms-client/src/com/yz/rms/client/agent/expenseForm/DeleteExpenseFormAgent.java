/*
 * DeleteExpenseFormAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 10:08:24
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.rpc.ExpenseFormService;

/**
 * 删除我的报销单
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class DeleteExpenseFormAgent extends AbstractAgent<ExpenseForm> {

    private String expenseId;

    public void setPrama(String expenseId) {
	this.expenseId = expenseId;
    }

    @Override
    protected ExpenseForm doExecute() throws HttpRPCException {
	if (FakeDataFactory.isFake()) {
	    return FakeDataFactory.deleteExpenseForm(expenseId);
	} else {
	    ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
		    getSysServerRPCURL());
	    return service.deleteExpenseForm(expenseId);
	}
    }

}
