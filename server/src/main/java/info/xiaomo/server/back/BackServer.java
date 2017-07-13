package info.xiaomo.server.back;

import info.xiaomo.core.net.NetworkService;
import info.xiaomo.core.net.NetworkServiceBuilder;
import info.xiaomo.server.server.ServerOption;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * QQ_NO: 83387856
 * Date: 2016/11/24 10:11
 * Copyright(©) 2015 by xiaomo.
 **/

public class BackServer {

    private NetworkService service;

    public BackServer(ServerOption option) {

        int bossLoopGroupCount = 4;
        int workerLoopGroupCount = 4;
        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setMsgPool(new BackMessagePool());
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setNetworkEventListener(new BackEventListener());
        builder.setConsumer(new BackMessageRouter());
        builder.setPort(option.getBackServerPort());

        service = builder.createService();
    }

    public void start() {
        service.start();
    }

    public void stop() {
        service.stop();
    }
}