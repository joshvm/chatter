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
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.utils.Utils;

public class GetProfileByUserResponse extends Response{

    public enum Code implements Response.Code {

        INVALID_USER(1),
        USER_NOT_FOUND(2),
        CANT_SEARCH_YOURSELF(3),
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

    private final Profile resultProfile;

    public GetProfileByUserResponse(final Code code,
                                    final Profile resultProfile){
        super(RequestType.GET_PROFILE_BY_USER, code);
        this.resultProfile = resultProfile;
    }

    public Profile resultProfile(){
        return resultProfile;
    }
}
