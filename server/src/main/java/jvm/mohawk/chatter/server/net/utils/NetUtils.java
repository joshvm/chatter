package jvm.mohawk.chatter.server.net.utils;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;

public final class NetUtils {

    private NetUtils(){}

    public static String ip(final ChannelHandlerContext ctx){
        return ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString();
    }

    public static ChannelFuture writeLater(final ChannelHandlerContext ctx,
                                           final Packet pkt){
        return ctx.write(pkt);
    }

    public static ChannelFuture write(final ChannelHandlerContext ctx,
                                         final Packet pkt){
        return ctx.writeAndFlush(pkt);
    }

    public static ChannelFuture writeResponse(final ChannelHandlerContext ctx,
                                              final Opcode opcode,
                                              final int response){
        return write(ctx, new Packet(opcode).writeByte(response));
    }
}
