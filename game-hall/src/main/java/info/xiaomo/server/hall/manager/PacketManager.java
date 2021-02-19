package info.xiaomo.server.hall.manager;

import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.hall.script.IPacketScript;
import info.xiaomo.server.shared.entity.Item;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.protocol.hall.packet.PacketItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 背包
 *
 * <p>2017年9月18日 下午2:49:17
 */
public class PacketManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(PacketManager.class);
	private static volatile PacketManager packetManager;

	private PacketManager() {
	}

	public static PacketManager getInstance() {
		if (packetManager == null) {
			synchronized (PacketManager.class) {
				if (packetManager == null) {
					packetManager = new PacketManager();
				}
			}
		}
		return packetManager;
	}

	/**
	 * 使用道具
	 *
	 * <p>2017年9月18日 下午4:25:54
	 *
	 * @param id
	 * @param num
	 * @param reason
	 * @param itemConsumer
	 */
	public void useItem(UserRole userRole, long id, int num, GlobalReason reason, Consumer<Item> itemConsumer) {
		ScriptManager.getInstance()
				.getBaseScriptEntry()
				.executeScripts(
						IPacketScript.class, script -> script.useItem(userRole, id, num, reason, itemConsumer));
	}

	/**
	 * 添加道具
	 *
	 * <p>2017年9月18日 下午4:23:47
	 *
	 * @param configId
	 * @param num          数量
	 * @param reason
	 * @param itemConsumer
	 */
	public Item addItem(
			UserRole userRole, int configId, int num, GlobalReason reason, Consumer<Item> itemConsumer) {
		return ScriptManager.getInstance()
				.getBaseScriptEntry()
				.functionScripts(
						IPacketScript.class,
						(IPacketScript script) -> script.addItem(userRole, configId, num, reason, itemConsumer));
	}

	/**
	 * 构建
	 *
	 * <p>2017年9月18日 下午4:07:49
	 *
	 * @param Item
	 * @return
	 */
	public PacketItem buildPacketItem(Item Item) {
		PacketItem.Builder builder = PacketItem.newBuilder();
		builder.setId(Item.getId());
		builder.setConfigId(Item.getConfigId());
		builder.setNum(Item.getNum());
		return builder.build();
	}

	/**
	 * 获取物品
	 *
	 * <p>2017年9月18日 下午5:10:36
	 *
	 * @param itemId
	 * @return
	 */
	public Item getItem(UserRole userRole, long itemId) {
//		return (Item) userRole.getItem(itemId);
		return null;
	}
}
