package info.xiaomo.server.cluster.server;

import info.xiaomo.gengine.network.handler.*;
import info.xiaomo.gengine.network.mina.HttpServer;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.handler.HttpServerIoHandler;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http连接
 * <p>
 * 2017-03-31
 */
public class ClusterHttpServer extends GameService<MinaServerConfig> {

    private static final Logger log = LoggerFactory.getLogger(ClusterHttpServer.class);

    private final HttpServer httpServer;
    private final MinaServerConfig minaServerConfig;

    public ClusterHttpServer(
            ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig) {
        super(threadExecutorConfig);
        this.minaServerConfig = minaServerConfig;
        this.httpServer = new HttpServer(minaServerConfig, new ClusterHttpServerHandler(this));
    }

    @Override
    protected void running() {
        log.debug(" run ... ");
        httpServer.run();
        ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
        ScriptManager.getInstance().addIHandler(CloseServerHandler.class);
        ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
        ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
        ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
        ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
        log.debug(" stop ... ");
        httpServer.stop();
    }

    @Override
    public String toString() {
        return minaServerConfig.getName();
    }
}

/**
 * 消息处理器
 * <p>
 * 2017-03-31
 */
class ClusterHttpServerHandler extends HttpServerIoHandler {

    private final GameService<MinaServerConfig> service;

    public ClusterHttpServerHandler(GameService<MinaServerConfig> service) {
        this.service = service;
    }

    @Override
    protected GameService<MinaServerConfig> getService() {
        return this.service;
    }

    @Override
    public void event(IoSession ioSession, FilterEvent filterEvent) {

    }
}
