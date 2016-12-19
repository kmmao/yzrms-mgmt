/*
 * AuditExpenseFromAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 12:12:31
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.rpc.ExpenseFormService;

/**
 * 审核报销单Agent
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class AuditExpenseFromAgent extends AbstractAgent<Boolean>{
    
    private String expenseId;
    private String memo;
    private boolean isPass;

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }
    
    @Override
    protected Boolean doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if(FakeDataFactory.isFake()){
            FakeDataFactory.auditExpenseForm(expenseId, memo, isPass);
        }else{
            ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
                    getSysServerRPCURL());
            service.auditExpenseForm(expenseId, memo, isPass);
        }
        tf.sleepIfNecessary();
        return true;
    }
}
