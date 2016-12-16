package jvm.mohawk.chatter.clientapi.data.response.mappers;

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
import jvm.mohawk.chatter.clientapi.data.response.ResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.types.AnswerFriendRequestResponse;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class AnswerFriendRequestResponseMapper implements ResponseMapper<AnswerFriendRequestResponse> {

    @Override
    public AnswerFriendRequestResponse map(final ChatterClient client, final Packet pkt){
        return new AnswerFriendRequestResponse(
                AnswerFriendRequestResponse.Code.forId(pkt.readUnsignedByte())
        );
    }
}
