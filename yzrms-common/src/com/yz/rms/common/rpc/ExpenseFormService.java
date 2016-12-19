/*
 * ExpenseFormInterface.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 11:30:29
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCSessionTokenRequired;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;
import com.nazca.sql.PageResult;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.wrap.ExpenseFormStateCountWrap;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报销单接口类
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING,
identifier = "com.yz.rms.server.rpcimpl.ExpenseFormServiceImpl")
public interface ExpenseFormService {

    /**
     * 根据角色获取可查看的报销单状态列表，并统计各状态的报销单数量
     *
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<ExpenseFormStateCountWrap> queryExpenseFromStateCountByRole() throws HttpRPCException;

    /**
     * 根据状态、项目、团队、报销时间、搜索关键字分页查询报销单
     *
     * @param state
     * @param projectId
     * @param teamId
     * @param startTime
     * @param endTime
     * @param keywords
     * @param curPage
     * @param pageSize
     * @return
     * @throws HttpRPCException
     * 
     */
    @HttpRPCSessionTokenRequired
    PageResult<ExpenseFormWrap> queryExpenseFormByState(ExpenseFormStateEnums state, String projectId, String teamId, Date startTime, Date endTime, String keywords, int curPage, int pageSize) throws HttpRPCException;

    /**
     * 审核报销单
     *
     * @param expenseId
     * @param memo
     * @param isPass
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    void auditExpenseForm(String expenseId, String memo, boolean isPass) throws HttpRPCException;

    /**
     * 获取所有待发放统计信息
     *
     * @return 返回以年为键 WaitPayStatWrap集合为值的map
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Map<Integer, List<WaitPayStatWrap>> queryWaitPayStat() throws HttpRPCException;

    /**
     * 标记发放
     *
     * @param expenseFormIdList
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    void markPaid(List<String> expenseFormIdList) throws HttpRPCException;

    /**
     * 根据状态、项目、报销时间分页查询登录用户的报销单
     *
     * @param state
     * @param projectId
     * @param startTime
     * @param endTime
     * @param curPage
     * @param pageSize
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    PageResult<ExpenseFormWrap> queryMyExpenseForm(ExpenseFormStateEnums state, String projectId, Date startTime, Date endTime, int curPage, int pageSize) throws HttpRPCException;

    /**
     * 添加报销单
     *
     * @param expenseForm 参数为报销单对象
     * @return 返回报销单对象
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    ExpenseForm createExpenseForm(ExpenseForm expenseForm) throws HttpRPCException;

    /**
     * 修改报销单
     *
     * @param expenseForm 参数为报销单对象
     * @return 返回报销单对象
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    ExpenseForm modifyExpenseForm(ExpenseForm expenseForm) throws HttpRPCException;

    /**
     * 删除报销单
     *
     * @param expenseId 参数为报销单id
     * @return 返回报销单对象
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    ExpenseForm deleteExpenseForm(String expenseId) throws HttpRPCException;

    /**
     * 提交报销单
     *
     * @param expenseId 参数为报销单id
     * @return 返回报销单对象
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    ExpenseForm submitExpenseForm(String expenseId) throws HttpRPCException;

}
