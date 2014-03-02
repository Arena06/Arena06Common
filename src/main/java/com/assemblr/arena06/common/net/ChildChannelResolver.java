/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.net.InetSocketAddress;
import java.util.Map;



public class ChildChannelResolver extends ChannelOutboundHandlerAdapter {
    private Map<InetSocketAddress, Channel> resolutionTable;
    
    public ChildChannelResolver(Map<InetSocketAddress, Channel> resolutionTable) {
        this.resolutionTable = resolutionTable;
    }
    
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
	       if (msg instanceof AddressedData) {
            Channel c = resolutionTable.get(((AddressedData) msg).getDestination());
            if (c != null) {
                c.writeAndFlush(msg);
            } else {
            }
        } else {
            super.write(ctx, msg, promise);
        }
    }
    
}
