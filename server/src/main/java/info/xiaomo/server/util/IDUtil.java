package info.xiaomo.server.util;

import info.xiaomo.server.server.GameContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器配置
 *
 * @author Administrator
 */
public class IDUtil {

	/**
	 * 2017年4月1日凌晨
	 */
	public static final long _2017_4_1_0_0_0_0 = 1490976000000L;
	/**
	 * 计数器
	 */
	private static int id = 0;

	/**
	 * 当前秒数
	 */
	private static long curSec = System.currentTimeMillis() / 1000L;

	private static long nowSec = getNowSec();
	
	private static final AtomicInteger INSTANCE_ID = new AtomicInteger(10000);

	/**
	 * 锁对象
	 */

	private final static Object obj = new Object();

	/**
	 * 获取一个唯一ID ID 是一个64位long 16位服务器id + 48位时间秒数 + 16位自增ID
	 * <p>
	 * <p>
	 * 规则：1、服务器id不变 ，2、时间随着当前时间变更，3、自增id如果从1到65000后，ID复位，此时需要时间增1防止重复
	 * 4、自增后的时间小于了当前时间，那么就更新当前时间
	 *
	 * @return
	 */
	public static long getId() {
		int nowId;
		long nowCurSec = System.currentTimeMillis() / 1000L;
		synchronized (obj) {
			// ID增1
			id += 1;
			// 当前ID赋值id
			nowId = id;

			if (id > 65000) {
				// 如果ID大于65000 这里其实是 2的16次方 = 65535 因为自增ID只能占16位，所以不能超过65000
				// ID大于65000后id复位，如果时间不增1，那么将会产生重复
				id = 0;
				// 每过65000当前秒数就增1
				curSec += 1L;
			}

			if (nowCurSec > curSec) {
				// 自增后的时间小于了当前时间，那么就更新自增时间为当前时间
				curSec = nowCurSec;
			} else {
				// 自增时间大于当前时间（这种情况在id获取速度过快，1秒中内获取了额超过65000个id的时候会出现），那么就以自增时间为准
				nowCurSec = curSec;
			}
		}
		return ((long) (GameContext.getOption().getServerId() & 0xFFFF)) << 48 | (nowCurSec) << 16 | nowId & 0xFFFF;
	}

	public static long getNewId() {
		int nowId;
		long now = getNowSec();
		synchronized (obj) {
			id += 1;
			if (id > 65000) {
				id = 0;
				nowSec += 1L;
			}
			nowId = id;
			if (now > nowSec) {
				nowSec = now;
			} else {
				now = nowSec;
			}
		}
		return ((GameContext.getOption().getServerId() & 0x7_FFFFL) << 45) | ((now & 0x1FFF_FFFF) << 16) | (nowId & 0xFFFF);
	}

	private static long getNowSec() {
		return (System.currentTimeMillis() - _2017_4_1_0_0_0_0) / 1000L;
	}
	
	public static int getInstanceId(){
		return INSTANCE_ID.incrementAndGet();
	}
}