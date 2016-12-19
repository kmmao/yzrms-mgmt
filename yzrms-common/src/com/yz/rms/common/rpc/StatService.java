/*
 * ExpenseService.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 16:10:11
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCSessionTokenRequired;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;
import com.yz.rms.common.model.wrap.StatItemWrap;
import java.util.List;

/**
 * 报销统计接口类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING,
        identifier = "com.yz.rms.server.rpcimpl.StatServiceImpl")
public interface StatService {

    /**
     * 按团队报销统计
     *
     * @param teamId
     * @param year
     * @param month
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<StatItemWrap> queryExpenseForTeamStat(String teamId, int year, Integer month) throws HttpRPCException;

    /**
     * 按项目报销统计
     *
     * @param projectId
     * @param year
     * @param month
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<StatItemWrap> queryExpenseForProjectStat(String projectId, int year, Integer month) throws HttpRPCException;
}
