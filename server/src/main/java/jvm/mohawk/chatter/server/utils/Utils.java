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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {

    private Utils(){}

    public static Props props(final Path path) throws IOException {
        try(final InputStream in = Files.newInputStream(path)){
            final Properties props = new Properties();
            props.load(in);
            return new Props(props);
        }
    }

    public static <K, V> Map<K, V> map(final V[] values, Function<V, K> key){
        return Stream.of(values)
                .collect(Collectors.toMap(key, Function.identity()));
    }

    public static Timestamp timestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static boolean between(final int value, final int min, final int max){
        return value >= min && value <= max;
    }

    public static boolean patternsContain(final Collection<String> listOfPatterns, final String value){
        return listOfPatterns.stream()
                .anyMatch(pattern -> pattern.matches(value));
    }

    public static boolean ipListContains(final Collection<String> ipPatterns,
                                         final String ip){
        return ipPatterns.stream()
                .map(Utils::convertIpToPattern)
                .anyMatch(ip::matches);
    }

    public static String convertIpToPattern(final String ipPattern){
        return ipPattern.replace(".", "\\.")
                .replace("*", "\\d");
    }

    public static int other(final int checkValue, final int value1, final int value2){
        return checkValue == value1 ? value2 : value1;
    }

}
