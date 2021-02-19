package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.Item;
import info.xiaomo.server.shared.entity.UserRole;

/**
 * 道具
 *
 *  下午4:18:44
 */
public interface IPacketScript extends IScript {

	/**
	 * 使用道具
	 *
	 *  下午4:20:16
	 *
	 * @param id           道具Id
	 * @param num          数量
	 * @param itemConsumer
	 */
	default void useItem(UserRole userRole, long id, int num, GlobalReason reason, Consumer<Item> itemConsumer) {
	}

	/**
	 * 添加道具
	 *
	 *  下午4:23:47
	 *
	 * @param configId
	 * @param num          数量
	 * @param reason
	 * @param itemConsumer
	 */
	default Item addItem(
			UserRole userRole, int configId, int num, GlobalReason reason, Consumer<Item> itemConsumer) {
		return null;
	}
}
