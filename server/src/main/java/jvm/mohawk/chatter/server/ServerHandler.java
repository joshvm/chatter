package jvm.mohawk.chatter.server;

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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import jvm.mohawk.chatter.server.message.Message;
import jvm.mohawk.chatter.server.message.MessageListener;
import jvm.mohawk.chatter.server.message.MessageMapper;
import jvm.mohawk.chatter.server.message.MessageType;
import jvm.mohawk.chatter.server.message.listeners.AnswerFriendRequestListener;
import jvm.mohawk.chatter.server.message.listeners.CancelPendingFriendRequestListener;
import jvm.mohawk.chatter.server.message.listeners.CreateChatroomListener;
import jvm.mohawk.chatter.server.message.listeners.CreateFriendRequestListener;
import jvm.mohawk.chatter.server.message.listeners.ExecuteCommandMessageListener;
import jvm.mohawk.chatter.server.message.listeners.GetChatroomByNameListener;
import jvm.mohawk.chatter.server.message.listeners.GetProfileByUserListener;
import jvm.mohawk.chatter.server.message.listeners.JoinChatroomListener;
import jvm.mohawk.chatter.server.message.listeners.KickListener;
import jvm.mohawk.chatter.server.message.listeners.LeaveChatroomListener;
import jvm.mohawk.chatter.server.message.listeners.LoginListener;
import jvm.mohawk.chatter.server.message.listeners.RecoverAccountRequestMessageListener;
import jvm.mohawk.chatter.server.message.listeners.RegisterListener;
import jvm.mohawk.chatter.server.message.listeners.RemoveFriendListener;
import jvm.mohawk.chatter.server.message.listeners.SendChatMessageListener;
import jvm.mohawk.chatter.server.message.listeners.TryRecoverAccountMessageListener;
import jvm.mohawk.chatter.server.message.listeners.UpdateProfileStatusMessageListener;
import jvm.mohawk.chatter.server.message.mappers.AnswerFriendRequestMapper;
import jvm.mohawk.chatter.server.message.mappers.CancelPendingFriendRequestMapper;
import jvm.mohawk.chatter.server.message.mappers.CreateChatroomMapper;
import jvm.mohawk.chatter.server.message.mappers.CreateFriendRequestMapper;
import jvm.mohawk.chatter.server.message.mappers.ExecuteCommandMessageMapper;
import jvm.mohawk.chatter.server.message.mappers.GetChatroomByNameMapper;
import jvm.mohawk.chatter.server.message.mappers.GetProfileByUserMapper;
import jvm.mohawk.chatter.server.message.mappers.JoinChatroomMapper;
import jvm.mohawk.chatter.server.message.mappers.KickMapper;
import jvm.mohawk.chatter.server.message.mappers.LeaveChatroomMapper;
import jvm.mohawk.chatter.server.message.mappers.LoginMapper;
import jvm.mohawk.chatter.server.message.mappers.RecoverAccountRequestMessageMapper;
import jvm.mohawk.chatter.server.message.mappers.RegisterMapper;
import jvm.mohawk.chatter.server.message.mappers.RemoveFriendMapper;
import jvm.mohawk.chatter.server.message.mappers.SendChatMessageMapper;
import jvm.mohawk.chatter.server.message.mappers.TryRecoverAccountMessageMapper;
import jvm.mohawk.chatter.server.message.mappers.UpdateProfileStatusMessageMapper;
import jvm.mohawk.chatter.server.model.Status;
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.client.Client;


