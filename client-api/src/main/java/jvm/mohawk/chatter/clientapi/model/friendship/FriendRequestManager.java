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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * used for managing friend requests
 */
public class FriendRequestManager {

    private final Map<Integer, FriendRequest> map;

    private final Map<FriendRequest.Type, ObservableList<FriendRequest>> typeMap;

    public FriendRequestManager(){
        map = new LinkedHashMap<>();

        typeMap = new HashMap<>();
    }

    public FriendRequest forId(final int id){
        return map.get(id);
    }

    public ObservableList<FriendRequest> list(final FriendRequest.Type type){
        if(!typeMap.containsKey(type))
            typeMap.put(type, FXCollections.observableArrayList());
        return typeMap.get(type);
    }

    public Optional<FriendRequest> byProfileId(final FriendRequest.Type type,
                                               final int id){
        return list(type).stream()
                .filter(f -> f.targetProfile().id() == id)
                .findFirst();
    }

    public Optional<FriendRequest> byProfileUser(final FriendRequest.Type type,
                                                 final String user){
        return list(type).stream()
                .filter(f -> f.targetProfile().user().equalsIgnoreCase(user))
                .findFirst();
    }

    public void add(final FriendRequest request){
        map.put(request.id(), request);
        list(request.type()).add(request);
    }

    public void remove(final FriendRequest request){
        map.remove(request.id());
        list(request.type()).remove(request);
    }
}
