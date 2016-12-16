package jvm.mohawk.chatter.server;

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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;

public class Exercise {

    public static void main(String[] args) throws Exception {
        final List<String> websites = Arrays.asList(
                "google.ca", "yahoo.ca",
                "wikipedia.com", "netbeans.org", "theweathernetwork.com",
                "mohawkcollege.ca"
        );
        final ExecutorService service = Executors.newFixedThreadPool(5);
        final List<Future<String>> futures = websites.stream()
                .map(s -> service.submit(() -> Jsoup.connect("http://www." + s).get().html()))
                .collect(Collectors.toList());
        service.shutdown();
        while(!futures.isEmpty()){
            final Iterator<Future<String>> itr = futures.iterator();
            while(itr.hasNext()){
                final Future<String> future = itr.next();
                if(!future.isDone())
                    continue;
                itr.remove();
                System.out.println("Size: " + future.get().length());
            }
        }
    }
}
