package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet01JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;


public class DataEncoder extends MessageToMessageEncoder<AddressedData> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AddressedData msg, List<Object> out) throws Exception {
        out.add(new AddressedPacket(new Packet01JSON(msg.getData()), msg.getSender(), msg.getDestination()));
    }
    
}
