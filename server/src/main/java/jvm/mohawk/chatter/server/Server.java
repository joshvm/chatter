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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.io.IOException;
import java.nio.file.Path;
import jvm.mohawk.chatter.server.net.codec.PacketDecoder;
import jvm.mohawk.chatter.server.net.codec.PacketEncoder;
import jvm.mohawk.chatter.server.utils.Props;
import jvm.mohawk.chatter.server.utils.Utils;

public class Server {

    public static class Config {

        public static final String DEFAULT_HOST = "localhost";

        private final String host;
        private final int port;

        public Config(final String host,
                      final int port){
            this.host = host;
            this.port = port;
        }

        public String host(){
            return host;
        }

        public int port(){
            return port;
        }

        public static Config parse(final Path path) throws IOException {
            final Props props = Utils.props(path);
            return new Config(
                    props.string("host", DEFAULT_HOST),
                    props.integer("port")
            );
        }
    }

    private final Config config;

    private NioServerSocketChannel channel;

    public Server(final Config config){
        this.config = config;
    }

    public Config config(){
        return config;
    }

    public void init(){

    }

    public void start() throws InterruptedException {
        final NioEventLoopGroup boss = new NioEventLoopGroup();
        final NioEventLoopGroup worker = new NioEventLoopGroup();
        final ServerBootstrap bs = new ServerBootstrap();
        bs.group(boss, worker);
        bs.channel(NioServerSocketChannel.class);
        bs.childHandler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) throws Exception{
                        ch.pipeline().addLast("encoder", new PacketEncoder());
                        ch.pipeline().addLast("decoder", new PacketDecoder());
                        ch.pipeline().addLast("handler", new ServerHandler());
                    }
                }
        );
        channel = (NioServerSocketChannel) bs.bind(config.host, config.port).sync().channel();
        try{
            channel.closeFuture().sync();
        }finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void stop(){
        if(channel != null)
            channel.disconnect();
    }
}
