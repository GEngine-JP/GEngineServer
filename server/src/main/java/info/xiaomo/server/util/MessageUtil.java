package info.xiaomo.server.util;


import info.xiaomo.core.net.Message;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionManager;

import java.util.Collection;

public class MessageUtil {

	public static void sendMsg(Message msg, long id) {
		Session session = SessionManager.getInstance().getSession(id);
		if (session == null) {
			return;
		}
		session.sendMessage(msg);
	}

	public static void sendMsgToRids(Message msg, long... rids) {
		for (long rid : rids) {
			sendMsg(msg, rid);
		}
	}

	public static void sendMsgToRids(Message msg, Collection<Long> rids) {
		for (Long rid : rids) {
			if (rid != null) {
				sendMsg(msg, rid);
			}
		}
	}

	public static void sendMsgToRids(Message msg, Collection<Long> rids, Long exceptRoleId) {
		for (Long rid : rids) {
			if (rid != null && (!rid.equals(exceptRoleId))) {
				sendMsg(msg, rid);
			}
		}
	}
	
//	public static void sendRoundMessage(Message msg, IMapObject obj) {
//		GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
//		if (map == null) {
//			return;
//		}
//		Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());
//		sendMsgToRids(msg, watchers.keySet());
//	}
//
//	public static void sendRoundMessage(Message msg, IMapObject obj, long excludeId) {
//		GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
//		if (map == null) {
//			return;
//		}
//		Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());
//
//		Set<Long> set = new HashSet<>(watchers.keySet());
//		set.remove(excludeId);
//
//		sendMsgToRids(msg, watchers.keySet());
//	}
//
//	public static void sendMapMessage(Message msg, GameMap map) {
//		Map<Long, Player> players = map.getPlayerMap();
//		sendMsgToRids(msg, players.keySet());
//	}

}
