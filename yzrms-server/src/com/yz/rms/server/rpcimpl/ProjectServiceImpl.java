/*
 * ProjectServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-15 14:08:08
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCInjection;
import com.nazca.sql.PageResult;
import com.nazca.usm.common.SessionConst;
import com.nazca.usm.model.USMSUser;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.rpc.ProjectService;
import com.yz.rms.common.util.ErrorCodeFormater;
import com.yz.rms.server.dao.ProjectDAO;
import com.yz.rms.server.util.USMSServiceUtils;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 项目管理实现类
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
public class ProjectServiceImpl implements ProjectService {

    private static final Log log = LogFactory.getLog(ProjectServiceImpl.class);
    @HttpRPCInjection
    private HttpSession session;

    @Override
    public Project createProject(Project project) throws HttpRPCException {
	ProjectDAO pd = new ProjectDAO();
	Project pro = null;
	try {
	    //获取当前登录用户
	    String loginName = (String) session.getAttribute(SessionConst.KEY_USER_ID);
//	    USMSUser user = USMSServiceUtils.getUserInfoByLoginName(loginName);
	    project.setCreator(loginName);
	    project.setCreateTime(new Date());
	    project.setModifier(loginName);
	    project.setModifyTime(new Date());
	    pro = pd.createProject(project);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("create project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
	return pro;
    }

    @Override
    public void deleteProject(String projectId) throws HttpRPCException {
	ProjectDAO pd = new ProjectDAO();
	Project pro = null;
	try {
            String name = (String) session.getAttribute(SessionConst.KEY_USER_ID);
	    pd.deleteProject(projectId, name);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("delete project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
    }

    @Override
    public Project modifyProject(Project project) throws HttpRPCException {
	ProjectDAO pd = new ProjectDAO();
	Project pro = null;
	try {
	    String userId = (String) session.getAttribute(SessionConst.KEY_USER_ID);
	    project.setModifier(userId);
	    pro = pd.modifyProject(project);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("modify project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
	return pro;
    }

    @Override
    public PageResult<Project> queryProject(ProjectEnums state, String keywords, int curPage, int pageSize) throws HttpRPCException {
	PageResult<Project> result = null;
	List<Project> list = null;
	ProjectDAO dao = new ProjectDAO();
	try {
	    int totalCount = dao.queryProjectTotalCount(state, keywords);
	    curPage = PageResult.recalculateCurPage(totalCount, curPage, pageSize);
	    int start = PageResult.getFromIndex(curPage, pageSize);
	    list = dao.queryProjectList(state, keywords, start, pageSize);
	    result = new PageResult<>(totalCount, curPage, pageSize, list);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("qurey project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
	return result;
    }

    @Override
    public Project changeProjectState(String projectId, ProjectEnums state) throws HttpRPCException {
	ProjectDAO dao = new ProjectDAO();
	try {
            String userId = (String) session.getAttribute(SessionConst.KEY_USER_ID);
	    dao.changeProjectState(projectId, state, userId);
            return dao.queryProjectById(projectId);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("mark project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
    }

    @Override
    public List<Project> queryAllProject(ProjectEnums state) throws HttpRPCException {
	List<Project> list = null;
	ProjectDAO dao = new ProjectDAO();
	try {
	    list = dao.queryProjectList(state);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("qurey project failed!", ex);
	    throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
	}
	return list;
    }

}
