package info.xiaomo.server.hall.script;


import info.xiaomo.gengine.script.IScript;

/**
 * gm脚本
 * <p>
 * <p>
 * 2017年10月16日 下午6:04:35
 */
public interface IGmScript extends IScript {

	default String executeGm(long roleId, String gmCmd) {
		return String.format("GM {}未执行", gmCmd);
	}

	/**
	 * 是否为gm命令
	 * <p>
	 * <p>
	 * 2017年10月16日 下午6:07:32
	 *
	 * @param gmCmd
	 */
	default boolean isGMCmd(String gmCmd) {
		return gmCmd != null && gmCmd.startsWith("&");
	}
}
