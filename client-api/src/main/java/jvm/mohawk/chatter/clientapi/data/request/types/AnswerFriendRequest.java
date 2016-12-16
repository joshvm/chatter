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
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequest;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;

public class AnswerFriendRequest extends Request {

    private final FriendRequest friendRequest;
    private final FriendRequest.Answer answer;

    public AnswerFriendRequest(final FriendRequest friendRequest,
                               final FriendRequest.Answer answer){
        super(RequestType.ANSWER_FRIEND_REQUEST);
        this.friendRequest = friendRequest;
        this.answer = answer;
    }

    public FriendRequest friendRequest(){
        return friendRequest;
    }

    public FriendRequest.Answer answer(){
        return answer;
    }

    @Override
    public Packet serialize(){
        return new Packet(Opcode.ANSWER_FRIEND_REQUEST)
                .writeInt(friendRequest.id())
                .writeByte(answer.id());
    }
}
