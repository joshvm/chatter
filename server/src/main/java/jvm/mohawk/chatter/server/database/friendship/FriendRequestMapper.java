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
import jvm.mohawk.chatter.server.model.friendship.FriendRequest;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class FriendRequestMapper implements ResultSetMapper<FriendRequest> {

    @Override
    public FriendRequest map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new FriendRequest(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                rs.getInt("requester_profile_id"),
                rs.getInt("target_profile_id")
        );
    }
}
