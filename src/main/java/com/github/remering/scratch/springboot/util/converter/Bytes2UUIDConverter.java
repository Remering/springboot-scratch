package com.github.remering.scratch.springboot.util.converter;


import lombok.val;
import org.springframework.core.convert.converter.Converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class Bytes2UUIDConverter implements Converter<byte[], UUID> {

    private final ThreadLocal<ByteBuffer> threadLocalByteBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocate(8 * 2).order(ByteOrder.BIG_ENDIAN)
    );

    private ByteBuffer byteBuffer() {
        return threadLocalByteBuffer.get();
    }

    @Override
    public UUID convert(byte[] source) {
        val buffer = byteBuffer();
        buffer.clear();
        buffer.put(source);
        buffer.flip();
        val msb = byteBuffer().getLong();
        val lsb = byteBuffer().getLong();
        return new UUID(msb, lsb);
    }
}
