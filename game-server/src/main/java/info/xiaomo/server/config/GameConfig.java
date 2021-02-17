package info.xiaomo.server.config;

import info.xiaomo.gengine.bean.Config;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 游戏配置
 *
 *
 * @date 2017-04-14
 */
@EqualsAndHashCode(callSuper = true)
@Root
@Data
public class GameConfig extends Config {

	//线程房间数
	@Element(required = false)
	private int threadRoomNum = 4;

	// 房间位置大小
	@Element(required = false)
	private int roomSize = 4;

	// 机器人个数
	@Element(required = false)
	private int robotNum = 30;


}
