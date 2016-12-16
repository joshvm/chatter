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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jvm.mohawk.chatter.server.model.friendship.Friendship;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindFriendship.FriendshipBinderFactory.class)
public @interface BindFriendship {

    class FriendshipBinderFactory implements BinderFactory {

        @Override
        public Binder<BindFriendship, Friendship> build(final Annotation annotation){
            return (q, bind, f) -> {
                q.bind("timestamp", f.timestamp());
                q.bind("id", f.id());
                q.bind("profile1_id", f.profile1Id());
                q.bind("profile2_id", f.profile2Id());
            };
        }
    }
}
