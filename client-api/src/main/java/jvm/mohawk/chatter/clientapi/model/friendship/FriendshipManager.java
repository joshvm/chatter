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
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * used for managing friendships
 */
public class FriendshipManager {

    private final Map<Integer, Friendship> idMap;
    private final ObservableList<Friendship> list;

    public FriendshipManager(){
        idMap = new HashMap<>();
        list = FXCollections.observableArrayList();
    }

    public ObservableList<Friendship> list(){
        return list;
    }

    public Friendship forId(final int friendshipId){
        return idMap.get(friendshipId);
    }

    public Optional<Friendship> byProfileUser(final String user){
        return list.stream()
                .filter(f -> f.profile().user().equalsIgnoreCase(user))
                .findFirst();
    }

    public Optional<Friendship> byProfileId(final int id){
        return list.stream()
                .filter(f -> f.profile().id() == id)
                .findFirst();
    }

    public void add(final Friendship friendship){
        idMap.put(friendship.id(), friendship);
        list.add(friendship);
    }

    public void remove(final Friendship friendship){
        idMap.remove(friendship.id());
        list.remove(friendship);
    }
}
