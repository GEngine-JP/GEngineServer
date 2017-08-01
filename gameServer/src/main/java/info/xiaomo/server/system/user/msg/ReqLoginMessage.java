package info.xiaomo.server.system.user.msg;

import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.user.UserManager;
import io.netty.buffer.ByteBuf;


/**
 * 请求登录
 */
public class ReqLoginMessage extends AbstractMessage {

    @Override
    public void doAction() {
        UserManager.getInstance().login(session, loginName);
    }

    public ReqLoginMessage() {
        this.queueId = 1;
    }

    @Override
    public int getId() {
        return 1001;
    }

    /**
     * 登录账户
     */
    private String loginName;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.loginName = readString(buf);

        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {
        this.writeString(buf, loginName);

        return true;
    }
}

