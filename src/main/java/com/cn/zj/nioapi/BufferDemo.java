package com.cn.zj.nioapi;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author: zjdking
 * @Description:
 * @date: 2021/11/13 22:50
 */
public class BufferDemo {
    @Test
    public void buffer_test(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("容量："+buffer.capacity());
        System.out.println("limit："+buffer.limit());
        System.out.println("position："+buffer.position());
        System.out.println("position到limit元素的中间位置个数 remaining："+buffer.remaining());
        System.out.println("===========================");
        buffer.put("我hello".getBytes());
        System.out.println("容量："+buffer.capacity());
        System.out.println("limit："+buffer.limit());
        System.out.println("position："+buffer.position());
        System.out.println("remaining："+buffer.remaining());
        System.out.println("===========================");
        System.out.println((char)buffer.get(3));
        // 他会将position变成0 limit设置为当前位置。
        buffer.flip();
        System.out.println("容量："+buffer.capacity());
        System.out.println("limit："+buffer.limit());
        System.out.println("position："+buffer.position());
        System.out.println("remaining："+buffer.remaining());
        System.out.println("===========================");
    }
}
