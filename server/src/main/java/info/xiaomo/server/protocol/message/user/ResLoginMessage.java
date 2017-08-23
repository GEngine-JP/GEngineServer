package info.xiaomo.server.protocol.message.user;

import info.xiaomo.server.protocol.proto.UserProto.LoginResponse;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 登录返回
 */
public class ResLoginMessage extends AbstractMessage {

    private LoginResponse res;

    @Override
    public int getId() {
        return 101102;
    }

    @Override
    public byte[] getContent() {
        return res.toByteArray();
    }

    public void setLoginResponse(LoginResponse res) {
        this.res = res;
    }


}

