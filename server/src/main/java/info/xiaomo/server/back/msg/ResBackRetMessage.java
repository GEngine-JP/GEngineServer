package info.xiaomo.server.back.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 返回后台请求的结果信息
 */
public class ResBackRetMessage extends AbstractMessage {

	@Override
	public void doAction() {
		
	}
	
	public ResBackRetMessage() {
		this.queueId = 1;
	}
	
	@Override
	public int getId() {
		return 1004;
	}
	
	/**
	 * 返回信息
	 */
	private String ret;


	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	

	@Override
	public boolean read(KryoInput buf) {
		this.ret = readString(buf);

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		this.writeString(buf, ret);

		return true;
	}
}

