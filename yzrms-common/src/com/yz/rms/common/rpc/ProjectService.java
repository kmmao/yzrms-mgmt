/*
 * ProjectInterface.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 11:23:43
 */
package com.yz.rms.common.rpc;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.io.httprpc.HttpRPCSessionTokenRequired;
import com.nazca.io.httprpc.InvokingMethod;
import com.nazca.io.httprpc.ServerInvoking;
import com.nazca.sql.PageResult;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.model.Project;
import java.util.List;

/**
 * 项目管理接口
 *
 * @author 上官新建 <shangguanxinjian@yzhtech.com>
 */
@ServerInvoking(method = InvokingMethod.SERVICE_MAPPING, identifier = "com.yz.rms.server.rpcimpl.ProjectServiceImpl")
public interface ProjectService {

    /**
     * 新增项目
     *
     * @param project 将对象作为参数
     * @return 返回新增的项目
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Project createProject(Project project) throws HttpRPCException;

    /**
     * 删除项目
     *
     * @param projectId 将项目id作为参数
     * @return 返回删除的项目
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    void deleteProject(String projectId) throws HttpRPCException;

    /**
     * 修改项目
     *
     * @param project 将对象作为参数
     * @return 返回修改的项目
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Project modifyProject(Project project) throws HttpRPCException;

    /**
     * 根据状态、项目名称、甲方分页查询项目列表，并按项目添加时间倒序排列
     *
     * @param state 当前状态
     * @param projectName 项目名称
     * @param customer 甲方
     * @param curPage 当前页
     * @param pageSize 页面大小
     * @return 返回查询到的集合
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    PageResult<Project> queryProject(ProjectEnums state, String keyWords, int curPage, int pageSize) throws HttpRPCException;

    /**
     * 根据项目id、标记状态标记项目
     *
     * @param projectId 项目id
     * @param state 当前状态
     * @return 返回标记的项目
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    Project changeProjectState(String projectId, ProjectEnums state) throws HttpRPCException;

    /**
     * 获取所有项目信息
     *
     * @param state 项目状态
     * @return
     * @throws HttpRPCException
     */
    @HttpRPCSessionTokenRequired
    List<Project> queryAllProject(ProjectEnums state) throws HttpRPCException;
}
