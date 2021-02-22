package info.xiaomo.server.rpg.server;


import java.util.Date;
import lombok.Data;

@Data
public class ServerOption {

	private int serverId;

	private int serverType;

	private int gameServerPort;

	private int backServerPort;

	private String configDataPath;

	private String gameDbConfigPath;

	private String configPath;

	private boolean isDebug;

	private int platformId;

	private int httpServerPort;

	private Date openTime;

	private Date combineTime;

	private String logDBConfigPath;

	private String httpDBConfigPath;


}
