package info.xiaomo.server;

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
 * Date  : 2017/7/11 15:10
 * desc  : 服务器上下文
 * Copyright(©) 2017 by xiaomo.
 */
public class Context {

    /**
     * 服务器配置
     */
    private static ServerOption serverOption;

    /**
     * 开服当天凌晨0点时间戳
     */
    private static long openDayZeroTime;


    /**
     * 合服日期当天凌晨0点时间戳
     */
    private static long combineDayZeroTime;

    private static boolean combined = false;

    public static void init(ServerOption serverOption){
        Context.serverOption = serverOption;
    }

    public static GameServer createServer(){
        return new GameServer(serverOption);
    }

    public static ServerOption getServerOption() {
        return serverOption;
    }

    public static void setServerOption(ServerOption serverOption) {
        Context.serverOption = serverOption;
    }

    public static long getOpenDayZeroTime() {
        return openDayZeroTime;
    }

    public static void setOpenDayZeroTime(long openDayZeroTime) {
        Context.openDayZeroTime = openDayZeroTime;
    }

    public static long getCombineDayZeroTime() {
        return combineDayZeroTime;
    }

    public static void setCombineDayZeroTime(long combineDayZeroTime) {
        Context.combineDayZeroTime = combineDayZeroTime;
    }

    public static boolean isCombined() {
        return combined;
    }

    public void setCombined(boolean combined) {
        this.combined = combined;
    }
}
