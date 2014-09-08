package com.sillygames.killingSpree.networking.messages;

import java.util.ArrayList;

public class ConnectMessage {
    
    public ArrayList<String> hosts;

    public ConnectMessage() {
        hosts = new ArrayList<String>();
    }
    
    public void insertNewHost(String host) {
        hosts.add(host);
    }
}