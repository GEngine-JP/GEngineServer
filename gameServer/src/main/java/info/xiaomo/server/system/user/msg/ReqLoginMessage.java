package info.xiaomo.server.system.user.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.user.UserManager;

/**
 * 请求登录
 */
public class ReqLoginMessage extends AbstractMessage {

    @Override
    public void action() {
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

    /**
     * 服务器id
     */
    private int sid;

    /**
     * 平台id
     */
    private int pid;

    /**
     * 登录方式（1、网页，2、微端
     */
    private int client;

    /**
     * 身份证号码
     */
    private String IDNumber;

    /**
     * ip地址
     */
    private String ip;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }



    @Override
    public boolean read(KryoInput buf) {
        this.loginName = readString(buf);
        return true;
    }

    @Override
    public boolean write(KryoOutput buf) {
        this.writeString(buf, loginName);
        return true;
    }
}

