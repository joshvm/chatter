package jvm.mohawk.chatter.clientapi;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import jvm.mohawk.chatter.clientapi.data.Data;
import jvm.mohawk.chatter.clientapi.data.DataMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddChatroomEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddChatroomUserEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddCommandMessageEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddFriendEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddFriendRequestEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddGeneralLogEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.AddLoginLogEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.ChatMessageEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.RemoveChatroomEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.RemoveChatroomUserEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.RemoveFriendEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.RemoveFriendRequestEventMapper;
import jvm.mohawk.chatter.clientapi.data.event.mappers.SetProfileStatusEventMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.AnswerFriendRequestResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.CancelFriendRequestResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.CreateChatroomResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.CreateFriendRequestResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.GetChatroomByNameResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.GetProfileByUserResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.JoinChatroomResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.LeaveChatroomResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.RemoveFriendResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.SendChatMessageResponseMapper;
import jvm.mohawk.chatter.clientapi.data.response.mappers.UpdateProfileStatusResponseMapper;
import jvm.mohawk.chatter.clientapi.model.friendship.FriendRequest;
import jvm.mohawk.chatter.clientapi.net.Buffer;
import jvm.mohawk.chatter.clientapi.net.Opcode;
import jvm.mohawk.chatter.clientapi.net.Packet;
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * The connection class which handles the reading/writing of packets.
 * Writing packets is done via the {@code Packet} class, which are then encoded to byte arrays.
 * Incoming data is mapped to packets which are then decoded to readable event/response types with named data for easy access.
 */
class Connection implements Runnable {

    /**
     * The callback used to notify when data (events/responses) is read and when the client loses connection with the server
     */
    interface Listener {

        void onRead(final Data data);

        void onDisconnect();
    }

    private static final Map<Opcode, DataMapper> MAPPERS = new HashMap<>();

    static {
        //register response mappers
        MAPPERS.put(Opcode.CREATE_FRIEND_REQUEST_RESPONSE, new CreateFriendRequestResponseMapper());
        MAPPERS.put(Opcode.CANCEL_PENDING_FRIEND_REQUEST_RESPONSE, new CancelFriendRequestResponseMapper());
        MAPPERS.put(Opcode.ANSWER_FRIEND_REQUEST_RESPONSE, new AnswerFriendRequestResponseMapper());
        MAPPERS.put(Opcode.REMOVE_FRIEND_RESPONSE, new RemoveFriendResponseMapper());
        MAPPERS.put(Opcode.GET_PROFILE_BY_USER_RESPONSE, new GetProfileByUserResponseMapper());
        MAPPERS.put(Opcode.CREATE_CHATROOM_RESPONSE, new CreateChatroomResponseMapper());
        MAPPERS.put(Opcode.JOIN_CHATROOM_RESPONSE, new JoinChatroomResponseMapper());
        MAPPERS.put(Opcode.LEAVE_CHATROOM_RESPONSE, new LeaveChatroomResponseMapper());
        MAPPERS.put(Opcode.GET_CHATROOM_BY_NAME_RESPONSE, new GetChatroomByNameResponseMapper());
        MAPPERS.put(Opcode.SEND_CHAT_MESSAGE_RESPONSE, new SendChatMessageResponseMapper());
        MAPPERS.put(Opcode.UPDATE_PROFILE_STATUS_RESPONSE, new UpdateProfileStatusResponseMapper());

        //register event mappers
        MAPPERS.put(Opcode.ADD_FRIEND, new AddFriendEventMapper());
        MAPPERS.put(Opcode.REMOVE_FRIEND, new RemoveFriendEventMapper());
        MAPPERS.put(Opcode.ADD_PENDING_FRIEND_REQUEST, new AddFriendRequestEventMapper(FriendRequest.Type.PENDING));
        MAPPERS.put(Opcode.ADD_ANSWERABLE_FRIEND_REQUEST, new AddFriendRequestEventMapper(FriendRequest.Type.ANSWERABLE));
        MAPPERS.put(Opcode.REMOVE_PENDING_FRIEND_REQUEST, new RemoveFriendRequestEventMapper());
        MAPPERS.put(Opcode.REMOVE_ANSWERABLE_FRIEND_REQUEST, new RemoveFriendRequestEventMapper());
        MAPPERS.put(Opcode.ADD_CHAT_MESSAGE, new ChatMessageEventMapper());
        MAPPERS.put(Opcode.ADD_CHATROOM, new AddChatroomEventMapper());
        MAPPERS.put(Opcode.ADD_CHATROOM_USER, new AddChatroomUserEventMapper());
        MAPPERS.put(Opcode.REMOVE_CHATROOM, new RemoveChatroomEventMapper());
        MAPPERS.put(Opcode.REMOVE_CHATROOM_USER, new RemoveChatroomUserEventMapper());
        MAPPERS.put(Opcode.ADD_COMMAND_MESSAGE, new AddCommandMessageEventMapper());
        MAPPERS.put(Opcode.SET_PROFILE_STATUS, new SetProfileStatusEventMapper());
        MAPPERS.put(Opcode.ADD_LOGIN_LOG, new AddLoginLogEventMapper());
        MAPPERS.put(Opcode.ADD_GENERAL_LOG, new AddGeneralLogEventMapper());
    }

    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    protected boolean started;

