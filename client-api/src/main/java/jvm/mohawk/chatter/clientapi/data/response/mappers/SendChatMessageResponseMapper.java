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
import jvm.mohawk.chatter.clientapi.data.request.RequestType;
import jvm.mohawk.chatter.clientapi.data.request.types.SendChatMessage;
import jvm.mohawk.chatter.clientapi.data.response.Response;
import jvm.mohawk.chatter.clientapi.data.response.ResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.types.SendChatroomMessageResponse;
import jvm.mohawk.chatter.clientapi.data.response.types.SendFriendMessageResponse;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class SendChatMessageResponseMapper implements ResponseMapper {

    @Override
    public Response map(final ChatterClient client, final Packet pkt){
        final int codeId = pkt.readUnsignedByte();
        final SendChatMessage req = (SendChatMessage) client.pendingRequest(RequestType.SEND_CHAT_MESSAGE);
        if(req == null)
            return null;
        switch(req.conversable().messageType()){
            case FRIENDSHIP:
                return new SendFriendMessageResponse(SendFriendMessageResponse.Code.forId(codeId));
            case CHATROOM:
                return new SendChatroomMessageResponse(SendChatroomMessageResponse.Code.forId(codeId));
            default:
                return null;
        }
    }
}
