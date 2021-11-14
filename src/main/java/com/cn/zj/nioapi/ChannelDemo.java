package com.cn.zj.nioapi;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zjdking
 * @Description:
 * @date: 2021/11/14 17:29
 */
public class ChannelDemo {
    @Test
    public void channel_test() throws Exception {
        // 1.首先channel不能单独创建，需要通过输入输出流来获取。并且channel只是负责传输buffer,所以我们要配合buffer使用。
        FileInputStream is = new FileInputStream("C:\\Users\\code_\\Pictures\\Saved Pictures\\google.jpg");
        FileChannel in = is.getChannel();
        // 2.创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        FileOutputStream fos = new FileOutputStream("channel.jpg");
        FileChannel out = fos.getChannel();

        int len;
        while((len = in.read(buffer))!=-1){
            // 将position置为0；
            buffer.flip();
            out.write(buffer);
            // 强制落盘。
            out.force(true);
            // 清空，为了迎接新的数据。
            buffer.clear();
        }
        in.close();
        out.close();



    }
}
