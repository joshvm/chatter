package jvm.mohawk.chatter.server.model.chatroom;

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

import jvm.mohawk.chatter.server.utils.Utils;

public final class ChatroomValidation {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    private static final int MIN_DESC_LENGTH = 1;
    private static final int MAX_DESC_LENGTH = 150;

    private static final String DESC_ALLOWED_CHARACTERS = " ._!@#$%^&*()_+-=`:;,<>/?";

    private ChatroomValidation(){}

    public static boolean isValidName(final String name){
        return Utils.between(name.length(), MIN_NAME_LENGTH, MAX_NAME_LENGTH)
                && name.chars().allMatch(Character::isLetterOrDigit);
    }

    public static boolean isValidDesc(final String desc){
        return Utils.between(desc.length(), MIN_DESC_LENGTH, MAX_DESC_LENGTH)
                && desc.chars()
                    .noneMatch(c -> !Character.isLetterOrDigit(c) && DESC_ALLOWED_CHARACTERS.indexOf(c) < 0);
    }
}
