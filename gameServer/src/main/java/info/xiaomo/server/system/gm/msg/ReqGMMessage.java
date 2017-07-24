package info.xiaomo.server.system.gm.msg;


import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.gm.GMManager;
import io.netty.buffer.ByteBuf;

/**
 * 请求GM命令
 */
public class ReqGMMessage extends AbstractMessage {

    @Override
    public void doAction() {
        GMManager.getInstance().execGMCmdFromGame(session, command);
    }

    public ReqGMMessage() {
        this.queueId = 2;
    }

    @Override
    public int getId() {
        return 6001;
    }

    /**
     * gm命令
     */
    private String command;


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.command = readString(buf);

        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {
        this.writeString(buf, command);

        return true;
    }
}

