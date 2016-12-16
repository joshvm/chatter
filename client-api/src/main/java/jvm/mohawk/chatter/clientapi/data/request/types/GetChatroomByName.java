package jvm.mohawk.chatter.clientapi.data.request.types;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import jvm.mohawk.chatter.clientapi.data.request.Request;
import jvm.mohawk.chatter.clientapi.data.request.RequestType;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class GetChatroomByName extends Request {

    private final String chatroomName;

    public GetChatroomByName(final String chatroomName){
        super(RequestType.GET_CHATROOM_BY_NAME);
        this.chatroomName = chatroomName;
    }

    public String chatroomName(){
        return chatroomName;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.GET_CHATROOM_BY_NAME)
                .writeString(chatroomName);
    }
}
