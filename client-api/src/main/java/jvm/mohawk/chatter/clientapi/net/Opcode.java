package jvm.mohawk.chatter.clientapi.net;

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
import jvm.mohawk.chatter.clientapi.utils.Utils;

/**
 * the opcode is the first thing sent in the packet structure to let the
 * receiving end know how to decode the following data.
 * each opcode contains in and out lengths.
 * in length is for decoding the packet
 * out length is for encoding the packet
 */
public enum Opcode {

    REGISTER(1, 0, PacketSize.VAR_SHORT),
    REGISTER_RESPONSE(2, 1, null),
    LOGIN(3, 0, PacketSize.VAR_SHORT),
    LOGIN_RESPONSE(4, 1, null),
    INIT(5, PacketSize.VAR_SHORT, null),
    KICK(6, 0, PacketSize.VAR_BYTE), //
    KICK_RESPONSE(7, 1, null), //
    CREATE_FRIEND_REQUEST(8, 0, PacketSize.FIXED), //
    CREATE_FRIEND_REQUEST_RESPONSE(9, 1, null), //
    ADD_PENDING_FRIEND_REQUEST(10, PacketSize.VAR_BYTE, null), //
    CANCEL_PENDING_FRIEND_REQUEST(11, 0, PacketSize.FIXED), //
    CANCEL_PENDING_FRIEND_REQUEST_RESPONSE(12, 1, null), //
    ADD_ANSWERABLE_FRIEND_REQUEST(13, PacketSize.VAR_BYTE, null), //
    ANSWER_FRIEND_REQUEST(14, 0, PacketSize.FIXED), //
    ANSWER_FRIEND_REQUEST_RESPONSE(15, 1, null), //
    REMOVE_PENDING_FRIEND_REQUEST(16, 4, null), //
    REMOVE_ANSWERABLE_FRIEND_REQUEST(17, 4, null), //
    ADD_FRIEND(18, PacketSize.VAR_BYTE, null), //
    REMOVE_FRIEND(19, 4, PacketSize.FIXED), //
    REMOVE_FRIEND_RESPONSE(20, 1, null), //
    SEND_CHAT_MESSAGE(21, 0, PacketSize.VAR_SHORT),
    SEND_CHAT_MESSAGE_RESPONSE(22, 1, null),
    ADD_CHAT_MESSAGE(23, PacketSize.VAR_SHORT, null),
    EDIT_CHAT_MESSAGE(24, PacketSize.VAR_SHORT, PacketSize.VAR_SHORT),
    EDIT_CHAT_MESSAGE_RESPONSE(25, 1, null),
    DELETE_CHAT_MESSAGE(26, 0, PacketSize.FIXED),
    DELETE_CHAT_MESSAGE_RESPONSE(27, 1, null),
    REMOVE_CHAT_MESSAGE(28, 13, null), //type_id(1) + friendship_id(4) + message_id(8)
    GET_PROFILE_BY_USER(29, 0, PacketSize.VAR_BYTE),
    GET_PROFILE_BY_USER_RESPONSE(30, PacketSize.VAR_BYTE, null),
    CREATE_CHATROOM(31, 0, PacketSize.VAR_SHORT),
    CREATE_CHATROOM_RESPONSE(32, 1, null), //
    JOIN_CHATROOM(33, 0, PacketSize.FIXED),
    JOIN_CHATROOM_RESPONSE(34, 1, null),
    ADD_CHATROOM(35, PacketSize.VAR_SHORT, null), //
    LEAVE_CHATROOM(36, 0, PacketSize.FIXED),
    LEAVE_CHATROOM_RESPONSE(37, 1, null),
    REMOVE_CHATROOM(38, 4, null),
    ADD_CHATROOM_USER(39, PacketSize.VAR_BYTE, null),
    REMOVE_CHATROOM_USER(40, 8, null),
    GET_CHATROOM_BY_NAME(41, 0, PacketSize.VAR_BYTE),
    GET_CHATROOM_BY_NAME_RESPONSE(42, PacketSize.VAR_SHORT, null),
    EXECUTE_COMMAND(43, 0, PacketSize.VAR_SHORT),
    ADD_COMMAND_MESSAGE(44, PacketSize.VAR_SHORT, null),
    UPDATE_PROFILE_STATUS(45, 0, PacketSize.FIXED),
    UPDATE_PROFILE_STATUS_RESPONSE(46, 1, null),
    SET_PROFILE_STATUS(47, 5, null),
    RECOVER_ACCOUNT_REQUEST(48, 0, PacketSize.VAR_BYTE),
    RECOVER_ACCOUNT_REQUEST_RESPONSE(49, PacketSize.VAR_SHORT, null),
    TRY_RECOVER_ACCOUNT(50, 0, PacketSize.VAR_SHORT),
    TRY_RECOVER_ACCOUNT_RESPONSE(51, 1, PacketSize.VAR_SHORT),
    ADD_LOGIN_LOG(52, PacketSize.VAR_SHORT, null),
    ADD_GENERAL_LOG(53, PacketSize.VAR_SHORT, null);

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
           final PacketSize inSize,
           final PacketSize outSize){
        this(value, inSize.value(), outSize);
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
