/*
 * QueryExpenseStateCountAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 15:34:56
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.util.TimeFairy;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.wrap.ExpenseFormStateCountWrap;
import com.yz.rms.common.rpc.ExpenseFormService;
import java.util.List;

/**
 * 查询报销单状态列表的Agent
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class QueryExpenseStateCountAgent extends AbstractAgent<List<ExpenseFormStateCountWrap>>{
    List<ExpenseFormStateCountWrap> expenseFormStateCount = null;
    @Override
    protected List<ExpenseFormStateCountWrap> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        if(FakeDataFactory.isFake()){
            expenseFormStateCount = FakeDataFactory.queryExpenseFromStateCountByRole();
        }else{
            ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
                    getSysServerRPCURL());
            expenseFormStateCount = service.queryExpenseFromStateCountByRole();
        }
        tf.sleepIfNecessary();
        return expenseFormStateCount;
    }
    
}
