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

import java.sql.Timestamp;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.event.EventMapper;
import jvm.mohawk.chatter.clientapi.data.event.types.AddGeneralLogEvent;
import jvm.mohawk.chatter.clientapi.model.log.GeneralLog;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class AddGeneralLogEventMapper implements EventMapper<AddGeneralLogEvent> {

    @Override
    public AddGeneralLogEvent map(final ChatterClient client, final Packet pkt){
        return new AddGeneralLogEvent(
                new GeneralLog(
                        new Timestamp(pkt.readLong()),
                        pkt.readString()
                )
        );
    }
}
