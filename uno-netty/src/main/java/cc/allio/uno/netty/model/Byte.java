package cc.allio.uno.netty.model;

import java.io.Serializable;

/**
 * @author jiangw
 * @date 2020/11/25 17:51
 * @since 1.0
 */
public class Byte implements Serializable {

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int length() {
        return bytes.length;
    }
}
