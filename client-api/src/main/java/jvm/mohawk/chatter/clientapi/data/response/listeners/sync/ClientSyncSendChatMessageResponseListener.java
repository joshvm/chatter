package jvm.mohawk.chatter.clientapi.data.response.listeners.sync;

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
import jvm.mohawk.chatter.clientapi.data.event.types.ChatroomMessageEvent;
import jvm.mohawk.chatter.clientapi.data.event.types.FriendMessageEvent;
import jvm.mohawk.chatter.clientapi.data.request.types.SendChatMessage;
import jvm.mohawk.chatter.clientapi.data.response.Response;
import jvm.mohawk.chatter.clientapi.data.response.ResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.SendChatroomMessageResponse;
import jvm.mohawk.chatter.clientapi.data.response.types.SendFriendMessageResponse;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class ClientSyncSendChatMessageResponseListener implements ResponseListener<SendChatMessage, Response> {

    @Override
    public void onResponse(final ChatterClient client, final SendChatMessage req, final Response resp){
        final ChatMessage msg = new ChatMessage(-1, Utils.timestamp(), client.profile(), req.text());
        switch(req.conversable().messageType()){
            case FRIENDSHIP:
                if(resp.code() != SendFriendMessageResponse.Code.SUCCESS)
                    break;
                final Friendship friendship = (Friendship) req.conversable();
                client.eventHandler().fireAll(client, new FriendMessageEvent(friendship, msg));
                break;
            case CHATROOM:
                if(resp.code() != SendChatroomMessageResponse.Code.SUCCESS)
                    break;
                final Chatroom chatroom = (Chatroom) req.conversable();
                client.eventHandler().fireAll(client, new ChatroomMessageEvent(chatroom, msg));
                break;
        }
    }
}
