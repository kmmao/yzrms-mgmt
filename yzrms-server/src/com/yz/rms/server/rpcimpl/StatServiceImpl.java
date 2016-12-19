/*
 * StatServiceImpl.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-19 19:27:04
 */
package com.yz.rms.server.rpcimpl;

import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.usm.client.connector.USMSRPCServiceException;
import com.nazca.usm.model.USMSUser;
import com.nazca.util.NazcaFormater;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.enums.ExpenseItemEnum;
import com.yz.rms.common.model.wrap.ExpenseTIdTNamePIdPNameWrap;
import com.yz.rms.common.model.wrap.StatExpensePersonWrap;
import com.yz.rms.common.model.wrap.StatItemWrap;
import com.yz.rms.common.model.wrap.StatTeamOrProjectWrap;
import com.yz.rms.common.rpc.StatService;
import com.yz.rms.common.util.ErrorCodeFormater;
import com.yz.rms.server.dao.StatDAO;
import com.yz.rms.server.util.USMSServiceUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 统计实现类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class StatServiceImpl implements StatService {

    private static final Log log = LogFactory.getLog(TeamServiceImpl.class);
    //最后一列合计的合计值
    double allTeamAllItemAmount = 0;
    StatDAO dao = new StatDAO();
    List<USMSUser> uSMSUserLis;

    public StatServiceImpl() throws USMSRPCServiceException {
        this.uSMSUserLis = USMSServiceUtils.getAllUsers();
    }
    @Override
    public List<StatItemWrap> queryExpenseForTeamStat(String teamId, int year, Integer month) throws HttpRPCException {
        List<StatItemWrap> statItemWrapList = new ArrayList<>();
        //把teamID作为key，团队ID，团队名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> teamMap;

        Date start;
        Date end;
        if (month == null) {
            start = getDateByYear(year, 0, 1, 0, 0, 0);
            end = getDateByYear(year, 11, 31, 23, 59, 59);
        } else {
            start = getFirstDayByMonth(year, month);
            end = getLastDayByMonth(year, month);
        }
        try {
            if (teamId == null) {
                teamMap = dao.queryAllTeamStatDatas(start, end);
            } else {
                teamMap = dao.queryStatDatasByTeam(teamId, start, end);
            }

            //把查询出来的信息封装成List<StatItemWrap>返回,第一个参数区分是团队还是项目做不同的处理
            statItemWrapList = getTeamOrProjectStatItemWrapList(0, teamMap);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query team expense stat failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return statItemWrapList;
    }

    @Override
    public List<StatItemWrap> queryExpenseForProjectStat(String projectId, int year, Integer month) throws HttpRPCException {
        List<StatItemWrap> statItemWrapList = new ArrayList<>();

        //把projectID作为key，项目ID，项目名，报销单信息为封装对象的list作为value存入teamMap中
        HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap;
        Date start;
        Date end;
        if (month == null) {
            start = getDateByYear(year, 0, 1, 0, 0, 0);
            end = getDateByYear(year, 11, 31, 23, 59, 59);
        } else {
            start = getFirstDayByMonth(year, month);
            end = getLastDayByMonth(year, month);
        }
        try {
            if (projectId == null) {
                projectMap = dao.queryAllProjectStatDatas(start, end);
            } else {
                projectMap = dao.queryStatDatasByProjct(projectId, start, end);
            }

            //把查询出来的信息封装成List<StatItemWrap>返回
            statItemWrapList = getTeamOrProjectStatItemWrapList(1, projectMap);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("query team expense stat failed!", ex);
            throw new HttpRPCException(ErrorCodeFormater.explainErrorCode(ErrorCode.DB_ERROR), ErrorCode.DB_ERROR);
        }
        return statItemWrapList;
    }

    private Date getDateByYear(int year, int hour, int minute, int second, int millisecond, int mill) {
        Calendar c = Calendar.getInstance();
        c.set(year, hour, minute, second, millisecond, mill);
        return c.getTime();
    }

    private Date getFirstDayByMonth(int year, Integer month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1, 0, 0, 0);
        return c.getTime();
    }

    private Date getLastDayByMonth(int year, Integer month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1, 23, 59, 59);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    private List<StatItemWrap> getTeamOrProjectStatItemWrapList(int diff, HashMap<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap) throws USMSRPCServiceException {

        //把报销项目的enum作为key，团队id，团队名或项目名和报销人员list作为封装对象的list作为value存入itemMap中
        HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> itemMap = new LinkedHashMap<>();

        List<StatItemWrap> statItemWrapLis = new ArrayList<>();
        //遍历把对应的信息封装入statTeamOrProjectWrap中
        //封装所有团队或项目有关cityTraffic的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.cityTraffic, projectMap, itemMap);
        //封装所有团队或项目有关booksMaterials的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.booksMaterials, projectMap, itemMap);
        //封装所有团队或项目有关cityGasoline的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.cityGasoline, projectMap, itemMap);
        //封装所有团队或项目有关copyBind的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.copyBind, projectMap, itemMap);
        //封装所有团队或项目有关entertain的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.entertain, projectMap, itemMap);
        //封装所有团队或项目有关material的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.material, projectMap, itemMap);
        //封装所有团队或项目有关train的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.train, projectMap, itemMap);
        //封装所有团队或项目有关officeSupplies的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.officeSupplies, projectMap, itemMap);
        //封装所有团队或项目有关telephoneBill的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.telephoneBill, projectMap, itemMap);
        //封装所有团队或项目有关postage的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.postage, projectMap, itemMap);
        //封装所有团队或项目有关travelAccommodation的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.travelAccommodation, projectMap, itemMap);
        //封装所有团队或项目有关travelTraffic的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.travelTraffic, projectMap, itemMap);
        //封装所有团队或项目有关travelMeals的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.travelMeals, projectMap, itemMap);
        //封装所有团队或项目有关travelAllowance的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.travelAllowance, projectMap, itemMap);
        //封装所有团队或项目有关spacePage的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.spacePage, projectMap, itemMap);
        //封装所有团队或项目有关conferences的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.conferences, projectMap, itemMap);
        //封装所有团队或项目有关fieldOperation的信息
        itemMap = setAllTeamOrProjectItemInfoToMap(diff, ExpenseItemEnum.fieldOperation, projectMap, itemMap);
        //得到封装每行数据总计的itemMap,第一个参数为0时 处理团队报销，为1时处理项目报销
        itemMap = setAllTeamOrProjectAmountInfo(diff, projectMap, itemMap);
        //遍历itemMap得到最后返回前台的封装list
        statItemWrapLis = getStatItemWrapList(itemMap, statItemWrapLis);
        return statItemWrapLis;
    }

    private List<StatItemWrap> getStatItemWrapList(HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> itemMap, List<StatItemWrap> statItemWrapLis) {
        //遍历itemMap得到最后返回前台的封装list
        for (ExpenseItemEnum key : itemMap.keySet()) {
            StatItemWrap statItemWrap = new StatItemWrap();
            statItemWrap.setItem(key);
            statItemWrap.setTpList(itemMap.get(key));
            statItemWrapLis.add(statItemWrap);
        }
        return statItemWrapLis;
    }

    private HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> setAllTeamOrProjectItemInfoToMap(int diff, ExpenseItemEnum expenseEnum, Map<String, List<ExpenseTIdTNamePIdPNameWrap>> teamMap, HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> itemMap) throws USMSRPCServiceException {
        //遍历把对应的信息封装入statTeamOrProjectWrap中
        List<StatTeamOrProjectWrap> itemExpenseWapLis = new ArrayList<>();
        //所有团队的总计
        double allTeamTotal = 0;
        for (String key : teamMap.keySet()) {
            List<ExpenseTIdTNamePIdPNameWrap> listExTmIdTmNm = teamMap.get(key);
            StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
            statTeamOrProjectWrap.setTpId(key);
            if (diff == 0) {
                statTeamOrProjectWrap.setTpName(listExTmIdTmNm.get(0).getTeamName());
            } else {
                statTeamOrProjectWrap.setTpName(listExTmIdTmNm.get(0).getProjectName());
            }
            List<StatExpensePersonWrap> personList = new ArrayList<>();
            double teamTotal = 0;
            for (ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap : listExTmIdTmNm) {
                StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
                for (USMSUser uSMSUser : uSMSUserLis) {
                    if (expenseProjectIdProjectNameWrap.getExpensePersonId().equals(uSMSUser.getId())) {
                        statExpensePersonWrap.setpName(uSMSUser.getName());//TODO同上
                        statExpensePersonWrap.setEmployeeNumber(uSMSUser.getEmployeeNumber());
                    }
                }
                ExpenseItemEnum itemType = expenseEnum;
                if (itemType != null) {
                    switch (itemType) {
                        case cityTraffic:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getCityTraffic());
                            break;
                        case booksMaterials:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getBooksMaterials());
                            break;
                        case cityGasoline:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getCityGasoline());
                            break;
                        case copyBind:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getCopyBind());
                            break;
                        case entertain:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getEntertain());
                            break;
                        case material:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getMaterial());
                            break;
                        case train:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTrain());
                            break;
                        case officeSupplies:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getOfficeSupplies());
                            break;
                        case telephoneBill:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTelephoneBill());
                            break;
                        case postage:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getPostage());
                            break;
                        case travelAccommodation:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTravelAccommodation());
                            break;
                        case travelTraffic:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTravelTraffic());
                            break;
                        case travelMeals:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTravelMeals());
                            break;
                        case travelAllowance:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getTravelAllowance());
                            break;
                        case spacePage:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getSpacePage());
                            break;
                        case conferences:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getConferences());
                            break;
                        case fieldOperation:
                            statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getFieldOperation());
                            break;
                        default:
                            break;
                    }
                }
                teamTotal += statExpensePersonWrap.getMoney();
                personList.add(statExpensePersonWrap);
            }
            allTeamTotal += teamTotal;
            //对合计进行精度运算，保留两位小数
            String allTeamTotalString = NazcaFormater.getCashString(allTeamTotal);
            allTeamTotal = NazcaFormater.parseCash(allTeamTotalString);
            //当团队人数超过一个人的时候显示合计
            if (personList.size() > 1) {
                StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
                statExpensePersonWrap.setpId(StatExpensePersonWrap.TOTAL_ID);
                statExpensePersonWrap.setpName(StatExpensePersonWrap.TOTAL_NAME);
                statExpensePersonWrap.setEmployeeNumber(String.valueOf(Integer.MAX_VALUE));
                statExpensePersonWrap.setMoney(teamTotal);
                personList.add(statExpensePersonWrap);
            }
            StatExpensePersonWrap comparator = new StatExpensePersonWrap();
            Collections.sort(personList, comparator);
            statTeamOrProjectWrap.setPersonList(personList);
            itemExpenseWapLis.add(statTeamOrProjectWrap);
        }

        //当团队数大于1的时候显示所有团队或项目合计
        if (teamMap.keySet().size() > 1) {
            StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
            statTeamOrProjectWrap.setTpId(StatTeamOrProjectWrap.TOTAL_ID);
            statTeamOrProjectWrap.setTpName(StatTeamOrProjectWrap.TOTAL_NAME);
            List<StatExpensePersonWrap> personList = new ArrayList<>();
            StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
            statExpensePersonWrap.setpId(StatExpensePersonWrap.TOTAL_ID);
            statExpensePersonWrap.setpName(StatExpensePersonWrap.TOTAL_NAME);
            statExpensePersonWrap.setMoney(allTeamTotal);
            personList.add(statExpensePersonWrap);
            statTeamOrProjectWrap.setPersonList(personList);
            itemExpenseWapLis.add(statTeamOrProjectWrap);
            allTeamAllItemAmount += allTeamTotal;
            String allTeamAllItemAmountString = NazcaFormater.getCashString(allTeamAllItemAmount);
            allTeamAllItemAmount = NazcaFormater.parseCash(allTeamAllItemAmountString);
        }
        //添加最后一行的合计数据
        itemMap.put(expenseEnum, itemExpenseWapLis);
        return itemMap;
    }

    private HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> setAllTeamOrProjectAmountInfo(int diff, Map<String, List<ExpenseTIdTNamePIdPNameWrap>> projectMap, HashMap<ExpenseItemEnum, List<StatTeamOrProjectWrap>> itemMap) throws USMSRPCServiceException {
        //          amount
        List<StatTeamOrProjectWrap> amountWapLis = new ArrayList<>();
        for (String key : projectMap.keySet()) {
            List<ExpenseTIdTNamePIdPNameWrap> listExTmIdTmNm = projectMap.get(key);
            StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
            statTeamOrProjectWrap.setTpId(key);
            if (diff == 0) {
                statTeamOrProjectWrap.setTpName(listExTmIdTmNm.get(0).getTeamName());
            } else {
                statTeamOrProjectWrap.setTpName(listExTmIdTmNm.get(0).getProjectName());
            }
            List<StatExpensePersonWrap> personList = new ArrayList<>();
            double teamTotal = 0;
            for (ExpenseTIdTNamePIdPNameWrap expenseProjectIdProjectNameWrap : listExTmIdTmNm) {
                StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
                for (USMSUser uSMSUser : uSMSUserLis) {
                    if (expenseProjectIdProjectNameWrap.getExpensePersonId().equals(uSMSUser.getId())) {
                        statExpensePersonWrap.setpName(uSMSUser.getName());//TODO同上
                        statExpensePersonWrap.setEmployeeNumber(uSMSUser.getEmployeeNumber());
                    }
                }
                statExpensePersonWrap.setMoney(expenseProjectIdProjectNameWrap.getCityTraffic() + expenseProjectIdProjectNameWrap.getBooksMaterials()
                        + expenseProjectIdProjectNameWrap.getCityGasoline() + expenseProjectIdProjectNameWrap.getCopyBind() + expenseProjectIdProjectNameWrap.getEntertain()
                        + expenseProjectIdProjectNameWrap.getMaterial() + expenseProjectIdProjectNameWrap.getTrain() + expenseProjectIdProjectNameWrap.getOfficeSupplies()
                        + expenseProjectIdProjectNameWrap.getTelephoneBill() + expenseProjectIdProjectNameWrap.getPostage() + expenseProjectIdProjectNameWrap.getTravelAccommodation()
                        + expenseProjectIdProjectNameWrap.getTravelTraffic() + expenseProjectIdProjectNameWrap.getTravelMeals() + expenseProjectIdProjectNameWrap.getTravelAllowance()
                        + expenseProjectIdProjectNameWrap.getSpacePage() + expenseProjectIdProjectNameWrap.getConferences()
                        + expenseProjectIdProjectNameWrap.getFieldOperation());

                teamTotal += statExpensePersonWrap.getMoney();
                personList.add(statExpensePersonWrap);
            }
            if (personList.size() > 1) {
                StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
                statExpensePersonWrap.setpId(StatExpensePersonWrap.TOTAL_ID);
                statExpensePersonWrap.setpName(StatExpensePersonWrap.TOTAL_NAME);
                statExpensePersonWrap.setEmployeeNumber(String.valueOf(Integer.MAX_VALUE));
                statExpensePersonWrap.setMoney(teamTotal);
                personList.add(statExpensePersonWrap);
            }
            StatExpensePersonWrap comparator = new StatExpensePersonWrap();
            Collections.sort(personList, comparator);
            statTeamOrProjectWrap.setPersonList(personList);
            amountWapLis.add(statTeamOrProjectWrap);
        }
        //将最后一列的所有合计的值的和放入到map中
        StatTeamOrProjectWrap statTeamOrProjectWrap = new StatTeamOrProjectWrap();
        statTeamOrProjectWrap.setTpId(StatTeamOrProjectWrap.TOTAL_ID);
        statTeamOrProjectWrap.setTpName(StatTeamOrProjectWrap.TOTAL_NAME);
        //当团队或项目数大于1的时候显示最后一行最后一列的合计
        if (projectMap.keySet().size() > 1) {
            List<StatExpensePersonWrap> personList = new ArrayList<>();
            StatExpensePersonWrap statExpensePersonWrap = new StatExpensePersonWrap();
            statExpensePersonWrap.setpId(StatExpensePersonWrap.TOTAL_ID);
            statExpensePersonWrap.setpName(StatExpensePersonWrap.TOTAL_NAME);
            statExpensePersonWrap.setMoney(allTeamAllItemAmount);
            personList.add(statExpensePersonWrap);
            statTeamOrProjectWrap.setPersonList(personList);
            amountWapLis.add(statTeamOrProjectWrap);
        }

        itemMap.put(ExpenseItemEnum.amount, amountWapLis);
        return itemMap;
    }
}
