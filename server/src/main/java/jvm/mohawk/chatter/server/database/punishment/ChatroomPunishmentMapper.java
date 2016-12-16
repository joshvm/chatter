package jvm.mohawk.chatter.server.database.punishment;

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
import jvm.mohawk.chatter.server.model.punishment.ChatroomPunishment;
import jvm.mohawk.chatter.server.model.punishment.Punishment;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ChatroomPunishmentMapper implements ResultSetMapper<ChatroomPunishment> {

    @Override
    public ChatroomPunishment map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ChatroomPunishment(
                rs.getInt("id"),
                rs.getTimestamp("timestamp"),
                rs.getInt("chatroom_id"),
                rs.getInt("punisher_id"),
                rs.getString("punisher_user"),
                rs.getInt("victim_id"),
                rs.getString("victim_user"),
                Punishment.Type.forId(rs.getInt("type_id")),
                rs.getInt("duration_in_seconds"),
                rs.getString("reason"),
                rs.getBoolean("active")
        );
    }
}
