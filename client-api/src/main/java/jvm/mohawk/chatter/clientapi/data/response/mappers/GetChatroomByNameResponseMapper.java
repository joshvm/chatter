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
import jvm.mohawk.chatter.clientapi.data.response.types.GetChatroomByNameResponse;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class GetChatroomByNameResponseMapper implements ResponseMapper<GetChatroomByNameResponse> {

    @Override
    public GetChatroomByNameResponse map(final ChatterClient client, final Packet pkt){
        final GetChatroomByNameResponse.Code code = GetChatroomByNameResponse.Code.forId(pkt.readUnsignedByte());
        final Chatroom resultChatroom = code == GetChatroomByNameResponse.Code.SUCCESS
                ? Chatroom.deserialize(pkt) : null;
        return new GetChatroomByNameResponse(
                code,
                resultChatroom
        );
    }
}
