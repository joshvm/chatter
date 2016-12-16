package jvm.mohawk.chatter.clientapi.model.friendship;

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
import jvm.mohawk.chatter.clientapi.model.profile.Profile;
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * this class represents a friend request between the client and another problem
 * there are 2 types of friend requests:
 * 1 - friend requests that you send (type PENDING, type ANSWERABLE for target profile)
 * 2 - friend requests that you receive (type ANSWERABLE, type PENDING for target profile)
 * no need to store the other profile id because in order to get an instance of this class
 * you must have an instance of chatter client.
 */
public class FriendRequest {

    public enum Type {

        PENDING,
        ANSWERABLE
    }

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

    private final int id;
    private final Type type;
    private final Profile targetProfile;

    public FriendRequest(final int id,
                            final Type type,
                         final Profile targetProfile){
        this.id = id;
        this.type = type;
        this.targetProfile = targetProfile;
    }

    public int id(){
        return id;
    }

    public Type type(){
        return type;
    }

    public boolean isPending(){
        return type == Type.PENDING;
    }

    public boolean isAnswerable(){
        return type == Type.ANSWERABLE;
    }

    public Profile targetProfile(){
        return targetProfile;
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof FriendRequest))
            return false;
        final FriendRequest fr = (FriendRequest) obj;
        return id == fr.id;
    }
}