    protected Listener listener;

    protected ChatterClient client;

    /**
     *
     * package local constructor because we really don't need to give access to the end user... they just need ChatterClient
     *
     * @param host the server host
     * @param port the server port
     * @throws IOException
     */
    Connection(final String host,
               final int port) throws IOException{
        socket = new Socket(host, port);

        out = new DataOutputStream(socket.getOutputStream());
        out.flush();

        in = new DataInputStream(socket.getInputStream());
    }

    /**
     *
     * The packet is encoded then sent to the server
     *
     * @param pkt the packet to be sent to the server
     * @throws IOException if there is an error writing a packet
     */
    void send(final Packet pkt) throws IOException {
        ChatterClient.debug("Writing %s packet (%,d bytes)%n", pkt.opcode(), pkt.buf().readableBytes());
        out.writeByte(pkt.opcode().value());
        switch(pkt.opcode().outSize()){
            case VAR_BYTE:
                out.writeByte(pkt.readableBytes());
                break;
            case VAR_SHORT:
                out.writeShort(pkt.readableBytes());
                break;
        }
        out.write(pkt.buf().array(), 0, pkt.readableBytes());
        out.flush();
    }

    /**
     *
     * attempts to read a packet from the server, null if corrupt or unable to read)
     * this method blocks until a packet is read
     *
     * @return the packet
     * @throws IOException if there is an error reading a packet (likely eof)
     */
    Packet read() throws IOException {
        final Opcode opcode = Opcode.forValue(in.readUnsignedByte());
        if(opcode == null)
            return null;
        ChatterClient.debug("Decoded opcode: %s", opcode);
        int length = opcode.inLength();
        switch(opcode.inSize()){
            case VAR_BYTE:
                length = in.readUnsignedByte();
                break;
            case VAR_SHORT:
                length = in.readUnsignedShort();
                break;
        }
        final byte[] payload = new byte[length];
        in.readFully(payload);
        return new Packet(opcode, Buffer.wrap(payload));
    }

    /**
     *
     * a variation of the simple read method, except this one ensures that the packet read is the same opcode
     *
     * @param opcode the opcode that the packet must be
     * @return the packet with the opcode matching the argument
     * @throws IOException if there is an error reading the packet
     */
    Packet readForOpcode(final Opcode opcode) throws IOException {
        Packet pkt = read();
        while(pkt == null || pkt.opcode() != opcode)
            pkt = read();
        return pkt;
    }

    /**
     * disconnects the client from the server
     */
    void disconnect() {
        Utils.close(out, true);
        Utils.close(in, true);
        Utils.close(socket, true);
    }

    /**
     *
     * @return true if the client is connected to the server, false otherwise
     */
    boolean connected(){
        return socket.isConnected();
    }

    /**
     * starts reading packets on a different thread
     */
    void startThread(){
        final Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        started = true;
    }

    @Override
    public void run(){
        while(true){
            try{
                final Packet pkt = read();
                final DataMapper mapper = MAPPERS.get(pkt.opcode());
                if(mapper == null){
                    System.out.println("no mapper available for : " + pkt.opcode());
                    continue;
                }
                final Data data = mapper.map(client, pkt);
                if(data != null) //could be an invalid packet or potentially out of sync...
                    listener.onRead(data);
            }catch(Exception ex){
                ex.printStackTrace();
                break;
            }
        }
        listener.onDisconnect();
    }
}
