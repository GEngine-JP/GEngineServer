package info.xiaomo.server.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2017/4/24 10:22.
 *
 * @author 周锟
 */
@Slf4j
@AllArgsConstructor
public class WrapTask implements Runnable {

	private Runnable task;

	@Override
	public void run() {
		try {
			task.run();
		} catch (Throwable t) {
			log.error("WrapTask", t);
		}
	}
}
