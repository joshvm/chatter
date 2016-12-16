package jvm.mohawk.chatter.clientapi.data.response;

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
import jvm.mohawk.chatter.clientapi.ChatterClient;
import jvm.mohawk.chatter.clientapi.data.request.Request;
import jvm.mohawk.chatter.clientapi.data.request.RequestType;
import jvm.mohawk.chatter.clientapi.data.request.types.SendChatroomMessage;
import jvm.mohawk.chatter.clientapi.data.request.types.SendFriendMessage;
import jvm.mohawk.chatter.clientapi.data.response.listeners.AnswerFriendRequestResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.CancelFriendRequestResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.CreateChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.CreateFriendRequestResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.GetChatroomByNameResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.GetProfileByUserResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.JoinChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.LeaveChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.RemoveFriendResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.SendChatroomMessageResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.SendFriendMessageResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.sync.ClientSyncJoinChatroomResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.listeners.sync.ClientSyncUpdateProfileStatusResponseListener;
import jvm.mohawk.chatter.clientapi.data.response.types.SendChatroomMessageResponse;
import jvm.mohawk.chatter.clientapi.data.response.types.SendFriendMessageResponse;

/**
 * this class is responsible for handling responses (contains various client sync for responses)
 * the client sync listeners for this are optimizations
 */
public class ResponseHandler {

    private final Map<RequestType, List<ResponseListener>> listenerMap;
    private final List<ResponseListener> globalListeners;

    private final Map<RequestType, ResponseListener> clientSyncListenerMap;

    public ResponseHandler(){
        listenerMap = new HashMap<>();
        globalListeners = new ArrayList<>();

        clientSyncListenerMap = new HashMap<>();
//        clientSyncListenerMap.put(RequestType.SEND_CHAT_MESSAGE, new ClientSyncSendChatMessageResponseListener());
        clientSyncListenerMap.put(RequestType.JOIN_CHATROOM, new ClientSyncJoinChatroomResponseListener());
//        clientSyncListenerMap.put(RequestType.LEAVE_CHATROOM, new ClientSyncLeaveChatroomResponseListener());
        clientSyncListenerMap.put(RequestType.UPDATE_PROFILE_STATUS, new ClientSyncUpdateProfileStatusResponseListener());

    }

    public boolean onGlobalResponse(final ResponseListener listener){
        return globalListeners.add(listener);
    }

    public boolean removeGlobalListener(final ResponseListener listener){
        return globalListeners.remove(listener);
    }

    public void removeAllGlobalListeners(){
        globalListeners.clear();
    }

    public List<ResponseListener> globalListeners(){
        return globalListeners;
    }

    public boolean onResponse(final RequestType type, final ResponseListener listener){
        if(!listenerMap.containsKey(type))
            listenerMap.put(type, new ArrayList<>());
        return listenerMap.get(type).add(listener);
    }

    public boolean onCreateFriendRequestResponse(final CreateFriendRequestResponseListener listener){
        return onResponse(RequestType.CREATE_FRIEND_REQUEST, listener);
    }

    public boolean onCancelFriendRequestResponse(final CancelFriendRequestResponseListener listener){
        return onResponse(RequestType.CANCEL_FRIEND_REQUEST, listener);
    }

    public boolean onAnswerFriendRequestResponse(final AnswerFriendRequestResponseListener listener){
        return onResponse(RequestType.ANSWER_FRIEND_REQUEST, listener);
    }

    public boolean onRemoveFriendResponse(final RemoveFriendResponseListener listener){
        return onResponse(RequestType.REMOVE_FRIEND, listener);
    }

    public boolean onGetProfileByUserResponse(final GetProfileByUserResponseListener listener){
        return onResponse(RequestType.GET_PROFILE_BY_USER, listener);
    }

    public boolean onCreateChatroomResponse(final CreateChatroomResponseListener listener){
        return onResponse(RequestType.CREATE_CHATROOM, listener);
    }

    public boolean onJoinChatroomResponse(final JoinChatroomResponseListener listener){
        return onResponse(RequestType.JOIN_CHATROOM, listener);
    }

    public boolean onLeaveChatroomResponse(final LeaveChatroomResponseListener listener){
        return onResponse(RequestType.LEAVE_CHATROOM, listener);
    }

    public boolean onSendFriendMessageResponse(final SendFriendMessageResponseListener listener){
        return onResponse(RequestType.SEND_CHAT_MESSAGE, (client, req, resp) -> {
            if(req instanceof SendFriendMessage && resp instanceof SendFriendMessageResponse)
                listener.onResponse(client, (SendFriendMessage)req, (SendFriendMessageResponse)resp);
        });
    }

    public boolean onSendChatroomMessageResponse(final SendChatroomMessageResponseListener listener){
        return onResponse(RequestType.SEND_CHAT_MESSAGE, (client, req, resp) -> {
            if(req instanceof SendChatroomMessage && resp instanceof SendChatroomMessageResponse)
                listener.onResponse(client, (SendChatroomMessage)req, (SendChatroomMessageResponse)resp);
        });
    }

    public boolean onGetChatroomByNameResponse(final GetChatroomByNameResponseListener listener){
        return onResponse(RequestType.GET_CHATROOM_BY_NAME, listener);
    }

    public boolean removeListener(final RequestType type, final ResponseListener listener){
        return listenerMap.containsKey(type)
                && listenerMap.get(type).remove(listener);
    }

    public void removeAllListeners(final RequestType type){
        if(listenerMap.containsKey(type))
            listenerMap.get(type).clear();
    }

    public List<ResponseListener> listeners(final RequestType type){
        return listenerMap.getOrDefault(type, Collections.emptyList());
    }

    public void fireGlobal(final ChatterClient client,
                           final Request request,
                           final Response response){
        fire(globalListeners, client, request, response);
    }

    public void fire(final ChatterClient client,
                     final Request request,
                     final Response response){
        fire(listeners(request.type()), client, request, response);
    }

    public void fireClientSync(final ChatterClient client,
                               final Request request,
                               final Response response){
        Optional.ofNullable(clientSyncListenerMap.get(request.type()))
                .ifPresent(l -> l.onResponse(client, request, response));
    }

    public void fireAll(final ChatterClient client,
                        final Request request,
                        final Response response){
        fireClientSync(client, request, response);
        fireGlobal(client, request, response);
        fire(client, request, response);
    }

    private void fire(final List<ResponseListener> listeners,
                      final ChatterClient client,
                      final Request request,
                      final Response response){
        if(client == null)
            throw new NullPointerException("Client can't be null");
        if(request.type() != response.type())
            throw new IllegalArgumentException("Request type != response type");
        listeners.forEach(
                l -> l.onResponse(client, request, response)
        );
    }
}
