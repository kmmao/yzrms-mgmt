/*
 * QueryWaitPayStatAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-12 14:44:05
 */
package com.yz.rms.client.agent.expenseform;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.yz.rms.client.ClientContext;
import com.yz.rms.client.agent.AbstractAgent;
import com.yz.rms.client.util.FakeDataFactory;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import com.yz.rms.common.rpc.ExpenseFormService;
import java.util.List;
import java.util.Map;

/**
 *查询所有的未发放报销的Agent
 * @author Hu Qin<huqin@yzhtech.com>
 */
public class QueryWaitPayStatAgent extends AbstractAgent<Map<Integer, List<WaitPayStatWrap>>>{
    private Map<Integer, List<WaitPayStatWrap>> waitPayStats;
    
    @Override
    protected Map<Integer, List<WaitPayStatWrap>> doExecute() throws HttpRPCException {
        if (FakeDataFactory.isFake()) {
           return FakeDataFactory.queryWaitPayStat();
        }else{
             ExpenseFormService service =  HttpRPC.getService(ExpenseFormService.class, ClientContext.
                    getSysServerRPCURL());
         waitPayStats = service.queryWaitPayStat();
        }
         
        return waitPayStats;
    }
    
}
