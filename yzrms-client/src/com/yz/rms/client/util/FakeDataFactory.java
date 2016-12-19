/*
 * FakeDataFactory.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-02-06 12:04:53
 */
package com.yz.rms.client.util;

import com.nazca.sql.PageResult;
import com.nazca.util.NazcaFormater;
import com.yz.rms.common.enums.ExpenseFormStateEnums;
import com.yz.rms.common.enums.ExpenseItemEnum;
import com.yz.rms.common.enums.ProjectEnums;
import com.yz.rms.common.enums.RecordState;
import com.yz.rms.common.model.ExpenseForm;
import com.yz.rms.common.model.Member;
import com.yz.rms.common.model.Project;
import com.yz.rms.common.model.Team;
import com.yz.rms.common.model.wrap.ExpenseFormStateCountWrap;
import com.yz.rms.common.model.wrap.ExpenseFormWrap;
import com.yz.rms.common.model.wrap.MemberWrap;
import com.yz.rms.common.model.wrap.StatExpensePersonWrap;
import com.yz.rms.common.model.wrap.StatItemWrap;
import com.yz.rms.common.model.wrap.StatTeamOrProjectWrap;
import com.yz.rms.common.model.wrap.TeamMemberWrap;
import com.yz.rms.common.model.wrap.WaitPayStatWrap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试数据工厂类
 *
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
public class FakeDataFactory {

    private static boolean isFakeData = false;

    /**
     * 是否采用测试数据
     *
     * @return
     */
    public static boolean isFake() {
	return isFakeData;
    }

    /**
     * 测试数据设置
     *
     * @param flag
     */
    public static void setFake(boolean flag) {
	isFakeData = flag;
    }

    /**
     * 查询所有的团队信息
     *
     * @return
     */
    public static List<Team> queryAllTeams() {
	List<Team> list = new ArrayList<>();
	Team t = new Team();
	t.setTeamId("1");
	t.setTeamName("团队1");
	t.setDeleteState(RecordState.deleted);
	t.setCreateTime(new Date());
	t.setCreator("张");
	t.setModifyTime(new Date());
	t.setModifier("张");
	list.add(t);
	return list;
    }

    /**
     * 查询团队成员
     *
     * @param teamId
     * @return
     */
    public static List<TeamMemberWrap> queryMembersInTeam(String teamId) {
	List<TeamMemberWrap> list = new ArrayList<>();
	TeamMemberWrap tw = new TeamMemberWrap();
	tw.setMemberId("1");
	tw.setAuthority(true);
	tw.setEmployeeName("1");
	tw.setEmployeeNumber("1");
	tw.setPhone("1");
	tw.setPosition("1");
	list.add(tw);
	return list;
    }

    /**
     * 创建团队
     *
     * @param team
     * @return
     */
    public static Team createTeam(Team team) {
	Team t = new Team();
	t.setTeamId("2");
	t.setTeamName("2");
	t.setDeleteState(RecordState.deleted);
	t.setCreateTime(new Date());
	t.setCreator("张");
	t.setModifyTime(new Date());
	t.setModifier("张");
	return t;
    }

    /**
     * 修改团队
     *
     * @param team
     * @return
     */
    public static Team modifyTeam(Team team) {
	Team t = new Team();
	t.setTeamId("2");
	t.setTeamName("2");
	t.setDeleteState(RecordState.deleted);
	t.setCreateTime(new Date());
	t.setCreator("张");
	t.setModifyTime(new Date());
	t.setModifier("张");
	return t;
    }

    /**
     * 删除团队
     *
     * @param teamId
     * @return
     */
    public static Team deleteTeam(String teamId) {
	Team t = new Team();
	t.setTeamId("2");
	t.setTeamName("2");
	t.setDeleteState(RecordState.deleted);
	t.setCreateTime(new Date());
	t.setCreator("张");
	t.setModifyTime(new Date());
	t.setModifier("张");
	return t;
    }

