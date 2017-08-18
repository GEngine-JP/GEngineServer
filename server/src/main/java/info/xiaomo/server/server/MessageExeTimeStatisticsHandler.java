package info.xiaomo.server.server;


import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.base.concurrent.HandlerFilter;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.entify.Player;
import info.xiaomo.server.util.MapUtil;
import info.xiaomo.server.util.MsgExeTimeUtil;

public class MessageExeTimeStatisticsHandler implements HandlerFilter {

	@Override
	public boolean before(AbstractHandler handler) {
		handler.setStartTime(System.currentTimeMillis());
		return true;
	}

	@Override
	public boolean after(AbstractHandler handler) {
		long total = System.currentTimeMillis() - handler.getStartTime();
		Session session = (Session) handler.getParam();
		if (session == null) {
			return false;
		}

		Player player = session.getPlayer();
		if (player == null) {
			return false;
		}

		Long mapKey = MapUtil.getMapKey(player.getMapId(), player.getLine());

		AbstractMessage msg = (AbstractMessage) handler.getMessage();
		MsgExeTimeUtil.ThreadExeTime threadExeTime = MsgExeTimeUtil.getTimeMap().get(mapKey);
		int idMsg = ((AbstractMessage) handler.getMessage()).getId();
		if (threadExeTime == null) {
			MsgExeTimeUtil.ThreadExeTime threadExeTime1 = MsgExeTimeUtil.getInstance().newThreadExeTime();
			MsgExeTimeUtil.getTimeMap().put(mapKey, threadExeTime1);
			threadExeTime1.allExeTime += total;
			threadExeTime1.timeMapAll.put(idMsg, total);
			threadExeTime1.timeMapMax.put(idMsg, total);
		} else {
			threadExeTime.allExeTime += total;
			Long aLong = threadExeTime.timeMapAll.get(idMsg);
			if (aLong == null) {
				threadExeTime.timeMapAll.put(idMsg, total);
			} else {
				threadExeTime.timeMapAll.put(idMsg, total + aLong);
			}
			Long timeMax = threadExeTime.timeMapMax.get(idMsg);
			if (timeMax == null) {
				threadExeTime.timeMapMax.put(idMsg, total);
			} else if (total > timeMax) {
				threadExeTime.timeMapMax.put(idMsg, total);
			}
		}
		return true;
	}

}
