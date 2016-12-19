/*
 * QueryExpenseFormAgent.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-18 12:22:47
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
 * 查询报销单列表Agent
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class QueryExpenseFormAgent extends AbstractAgent<PageResult<ExpenseFormWrap>> {

    private ExpenseFormStateEnums state;
    private String projectId;
    private String teamId;
    private Date startTime;
    private Date endTime;
    private String keywords;
    private int curPage;
    private int pageSize;

    public void setState(ExpenseFormStateEnums state) {
        this.state = state;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    protected PageResult<ExpenseFormWrap> doExecute() throws HttpRPCException {
        TimeFairy tf = new TimeFairy();
        PageResult<ExpenseFormWrap> expenseFormResult = null;
        if (FakeDataFactory.isFake()) {
            expenseFormResult = FakeDataFactory.queryExpenseFormByState(state, projectId, teamId, startTime, endTime, keywords, curPage, pageSize);
        } else {
            ExpenseFormService service = HttpRPC.getService(ExpenseFormService.class, ClientContext.
                    getSysServerRPCURL());
            expenseFormResult = service.queryExpenseFormByState(state, projectId, teamId, startTime, endTime, keywords, curPage, pageSize);
        }
        tf.sleepIfNecessary();
        return expenseFormResult;
    }

}
