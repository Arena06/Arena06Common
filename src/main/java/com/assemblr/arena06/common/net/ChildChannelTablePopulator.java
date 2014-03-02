/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.assemblr.arena06.common.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 *
 * @author Henry
 */
public class ChildChannelTablePopulator extends ChannelInboundHandlerAdapter {
    
    public Map<InetSocketAddress, Channel> resolutionTable;

    public ChildChannelTablePopulator(Map<InetSocketAddress, Channel> resolutionTable) {
	this.resolutionTable = resolutionTable;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	if (msg instanceof AddressedData) {
	    resolutionTable.put(((AddressedData)msg).getSender(), ctx.channel());
	}
	super.channelRead(ctx, msg);
    }

    

}