    /**
     * 添加团队成员
     *
     * @param memberIdLis
     * @param teamId
     * @return
     */
    public static List<Member> createMemberToTeam(List<String> memberIdLis, String teamId) {
	List<Member> ml = new ArrayList<>();
	Member m = new Member();
	m.setTeamId("1");
	m.setMemberId("1");
	m.setLeader(true);
	ml.add(m);
	return ml;
    }

    /**
     * 删除团队成员
     *
     * @param teamId
     * @param memberId
     * @return
     */
    public static Member deleteMemberFromTeam(String teamId, String memberId) {
	Member m = new Member();
	m.setTeamId("1");
	m.setMemberId("1");
	m.setLeader(true);
	return m;
    }

    /**
     * 改变团队成员权限，设置负责人
     *
     * @param teamId
     * @param memberId
     * @param authority
     * @return
     */
    public static TeamMemberWrap modifyMemberAuthority(String teamId, String memberId, boolean authority) {
	TeamMemberWrap m = new TeamMemberWrap();
	m.setMemberId("1");
	m.setEmployeeNumber("1");
	m.setEmployeeName("1");
	m.setPosition("1");
	m.setAuthority(true);
	return m;
    }

    /**
     * 添加项目
     *
     * @param project
     * @return
     */
    public static Project createProject(Project project) throws ParseException {
	Project pro = new Project();
	pro.setProjectId("1");
	pro.setProjectName("报销管理系统");
	pro.setCustomer("甲方");
	pro.setAmount(100000.00);
	pro.setState(ProjectEnums.underway);
	pro.setStartTime(NazcaFormater.parseSimpleDate("2016-7-15"));
	pro.setEndTime(new Date());
	return pro;
    }

    /**
     * 删除项目
     *
     * @param projectId
     * @return
     */
    public static String deleteProject(String projectId) throws ParseException {
	Project pro = new Project();
	pro.setProjectId("1");
	pro.setProjectName("报销管理系统");
	pro.setCustomer("甲方");
	pro.setAmount(100000.00);
	pro.setState(ProjectEnums.underway);
	pro.setStartTime(NazcaFormater.parseSimpleDate("2016-7-15"));
	pro.setEndTime(new Date());
	return projectId;
    }

    /**
     * 查询项目
     *
     * @param state
     * @param projectName
     * @param customer
     * @param curPage
     * @param pageSize
     * @return
     */
    public static PageResult<Project> queryProject(ProjectEnums state, String keywords, int curPage, int pageSize) {
	List<Project> list = new ArrayList<>();
	Project projectWrap1 = new Project();
	Project projectWrap2 = new Project();

	projectWrap1.setProjectId("1");
	projectWrap1.setProjectName("报销管理系统");
	projectWrap1.setCustomer("甲方");
	projectWrap1.setAmount(100000.00);
	projectWrap1.setState(ProjectEnums.underway);
	projectWrap1.setStartTime(NazcaFormater.parseSimpleDate("2016-7-15"));
	projectWrap1.setEndTime(new Date());

	projectWrap2.setProjectId("1");
	projectWrap2.setProjectName("绩效考核管理系统");
	projectWrap2.setCustomer("甲方");
	projectWrap2.setAmount(100000.00);
	projectWrap2.setState(ProjectEnums.expired);
	projectWrap2.setStartTime(NazcaFormater.parseSimpleDate("2015-7-15"));
	projectWrap2.setEndTime(new Date());
	list.add(projectWrap1);
	list.add(projectWrap2);
	PageResult<Project> pageResult = new PageResult<>(100, 1, 20, list);
	return pageResult;
    }

    /**
     * 修改项目
     *
     * @param project
     * @return
     */
    public static Project modifyProject(Project project) throws ParseException {
	Project pro = new Project();
	pro.setProjectId("1");
	pro.setProjectName("报销管理系统");
	pro.setCustomer("甲方");
	pro.setAmount(100000.00);
	pro.setState(ProjectEnums.expired);
	pro.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016-7-15"));
	pro.setEndTime(new Date());
	return pro;
    }

