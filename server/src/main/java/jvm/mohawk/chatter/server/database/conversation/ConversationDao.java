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

import java.util.Collection;
import jvm.mohawk.chatter.server.database.Dao;
import jvm.mohawk.chatter.server.model.conversation.ChatMessage;
import jvm.mohawk.chatter.server.model.conversation.ChatMessageType;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ChatMessageMapper.class)
public interface ConversationDao extends Dao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS conversations (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "type_id INT NOT NULL, " +
            "conversable_id INT NOT NULL, " +
            "sender_id INT NOT NULL, " +
            "message TEXT NOT NULL, " +
            "PRIMARY KEY (id))")
    void init();

    @SqlUpdate("INSERT INTO conversations " +
            "(type_id, conversable_id, sender_id, message) VALUES " +
            "(:type_id, :conversable_id, :sender_id, :message)")
    @GetGeneratedKeys
    long insert(@BindChatMessageType("type_id") final ChatMessageType type,
               @Bind("conversable_id") final int conversableId,
               @Bind("sender_id") final int senderId,
               @Bind("message") final String message);

    @SqlQuery("SELECT * FROM conversations WHERE id = :id")
    ChatMessage forId(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM conversations WHERE type_id = :type_id AND conversable_id = :conversable_id ORDER BY timestamp")
    Collection<ChatMessage> get(@BindChatMessageType("type_id") final ChatMessageType type,
                                @Bind("conversable_id") final int conversableId);
}
