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
import jvm.mohawk.chatter.clientapi.model.Status;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class UpdateProfileStatusRequest extends Request {

    private final Status status;

    public UpdateProfileStatusRequest(final Status status){
        super(RequestType.UPDATE_PROFILE_STATUS);
        this.status = status;
    }

    public Status status(){
        return status;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.UPDATE_PROFILE_STATUS)
                .writeByte(status.id());
    }
}
