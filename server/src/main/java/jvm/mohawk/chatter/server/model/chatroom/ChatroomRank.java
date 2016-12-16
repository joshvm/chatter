package jvm.mohawk.chatter.server.model.chatroom;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import jvm.mohawk.chatter.server.model.Rank;

public class ChatroomRank {

    private final int chatroomId;
    private final int profileId;
    private final Rank rank;

    public ChatroomRank(final int chatroomId,
                        final int profileId,
                        final Rank rank){
        this.chatroomId = chatroomId;
        this.profileId = profileId;
        this.rank = rank;
    }

    public int chatroomId(){
        return chatroomId;
    }

    public int profileId(){
        return profileId;
    }

    public Rank rank(){
        return rank;
    }
}
