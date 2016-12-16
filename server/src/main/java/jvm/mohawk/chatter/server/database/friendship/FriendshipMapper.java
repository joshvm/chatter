package jvm.mohawk.chatter.server.database.friendship;

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
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class FriendshipMapper implements ResultSetMapper<Friendship> {

    @Override
    public Friendship map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Friendship(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                rs.getInt("profile1_id"),
                rs.getInt("profile2_id")
        );
    }
}
