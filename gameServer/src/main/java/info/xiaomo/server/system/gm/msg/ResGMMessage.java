package info.xiaomo.server.system.gm.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 返回GM命令执行结果
 */
public class ResGMMessage extends AbstractMessage {

	@Override
	public void doAction() {
		
	}
	
	public ResGMMessage() {
		this.queueId = 2;
	}
	
	@Override
	public int getId() {
		return 6002;
	}
	
	/**
	 * 执行结果
	 */
	private String content;


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	@Override
	public boolean read(KryoInput buf) {

		this.content = readString(buf);

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		this.writeString(buf, content);

		return true;
	}
}

