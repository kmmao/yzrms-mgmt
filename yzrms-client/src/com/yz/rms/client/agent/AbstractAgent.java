/*
 * AgentListener.java
 * 
 * Copyright(c) 2007-2012 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2012-09-17 17:48:48
 */
package com.yz.rms.client.agent;

import com.nazca.io.httprpc.HttpRPCException;
import com.yz.rms.client.listener.AgentEventListenerList;
import com.yz.rms.client.listener.AgentListener;
import com.yz.rms.common.consts.ErrorCode;
import com.yz.rms.common.util.ErrorCodeFormater;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 * 抽象的公共Agent
 * @param <T>
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public abstract class AbstractAgent<T> {
    private AgentEventListenerList listeners = new AgentEventListenerList();
    private SwingWorker<T, Object> worker;

    public final long start() {
        final long seq = System.currentTimeMillis();
        fireStart(seq);

        worker = new SwingWorker<T, Object>() {
            @Override
            protected T doInBackground() throws Exception {
                return doExecute();
            }
            
            @Override
            protected void done() {
                try {
                    T o = get();
                    fireSucceeded(o,seq);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    fireFailed(ErrorCodeFormater.explainErrorCode(ErrorCode.THREAD_INTERRUPTED), 
                            ErrorCode.THREAD_INTERRUPTED, seq);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    Throwable e = ex;
                    if(ex instanceof ExecutionException){
                        e = ex.getCause();
                    }
                    if (e instanceof HttpRPCException) {
                        HttpRPCException hre = (HttpRPCException) e;
                        fireFailed(hre.getMessage(), hre.getCode(),seq);
                    } else {
                        fireFailed(ErrorCodeFormater.explainErrorCode(ErrorCode.UNKNOWN_ERROR), 
                                ErrorCode.UNKNOWN_ERROR, seq);
                    }
                }
            }
        };
        
        worker.execute();
        return seq;
    }

    protected abstract T doExecute() throws HttpRPCException;

    public void addListener(AgentListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(AgentListener<T> listener){
        listeners.remove(listener);
    }

    public void removeAllListeners(){
        listeners.clear();
    }
    
    public AgentListener<T>[] getListeners(){
        return listeners.getListeners();
    }

    protected void fireStart(long seq) {
        for (AgentListener<T> l : listeners.getListeners()) {
            l.onStarted(seq);
        }
    }

    protected void fireSucceeded(T result, long seq) {
        for (AgentListener<T> l : listeners.getListeners()) {
            l.onSucceeded(result, seq);
        }
    }

    protected void fireFailed(String msg, int errorCode, long seq) {
        for (AgentListener<T> l : listeners.getListeners()) {
            l.onFailed(msg, errorCode, seq);
        }
    }

    public void stop() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
    }
}
