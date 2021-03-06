package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Method;
import java.util.List;


public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    
    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf buf, List<Object> out) throws Exception {
        byte id = buf.readByte();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        
        Class<? extends Packet> clazz = Packet.packetIdMap.get(id);
        if (clazz == null) {
            throw new RuntimeException("Encountered illegal packet type: " + id);
        }
        
        Method decoder = clazz.getMethod("decode", byte[].class);
        Packet packet = (Packet) decoder.invoke(null, (Object) data);
        out.add(packet);
    }
    
}
