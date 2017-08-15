package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.IQueueCommand;
import info.xiaomo.server.server.MessageProcessor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
 * Date  : 2017/7/11 19:20
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class LoginAndLogoutProcessor implements MessageProcessor {
    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Login"));

    @Override
    public void process(IQueueCommand handler) {

        this.executor.execute(handler);

    }
}
