package info.xiaomo.server.back.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 请求关服
 */
public class ResCloseServerMessage extends AbstractMessage {

	@Override
	public void doAction() {
		
	}
	
	public ResCloseServerMessage() {
		this.queueId = 1;
	}
	
	@Override
	public int getId() {
		return 1002;
	}
	
	/**
	 * 执行代码
	 */
	private int code;

	/**
	 * 信息
	 */
	private String info;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	

	@Override
	public boolean read(KryoInput buf) {
		this.code = readInt(buf, false);
		this.info = readString(buf);

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		this.writeInt(buf, code, false);
		this.writeString(buf, info);

		return true;
	}
}

