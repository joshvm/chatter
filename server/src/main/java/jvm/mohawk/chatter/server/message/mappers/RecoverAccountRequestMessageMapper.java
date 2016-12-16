package jvm.mohawk.chatter.server.message.mappers;

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

import jvm.mohawk.chatter.server.message.MessageMapper;
import jvm.mohawk.chatter.server.message.types.RecoverAccountRequestMessage;
import jvm.mohawk.chatter.server.net.Packet;

public class RecoverAccountRequestMessageMapper implements MessageMapper<RecoverAccountRequestMessage> {

    @Override
    public RecoverAccountRequestMessage map(final Packet pkt){
        return new RecoverAccountRequestMessage(
                pkt.readString()
        );
    }
}
