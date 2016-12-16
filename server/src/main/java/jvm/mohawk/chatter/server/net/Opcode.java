package jvm.mohawk.chatter.server.net;

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

import java.util.Map;
import jvm.mohawk.chatter.server.utils.Utils;

public enum Opcode {

    REGISTER(1, PacketSize.VAR_SHORT, null),
    REGISTER_RESPONSE(2, 0, PacketSize.FIXED),
    LOGIN(3, PacketSize.VAR_SHORT, null),
    LOGIN_RESPONSE(4, 0, PacketSize.FIXED),
    INIT(5, 0, PacketSize.VAR_SHORT),
    KICK(6, PacketSize.VAR_BYTE, null),
    KICK_RESPONSE(7, 0, PacketSize.FIXED),
    CREATE_FRIEND_REQUEST(8, 4, null),
    CREATE_FRIEND_REQUEST_RESPONSE(9, 0, PacketSize.FIXED),
    ADD_PENDING_FRIEND_REQUEST(10, 0, PacketSize.VAR_BYTE),
    CANCEL_PENDING_FRIEND_REQUEST(11, 4, null),
    CANCEL_PENDING_FRIEND_REQUEST_RESPONSE(12, 0, PacketSize.FIXED),
    ADD_ANSWERABLE_FRIEND_REQUEST(13, 0, PacketSize.VAR_BYTE),
    ANSWER_FRIEND_REQUEST(14, 5, null),
    ANSWER_FRIEND_REQUEST_RESPONSE(15, 0, PacketSize.FIXED),
    REMOVE_PENDING_FRIEND_REQUEST(16, 0, PacketSize.FIXED),
    REMOVE_ANSWERABLE_FRIEND_REQUEST(17, 0, PacketSize.FIXED),
    ADD_FRIEND(18, 0, PacketSize.VAR_BYTE),
    REMOVE_FRIEND(19, 4, PacketSize.FIXED),
    REMOVE_FRIEND_RESPONSE(20, 0, PacketSize.FIXED),
    SEND_CHAT_MESSAGE(21, PacketSize.VAR_SHORT, null),
    SEND_CHAT_MESSAGE_RESPONSE(22, 0, PacketSize.FIXED),
    ADD_CHAT_MESSAGE(23, 0, PacketSize.VAR_SHORT),
    EDIT_CHAT_MESSAGE(24, PacketSize.VAR_SHORT, PacketSize.VAR_SHORT),
    EDIT_CHAT_MESSAGE_RESPONSE(25, 0, PacketSize.FIXED),
    DELETE_CHAT_MESSAGE(26, 12, PacketSize.FIXED), //friendship_id(4) + message_id(8),
    DELETE_CHAT_MESSAGE_RESPONSE(27, 0, PacketSize.FIXED),
    REMOVE_CHAT_MESSAGE(28, 0, PacketSize.FIXED),
    GET_PROFILE_BY_USER(29, PacketSize.VAR_BYTE, null),
    GET_PROFILE_BY_USER_RESPONSE(30, 0, PacketSize.VAR_BYTE),
    CREATE_CHATROOM(31, PacketSize.VAR_SHORT, null),
    CREATE_CHATROOM_RESPONSE(32, 0, PacketSize.FIXED),
    JOIN_CHATROOM(33, 4, null),
    JOIN_CHATROOM_RESPONSE(34, 0, PacketSize.FIXED),
    ADD_CHATROOM(35, 0, PacketSize.VAR_SHORT),
    LEAVE_CHATROOM(36, 4, null),
    LEAVE_CHATROOM_RESPONSE(37, 0, PacketSize.FIXED),
    REMOVE_CHATROOM(38, 0, PacketSize.FIXED),
    ADD_CHATROOM_USER(39, 0, PacketSize.VAR_BYTE),
    REMOVE_CHATROOM_USER(40, 0, PacketSize.FIXED),
    GET_CHATROOM_BY_NAME(41, PacketSize.VAR_BYTE, null),
    GET_CHATROOM_BY_NAME_RESPONSE(42, 0, PacketSize.VAR_SHORT),
    EXECUTE_COMMAND(43, PacketSize.VAR_SHORT, null),
    ADD_COMMAND_MESSAGE(44, 0, PacketSize.VAR_SHORT),
    UPDATE_PROFILE_STATUS(45, 1, null),
    UPDATE_PROFILE_STATUS_RESPONSE(46, 0, PacketSize.FIXED),
    SET_PROFILE_STATUS(47, 0, PacketSize.FIXED),
    RECOVER_ACCOUNT_REQUEST(48, PacketSize.VAR_BYTE, null),
    RECOVER_ACCOUNT_REQUEST_RESPONSE(49, 0, PacketSize.VAR_SHORT),
    TRY_RECOVER_ACCOUNT(50, PacketSize.VAR_SHORT, null),
    TRY_RECOVER_ACCOUNT_RESPONSE(51, 0, PacketSize.FIXED),
    ADD_LOGIN_LOG(52, 0, PacketSize.VAR_SHORT),
    ADD_GENERAL_LOG(53, 0, PacketSize.VAR_SHORT);

    private static final Map<Integer, Opcode> MAP = Utils.map(values(), Opcode::value);

    private final int value;
    private final int inLength;
    private final PacketSize inSize;
    private final PacketSize outSize;

    Opcode(final int value,
           final int inLength,
           final PacketSize outSize){
        this.value = value;
        this.inLength = inLength;
        this.outSize = outSize;

        inSize = PacketSize.forValue(inLength);
    }

    Opcode(final int value,
           final PacketSize inLength,
           final PacketSize outSize){
        this(value, inLength.value(), outSize);
    }

    public int value(){
        return value;
    }

    public int inLength(){
        return inLength;
    }

    public PacketSize inSize(){
        return inSize;
    }

    public PacketSize outSize(){
        return outSize;
    }

    public static Opcode forValue(final int value){
        return MAP.get(value);
    }
}
