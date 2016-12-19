/*
 * SubmitExpenseFormAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 10:17:47
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.rpc.ExpenseFormService;

/**
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class SubmitExpenseFormAgent extends AbstractAgent<ExpenseForm> {


    private String expenseId;

    public void setPrama(String expenseId) {
	this.expenseId = expenseId;
    }
    @Override

    protected ExpenseForm doExecute() throws HttpRPCException {
	ExpenseForm expenseForm = null;
	TimeFairy tf = new TimeFairy();
	if (FakeDataFactory.isFake()) {
	    expenseForm = FakeDataFactory.submitExpenseForm(expenseId);
	} else {
	    ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
		    getSysServerRPCURL());
	    expenseForm = service.submitExpenseForm(expenseId);
	}
	tf.sleepIfNecessary();
	return expenseForm;
    }

}
