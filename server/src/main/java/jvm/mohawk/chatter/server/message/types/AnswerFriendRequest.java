package jvm.mohawk.chatter.server.message.types;

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

import java.sql.Timestamp;
import java.util.Map;
import jvm.mohawk.chatter.server.message.Message;
import jvm.mohawk.chatter.server.message.MessageType;
import jvm.mohawk.chatter.server.utils.Utils;

public class AnswerFriendRequest extends Message {

    public enum Answer {

        ACCEPT(1),
        DECLINE(2);

        private static final Map<Integer, Answer> MAP = Utils.map(values(), Answer::id);

        private final int id;

        Answer(final int id){
            this.id = id;
        }

        public int id(){
            return id;
        }

        public static Answer forId(final int id){
            return MAP.get(id);
        }
    }

    private final int friendRequestId;
    private final int answerId;

    public AnswerFriendRequest(final Timestamp timestamp,
                               final int friendRequestId,
                               final int answerId){
        super(MessageType.ANSWER_FRIEND_REQUEST, timestamp);
        this.friendRequestId = friendRequestId;
        this.answerId = answerId;
    }

    public int friendRequestId(){
        return friendRequestId;
    }

    public int answerId(){
        return answerId;
    }
}
