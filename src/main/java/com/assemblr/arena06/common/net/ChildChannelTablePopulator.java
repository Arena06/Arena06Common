package com.assemblr.arena06.common.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.Map;


public class ChildChannelTablePopulator extends ChannelInboundHandlerAdapter {
    
    private final Map<InetSocketAddress, Channel> resolutionTable;
    
    public ChildChannelTablePopulator(Map<InetSocketAddress, Channel> resolutionTable) {
        this.resolutionTable = resolutionTable;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof AddressedData) {
            resolutionTable.put(((AddressedData) msg).getSender(), ctx.channel());
        }
        super.channelRead(ctx, msg);
    }
    
}
