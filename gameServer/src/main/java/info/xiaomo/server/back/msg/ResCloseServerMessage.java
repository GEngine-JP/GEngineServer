package info.xiaomo.server.back.msg;


import info.xiaomo.server.server.AbstractMessage;
import io.netty.buffer.ByteBuf;

/**
 * 请求关服
 */
public class ResCloseServerMessage extends AbstractMessage {

    @Override
    public void doAction() {

    }

    public ResCloseServerMessage() {
        this.queueId = 1;
    }

    @Override
    public int getId() {
        return 1002;
    }

    /**
     * 执行代码
     */
    private int code;

    /**
     * 信息
     */
    private String info;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.code = readInt(buf);
        this.info = readString(buf);

        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {
        this.writeInt(buf, code);
        this.writeString(buf, info);

        return true;
    }
}

