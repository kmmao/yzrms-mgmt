/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yz.rms.client.agent;

import com.nazca.io.httpdao.HttpClientContext;
import com.nazca.io.httprpc.HttpRPC;
import com.yz.rms.client.ClientContext;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public class USMSessionKeeperAgent {

    private USMSessionKeeperAgent() {
    }
    private static USMSessionKeeperAgent agent = null;

    public synchronized static USMSessionKeeperAgent getAgent() {
        if (agent == null) {
            agent = new USMSessionKeeperAgent();
        }
        return agent;
    }

    public void startKeepSession() {
        USMClockSyncAgent.getInstance().startSyncTime();
    }

    public void stopKeepSession() {
        USMClockSyncAgent.getInstance().stopSyncTime();
        HttpClientContext context = HttpRPC.getClientContext(ClientContext.getUsmsServerRPCURL());
        context.setUserHttpSession("");
        context.setUserToken("");
    }
}
