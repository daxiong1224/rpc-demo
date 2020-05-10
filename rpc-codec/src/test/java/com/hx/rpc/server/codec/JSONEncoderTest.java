package com.hx.rpc.server.codec;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONEncoderTest {

    @Test
    public void encode() {
        Encoder encoder = new JSONEncoder();
        TestBean testBean = new TestBean();
        testBean.setName("daxiong");
        testBean.setAge(19);

        byte[] bytes = encoder.encode(testBean);
        assertNotNull(bytes);
    }
}