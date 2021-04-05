package com.github.remering.scratch.springboot.util;

import lombok.SneakyThrows;
import lombok.val;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;
import java.util.Base64;

public class Codec {

    private Codec() {
    }

    public static byte[] hexStr2Bytes(String hexStr) {
        return DatatypeConverter.parseHexBinary(hexStr);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytes2HexStr(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @SneakyThrows
    public static byte[] calculateSha256AndWrite(ReadableByteChannel channel, WritableByteChannel outChannel) {
        val block = ByteBuffer.allocate(4 * 1024 * 1024); // 4M
        val digest = MessageDigest.getInstance("SHA-256");
        while (channel.read(block) > 0){
            block.flip();
            digest.update(block); // block has array so it won't change it's position and limit
            block.rewind();
            outChannel.write(block);
            block.flip();
        }
        return digest.digest();
    }

    public String decodeBase64(String base64String) {
        return new String(Base64.getDecoder().decode(base64String));
    }
}
