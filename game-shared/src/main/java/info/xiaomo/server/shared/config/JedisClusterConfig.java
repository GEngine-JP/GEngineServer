package info.xiaomo.server.shared.config;

import java.util.List;
import lombok.Data;

@Data
public class JedisClusterConfig {

	private int poolMaxTotal;

	private int poolMaxIdle;

	private int connectionTimeout;

	private int soTimeout;

	private int maxRedirections;

	private int timeBetweenEvictionRunsMillis;

	private int minEvictableIdleTimeMillis;

	private int softMinEvictableIdleTimeMillis;

	private int maxWaitMillis;

	private boolean testOnBorrow;

	private boolean testWhileIdle;

	private boolean testOnReturn;

	private List<Node> nodes;


	@Data
	public static class Node {
		private String ip;
		private int port;
	}
}
