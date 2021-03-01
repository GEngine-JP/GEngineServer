package info.xiaomo.server.rpg.server;

import info.xiaomo.gengine.network.NetworkServiceBuilder;
import info.xiaomo.gengine.network.NetworkServiceImpl;
import info.xiaomo.server.rpg.server.back.BackMessagePool;
import info.xiaomo.server.rpg.server.back.BackMessageRouter;
import info.xiaomo.server.rpg.server.game.GameContext;
import info.xiaomo.server.rpg.server.game.NetworkListener;

/** Copyright(©) 2015 by xiaomo. */
public class BackServer {

    private final NetworkServiceImpl service;

    public BackServer() {
        BackMessagePool pool = new BackMessagePool();
        int bossLoopGroupCount = 1;
        int workerLoopGroupCount = 4;
        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setPort(GameContext.getBackServerPort());
        builder.setMessagePool(pool);
        builder.setConsumer(new BackMessageRouter(pool));
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