    /**
     * 标记项目
     *
     * @param projectId
     * @param state
     * @return
     */
    public static Project changeProjectState(String projectId, ProjectEnums state) throws ParseException {
	Project projectwrap = new Project();
	projectwrap.setProjectId("1");
	projectwrap.setProjectName("报销管理系统");
	projectwrap.setCustomer("甲方");
	projectwrap.setAmount(100000.00);
	projectwrap.setState(ProjectEnums.underway);
	projectwrap.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016-7-15"));
	projectwrap.setEndTime(new Date());
	return projectwrap;
    }

    /**
     * 查询我的报销单
     *
     * @param state
     * @param projectId
     * @param startTime
     * @param endTime
     * @param curPage
     * @param pageSize
     * @return
     */
    public static PageResult<ExpenseFormWrap> queryMyExpenseForm(ExpenseFormStateEnums state, String projectId, Date startTime, Date endTime, int curPage, int pageSize) {
	List<ExpenseFormWrap> list = new ArrayList<>();
	ExpenseFormWrap expenseFormWrap1 = new ExpenseFormWrap();
	ExpenseFormWrap expenseFormWrap2 = new ExpenseFormWrap();
	ExpenseForm expenseForm1 = new ExpenseForm();
	expenseForm1.setExpenseTime(new Date());
	expenseForm1.setExpenseTotal(150.00);
	expenseForm1.setCityTraffic(120.00);
	expenseForm1.setBooksMaterials(30.00);
	expenseForm1.setState(ExpenseFormStateEnums.newForm);
//	USMSUserSyncTool.getInstance().getUserNameById(projectId);
	expenseFormWrap1.setTeamName("开发组");
	expenseFormWrap1.setProjectName("报销管理系统");
	expenseFormWrap1.setExpenseForm(expenseForm1);

	ExpenseForm expenseForm2 = new ExpenseForm();
	expenseForm2.setExpenseTime(new Date());
	expenseForm2.setExpenseTotal(350.00);
	expenseForm2.setCityTraffic(120.00);
	expenseForm2.setBooksMaterials(130.00);
	expenseForm2.setCityGasoline(100.00);
	expenseForm2.setState(ExpenseFormStateEnums.waitAuditForPm);
	expenseFormWrap2.setTeamName("开发组");
	expenseFormWrap2.setProjectName("绩效考核管理系统");
	expenseFormWrap2.setExpenseForm(expenseForm2);
	list.add(expenseFormWrap1);
	list.add(expenseFormWrap2);
	PageResult<ExpenseFormWrap> pageResult = new PageResult<>(100, 1, 20, list);
	return pageResult;
    }

    /**
     * 添加报销单
     *
     * @param project
     * @return
     */
    public static ExpenseForm createExpenseForm(ExpenseForm expenseForm) throws ParseException {
	ExpenseForm ef = new ExpenseForm();
//	pro.setProjectId("1");
//	pro.setProjectName("报销管理系统");
//	pro.setCustomer("甲方");
//	pro.setAmount(100000.00);
//	pro.setState("进行中");
//	pro.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016.7.15"));
//	pro.setEndTime(new Date());
	return expenseForm;
    }

    /**
     * 修改我的报销单
     *
     * @param expenseForm
     * @return
     */
    public static ExpenseForm modifyExpenseForm(ExpenseForm expenseForm) {

	return expenseForm;
    }

    /**
     * 删除我的报销单
     *
     * @param expenseId
     * @return
     */
    public static ExpenseForm deleteExpenseForm(String expenseId) {
	ExpenseForm expenseForm = new ExpenseForm();
	expenseForm.setExpenseTime(new Date());
	expenseForm.setExpenseTotal(120.00);
	expenseForm.setProjectId("1");
	expenseForm.setState(ExpenseFormStateEnums.newForm);
	return expenseForm;
    }

    /**
     * 提交我的报销单
     *
     * @param expenseId
     * @return
     */
    public static ExpenseForm submitExpenseForm(String expenseId) {
	ExpenseForm expenseForm = new ExpenseForm();
	expenseForm.setExpenseTime(new Date());
	expenseForm.setExpenseTotal(120.00);
	expenseForm.setProjectId("1");
	expenseForm.setState(ExpenseFormStateEnums.newForm);
	return expenseForm;
    }

