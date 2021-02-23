package info.xiaomo.server.rpg.server;

import info.xiaomo.gengine.network.NetworkServiceBuilder;
import info.xiaomo.gengine.network.NetworkServiceImpl;
import info.xiaomo.server.rpg.server.back.BackMessageAndHandler;
import info.xiaomo.server.rpg.server.back.BackMessageRouter;
import info.xiaomo.server.rpg.server.game.GameContext;
import info.xiaomo.server.rpg.server.game.NetworkListener;
import info.xiaomo.server.rpg.server.game.ServerOption;

/**
 * Copyright(©) 2015 by xiaomo.
 **/

public class BackServer {

    private final NetworkServiceImpl service;

    public BackServer() {
        BackMessageAndHandler pool = new BackMessageAndHandler();
        int bossLoopGroupCount = 1;
        int workerLoopGroupCount = 4;
        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setPort(GameContext.getBackServerPort());
        builder.setImessageandhandler(pool);
        builder.setConsumer(new BackMessageRouter());
        builder.setListener(new NetworkListener());

        service = builder.createService();
    }

    public void start() {
        service.start();
    }

    public void stop() {
        service.stop();
    }
}