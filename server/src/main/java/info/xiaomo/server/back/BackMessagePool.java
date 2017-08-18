package info.xiaomo.server.back;


import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

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
public class BackMessagePool implements MessagePool, HandlerPool {


    @Override
    public AbstractMessage getMessage(int messageId) {
        return null;
    }

    @Override
    public AbstractHandler getHandler(int messageId) {
        return null;
    }
}