import static jvm.mohawk.chatter.server.Chatter.activeChatrooms;
import static jvm.mohawk.chatter.server.Chatter.activeClients;
import static jvm.mohawk.chatter.server.Chatter.database;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Map<Opcode, MessageMapper> MESSAGE_MAPPERS = new HashMap<>();
    private static final Map<MessageType, MessageListener> MESSAGE_LISTENERS = new HashMap<>();

    static {
        MESSAGE_MAPPERS.put(Opcode.REGISTER, new RegisterMapper());
        MESSAGE_LISTENERS.put(MessageType.REGISTER, new RegisterListener());

        MESSAGE_MAPPERS.put(Opcode.LOGIN, new LoginMapper());
        MESSAGE_LISTENERS.put(MessageType.LOGIN, new LoginListener());

        MESSAGE_MAPPERS.put(Opcode.KICK, new KickMapper());
        MESSAGE_LISTENERS.put(MessageType.KICK, new KickListener());

        MESSAGE_MAPPERS.put(Opcode.RECOVER_ACCOUNT_REQUEST, new RecoverAccountRequestMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.RECOVER_ACCOUNT_REQUEST, new RecoverAccountRequestMessageListener());

        MESSAGE_MAPPERS.put(Opcode.TRY_RECOVER_ACCOUNT, new TryRecoverAccountMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.TRY_RECOVER_ACCOUNT, new TryRecoverAccountMessageListener());

        MESSAGE_MAPPERS.put(Opcode.TRY_RECOVER_ACCOUNT, new TryRecoverAccountMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.TRY_RECOVER_ACCOUNT, new TryRecoverAccountMessageListener());

        MESSAGE_MAPPERS.put(Opcode.CREATE_FRIEND_REQUEST, new CreateFriendRequestMapper());
        MESSAGE_LISTENERS.put(MessageType.CREATE_FRIEND_REQUEST, new CreateFriendRequestListener());

        MESSAGE_MAPPERS.put(Opcode.CANCEL_PENDING_FRIEND_REQUEST, new CancelPendingFriendRequestMapper());
        MESSAGE_LISTENERS.put(MessageType.CANCEL_PENDING_FRIEND_REQUEST, new CancelPendingFriendRequestListener());

        MESSAGE_MAPPERS.put(Opcode.ANSWER_FRIEND_REQUEST, new AnswerFriendRequestMapper());
        MESSAGE_LISTENERS.put(MessageType.ANSWER_FRIEND_REQUEST, new AnswerFriendRequestListener());

        MESSAGE_MAPPERS.put(Opcode.REMOVE_FRIEND, new RemoveFriendMapper());
        MESSAGE_LISTENERS.put(MessageType.REMOVE_FRIEND, new RemoveFriendListener());

        MESSAGE_MAPPERS.put(Opcode.SEND_CHAT_MESSAGE, new SendChatMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.SEND_CHAT_MESSAGE, new SendChatMessageListener());

        MESSAGE_MAPPERS.put(Opcode.GET_PROFILE_BY_USER, new GetProfileByUserMapper());
        MESSAGE_LISTENERS.put(MessageType.GET_PROFILE_BY_USER, new GetProfileByUserListener());

        MESSAGE_MAPPERS.put(Opcode.CREATE_CHATROOM, new CreateChatroomMapper());
        MESSAGE_LISTENERS.put(MessageType.CREATE_CHATROOM, new CreateChatroomListener());

        MESSAGE_MAPPERS.put(Opcode.JOIN_CHATROOM, new JoinChatroomMapper());
        MESSAGE_LISTENERS.put(MessageType.JOIN_CHATROOM, new JoinChatroomListener());

        MESSAGE_MAPPERS.put(Opcode.LEAVE_CHATROOM, new LeaveChatroomMapper());
        MESSAGE_LISTENERS.put(MessageType.LEAVE_CHATROOM, new LeaveChatroomListener());

        MESSAGE_MAPPERS.put(Opcode.GET_CHATROOM_BY_NAME, new GetChatroomByNameMapper());
        MESSAGE_LISTENERS.put(MessageType.GET_CHATROOM_BY_NAME, new GetChatroomByNameListener());

        MESSAGE_MAPPERS.put(Opcode.EXECUTE_COMMAND, new ExecuteCommandMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.EXECUTE_COMMAND, new ExecuteCommandMessageListener());

        MESSAGE_MAPPERS.put(Opcode.UPDATE_PROFILE_STATUS, new UpdateProfileStatusMessageMapper());
        MESSAGE_LISTENERS.put(MessageType.UPDATE_PROFILE_STATUS, new UpdateProfileStatusMessageListener());
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception{
        System.out.println("connection established: " + ctx);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object obj) throws Exception{
        final Packet pkt = (Packet) obj;
        System.out.println(pkt);
        final MessageMapper mapper = MESSAGE_MAPPERS.get(pkt.opcode());
        if(mapper == null){
            System.err.println("No event mapper found for packet: " + pkt.opcode());
            return;
        }
        final Message msg = MESSAGE_MAPPERS.get(pkt.opcode()).map(pkt);
        pkt.buf().release();
        final MessageListener listener = MESSAGE_LISTENERS.get(msg.type());
        if(listener == null){
            System.err.println("No event listener for event type: " + msg.type());
            return;
        }
        System.out.println(msg);
        try{
            listener.onMessage(ctx, msg);
        }catch(Exception err){
            listener.onError(ctx, msg, err);
        }
    }

    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception{
        System.out.println("removed: " + ctx);
        final Client client = ctx.channel().attr(Client.KEY).getAndSet(null);
        if(client == null)
            return;
        activeClients.remove(client);
        for(final Chatroom chatroom : client.chatrooms().list()){
            chatroom.clients().remove(client);
            if(chatroom.clients().list().isEmpty())
                activeChatrooms.remove(chatroom);
            for(final Client c : chatroom.clients().list()){
                c.write(
                        new Packet(Opcode.REMOVE_CHATROOM_USER)
                            .writeInt(chatroom.id())
                            .writeInt(client.profile().id())
                );
            }
        }
        //change status of all friends
        database.friendships().involving(client.profile().id())
                .stream()
                .map(f -> f.otherProfileId(client.profile().id()))
                .map(activeClients::forIdOrUser)
                .filter(Objects::nonNull)
                .forEach(c -> c.write(new Packet(Opcode.SET_PROFILE_STATUS).writeInt(client.profile().id()).writeByte(Status.OFFLINE.id())));
        System.out.println("client disconnected: " + client.profile().user());
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable err) throws Exception{
        err.printStackTrace();
        ctx.close();
    }
}
