package jvm.mohawk.chatter.server.net.codec;

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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import jvm.mohawk.chatter.server.net.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(final ChannelHandlerContext ctx,
                          final Packet pkt,
                          final ByteBuf out) throws Exception{
        out.writeByte(pkt.opcode().value());
        switch(pkt.opcode().outSize()){
            case VAR_BYTE:
                out.writeByte(pkt.readableBytes());
                break;
            case VAR_SHORT:
                out.writeShort(pkt.readableBytes());
                break;
        }
        out.writeBytes(pkt.buf());
    }
}
