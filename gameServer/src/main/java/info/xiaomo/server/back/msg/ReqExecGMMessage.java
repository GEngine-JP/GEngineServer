package info.xiaomo.server.back.msg;

import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.AbstractMessage;


/**
 * 请求执行GM命令
 */
public class ReqExecGMMessage extends AbstractMessage {

	@Override
	public void action() {
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
	public boolean read(KryoInput buf) {
		this.command = readString(buf);

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		this.writeString(buf, command);

		return true;
	}
}

