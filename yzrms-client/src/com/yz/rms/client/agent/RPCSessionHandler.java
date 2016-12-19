/*
 * Copyright(c) 2007-2010 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2010-06-15 19:01:35
 */
package com.yz.rms.client.agent;

import com.nazca.io.httpdao.HttpClientContext;
import com.nazca.io.httpdao.HttpClientContextListener;
import com.nazca.io.httprpc.HttpRPC;
import com.nazca.ui.UIUtilities;
import com.yz.rms.client.ClientContext;

/**
 * RPC会话处理类
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public final class RPCSessionHandler {
    private static boolean listenerAdded = false;

    private RPCSessionHandler() {
    }
    private static HttpClientContextListener lis = new HttpClientContextListener() {
        @Override
        public void onHttpSessionTimeOut(HttpClientContext context) {
            System.out.println("*** session timeout " + context.getUrl() + " " + context.getUserHttpSession());
        }

        @Override
        public void onSessionTokenNotValid(HttpClientContext context, String msg, int code) {
            System.out.println("*** session notvalid " + context.getUrl() + " " + context.getUserHttpSession());
            UIUtilities.errorDlg(null, "您的会话已超时，请重新登录");
            System.exit(0);
        }

        @Override
        public void onHttpSessionCreated(HttpClientContext context) {
            System.out.println("*** session crated " + context.getUrl() + " " + context.getUserHttpSession());
        }

        @Override
        public void startRebinding() {
        }

        @Override
        public void onBindingFailed(String msg, int code) {
            System.out.println(msg + ", error code = " + code);
        }

        public void onBidingSuc(HttpClientContext context) {
        }
    };

    /**
     * 开始监听session变化事件
     */
    public synchronized static void startListenSession() {
        if (!listenerAdded) {
            HttpClientContext ctx = HttpRPC.getClientContext(ClientContext.getSysServerRPCURL());
            ctx.addHttpContextListener(lis);
            listenerAdded = true;
        }
    }

    /**
     * 结束监听session变化事件
     */
    public synchronized static void stopListenSession() {
        HttpClientContext ctx = HttpRPC.getClientContext(ClientContext.getSysServerRPCURL());
        ctx.removeHttpContextListener(lis);
        listenerAdded = false;
    }
}
