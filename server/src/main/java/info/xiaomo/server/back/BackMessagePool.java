package info.xiaomo.server.back;


import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.server.system.user.handler.LoginHandler;

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
 * Date  : 2017/7/11 16:00
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class BackMessagePool extends MessagePool {

    /**
     * 在这里注册消息
     */
    public BackMessagePool() {
        registerGM();
    }

    /**
     * 注册gm
     */
    private void registerGM() {
        register(1, LoginHandler.class);
    }


    /**
     * 注册用户
     */
    private void registerUser() {
    }


}
