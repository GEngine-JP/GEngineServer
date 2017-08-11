package info.xiaomo.server.system.gm.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;
import info.xiaomo.server.system.gm.GMManager;

/**
 * 请求GM命令
 */
public class ReqGMMessage extends AbstractMessage {

	@Override
	public void action() {
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

