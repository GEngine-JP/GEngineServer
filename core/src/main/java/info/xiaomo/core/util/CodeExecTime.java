package info.xiaomo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeExecTime {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeExecTime.class);
	/**
	 * 超时阀值
	 */
	private int threshold;
	/**
	 * 开始时间
	 */
	private long startTime;
	/**
	 * 上一次step时间
	 */
	private long lastStepTime;

	public static CodeExecTime newCodeExecTime() {
		return new CodeExecTime();
	}

	public static CodeExecTime newCodeExecTime(int threshold) {
		return new CodeExecTime(threshold);
	}

	public CodeExecTime() {
		this.threshold = 50;
		this.startTime = System.currentTimeMillis();
		this.lastStepTime = this.startTime;
	}

	public CodeExecTime(int threshold) {
		this.threshold = threshold;
		this.startTime = System.currentTimeMillis();
		this.lastStepTime = this.startTime;
	}

	public boolean total(Logger logger, int threshold, String msg) {
		try {
			long time = System.currentTimeMillis() - this.startTime;
			if (time >= threshold) {
				logger.error(String.format("total:代码执行超时：%s,超时 %d>=%d ms", msg, time, threshold));
				return true;
			}
			return false;
		} finally {
			this.lastStepTime = System.currentTimeMillis();
		}
	}

	public boolean total(Logger logger, String msg) {
		return total(logger, this.threshold, msg);
	}

	public boolean total(int threshold, String msg) {
		return total(LOGGER, threshold, msg);
	}

	public boolean total(String msg) {
		return total(LOGGER, this.threshold, msg);
	}

	public boolean step(Logger logger, int threshold, String msg) {
		try {
			long time = System.currentTimeMillis() - this.lastStepTime;
			if (time >= threshold) {
				logger.error(String.format("step:代码执行超时：%s,超时 %d>=%d ms", msg, time,threshold));
				return true;
			}
			return false;
		} finally {
			this.lastStepTime = System.currentTimeMillis();
		}
	}

	public boolean step(Logger logger, String msg) {
		return step(logger, this.threshold, msg);
	}

	public boolean step(int threshold, String msg) {
		return step(LOGGER, threshold, msg);
	}

	public boolean step(String msg) {
		return step(LOGGER, this.threshold, msg);
	}

	public void restStep() {
		this.lastStepTime = System.currentTimeMillis();
	}

	public void restTotal() {
		this.startTime = System.currentTimeMillis();
		this.lastStepTime = this.startTime;
	}
}
