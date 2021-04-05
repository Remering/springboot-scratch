package com.github.remering.scratch.springboot.util.converter;


import lombok.val;
import org.springframework.core.convert.converter.Converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class UUID2BytesConverter implements Converter<UUID, byte[]> {


    private final ThreadLocal<ByteBuffer> threadLocalByteBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocate(8 * 2).order(ByteOrder.BIG_ENDIAN)
    );

    private ByteBuffer byteBuffer() {
        return threadLocalByteBuffer.get();
    }

    @Override
    public byte[] convert(UUID source) {
        val buffer = byteBuffer();
        buffer.clear();
        buffer.putLong(source.getMostSignificantBits());
        buffer.putLong(source.getLeastSignificantBits());
        return buffer.array();
    }
}
