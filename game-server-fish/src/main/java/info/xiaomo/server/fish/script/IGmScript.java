package info.xiaomo.server.fish.script;


import info.xiaomo.gengine.network.handler.HttpHandler;
import info.xiaomo.gengine.script.IScript;

/**
 * gm
 * <p>
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
