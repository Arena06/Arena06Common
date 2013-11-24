package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Method;
import java.util.List;


public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf buf = msg.content();
        byte id = buf.readByte();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        
        Class<? extends Packet> clazz = Packet.packetIdMap.get(id);
        if (clazz == null) {
            throw new RuntimeException("Encountered illegal packet type: " + id);
        }
        Method decoder = clazz.getMethod("decode", byte[].class);
        Packet packet = (Packet) decoder.invoke(null, data);
        out.add(new AddressedPacket(packet, msg.sender(), msg.recipient()));
    }
    
}
