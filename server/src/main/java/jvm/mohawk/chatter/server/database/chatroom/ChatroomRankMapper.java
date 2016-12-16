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
import jvm.mohawk.chatter.server.model.Rank;
import jvm.mohawk.chatter.server.model.chatroom.ChatroomRank;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ChatroomRankMapper implements ResultSetMapper<ChatroomRank> {

    @Override
    public ChatroomRank map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ChatroomRank(
                rs.getInt("chatroom_id"),
                rs.getInt("profile_id"),
                Rank.forId(rs.getInt("rank_id"))
        );
    }
}
