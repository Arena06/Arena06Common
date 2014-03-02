package com.assemblr.arena06.common.net;

import com.assemblr.arena06.common.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;


public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    int bytesRemainingInPacket = 0;
    byte firstLengthIndicator = 0;
    ByteBuf packet;
    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf buf, List<Object> out) throws Exception {
	for (byte bi = buf.readByte(); buf.isReadable(); bi = buf.readByte()) {
	    if (bytesRemainingInPacket == 0) {
		firstLengthIndicator = bi;
		bytesRemainingInPacket = -1;
	    } else if (bytesRemainingInPacket == -1) {
		bytesRemainingInPacket =  ((firstLengthIndicator << 8) + bi);
		firstLengthIndicator = 0;
		packet = chc.alloc().buffer(bytesRemainingInPacket);
	    } else if (bytesRemainingInPacket > 0) {
		packet.writeByte(bi);
		bytesRemainingInPacket--;
		if (bytesRemainingInPacket == 0) {
		    decodeFullPacket(chc, packet, out);
		    packet.release();
		}
	    }
	}
    }
    
    
    
    protected void decodeFullPacket(ChannelHandlerContext ctx, ByteBuf packet, List<Object> out) throws Exception {
        byte id = packet.readByte();
        byte[] data = new byte[packet.readableBytes()];
        packet.readBytes(data);
        
        Class<? extends Packet> clazz = Packet.packetIdMap.get(id);
        if (clazz == null) {
            throw new RuntimeException("Encountered illegal packet type: " + id);
        }
        Method decoder = clazz.getMethod("decode", byte[].class);
        Packet packetData = (Packet) decoder.invoke(null, data);
        out.add(new AddressedPacket(packetData, (InetSocketAddress) ctx.channel().remoteAddress(), (InetSocketAddress) ctx.channel().localAddress()));
    }
    
}
