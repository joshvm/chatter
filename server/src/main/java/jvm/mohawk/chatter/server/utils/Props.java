package jvm.mohawk.chatter.server.utils;

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

import java.util.Properties;
import java.util.function.Function;


import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class Props {

    private final Properties props;

    public Props(final Properties props){
        this.props = props;
    }

    public boolean contains(final String key){
        return props.containsKey(key);
    }

    public String string(final String key, final String defaultValue){
        return props.getProperty(key, defaultValue);
    }

    public String string(final String key){
        return string(key, null);
    }

    public <T> T object(final Function<String, T> mapper, final String key, final T defaultValue){
        return contains(key) ? mapper.apply(string(key)) : defaultValue;
    }

    public <T> T object(final Function<String, T> mapper, final String key){
        return object(mapper, key, null);
    }

    public int integer(final String key, final int defaultValue){
        return contains(key) ? parseInt(string(key)) : defaultValue;
    }

    public int integer(final String key){
        return integer(key, -1);
    }

    public boolean bool(final String key, final boolean defaultValue){
        return contains(key) ? parseBoolean(string(key)) : defaultValue;
    }

    public boolean bool(final String key){
        return bool(key, false);
    }
}
