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
import jvm.mohawk.chatter.server.model.chatroom.Chatroom;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ChatroomMapper implements ResultSetMapper<Chatroom> {

    @Override
    public Chatroom map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Chatroom(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}
