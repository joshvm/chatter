package jvm.mohawk.chatter.server.net.client;

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

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomManager;
import jvm.mohawk.chatter.server.model.conversation.ChatMessageType;
import jvm.mohawk.chatter.server.model.conversation.ConversationManager;
import jvm.mohawk.chatter.server.model.profile.Profile;
import jvm.mohawk.chatter.server.net.Opcode;
import jvm.mohawk.chatter.server.net.Packet;
import jvm.mohawk.chatter.server.net.utils.NetUtils;

public class Client {

    public static final AttributeKey<Client> KEY = AttributeKey.valueOf("client");

    private final ChannelHandlerContext ctx;
    private final String device;
    private final Profile profile;

    private final ChatroomManager chatrooms;

    private boolean muted;

    private final ConversationManager friendshipConvoManager;
    private final ConversationManager chatroomConvoManager;

    private Client(final ChannelHandlerContext ctx,
                  final String device,
                  final Profile profile){
        this.ctx = ctx;
        this.device = device;
        this.profile = profile;

        chatrooms = new ChatroomManager();

        friendshipConvoManager = new ConversationManager(this, ChatMessageType.FRIEND);
        chatroomConvoManager = new ConversationManager(this, ChatMessageType.CHATROOM);
    }

    public ChannelHandlerContext ctx(){
        return ctx;
    }

    public String device(){
        return device;
    }

    public Profile profile(){
        return profile;
    }

    public ChannelFuture writeLater(final Packet pkt){
        return NetUtils.writeLater(ctx, pkt);
    }

    public void flush(){
        ctx.flush();
    }

    public ChannelFuture write(final Packet pkt){
        return NetUtils.write(ctx, pkt);
    }

    public ChannelFuture writeResponse(final Opcode opcode,
                                       final int response){
        return NetUtils.writeResponse(ctx, opcode, response);
    }

    public ChannelFuture disconnect(){
        return ctx.close();
    }

    public ChatroomManager chatrooms(){
        return chatrooms;
    }

    public ConversationManager friendshipConvoManager(){
        return friendshipConvoManager;
    }

    public ConversationManager chatroomConvoManager(){
        return chatroomConvoManager;
    }

    public boolean muted(){
        return muted;
    }

    public void muted(final boolean muted){
        this.muted = muted;
    }

    @Override
    public boolean equals(final Object obj){
        if(!(obj instanceof Client))
            return false;
        final Client c = (Client) obj;
        return profile.id() == c.profile.id();
    }

    public static Client register(final ChannelHandlerContext ctx,
                                  final String device,
                                  final Profile profile){
        final Client client = new Client(ctx, device, profile);
        ctx.channel().attr(KEY).set(client);
        return client;
    }
}
