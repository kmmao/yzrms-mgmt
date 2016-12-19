/*
 * markPaidAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 14:31:19
 */
package com.yz.rms.client.agent.expenseform;
import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.rpc.ExpenseFormService;
import java.util.List;

/**
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class markPaidAgent extends AbstractAgent<Boolean>{
    private List<String> expenseFormIdList;

    public void setExpenseFormIdList(List<String> expenseFormIdList) {
        this.expenseFormIdList = expenseFormIdList;
    }
    

    @Override
    protected Boolean doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if (FakeDataFactory.isFake()) {
	    FakeDataFactory.markPaid(expenseFormIdList);
	} else {
            ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
                getSysServerRPCURL());
            service.markPaid(expenseFormIdList);            
        }
        tf.sleepIfNecessary();
        return true;
    }
    
    
}
