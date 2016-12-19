/*
 * ModifyExpenseFormAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 14:38:40
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
 * 修改报销单的Agent
 *
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class ModifyExpenseFormAgent extends AbstractAgent<ExpenseForm> {
    private ExpenseForm expenseForm;

    public void setParameter(ExpenseForm expenseForm) {
        this.expenseForm = expenseForm;
    }

    @Override
    protected ExpenseForm doExecute() throws HttpRPCException {
        if (FakeDataFactory.isFake()) {
            return FakeDataFactory.modifyExpenseForm(expenseForm);
        } else {
            ExpenseFormService service = HttpRPC.getService(
                    ExpenseFormService.class, ClientContext.
                    getSysServerRPCURL());
            return service.modifyExpenseForm(expenseForm);
        }
    }
}
