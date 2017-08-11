package info.xiaomo.server.back.msg;


import info.xiaomo.core.net.kryo.KryoInput;
import info.xiaomo.core.net.kryo.KryoOutput;
import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.AbstractMessage;

/**
 * 请求关服
 */
public class ReqCloseServerMessage extends AbstractMessage {

	@Override
	public void action() {
		BackManager.getInstance().closeServer(sequence, session);
	}
	
	public ReqCloseServerMessage() {
		this.queueId = 1;
	}
	
	@Override
	public int getId() {
		return 1001;
	}
	


	@Override
	public boolean read(KryoInput buf) {

		return true;
	}

	@Override
	public boolean write(KryoOutput buf) {

		return true;
	}
}

