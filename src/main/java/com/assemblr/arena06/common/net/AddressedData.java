package com.assemblr.arena06.common.net;

import java.net.InetSocketAddress;
import java.util.Map;


public class AddressedData {
    
    private final Map<String, Object> data;
    private final InetSocketAddress sender;
    private final InetSocketAddress destination;
    
    public AddressedData(Map<String, Object> data, InetSocketAddress sender, InetSocketAddress destination) {
        this.data = data;
        this.sender = sender;
        this.destination = destination;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public InetSocketAddress getSender() {
        return sender;
    }
    
    public InetSocketAddress getDestination() {
        return destination;
    }
    
}
