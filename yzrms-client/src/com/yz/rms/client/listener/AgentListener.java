/*
 * AgentListener.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2012-09-17 17:48:48
 */
package com.yz.rms.client.listener;

/**
 * 公共的AgentListener
 * @param <T> 
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public interface AgentListener<T> {
    void onStarted(long seq);

    void onSucceeded(T result, long seq);

    void onFailed(String errMsg, int errCode, long seq);
}
