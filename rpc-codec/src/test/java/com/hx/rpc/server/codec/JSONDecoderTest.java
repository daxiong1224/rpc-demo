package com.hx.rpc.server.codec;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONDecoderTest {

    @Test
    public void decode() {
        Encoder encoder = new JSONEncoder();
        TestBean testBean = new TestBean();
        testBean.setName("daxiong");
        testBean.setAge(19);

        byte[] bytes = encoder.encode(testBean);

        Decoder decoder = new JSONDecoder();
        TestBean bean = decoder.decode(bytes, TestBean.class);
        assertEquals(testBean.getName(), bean.getName());
        assertEquals(testBean.getAge(), bean.getAge());
    }
}