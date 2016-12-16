package jvm.mohawk.chatter.server.database.conversation;

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
import jvm.mohawk.chatter.server.model.conversation.ChatMessage;
import jvm.mohawk.chatter.server.model.conversation.ChatMessageType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ChatMessageMapper implements ResultSetMapper<ChatMessage> {

    @Override
    public ChatMessage map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ChatMessage(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                ChatMessageType.forId(rs.getInt("type_id")),
                rs.getInt("conversable_id"),
                rs.getInt("sender_id"),
                rs.getString("message")
        );
    }
}