    /**
     * 查询未发放报销统计
     *
     * @return
     */
    public static Map<Integer, List<WaitPayStatWrap>> queryWaitPayStat() {
	Map<Integer, List<WaitPayStatWrap>> map = new HashMap();
	List<WaitPayStatWrap> warps = new ArrayList();
	WaitPayStatWrap warp = new WaitPayStatWrap();
	warp.setAmount(3.0);
	warp.setCount(20);
	warp.setMonth(8);
	warps.add(warp);
	map.put(2015, warps);
	WaitPayStatWrap warp1 = new WaitPayStatWrap();
	warp1.setAmount(3.0);
	warp1.setCount(20);
	warp1.setMonth(6);
	warps.add(warp1);
	map.put(2016, warps);
	WaitPayStatWrap warp2 = new WaitPayStatWrap();
	warp2.setAmount(3.0);
	warp2.setCount(20);
	warp2.setMonth(4);
	warps.add(warp2);
	map.put(2016, warps);

	return map;
    }

    /**
     * 查询所有成员
     *
     * @return
     */
    public static List<MemberWrap> queryAllMembers() {
	List<MemberWrap> ml = new ArrayList<>();
	MemberWrap m = new MemberWrap();
	m.setMemberId("1");
	m.setMemberName("1");
	ml.add(m);
	return ml;
    }

    /**
     * 查询报销单状态列表和各状态的报销单数量
     *
     * @return
     */
    public static List<ExpenseFormStateCountWrap> queryExpenseFromStateCountByRole() {
	List<ExpenseFormStateCountWrap> list = new ArrayList<>();
	ExpenseFormStateCountWrap wrap1 = new ExpenseFormStateCountWrap();
	ExpenseFormStateCountWrap wrap2 = new ExpenseFormStateCountWrap();
	wrap1.setState(ExpenseFormStateEnums.waitAuditForPm);
	wrap1.setCount(1);
	wrap2.setState(ExpenseFormStateEnums.waitAuditForCeo);
	wrap2.setCount(5);
	list.add(wrap1);
	list.add(wrap2);
	return list;
    }

    /**
     * 根据状态查询报销单信息
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
     */
    public static PageResult<ExpenseFormWrap> queryExpenseFormByState(ExpenseFormStateEnums state, String projectId, String teamId,
	    Date startTime, Date endTime, String keywords, int curPage, int pageSize) {
	List<ExpenseFormWrap> list = new ArrayList<>();
	PageResult<ExpenseFormWrap> result = new PageResult<>(1, curPage, pageSize, list);
	ExpenseForm expenseForm1 = new ExpenseForm();
	ExpenseForm expenseForm2 = new ExpenseForm();
	ExpenseFormWrap wrap1 = new ExpenseFormWrap();
	ExpenseFormWrap wrap2 = new ExpenseFormWrap();
	//一组数据
	expenseForm1.setExpenseTime(new Date());//获取当前时间
	expenseForm1.setExpenseTotal(120.0);
	expenseForm1.setState(ExpenseFormStateEnums.waitAuditForPm);
	expenseForm1.setCityTraffic(35.0);
	expenseForm1.setBooksMaterials(85.0);
	wrap1.setExpenseForm(expenseForm1);
//	wrap1.setExpenseName("张三");
	wrap1.setProjectName("报销管理系统");
	wrap1.setTeamName("组A");
	//二组数据
	expenseForm2.setExpenseTime(new Date());//获取当前时间
	expenseForm2.setExpenseTotal(529.0);
	expenseForm2.setState(ExpenseFormStateEnums.waitAuditForCeo);
	expenseForm2.setCityGasoline(123.0);
	expenseForm2.setCopyBind(74.0);
	expenseForm2.setConferences(32.0);
	expenseForm2.setOfficeSupplies(300.0);
	wrap2.setExpenseForm(expenseForm2);
//	wrap2.setExpenseName("李四");
	wrap2.setProjectName("考勤系统");
	wrap2.setTeamName("组B");
	list.add(wrap1);
	list.add(wrap2);
	return result;
    }

