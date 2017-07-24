package info.xiaomo.server.system.user.msg;


import info.xiaomo.server.server.AbstractMessage;
import io.netty.buffer.ByteBuf;

/**
 * 通知登录成功
 */
public class ResLoginMessage extends AbstractMessage {

    @Override
    public void doAction() {

    }

    public ResLoginMessage() {
        this.queueId = 1;
    }

    @Override
    public int getId() {
        return 1008;
    }

    /**
     * 玩家id
     */
    private long uid;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.uid = readLong(buf);

        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {
        this.writeLong(buf, uid);

        return true;
    }
}

