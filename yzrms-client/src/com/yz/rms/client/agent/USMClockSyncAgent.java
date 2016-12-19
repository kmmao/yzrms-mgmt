/*
 * ClientClockAgent.java
 * 
 * Copyright(c) 2007-2010 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2010-03-09 01:11:50
 */
package com.yz.rms.client.agent;

import com.nazca.io.httprpc.HttpRPC;
import com.nazca.io.httprpc.HttpRPCException;
import com.nazca.usm.service.rpc.TimeRPCService;
import com.yz.rms.client.ClientContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public final class USMClockSyncAgent {
    private static USMClockSyncAgent agent = new USMClockSyncAgent();

    public static synchronized USMClockSyncAgent getInstance() {
        return agent;
    }
    private int count = 0;
    private Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (count % 600 == 0) {
                startGetTime();
            }
            count++;
        }
    });

    public void startSyncTime() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stopSyncTime() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    private void startGetTime() {
        new Thread("GetUSMTime-Thread") {
            @Override
            public void run() {
                try {
                    doGetTime();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private void doGetTime() throws HttpRPCException {
        TimeRPCService ts = HttpRPC.getService(TimeRPCService.class,
                ClientContext.getUsmsServerRPCURL());
        Date d = ts.getTime();
        System.out.println("usm time synced " + d);
    }
}
