package jvm.mohawk.chatter.clientapi.data.event;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.event.listeners.AddChatroomEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.AddChatroomUserEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.AddFriendEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.AddFriendRequestEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.ChatroomMessageEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.FriendMessageEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.RemoveChatroomEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.RemoveChatroomUserEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.RemoveFriendEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.RemoveFriendRequestEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddChatroomEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddChatroomUserEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddCommandMessageEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddFriendEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddFriendRequestEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddGeneralLogEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncAddLoginLogEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncChatroomMessageEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncFriendMessageEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncRemoveChatroomEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncRemoveChatroomUserEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncRemoveFriendEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncRemoveFriendRequestEventListener;
import jvm.mohawk.chatter.clientapi.data.event.listeners.sync.ClientSyncSetProfileStatusEventListener;

public class EventHandler {

    private final Map<EventType, Consumer<Runnable>> clientSyncWorkerMap;
    private final Map<EventType, EventListener> clientSyncListenerMap;

    private final Map<EventType, List<EventListener>> listenerMap;
    private final List<EventListener> globalListeners;

    public EventHandler(){
        clientSyncWorkerMap = new HashMap<>();
        clientSyncListenerMap = new HashMap<>();
        listenerMap = new HashMap<>();
        globalListeners = new ArrayList<>();

        clientSyncListenerMap.put(EventType.ADD_FRIEND, new ClientSyncAddFriendEventListener());
        clientSyncListenerMap.put(EventType.REMOVE_FRIEND, new ClientSyncRemoveFriendEventListener());
        clientSyncListenerMap.put(EventType.ADD_FRIEND_REQUEST, new ClientSyncAddFriendRequestEventListener());
        clientSyncListenerMap.put(EventType.REMOVE_FRIEND_REQUEST, new ClientSyncRemoveFriendRequestEventListener());
        clientSyncListenerMap.put(EventType.FRIEND_MESSAGE, new ClientSyncFriendMessageEventListener());
        clientSyncListenerMap.put(EventType.ADD_CHATROOM, new ClientSyncAddChatroomEventListener());
        clientSyncListenerMap.put(EventType.ADD_CHATROOM_USER, new ClientSyncAddChatroomUserEventListener());
        clientSyncListenerMap.put(EventType.REMOVE_CHATROOM, new ClientSyncRemoveChatroomEventListener());
        clientSyncListenerMap.put(EventType.REMOVE_CHATROOM_USER, new ClientSyncRemoveChatroomUserEventListener());
        clientSyncListenerMap.put(EventType.CHATROOM_MESSAGE, new ClientSyncChatroomMessageEventListener());
        clientSyncListenerMap.put(EventType.ADD_COMMAND_MESSAGE, new ClientSyncAddCommandMessageEventListener());
        clientSyncListenerMap.put(EventType.SET_PROFILE_STATUS, new ClientSyncSetProfileStatusEventListener());
        clientSyncListenerMap.put(EventType.ADD_LOGIN_LOG, new ClientSyncAddLoginLogEventListener());
        clientSyncListenerMap.put(EventType.ADD_GENERAL_LOG, new ClientSyncAddGeneralLogEventListener());
    }

    public boolean onGlobalEvent(final EventListener listener){
        return globalListeners.add(listener);
    }

    public boolean removeGlobalListener(final EventListener listener){
        return globalListeners.remove(listener);
    }

    public void removeAllGlobalListeners(){
        globalListeners.clear();
    }

    public List<EventListener> globalListeners(){
        return globalListeners;
    }

    public void clientSyncWorker(final EventType type,
                                 final Consumer<Runnable> worker){
        clientSyncWorkerMap.put(type, worker);
    }

    public Consumer<Runnable> clientSyncWorker(final EventType type){
        return clientSyncWorkerMap.getOrDefault(type, Runnable::run);
    }

    public boolean onEvent(final EventType type, final EventListener listener){
        if(!listenerMap.containsKey(type))
            listenerMap.put(type, new ArrayList<>());
        return listenerMap.get(type).add(listener);
    }

    public boolean onAddFriendEvent(final AddFriendEventListener listener){
        return onEvent(EventType.ADD_FRIEND, listener);
    }

    public boolean onRemoveFriendEvent(final RemoveFriendEventListener listener){
        return onEvent(EventType.REMOVE_FRIEND, listener);
    }

    public boolean onAddFriendRequestEvent(final AddFriendRequestEventListener listener){
        return onEvent(EventType.ADD_FRIEND_REQUEST, listener);
    }

    public boolean onRemoveFriendRequestEvent(final RemoveFriendRequestEventListener listener){
        return onEvent(EventType.REMOVE_FRIEND_REQUEST, listener);
    }

    public boolean onFriendMessageEvent(final FriendMessageEventListener listener){
        return onEvent(EventType.FRIEND_MESSAGE, listener);
    }

    public boolean onAddChatroomEvent(final AddChatroomEventListener listener){
        return onEvent(EventType.ADD_CHATROOM, listener);
    }

    public boolean onAddChatroomUserEvent(final AddChatroomUserEventListener listener){
        return onEvent(EventType.ADD_CHATROOM_USER, listener);
    }

    public boolean onRemoveChatroomEvent(final RemoveChatroomEventListener listener){
        return onEvent(EventType.REMOVE_CHATROOM, listener);
    }

    public boolean onRemoveChatroomUserEvent(final RemoveChatroomUserEventListener listener){
        return onEvent(EventType.REMOVE_CHATROOM_USER, listener);
    }

    public boolean onChatroomMessageEvent(final ChatroomMessageEventListener listener){
        return onEvent(EventType.CHATROOM_MESSAGE, listener);
    }

    public boolean removeListener(final EventType type, final EventListener listener){
        return listenerMap.containsKey(type)
                && listenerMap.get(type).remove(listener);
    }

    public void removeAllListeners(final EventType type){
        if(listenerMap.containsKey(type))
            listenerMap.get(type).clear();
    }

    public List<EventListener> listeners(final EventType type){
        return listenerMap.getOrDefault(type, Collections.emptyList());
    }

    public void fireGlobal(final ChatterClient client,
                           final Event event){
        fire(globalListeners, client, event);
    }

    public void fire(final ChatterClient client,
                     final Event event){
        fire(listeners(event.type()), client, event);
    }

    public void fireClientSync(final ChatterClient client,
                               final Event event){
        Optional.of(clientSyncListenerMap.get(event.type()))
                .ifPresent(l -> clientSyncWorker(event.type()).accept(() -> l.onEvent(client, event)));
    }

    public void fireAll(final ChatterClient client,
                        final Event event){
        fireClientSync(client, event);
        fireGlobal(client, event);
        fire(client, event);
    }

    private void fire(final List<EventListener> listeners,
                      final ChatterClient client,
                      final Event event){
        if(client == null)
            throw new NullPointerException("Client can't be null");
        listeners.forEach(e -> e.onEvent(client, event));
    }
}
