package com.cn.zj.nioapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author: zjdking
 * @Description:
 * @date: 2021/11/14 17:29
 */
public class Client {
    private Selector selector;
    private SocketChannel socketChannel;
    public static void main(String[] args) {
        Client clinet = new Client();
        // 开启一个线程进行监控服务端发送过来的读事件。
        new Thread(()-> {
            while(true){
                clinet.readServerInfo();
            }
        }).start();
        Scanner scanner =new Scanner(System.in);
        while(scanner.hasNextLine()){
            String msg = scanner.nextLine();
            clinet.sendToServer(msg);
        }
    }

    private void sendToServer(String msg) {
        try {
            socketChannel.write(ByteBuffer.wrap(("dk :"+msg).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readServerInfo() {
        try{
            // 此处外边已经while了
            if(selector.select()>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey sk = iterator.next();
                    // 可读的
                    if(sk.isReadable()){
                        SocketChannel channel = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        buffer.flip();
                        String msg = new String(buffer.array(),0,buffer.remaining());
                        buffer.clear();
                        System.out.println(msg);

                    }
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Client() {
        try{
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端准备完成");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
