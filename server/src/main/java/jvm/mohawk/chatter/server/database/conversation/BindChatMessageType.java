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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jvm.mohawk.chatter.server.model.conversation.ChatMessageType;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindChatMessageType.ChatMessageTypeBinderFactory.class)
public @interface BindChatMessageType {

    String value() default "type_id";

    class ChatMessageTypeBinderFactory implements BinderFactory {

        @Override
        public Binder<BindChatMessageType, ChatMessageType> build(final Annotation annotation){
            return (q, bind, t) -> q.bind(bind.value(), t.id());
        }
    }
}