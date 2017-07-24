package info.xiaomo.server.system.user.msg;


import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.user.UserManager;
import io.netty.buffer.ByteBuf;

/**
 * 请求关服
 */
public class ReqLoginMessage extends AbstractMessage {

    @Override
    public void doAction() {
        UserManager.getInstance().login(session, loginName);
    }

    public ReqLoginMessage() {
        this.queueId = 1;
    }

    private String loginName;

    @Override
    public int getId() {
        return 1007;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.loginName = readString(buf);
        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {

        return true;
    }
}

