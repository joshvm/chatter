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
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;
import jvm.mohawk.chatter.server.net.Buffer;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;

public class PacketDecoder extends ReplayingDecoder {

    @Override
    protected void decode(final ChannelHandlerContext ctx,
                          final ByteBuf in,
                          final List<Object> out) throws Exception{
        while(in.readableBytes() > 0){
            final Opcode opcode = Opcode.forValue(in.readUnsignedByte());
            if(opcode == null){
                checkpoint();
                continue;
            }
            System.out.println("packet decoder opcode: " + opcode);
            int length = opcode.inLength();
            switch(opcode.inSize()){
                case VAR_SHORT:
                    if(in.readableBytes() >= 2)
                        length = in.readUnsignedShort();
                    break;
                case VAR_BYTE:
                    if(in.readableBytes() >= 1)
                        length = in.readUnsignedByte();
                    break;
            }
            System.out.println("length: " + length);
            System.out.println("available: " + in.readableBytes());
            if(length < 0 || in.readableBytes() < length)
                continue;
            out.add(new Packet(opcode, new Buffer(in.readBytes(length))));
            checkpoint();
        }
    }
}
