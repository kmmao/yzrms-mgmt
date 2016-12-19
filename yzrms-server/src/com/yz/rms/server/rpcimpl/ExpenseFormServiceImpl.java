/*
 * ExpenseFormServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 16:42:59
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCInjection;
import com.nazca.sql.PageResult;
import com.nazca.usm.common.SessionConst;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.consts.ProjectConst;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.enums.ExpenseRoleEnum;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.ExpenseFormStateCountWrap;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import com.yz.rms.common.rpc.ExpenseFormService;
import com.yz.rms.common.util.ErrorCodeFormater;
import com.yz.rms.server.dao.ExpenseFormDAO;
import com.yz.rms.server.util.USMSServiceUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 报销单管理实现类
 *
 * @author 张琪 <zhangqi@yzhtech.com>
 */
public class ExpenseFormServiceImpl implements ExpenseFormService {

    private static final Log log = LogFactory.getLog(ExpenseFormService.class);
    @HttpRPCInjection
    private HttpSession session;
    private static final Double expenseTotalLimit = 2000.0;

    @Override
    public List<ExpenseFormStateCountWrap> queryExpenseFromStateCountByRole() throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        List<ExpenseFormStateCountWrap> list = new ArrayList<>();
        List<ExpenseFormStateEnums> li = new ArrayList<>();
        if (session.getAttribute(SessionConst.KEY_USER_NAME) != null) {
            try {
                String loginName = (String) session.getAttribute(SessionConst.KEY_USER_LOGINNAME);//获取登录名
                USMSUser user = USMSServiceUtils.getUserInfoByLoginName(loginName);//获取登录人的信息 
                String teamId = dao.queryTeamId(user.getId());//根据登录人的id查询所在团队的teamId
                int totalCount = 0;
                //总经理
                if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name())) {
                    li = ExpenseFormStateEnums.getStateEnums4CEO();//获取总经理权限下的状态列表
                    for (ExpenseFormStateEnums expenseFormStateEnums : li) {
                        if (expenseFormStateEnums == ExpenseFormStateEnums.waitAuditForPm) {
                            totalCount = dao.queryWaitAuditExpenseFormCountForCeo(ExpenseFormStateEnums.waitAuditForPm, ExpenseFormStateEnums.waitAuditForCeo, null, null, null, null);
                        } else {
                            totalCount = dao.queryExpenseFormCountForCeo(expenseFormStateEnums, null, null, null, null);
                        }
                        ExpenseFormStateCountWrap wrap = new ExpenseFormStateCountWrap();
                        wrap.setState(expenseFormStateEnums);
                        wrap.setCount(totalCount);
                        list.add(wrap);
                    }
                } //行政
                else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {
                    if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.PM.name())) {
                        li = ExpenseFormStateEnums.getStateEnums5PM();//行政主管获取的状态列表
                    } else {
                        li = ExpenseFormStateEnums.getStateEnums3HR();//行政普通员工获取的状态列表
                    }
                    for (ExpenseFormStateEnums expenseFormStateEnums : li) {
                        if (expenseFormStateEnums == ExpenseFormStateEnums.waitAuditForPm || expenseFormStateEnums == ExpenseFormStateEnums.waitAuditForCeo) {
                            totalCount = dao.queryExpenseFormByStateCount(expenseFormStateEnums, teamId, null, null, null, null);
                        } else {
                            totalCount = dao.queryWaitPayAndPaidExpenseFormCount(expenseFormStateEnums, null, null, null, null);
                        }
                        ExpenseFormStateCountWrap wrap = new ExpenseFormStateCountWrap();
                        wrap.setState(expenseFormStateEnums);
                        wrap.setCount(totalCount);
                        list.add(wrap);
                    }
                }//主管
                else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.PM.name())) {
                    li = ExpenseFormStateEnums.getStateEnums5PM();//获取主管权限下的状态列表
                    for (ExpenseFormStateEnums expenseFormStateEnums : li) {
                        totalCount = dao.queryExpenseFormByStateCount(expenseFormStateEnums, teamId, null, null, null, null);
                        ExpenseFormStateCountWrap wrap = new ExpenseFormStateCountWrap();
                        wrap.setState(expenseFormStateEnums);
                        wrap.setCount(totalCount);
                        list.add(wrap);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public PageResult<ExpenseFormWrap> queryExpenseFormByState(ExpenseFormStateEnums state,
            String projectId, String teamId, Date startTime, Date endTime,
            String keywords, int curPage, int pageSize)
            throws HttpRPCException {
        PageResult<ExpenseFormWrap> result = null;
        List<ExpenseFormWrap> list = new ArrayList<>();
        ExpenseFormDAO dao = new ExpenseFormDAO();
        int totalCount;
        int start = 0;
        if (session.getAttribute(SessionConst.KEY_USER_NAME) != null) {
            try {
                String loginName = (String) session.getAttribute(SessionConst.KEY_USER_LOGINNAME);
                USMSUser user = USMSServiceUtils.getUserInfoByLoginName(loginName);
                String teamId2 = dao.queryTeamId(user.getId());
                //总经理
                if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name())) {
                    if (state == ExpenseFormStateEnums.waitAuditForPm) {
                        if (teamId != null) {   //当团队筛选框不为全部的时候 筛选报销单 
                            if (!teamId.equals(Team.MANAGER_TEAM)) {    //如果不是管理团队，查待总经理审核报销单
                                totalCount = dao.queryTeamExpenseFormCountForCeo(state, ExpenseFormStateEnums.waitAuditForCeo, teamId, projectId, keywords, startTime, endTime);
                                curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
                                start = PageResult.getFromIndex(curPage, pageSize);
                                list = dao.queryTeamExpenseFormForCeo(state, ExpenseFormStateEnums.waitAuditForCeo, teamId, projectId, keywords, startTime, endTime, start, pageSize);
                                result = new PageResult<>(totalCount, curPage, pageSize, list);
                            } else {
                                result = queryExpenseFormForManagerTeam(dao, state, projectId, keywords, startTime, endTime, curPage, pageSize);
                            }
                        } else {
                            totalCount = dao.queryWaitAuditExpenseFormCountForCeo(state, ExpenseFormStateEnums.waitAuditForCeo, projectId, keywords, startTime, endTime);
                            curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
                            start = PageResult.getFromIndex(curPage, pageSize);
                            list = dao.queryWaitAuditExpenseFormForCeo(state, ExpenseFormStateEnums.waitAuditForCeo, projectId, keywords, startTime, endTime, start, pageSize);
                            result = new PageResult<>(totalCount, curPage, pageSize, list);
                        }
                    } else {
                        result = queryExpenseFormForManagerTeam(dao, state, projectId, keywords, startTime, endTime, curPage, pageSize);
                    }
                } //行政
                else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {
                    if (state == ExpenseFormStateEnums.waitAuditForPm || state == ExpenseFormStateEnums.waitAuditForCeo) { 
                        result = queryExpenseFormListByState(dao, state, teamId2, projectId, keywords, startTime, endTime, curPage, pageSize);
                    } else if (teamId != null) {
                        //主管团队
                        if (!teamId.equals(Team.MANAGER_TEAM)) {
                            totalCount = dao.queryTeamExpenseFormCount(state, teamId, projectId, keywords, startTime, endTime);
                            curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
                            start = PageResult.getFromIndex(curPage, pageSize);
                            list = dao.queryTeamExpenseForm(state, teamId, projectId, keywords, startTime, endTime, start, pageSize);
                            result = new PageResult<>(totalCount, curPage, pageSize, list);
                        } else {
                            //管理团队
                            result = queryExpenseFormForManagerTeam(dao, state, projectId, keywords, startTime, endTime, curPage, pageSize);
                        }
                    } else {
                        totalCount = dao.queryWaitPayAndPaidExpenseFormCount(state, projectId, keywords, startTime, endTime); //待行政审核、待发放和已发放查询的是公司所有的人的报销单
                        curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
                        start = PageResult.getFromIndex(curPage, pageSize);
                        list = dao.queryWaitPayAndPaidExpenseForm(state, projectId, keywords, startTime, endTime, start, pageSize);
                        result = new PageResult<>(totalCount, curPage, pageSize, list);
                    }
                } //主管
                else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.PM.name())) {
                    result = queryExpenseFormListByState(dao, state, teamId2, projectId, keywords, startTime, endTime, curPage, pageSize);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("qurey expenseform failed!", ex);
                throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
            }
        }
        return result;
    }

    @Override
    public void auditExpenseForm(String expenseId, String memo, boolean isPass) throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            String loginName = (String) session.getAttribute(SessionConst.KEY_USER_LOGINNAME);
            USMSUser user = USMSServiceUtils.getUserInfoByLoginName(loginName);
            String modifier = user.getId();
            ExpenseFormStateEnums state = null;
            //根据报销单的ID查询该报销单的信息
            ExpenseForm expenseForm = dao.queryExpenseForm(expenseId);
            Double expenseTotal = expenseForm.getExpenseTotal();
            ExpenseFormStateEnums stateForAudit = expenseForm.getState();
            if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.CEO.name())) {    //总经理
                state = ExpenseFormStateEnums.waitAuditForHr;
            } else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.HR.name())) {  //行政
                //行政-待审核状态下的审核
                if (stateForAudit == ExpenseFormStateEnums.waitAuditForPm) {
                    if (expenseTotal >= expenseTotalLimit) {
                        //超额报表单
                        state = ExpenseFormStateEnums.waitAuditForCeo;
                    } else {
                        //正常报销单
                        state = ExpenseFormStateEnums.waitAuditForHr;
                    }
                } else {
                    //行政-待行政审核状态下的审核
                    state = ExpenseFormStateEnums.waitPay;
                }
            } else if (user.hasRole(ProjectConst.USMS_MODULE_ID, ExpenseRoleEnum.PM.name())) {  //主管
                if (expenseTotal >= expenseTotalLimit) {
                    //超额报表单
                    state = ExpenseFormStateEnums.waitAuditForCeo;
                } else {
                    //正常报销单
                    state = ExpenseFormStateEnums.waitAuditForHr;
                }
            }
            dao.auditExpenseForm(state, expenseId, memo, isPass, modifier);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("qurey expenseform failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
    }

    @Override
    public Map<Integer, List<WaitPayStatWrap>> queryWaitPayStat() throws HttpRPCException {
        Map<Integer, List<WaitPayStatWrap>> result = null;
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            result = dao.queryWaitPayStat();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query expenseForm failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }

        return result;
    }
    
    /**
     *  标记发放
     * @param expenseFormIdList
     * @throws HttpRPCException 
     */
    @Override
    public void markPaid(List<String> expenseFormIdList) throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            String loginName = (String) session.getAttribute(SessionConst.KEY_USER_LOGINNAME);
            USMSUser user = USMSServiceUtils.getUserInfoByLoginName(loginName);
            String modifier = user.getId();
            dao.markPaid(expenseFormIdList, modifier);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("qurey expenseform failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
    }
