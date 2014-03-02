package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;


public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    
    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() < 2)
            return;
        int size = buf.getUnsignedShort(buf.readerIndex());
        if (buf.readableBytes() < size)
            return;
        buf.readUnsignedShort();
        
        byte id = buf.readByte();
        byte[] data = new byte[size - 1];
        buf.readBytes(data);
        decodeFullPacket(chc, id, data, out);
    }
    
    private String byteBufToString(ByteBuf buf) {
        byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), data);
        return Arrays.toString(data);
    }
    
    protected void decodeFullPacket(ChannelHandlerContext ctx, byte id, byte[] data, List<Object> out) throws Exception {
        Class<? extends Packet> clazz = Packet.packetIdMap.get(id);
        if (clazz == null) {
            throw new RuntimeException("Encountered illegal packet type: " + id);
        }
        Method decoder = clazz.getMethod("decode", byte[].class);
        Packet packetData = (Packet) decoder.invoke(null, data);
        out.add(new AddressedPacket(packetData, (InetSocketAddress) ctx.channel().remoteAddress(), (InetSocketAddress) ctx.channel().localAddress()));
    }
    
}
