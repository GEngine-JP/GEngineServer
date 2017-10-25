package info.xiaomo.server.system.schedule;

import info.xiaomo.server.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @author xiaomo
 */
public class ScheduleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleManager.class);

	private static final ScheduleManager INSTANCE = new ScheduleManager();

	private ScheduleManager(){	}

	public static ScheduleManager getInstance(){
		return INSTANCE;
	}


	public void start(){

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long dayDelay = calendar.getTimeInMillis() + TimeUtil.ONE_DAY_IN_MILLISECONDS - TimeUtil.getNowOfMills();

		LOGGER.info("距离零点剩余：{}MS", dayDelay);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(TimeUtil.getNowOfMills());
		int second = c.get(Calendar.SECOND);
		int millisecond = c.get(Calendar.MILLISECOND);
		long minuteDelay = TimeUtil.ONE_MINUTE_IN_MILLISECONDS - (second * 1000 + millisecond);

		LOGGER.info("距离整分剩余：{}MS", minuteDelay);

		long secondDelay = 1000 - millisecond;

		LOGGER.info("距离整秒剩余：{}MS", secondDelay);
		//每秒
//		ExecutorUtil.scheduleAtFixedRate(new ServerHeartTask(), secondDelay, TimeUtil.ONE_MILLS, TimeUnit.MILLISECONDS);
	}

}
