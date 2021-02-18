package info.xiaomo.server.fish.event;

import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.server.fish.event.listener.SecondEventListener;

public class EventRegister {

	/**
	 * 可以隨時新增事件
	 */
	public static void registerPreparedListeners() {
		EventUtil.addListener(new SecondEventListener(), EventType.SERVER_SECOND_HEART);


	}

	private static void addLogoutListener() {
	}

	private static void addServerMinuteListener() {
	}

	private static void addServerSecondListener() {
	}

	/**
	 * 登录事件监听器
	 */
	private static void addLoginListener() {
	}
}
