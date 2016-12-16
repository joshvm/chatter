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

public class CreateChatroom extends Request {

    private final String name;
    private final String desc;

    public CreateChatroom(final String name,
                          final String desc){
        super(RequestType.CREATE_CHATROOM);
        this.name = name;
        this.desc = desc;
    }

    public String name(){
        return name;
    }

    public String desc(){
        return desc;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.CREATE_CHATROOM)
                .writeString(name)
                .writeString(desc);
    }
}
