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
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class RemoveFriend extends Request {

    private final Friendship friendship;

    public RemoveFriend(final Friendship friendship){
        super(RequestType.REMOVE_FRIEND);
        this.friendship = friendship;
    }

    public Friendship friendship(){
        return friendship;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.REMOVE_FRIEND)
                .writeInt(friendship.id());
    }
}
