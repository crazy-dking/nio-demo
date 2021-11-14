package com.cn.zj.nioapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: zjdking
 * @Description: 我们需要记录下来list
 * @date: 2021/11/14 17:29
 */
public class Server {
    //
    private Selector selector;
    private ServerSocketChannel severSocketChannel;
    public static void main(String[] args){
        // 初始化 selector serverChannel
        Server server = new Server();
        // 监听客户端
        server.listen();
    }

    private void listen() {
        try{
            while(selector.select()>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey sk = iterator.next();
                    if(sk.isAcceptable()){
                        // 如果是已经链接状态就转化成准备读状态
                        SocketChannel socketChannel = severSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else if(sk.isReadable()){
                        // 处理客户端消息
                        readClientData(sk);

                    }
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readClientData(SelectionKey sk) {
        SocketChannel channel = null;
        try{
            channel = (SocketChannel) sk.channel();
            // 这个channel就绪的channel
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = channel.read(buffer);
            if(len>0){
                buffer.flip();
                // 发送消息给所有人
                String msg = new String(buffer.array(), 0, buffer.remaining());
                System.out.println(msg);
                sendMsgToAll(msg,channel);
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                System.out.println("有人撤离"+channel.getRemoteAddress());
                sk.cancel();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void sendMsgToAll(String msg, SocketChannel channel) throws IOException {
        for(SelectionKey sk: selector.keys()){
            Channel  channel1 = sk.channel();
            //不发送给自己
            if(channel1 instanceof  SocketChannel && channel != channel1) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                channel.write(buffer);
            }
        }
    }

    public Server(){
        try {
            severSocketChannel = ServerSocketChannel.open();
            severSocketChannel.configureBlocking(false);
            severSocketChannel.bind(new InetSocketAddress(8080));
            selector = Selector.open();
            severSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
