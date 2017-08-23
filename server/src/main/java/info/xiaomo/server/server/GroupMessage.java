package info.xiaomo.server.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class GroupMessage extends AbstractMessage {

    private byte[] bytes = null;


    @Override
    public int getId() {
        return 0;
    }

    public void addMessage(AbstractMessage message) {

        ByteBuf curBuff = null;
        try {
            curBuff = message.encode();
            if (curBuff == null || curBuff.readableBytes() == 0) {
                return;
            }
            byte[] curBytes = curBuff.array();
            if (bytes != null && bytes.length > 0) {
                byte[] oldBytes = bytes;
                bytes = new byte[oldBytes.length + curBytes.length];
                if (oldBytes.length > 0) {
                    System.arraycopy(oldBytes, 0, bytes, 0, oldBytes.length);
                }
                System.arraycopy(curBytes, 0, bytes, oldBytes.length, curBytes.length);
            } else {
                bytes = curBytes;
            }
            this.setSize(bytes.length);
        } finally {
            if (curBuff != null) {
                curBuff.release();
            }
        }
    }


    @Override
    public ByteBuf encode() {
        if (this.bytes == null || this.bytes.length == 0) {
            return null;
        }
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(this.bytes.length);
        buf.readBytes(this.bytes);
        return buf;
    }

    @Override
    public void decode(byte[] bytes) {
    }


    @Override
    public void doAction() {

    }
}
