package info.xiaomo.server;

import info.xiaomo.core.util.Cast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 15:07
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
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

    private void build() throws IOException, ParseException {
        InputStream in = null;
        try {
            in = new FileInputStream(configPath);
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
            if (properties.getProperty("combineTime") != null) {
                this.combineTime = format.parse(properties.getProperty("combineTime"));
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


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public int getGameServerPort() {
        return gameServerPort;
    }

    public void setGameServerPort(int gameServerPort) {
        this.gameServerPort = gameServerPort;
    }

    public String getConfigDataPath() {
        return configDataPath;
    }

    public void setConfigDataPath(String configDataPath) {
        this.configDataPath = configDataPath;
    }

    public String getGameDbConfigPath() {
        return gameDbConfigPath;
    }

    public void setGameDbConfigPath(String gameDbConfigPath) {
        this.gameDbConfigPath = gameDbConfigPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public ServerOption(String configPath) throws IOException, ParseException {
        this.configPath = configPath;
        build();
    }


    public boolean isFcmCheck() {
        return fcmCheck;
    }

    public void setFcmCheck(boolean fcmCheck) {
        this.fcmCheck = fcmCheck;
    }

    public boolean isWgCheck() {
        return wgCheck;
    }

    public void setWgCheck(boolean wgCheck) {
        this.wgCheck = wgCheck;
    }

    public boolean isPushChat() {
        return pushChat;
    }

    public void setPushChat(boolean pushChat) {
        this.pushChat = pushChat;
    }

    public boolean isPushRole() {
        return pushRole;
    }

    public void setPushRole(boolean pushRole) {
        this.pushRole = pushRole;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public int getBackServerPort() {
        return backServerPort;
    }

    public void setBackServerPort(int backServerPort) {
        this.backServerPort = backServerPort;
    }

    public int getHttpServerPort() {
        return httpServerPort;
    }

    public void setHttpServerPort(int httpServerPort) {
        this.httpServerPort = httpServerPort;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCombineTime() {
        return combineTime;
    }

    public void setCombineTime(Date combineTime) {
        this.combineTime = combineTime;
    }

    public String getLogDBConfigPath() {
        return logDBConfigPath;
    }

    public void setLogDBConfigPath(String logDBConfigPath) {
        this.logDBConfigPath = logDBConfigPath;
    }

    public String getHttpDBConfigPath() {
        return httpDBConfigPath;
    }

    public void setHttpDBConfigPath(String httpDBConfigPath) {
        this.httpDBConfigPath = httpDBConfigPath;
    }
}
