package info.xiaomo.server.rpg.server;


import info.xiaomo.gengine.utils.Cast;
import info.xiaomo.gengine.utils.FileLoaderUtil;
import lombok.Data;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 15:07
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class ServerOption {

    private int serverId;

    private int serverType;

    private int gameServerPort;

    private String configDataPath;

    private String gameDbConfigPath;

    private String configPath;

    private boolean isDebug;

    private boolean fcmCheck;

    private boolean wgCheck;

    private boolean pushChat;

    private boolean pushRole;

    private int platformId;

    private int backServerPort;

    private int httpServerPort;

    private String desKey;

    private Date openTime;

    private Date combineTime;

    private String logDBConfigPath;

    private String httpDBConfigPath;

    private String springConfigFile;

    public ServerOption(String configPath) throws IOException, ParseException {
        this.configPath = configPath;
        build();
    }

    private void build() throws IOException, ParseException {
        InputStreamReader in = null;
        try {
            in = FileLoaderUtil.findInputStreamByFileName(configPath);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Properties properties = new Properties();
            properties.load(in);

            this.serverId = Cast.toInteger(properties.get("serverId"));
            this.configDataPath = (String) properties.get("configDataPath");
            this.gameDbConfigPath = (String) properties.get("gameDbConfigPath");
            this.serverType = Cast.toInteger(properties.get("serverType"));
            this.gameServerPort = Cast.toInteger(properties.get("gameServerPort"));
            this.isDebug = Boolean.parseBoolean(properties.getProperty("isDebug"));
            this.fcmCheck = Boolean.parseBoolean(properties.getProperty("fcmCheck"));
            this.wgCheck = Boolean.parseBoolean(properties.getProperty("wgCheck"));
            this.pushChat = Boolean.parseBoolean(properties.getProperty("pushChat"));
            this.pushRole = Boolean.parseBoolean(properties.getProperty("pushRole"));
            this.desKey = properties.getProperty("desKey");
            this.gameServerPort = Integer.parseInt(properties.getProperty("gameServerPort"));
            this.backServerPort = Integer.parseInt(properties.getProperty("backServerPort"));
            this.httpServerPort = Integer.parseInt(properties.getProperty("httpServerPort"));
            this.configDataPath = properties.getProperty("configDataPath");
            this.platformId = Cast.toInteger(properties.getProperty("platformId"));


            this.openTime = format.parse(properties.getProperty("openTime"));
            String combineTime = "combineTime";
            if (properties.getProperty(combineTime) != null) {
                this.combineTime = format.parse(properties.getProperty(combineTime));
            }

            this.gameDbConfigPath = properties.getProperty("gameDbConfigPath");
            this.logDBConfigPath = properties.getProperty("logDBConfigPath");
            this.httpDBConfigPath = properties.getProperty("httpDBConfigPath");

            this.fcmCheck = Boolean.parseBoolean(properties.getProperty("fcmCheck"));
            this.wgCheck = Boolean.parseBoolean(properties.getProperty("wgCheck"));
            this.pushChat = Boolean.parseBoolean(properties.getProperty("pushChat"));
            this.pushRole = Boolean.parseBoolean(properties.getProperty("pushRole"));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
