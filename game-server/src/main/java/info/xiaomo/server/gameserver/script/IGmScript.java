package info.xiaomo.server.gameserver.script;


import info.xiaomo.gengine.network.handler.HttpHandler;
import info.xiaomo.gengine.script.IScript;

/**
 * gm
 *
 * 2017-04-17
 */
public interface IGmScript extends IScript {

	/**
	 * 验证http请求sid
	 *
	 * @param handler
	 * @return
	 */
	default boolean authHttpSid(HttpHandler handler) {
		return false;
	}
}
