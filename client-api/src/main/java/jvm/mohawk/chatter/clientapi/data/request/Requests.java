package jvm.mohawk.chatter.clientapi.data.request;

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

import jvm.mohawk.chatter.clientapi.data.request.types.AnswerFriendRequest;
import jvm.mohawk.chatter.clientapi.data.request.types.CancelFriendRequest;
import jvm.mohawk.chatter.clientapi.data.request.types.CreateChatroom;
import jvm.mohawk.chatter.clientapi.data.request.types.CreateFriendRequest;
import jvm.mohawk.chatter.clientapi.data.request.types.GetChatroomByName;
import jvm.mohawk.chatter.clientapi.data.request.types.GetProfileByUser;
import jvm.mohawk.chatter.clientapi.data.request.types.JoinChatroom;
import jvm.mohawk.chatter.clientapi.data.request.types.LeaveChatroom;
import jvm.mohawk.chatter.clientapi.data.request.types.RemoveFriend;
import jvm.mohawk.chatter.clientapi.data.request.types.SendChatroomMessage;
import jvm.mohawk.chatter.clientapi.data.request.types.SendFriendMessage;
import jvm.mohawk.chatter.clientapi.data.request.types.UpdateProfileStatusRequest;
import jvm.mohawk.chatter.clientapi.model.Status;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequest;
import jvm.mohawk.chatter.clientapi.model.friendship.Friendship;
import jvm.mohawk.chatter.clientapi.model.profile.Profile;

/**
 * contains various different static methods for instantiating the requests that can be sent
 */
public final class Requests {

    private Requests(){}

    public static AnswerFriendRequest answerFriendRequest(final FriendRequest friendRequest,
                                                          final FriendRequest.Answer answer){
        return new AnswerFriendRequest(friendRequest, answer);
    }

    public static CancelFriendRequest cancelFriendRequest(final FriendRequest friendRequest){
        return new CancelFriendRequest(friendRequest);
    }

    public static CreateFriendRequest createFriendRequest(final Profile targetProfile){
        return new CreateFriendRequest(targetProfile);
    }

    public static RemoveFriend removeFriend(final Friendship friendship){
        return new RemoveFriend(friendship);
    }

    public static SendFriendMessage sendFriendMessage(final Friendship friendship,
                                                      final String text){
        return new SendFriendMessage(friendship, text);
    }

    public static GetProfileByUser getProfileByUser(final String profileUser){
        return new GetProfileByUser(profileUser);
    }

    public static CreateChatroom createChatroom(final String name,
                                                final String desc){
        return new CreateChatroom(name, desc);
    }

    public static JoinChatroom joinChatroom(final Chatroom chatroom){
        return new JoinChatroom(chatroom);
    }

    public static LeaveChatroom leaveChatroom(final Chatroom chatroom){
        return new LeaveChatroom(chatroom);
    }

    public static GetChatroomByName getChatroomByName(final String chatroomName){
        return new GetChatroomByName(chatroomName);
    }

    public static SendChatroomMessage sendChatroomMessage(final Chatroom chatroom,
                                                          final String text){
        return new SendChatroomMessage(chatroom, text);
    }

    public static UpdateProfileStatusRequest updateProfileStatus(final Status status){
        return new UpdateProfileStatusRequest(status);
    }
}