    /**
     * 审核报销单
     *
     * @param expenseId
     * @param memo
     * @param isPass
     * @return
     */
    public static ExpenseForm auditExpenseForm(String expenseId, String memo, boolean isPass) {
	ExpenseForm expenseForm = new ExpenseForm();
	expenseForm.setExpenseId(expenseId);
	expenseForm.setPassState(isPass);
	return expenseForm;
    }

    /**
     * 标记发放
     *
     * @param expenseFormIdList
     * @return
     */
    public static ExpenseForm markPaid(List<String> expenseFormIdList) {
	ExpenseForm expenseForm = new ExpenseForm();
	expenseForm.setState(ExpenseFormStateEnums.paid);
	return expenseForm;
    }

    public static List<Project> queryAllProjects() {
	List<Project> list = new ArrayList<>();
	Project p = new Project();
	p.setAmount(1.0);
	p.setCreateTime(new Date());
	p.setCreator("zhang");
	p.setCustomer("1");
	p.setDeleteState(RecordState.deleted);
	p.setEndTime(new Date());
	p.setModifier("1");
	p.setModifyTime(new Date());
	p.setProjectId("1");
	p.setProjectName("项目1");
	p.setStartTime(new Date());
	p.setState(ProjectEnums.expired);
	list.add(p);
	return list;
    }

    public static List<StatItemWrap> queryExpenseForTeamStat(String teamId, int year, Integer month) {

	//1-----------------------------------------------
	StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrap2 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrap3 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount2 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount3 = new StatExpensePersonWrap();
	List<StatExpensePersonWrap> personList = new ArrayList<>();
	List<StatExpensePersonWrap> personList2 = new ArrayList<>();
	//张三
	statExpensePersonWrap.setpId("1");
	statExpensePersonWrap.setpName("张三");
	statExpensePersonWrap.setMoney(1.0);
	//合计1
	statExpensePersonWrapAmount.setpId("1");
	statExpensePersonWrapAmount.setpName("张三");
	statExpensePersonWrapAmount.setMoney(statExpensePersonWrap.getMoney());
	//李四
	statExpensePersonWrap2.setpId("2");
	statExpensePersonWrap2.setpName("李四");
	statExpensePersonWrap2.setMoney(2.0);
	//合计2
	statExpensePersonWrapAmount2.setpId("2");
	statExpensePersonWrapAmount2.setpName("李四");
	statExpensePersonWrapAmount2.setMoney(statExpensePersonWrap2.getMoney());

	statExpensePersonWrap3.setpId(StatExpensePersonWrap.TOTAL_ID);
	statExpensePersonWrap3.setpName(StatExpensePersonWrap.TOTAL_NAME);
	statExpensePersonWrap3.setMoney(statExpensePersonWrap.getMoney() + statExpensePersonWrap2.getMoney());

	statExpensePersonWrapAmount3.setpId(StatExpensePersonWrap.TOTAL_ID);
	statExpensePersonWrapAmount3.setpName(StatExpensePersonWrap.TOTAL_NAME);
	statExpensePersonWrapAmount3.setMoney(statExpensePersonWrap3.getMoney());

	personList.add(statExpensePersonWrap);
	personList.add(statExpensePersonWrap2);
	personList.add(statExpensePersonWrap3);
	personList2.add(statExpensePersonWrap3);

	StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
	statTeamOrProjectWrap.setTpId("1");
	statTeamOrProjectWrap.setTpName("团队1");
	statTeamOrProjectWrap.setPersonList(personList);

	StatTeamOrProjectWrap statTeamOrProjectWrap2 = new StatTeamOrProjectWrap();
	statTeamOrProjectWrap2.setTpId("合计");
	statTeamOrProjectWrap2.setTpName("合计");
	statTeamOrProjectWrap2.setPersonList(personList2);

	List<StatTeamOrProjectWrap> statTeamOrProjectWrapLis = new ArrayList<>();
	statTeamOrProjectWrapLis.add(statTeamOrProjectWrap);
	statTeamOrProjectWrapLis.add(statTeamOrProjectWrap2);

	List<StatItemWrap> statItemWrapList = new ArrayList<>();
	StatItemWrap statItemWrap = new StatItemWrap();
	StatItemWrap statItemWrap2 = new StatItemWrap();
	statItemWrap2.setItem(ExpenseItemEnum.amount);
	statItemWrap2.setTpList(statTeamOrProjectWrapLis);
	statItemWrap.setItem(ExpenseItemEnum.train);
	statItemWrap.setTpList(statTeamOrProjectWrapLis);

	statItemWrapList.add(statItemWrap2);
	statItemWrapList.add(statItemWrap);
	return statItemWrapList;
    }

