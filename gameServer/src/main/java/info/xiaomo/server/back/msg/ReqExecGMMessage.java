package info.xiaomo.server.back.msg;


import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.AbstractMessage;
import io.netty.buffer.ByteBuf;

/**
 * 请求执行GM命令
 */
public class ReqExecGMMessage extends AbstractMessage {

    @Override
    public void doAction() {
        BackManager.getInstance().exeGM(sequence, session, command);
    }

    public ReqExecGMMessage() {
        this.queueId = 1;
    }

    @Override
    public int getId() {
        return 1003;
    }

    /**
     * 命令
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

