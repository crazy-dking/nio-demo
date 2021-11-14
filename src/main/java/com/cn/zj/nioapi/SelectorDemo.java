package com.cn.zj.nioapi;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description:
 * @author: zjdking
 * @date: 2021/11/14 19:21
 */
public class SelectorDemo {
    @Test
    public void selector_test() throws IOException {
        // 获取到channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 设置为非阻塞io
        channel.configureBlocking(false);
        // 监听端口
        channel.bind(new InetSocketAddress(8080));
        // 获取选择器
        Selector selector = Selector.open();
        // 将channel注册到selector上,类型是接受类型。
        channel.register(selector, SelectionKey.OP_ACCEPT);
        // selector中存在数据就进行循环
        while(selector.select()>0){
            // 通过迭代器获取其中selector key。这样做可以在遍历中进行删除
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while(iterator.hasNext()){
                SelectionKey sk = iterator.next();
                // 如果他是就绪状态就将他放入到读。
                if(sk.isAcceptable()){
                    SocketChannel socket = channel.accept();
                    socket.configureBlocking(false);
                    socket.register(selector,SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    // 我们想要操作数据就一定要有channel
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer bf = ByteBuffer.allocate(1024);

                    int len;
                    while((len = socketChannel.read(bf))!=-1){
                        bf.flip();
                        System.out.println(new String(bf.array(),0,len));
                        bf.clear();
                    }
                }
                // 操作完成我们要将任务移除。
                iterator.remove();
            }
        }
    }
}