//sg查询我的报销单

    @Override
    public PageResult<ExpenseFormWrap> queryMyExpenseForm(ExpenseFormStateEnums state, String projectId, Date startTime, Date endTime, int curPage, int pageSize) throws HttpRPCException {
        PageResult<ExpenseFormWrap> result = null;
        List<ExpenseFormWrap> list = null;
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            String expensePersonId = (String) session.getAttribute(SessionConst.KEY_USER_ID);
//	    String expensePersonId = ClientContext.getUserId();
            int totalCount = dao.queryMyExpenseTotalCount(state, projectId, startTime, endTime, expensePersonId);
            curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
            int start = PageResult.getFromIndex(curPage, pageSize);
            list = dao.queryMyExpenseForm(state, projectId, startTime, endTime, start, pageSize, expensePersonId);
            result = new PageResult<>(totalCount, curPage, pageSize, list);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query expenseForm failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return result;
    }

    @Override
    public ExpenseForm createExpenseForm(ExpenseForm expenseForm) throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            return dao.createExpenseForm(expenseForm);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("create expenseform failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
    }

    @Override
    public ExpenseForm modifyExpenseForm(ExpenseForm expenseForm) throws HttpRPCException {
        ExpenseForm result = null;
        ExpenseFormDAO dao = new ExpenseFormDAO();
        try {
            result = dao.modifyExpenseForm(expenseForm);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("modify expenseform failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return result;
    }
//sg删除报销单

    @Override
    public ExpenseForm deleteExpenseForm(String expenseId) throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        ExpenseForm expenseForm = null;
        try {
//            String name = (String) session.getAttribute(SessionConst.KEY_USER_NAME);
	    String modifier=(String)session.getAttribute(SessionConst.KEY_USER_ID);
            expenseForm = dao.deleteExpenseForm(expenseId, modifier);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("delete expense failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return expenseForm;
    }
//sg提交报销单

    @Override
    public ExpenseForm submitExpenseForm(String expenseId) throws HttpRPCException {
        ExpenseFormDAO dao = new ExpenseFormDAO();
        ExpenseForm expenseForm = null;
        try {
	    String modifier=(String)session.getAttribute(SessionConst.KEY_USER_ID);
            expenseForm = dao.submitExpenseForm(expenseId,modifier);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("submit expense failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return expenseForm;
    }

    /**
     * 根据传入状态查询报销单列表
     *
     * @param dao
     * @param teamId
     * @param state
     * @param projectId
     * @param startTime
     * @param endTime
     * @param keywords
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    private PageResult<ExpenseFormWrap> queryExpenseFormListByState(
            ExpenseFormDAO dao, ExpenseFormStateEnums state, String teamId,
            String projectId, String keywords, Date startTime, Date endTime,
            int curPage, int pageSize) throws Exception {
        int totalCount;
        int start;
        List<ExpenseFormWrap> list;
        PageResult<ExpenseFormWrap> result;
        totalCount = dao.queryExpenseFormByStateCount(state, teamId, projectId, keywords, startTime, endTime);
        curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
        start = PageResult.getFromIndex(curPage, pageSize);
        list = dao.queryExpenseFormByState(state, teamId, projectId, keywords, startTime, endTime, start, pageSize);
        result = new PageResult<>(totalCount, curPage, pageSize, list);
        return result;
    }

    /**
     * 查询管理团队的报销单
     *
     * @param dao
     * @param state
     * @param projectId
     * @param keywords
     * @param startTime
     * @param endTime
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    private PageResult<ExpenseFormWrap> queryExpenseFormForManagerTeam(
            ExpenseFormDAO dao, ExpenseFormStateEnums state, String projectId,
            String keywords, Date startTime, Date endTime, int curPage, int pageSize)
            throws Exception {
        int totalCount;
        int start;
        List<ExpenseFormWrap> list;
        PageResult<ExpenseFormWrap> result;
        totalCount = dao.queryExpenseFormCountForCeo(state, projectId, keywords, startTime, endTime);
        curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
        start = PageResult.getFromIndex(curPage, pageSize);
        list = dao.queryExpenseFormForCeo(state, projectId, keywords, startTime, endTime, start, pageSize);
        result = new PageResult<>(totalCount, curPage, pageSize, list);
        return result;
    }
}
