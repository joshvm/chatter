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
import jvm.mohawk.chatter.clientapi.data.event.Event;
import jvm.mohawk.chatter.clientapi.data.event.EventMapper;
import jvm.mohawk.chatter.clientapi.data.event.types.ChatroomMessageEvent;
import jvm.mohawk.chatter.clientapi.data.event.types.FriendMessageEvent;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.chatroom.ChatroomUser;
import jvm.mohawk.chatter.clientapi.model.conversation.ChatMessage;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class ChatMessageEventMapper implements EventMapper {

    @Override
    public Event map(final ChatterClient client, final Packet pkt){
        final int id = pkt.readInt();
        final Timestamp timestamp = new Timestamp(pkt.readLong());
        final ChatMessage.Type type = ChatMessage.Type.forId(pkt.readUnsignedByte());
        final int conversableId = pkt.readInt();
        final int senderId = pkt.readInt();
        final String text = pkt.readString();
        if(type == null)
            return null;
        switch(type){
            case FRIENDSHIP:
                return friendMessageEvent(client, id, timestamp, conversableId, senderId, text);
            case CHATROOM:
                return chatroomMessageEvent(client, id, timestamp, conversableId, senderId, text);
            default:
                return null;
        }
    }

    private FriendMessageEvent friendMessageEvent(final ChatterClient client,
                                                  final int id,
                                                  final Timestamp timestamp,
                                                  final int friendshipId,
                                                  final int senderId,
                                                  final String text){
        final Friendship friendship = client.friendshipManager().forId(friendshipId);
        System.out.println("friend message event friendshiP: " + friendship);
        if(friendship == null)
            return null;
        final Profile sender = senderId == client.profile().id() ?
                client.profile() : friendship.profile();
        System.out.println("friend message event sender: " + sender);
        if(sender == null)
            return null;
        return new FriendMessageEvent(
                friendship,
                new ChatMessage(id, timestamp, sender, text)
        );
    }

    private ChatroomMessageEvent chatroomMessageEvent(final ChatterClient client,
                                                      final int id,
                                                      final Timestamp timestamp,
                                                      final int chatroomId,
                                                      final int senderId,
                                                      final String text){
        final Chatroom chatroom = client.chatroomManager().forIdOrName(chatroomId);
        System.out.printf("chatroom_id: %d | chatroom: %s%n", chatroomId, chatroom);
        if(chatroom == null)
            return null;
        Profile sender = null;
        if(senderId == client.profile().id())
            sender = client.profile();
        else{
            final ChatroomUser user = chatroom.users().forIdOrUser(senderId);
            if(user != null)
                sender = user.profile();
        }
        System.out.printf("sender_id: %d | sender: %s%n", senderId, sender);
        if(sender == null)
            return null;
        return new ChatroomMessageEvent(
                chatroom,
                new ChatMessage(id, timestamp, sender, text)
        );
    }
}
