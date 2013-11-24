package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import java.net.InetSocketAddress;


public class AddressedPacket {
    
    private final Packet packet;
    private final InetSocketAddress sender;
    private final InetSocketAddress destination;
    
    public AddressedPacket(Packet packet, InetSocketAddress sender, InetSocketAddress destination) {
        this.packet = packet;
        this.sender = sender;
        this.destination = destination;
    }
    
    public Packet getPacket() {
        return packet;
    }
    
    public InetSocketAddress getSender() {
        return sender;
    }
    
    public InetSocketAddress getDestination() {
        return destination;
    }
    
}
