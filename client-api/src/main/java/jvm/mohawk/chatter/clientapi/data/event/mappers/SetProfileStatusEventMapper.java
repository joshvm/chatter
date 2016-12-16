package jvm.mohawk.chatter.clientapi.data.event.mappers;

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

import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.event.EventMapper;
import jvm.mohawk.chatter.clientapi.data.event.types.SetProfileStatusEvent;
import jvm.mohawk.chatter.clientapi.model.Status;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class SetProfileStatusEventMapper implements EventMapper<SetProfileStatusEvent> {

    @Override
    public SetProfileStatusEvent map(final ChatterClient client, final Packet pkt){
        final int profileId = pkt.readInt();
        final Status status = Status.forId(pkt.readUnsignedByte());
        if(status == null)
            return null;
        return new SetProfileStatusEvent(
                profileId,
                status
        );
    }
}