    public static List<StatItemWrap> queryExpenseForProjectStat(String projectId, int year, Integer month) {
	//1-----------------------------------------------
	StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrap2 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrap3 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount2 = new StatExpensePersonWrap();
	StatExpensePersonWrap statExpensePersonWrapAmount3 = new StatExpensePersonWrap();
	List<StatExpensePersonWrap> personList = new ArrayList<>();
	//张三
	statExpensePersonWrap.setpId("1");
	statExpensePersonWrap.setpName("张三");
	statExpensePersonWrap.setMoney(1.0);
	//合计1
	statExpensePersonWrapAmount.setpId("1");
	statExpensePersonWrapAmount.setpName("张三");
	statExpensePersonWrapAmount.setMoney(statExpensePersonWrap.getMoney());
	//李四
	statExpensePersonWrap2.setpId("2");
	statExpensePersonWrap2.setpName("李四");
	statExpensePersonWrap2.setMoney(2.0);
	//合计2
	statExpensePersonWrapAmount2.setpId("2");
	statExpensePersonWrapAmount2.setpName("李四");
	statExpensePersonWrapAmount2.setMoney(statExpensePersonWrap2.getMoney());

	statExpensePersonWrap3.setpId(StatExpensePersonWrap.TOTAL_ID);
	statExpensePersonWrap3.setpName(StatExpensePersonWrap.TOTAL_NAME);
	statExpensePersonWrap3.setMoney(statExpensePersonWrap.getMoney() + statExpensePersonWrap2.getMoney());

	statExpensePersonWrapAmount3.setpId(StatExpensePersonWrap.TOTAL_ID);
	statExpensePersonWrapAmount3.setpName(StatExpensePersonWrap.TOTAL_NAME);
	statExpensePersonWrapAmount3.setMoney(statExpensePersonWrap3.getMoney());

	personList.add(statExpensePersonWrap);
	personList.add(statExpensePersonWrap2);
	personList.add(statExpensePersonWrap3);

	StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
	statTeamOrProjectWrap.setTpId("1");
	statTeamOrProjectWrap.setTpName("项目1");
	statTeamOrProjectWrap.setPersonList(personList);

	List<StatTeamOrProjectWrap> statTeamOrProjectWrapLis = new ArrayList<>();
	statTeamOrProjectWrapLis.add(statTeamOrProjectWrap);

	List<StatItemWrap> statItemWrapList = new ArrayList<>();
	StatItemWrap statItemWrap = new StatItemWrap();
	StatItemWrap statItemWrap2 = new StatItemWrap();
	statItemWrap2.setItem(ExpenseItemEnum.amount);
	statItemWrap2.setTpList(statTeamOrProjectWrapLis);
	statItemWrap.setItem(ExpenseItemEnum.train);
	statItemWrap.setTpList(statTeamOrProjectWrapLis);

	statItemWrapList.add(statItemWrap);
	statItemWrapList.add(statItemWrap2);
	return statItemWrapList;
    }

    public static List<Team> queryAllTeamsByPM() {
	List<Team> list = new ArrayList<>();
	Team t = new Team();
	t.setTeamId("1");
	t.setTeamName("团队1");
	t.setDeleteState(RecordState.deleted);
	t.setCreateTime(new Date());
	t.setCreator("张");
	t.setModifyTime(new Date());
	t.setModifier("张");
	list.add(t);
	return list;
    }

}
