package jvm.mohawk.chatter.server.database.chatroom;

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

import java.sql.ResultSet;
import java.sql.SQLException;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomRegistrationDetails;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ChatroomRegistrationDetailsMapper implements ResultSetMapper<ChatroomRegistrationDetails> {

    @Override
    public ChatroomRegistrationDetails map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ChatroomRegistrationDetails(
                rs.getInt("chatroom_id"),
                rs.getTimestamp("timestamp"),
                rs.getInt("owner_profile_id")
        );
    }
}
