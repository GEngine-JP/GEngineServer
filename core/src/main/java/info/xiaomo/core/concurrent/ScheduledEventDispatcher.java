package info.xiaomo.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 场景定时事件派发器. </br>
 * 
 * 该分发器会按照设置好的周期和间隔将事件添加到QueueDriver中</br>
 * 
 * 只有初始化场景的时候，才能向派发器添加定时事件。
 * 
 * @author 张力
 * @date 2015-3-11 上午5:49:11
 * 
 */
public class ScheduledEventDispatcher implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledEventDispatcher.class);

	private boolean running = false;

	/**
	 * 事件列表
	 */
	private List<ScheduledEvent> events = new ArrayList<>();

	/**
	 * 场景id
	 */
	private int mapId;

	private int line;

	/**
	 * 该派发器所属场景的驱动器
	 */
	private QueueDriver diriver;

	/**
	 * 该派发器在线程池中的执行句柄
	 */
	private Future<?> future;

	public ScheduledEventDispatcher(QueueDriver driver, int mapId, int line) {
		super();
		this.diriver = driver;
		this.mapId = mapId;
		this.line = line;
	}

	/**
	 * 添加定时事件
	 * 
	 * @param event
	 *            定时事件
	 */
	public void addTimerEvent(ScheduledEvent event) {
		if (running) {
			throw new RuntimeException("已经启动的派发器不允许改变派发事件");
		}
		this.events.add(event);
		LOGGER.debug("ScheduledEvent事件:addScheduledEvent=" + this.mapId + "=event=" + event.getClass().getName());
	}

	/**
	 * 移除定时事件
	 * 
	 * @param event
	 *            定时事件
	 */
	public void removeTimerEvent(ScheduledEvent event) {
		if (running) {
			throw new RuntimeException("已经启动的派发器不允许改变派发事件");
		}
		this.events.remove(event);
		LOGGER.debug("ScheduledEvent事件:ScheduledEvent=remove");
	}

	/**
	 * 清除定时事件
	 */
	public void clearTimerEvent() {
		if (running) {
			throw new RuntimeException("已经启动的派发器不允许改变派发事件");
		}
		this.events.clear();
		LOGGER.debug("ScheduledEvent事件:ScheduledEvent=clear");
	}

	/**
	 * 停止
	 * 
	 * @param mayInterruptIfRunning
	 *            是否终端正在执行的操作
	 */
	public void stop(boolean mayInterruptIfRunning) {
		this.future.cancel(mayInterruptIfRunning);
	}

	@Override
	public void run() {
		Iterator<ScheduledEvent> it = this.events.iterator();
		long curTime = System.currentTimeMillis();
		while (it.hasNext()) {
			ScheduledEvent event = it.next();
			if (event.getEnd() - curTime <= 0L) {// 定时时间到
				if (event.getLoop() > 0) {
					// 设置下一个循环（同时更新了下一次的end时间）
					int loop = event.getLoop();
					loop--;
					if (loop == 0) {
						// 循环次数为0之后移除该事件
						it.remove();
						LOGGER.info(this.mapId + "移除定时事件：" + event.getClass().getName());
					} else {
						event.setLoop(loop);
						event.setEnd(curTime + event.getDelay());
					}

				} else {// 次数不限的周期时间
					// 设置下次执行时间
					event.setEnd(curTime + event.getDelay());
				}
				// 放入场景驱动
				this.diriver.addCommand(event);
			}

		}
	}

	public void start(ScheduledExecutorService service) {
		service.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
		this.running = true;
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

}
