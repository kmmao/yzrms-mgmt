/*
 * ErrorCode.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-03-24 16:12:17
 */
package com.yz.rms.common.consts;

/**
 * 错误编码
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public interface ErrorCode {
    // 000000 - 009999 公共错误
    int DB_ERROR = 10; //远程服务器内部错误
    int DB_TRANSACTION_ERROR = 12; //远程数据操作失败
    int DB_ROLLBACK_ERROR = 14; //远程数据访问错误
    int DB_CONNECT_FAILED = 15; //远程数据库连接错误
    int SERVER_ERROR = 20; //应用服务器内部错误
    int NETWORK_ERROR = 30; //网络错误
    int LACK_OF_AUTH = 110; //用户认证失败，请重新登录
    int PASSWORD_CHECK_AUTH = 120; //密码格式错误
    int NETWORK_CONNECTION_ERROR = 500; //网络连接错误
    int PARAMETER_ERROR = 600; //参数错误
    int DATA_CONFLICT_WITH_SERVER = 40014;//服务器端的数据已更新
//    int SENDING_SERVER_EXCEPTION = 5555;//发送短信服务器端异常
    int OBJECT_EXIST = 6666; //添加的记录已存在
    int OBJECT_NOT_EXIST = 7777; //待操作的记录不存在
    int THREAD_INTERRUPTED = 8888; //执行线程意外中断
    int UNKNOWN_ERROR = 9999; //未知错误
     
    //990000 - 999999 USM错误代码
    int USMS_ERROR_CODE_START = 990000; //USMS错误代码起始
    int USER_ID_ERROR = 990010; //用户名或密码错误
    int USER_PASSWORD_ERROR = 990011; //用户名或密码错误
    int USER_DISABLED = 990012; //用户已被禁用
    int LOGIN_TOO_MUCH = 990013; //尝试登录次数太多
    int CONNECT_USMS_FAILED = 990014; //无法连接到登录服务器
    int LACK_OF_ROLES = 991000; //您没有角色，不能登录
    int ORG_DELETED = 991010; //您的单位已被删除，不能登录
 
}
