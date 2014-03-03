package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<AddressedPacket> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AddressedPacket msg, List<Object> out) throws Exception {
        byte[] data = msg.getPacket().encode();
        ByteBuf buf = Unpooled.buffer(data.length + 1);
        // write the packet ID
        buf.writeByte(Packet.packetIdMap.inverse().get(msg.getPacket().getClass()));
        // write the data
        buf.writeBytes(data);
        out.add(buf);
    }
    
}
