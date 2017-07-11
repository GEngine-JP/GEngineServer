package com.sh.game.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sh.game.server.Session;
import com.sh.game.server.SessionManager;
import com.sh.game.system.map.MapManager;
import com.sh.game.system.map.obj.IMapObject;
import com.sh.game.system.map.obj.Player;
import com.sh.game.system.map.scene.GameMap;
import com.sh.net.Message;

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
	
	public static void sendRoundMessage(Message msg, IMapObject obj) {
		GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
		if (map == null) {
			return;
		}
		Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());
		sendMsgToRids(msg, watchers.keySet());
	}
	
	public static void sendRoundMessage(Message msg, IMapObject obj, long excludeId) {
		GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
		if (map == null) {
			return;
		}
		Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());
		
		Set<Long> set = new HashSet<>(watchers.keySet());
		set.remove(excludeId);
		
		sendMsgToRids(msg, watchers.keySet());
	}
	
	public static void sendMapMessage(Message msg, GameMap map) {
		Map<Long, Player> players = map.getPlayerMap();
		sendMsgToRids(msg, players.keySet());
	}

}
