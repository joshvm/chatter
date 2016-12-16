package jvm.mohawk.chatter.clientapi.data.response.types;

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

import java.util.Map;
import jvm.mohawk.chatter.clientapi.data.request.RequestType;
import jvm.mohawk.chatter.clientapi.data.response.Response;
import jvm.mohawk.chatter.clientapi.model.chatroom.Chatroom;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class GetChatroomByNameResponse extends Response<GetChatroomByNameResponse.Code> {

    public enum Code implements Response.Code {

        INVALID_NAME(1),
        CHATROOM_NOT_FOUND(2),
        SUCCESS(100),
        ERROR(101),
        UNKNOWN(102);

        private static final Map<Integer, Code> MAP = Utils.map(values(), Code::id);

        private final int id;

        Code(final int id){
            this.id = id;
        }

        @Override
        public int id(){
            return id;
        }

        public static Code forId(final int id){
            return MAP.getOrDefault(id, UNKNOWN);
        }
    }

    private final Chatroom resultChatroom;

    public GetChatroomByNameResponse(final Code code,
                                     final Chatroom resultChatroom){
        super(RequestType.GET_CHATROOM_BY_NAME, code);
        this.resultChatroom = resultChatroom;
    }

    public Chatroom resultChatroom(){
        return resultChatroom;
    }
}
