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

    public static void init(ServerOption serverOption){
        Context.serverOption = serverOption;
    }

    public static GameServer createServer(){
        return new GameServer(serverOption);
    }
}
