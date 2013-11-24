package com.assemblr.arena06.common.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;


public class DataDecoder extends MessageToMessageDecoder<AddressedPacket> {
    
    @Override
    protected void decode(ChannelHandlerContext ctx, AddressedPacket msg, List<Object> out) throws Exception {
        out.add(new AddressedData(msg.getPacket().getData(), msg.getSender(), msg.getDestination()));
    }
    
}
