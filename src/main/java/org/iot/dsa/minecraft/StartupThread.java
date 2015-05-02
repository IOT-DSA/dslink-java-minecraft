package org.iot.dsa.minecraft;

import org.dsa.iot.dslink.DSLinkFactory;

public class StartupThread implements Runnable {

    private String brokerUrl;

    public StartupThread(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    @Override
    public void run() {
        String[] args = new String[]{"-b", brokerUrl};
        DSLinkFactory.startResponder("minecraft", args, DSMResponder.instance());
    }
}
