package info.xiaomo.server.system.user.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 通知登录成功
 */
public class ResLoginMessage extends AbstractMessage {

	@Override
	public void doAction() {
		
	}
	
	public ResLoginMessage() {
		this.queueId = 1;
	}
	
	@Override
	public int getId() {
		return 1007;
	}
	
	/**
	 * 玩家id
	 */
	private long uid;


	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	

	@Override
	public boolean read(KryoInput buf) {
		this.uid = readLong(buf);

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {
		this.writeLong(buf, uid);

		return true;
	}
}

