package jvm.mohawk.chatter.server.message;

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

import io.netty.channel.ChannelHandlerContext;

public interface MessageListener<M extends Message> {

    void onMessage(final ChannelHandlerContext ctx, final M msg) throws Exception;

    default void onError(final ChannelHandlerContext ctx, final M msg, final Throwable err){
        err.printStackTrace();
    }
}
