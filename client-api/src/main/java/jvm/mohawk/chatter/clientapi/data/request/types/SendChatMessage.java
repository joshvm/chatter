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
import jvm.mohawk.chatter.clientapi.model.conversation.Conversable;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class SendChatMessage extends Request {

    private final Conversable conversable;
    private final String text;

    protected SendChatMessage(final Conversable conversable,
                              final String text){
        super(RequestType.SEND_CHAT_MESSAGE);
        this.conversable = conversable;
        this.text = text;
    }

    public Conversable conversable(){
        return conversable;
    }

    public String text(){
        return text;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.SEND_CHAT_MESSAGE)
                .writeByte(conversable.messageType().id())
                .writeInt(conversable.id())
                .writeString(text);
    }
}
