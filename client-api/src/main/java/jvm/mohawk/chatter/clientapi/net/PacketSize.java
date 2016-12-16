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
 * this enum represents the various different packet sizes.
 * fixed means that the server knows the length of the packet
 * var_byte (variable byte) means that the length of the payload will be sent as a byte (read as an unsigned byte)
 * var_short (variable short) means that the length of the payload will be sent as a short (read as an unsigned short)
 * var_byte is used for smaller (but variable sized) packets
 * var_short is used for larger (but variable sized) packets
 */
public enum PacketSize {

    FIXED(0),
    VAR_BYTE(-1),
    VAR_SHORT(-2);

    private static final Map<Integer, PacketSize> MAP = Utils.map(values(), PacketSize::value);

    private final int value;

    PacketSize(final int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static PacketSize forValue(final int value){
        return MAP.getOrDefault(value, FIXED);
    }
}
