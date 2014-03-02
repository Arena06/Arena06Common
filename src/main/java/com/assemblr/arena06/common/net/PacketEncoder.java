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
        ByteBuf buf = Unpooled.buffer(data.length + 3);
	// write the packet length 
	buf.writeBytes(encodeIntToBytes(data.length + 1));
        // write the packet ID
        buf.writeByte(Packet.packetIdMap.inverse().get(msg.getPacket().getClass()));
	// write the data
        buf.writeBytes(data);
	//buf.writeByte(0x0);
	//buf.writeByte(data[data.length - 1]);
        out.add(buf);
	
	
    }
    public void writeAllBytes(byte[] msg) {
        for (byte b : msg) {
	    System.out.println(b);
	}
        System.out.println();
    }
    private byte[] encodeIntToBytes(int i) {
	byte b1 = (byte)i;
	byte b2 = (byte)(i >> 8);
	return new byte[] {b2, b1};
    }
}
