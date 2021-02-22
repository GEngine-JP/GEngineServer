package info.xiaomo.server.fish.server;

import java.util.concurrent.Executor;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.bean.NetPort;
import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.service.ClientServerService;
import info.xiaomo.gengine.network.mina.service.GameHttpSevice;
import info.xiaomo.gengine.network.netty.config.NettyClientConfig;
import info.xiaomo.gengine.network.netty.service.SingleNettyTcpClientService;
import info.xiaomo.gengine.network.server.*;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubListener;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.fish.FishApp;
import info.xiaomo.server.fish.FishChannel;
import info.xiaomo.server.shared.config.Configs;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 捕鱼服务器
 * 可接收大厅服转发来的消息，和客户端直接发来的消息
 * <h4>mina和netty性能测试对比</h4>
 * 开启100个客户端，每个客户端每隔100ms发送一条消息
 * <li>在使用自定义线程池时，mina 50个连接,netty 1个连接:mina通信耗时在1~3毫秒，netty通信在0~100ms(变动幅度很大，多客户端性能较低，当单客户端发消息时变为0~3ms)</li>
 * <li>在使用自定义线程池时，mina 1个连接,netty 1个连接:mina通信耗时在1~3毫秒，netty通信耗时0~100ms. mina才启动比较耗时，然而netty才启动比较快；mina在单客户端比较耗时，多客户端比较快，netty相反</li>
 * <br><b>总结：多客户端情况下使用<code>channel.writeAndFlush()</code>代替<code>channel.write()</code></b>
 * 开启1个客户端，每个客户端每隔100ms发送一条消息
 * <li>在使用自定义线程池时，mina 1个连接,netty 1个连接:mina耗时0~3毫秒， netty耗时0~3毫秒</li>
 * Role> 2017年6月28日 下午3:37:19
 */
@Data
@RequiredArgsConstructor
public class FishServer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FishServer.class);

    /**
     * 连接网关 （接收网关转发过来的消息）
     */
    private final IMutilTcpClientService<? extends BaseServerConfig> FishGateClient;

    /**
     * 连接集群服 （获取各服务器信息）
     */
    private final ITcpClientService<? extends BaseServerConfig> FishClusterClient;
    /**
     * http服务器
     */
    private final FishHttpServer fishHttpServer;
    /**
     * redis订阅发布
     */
    private final JedisPubListener jedisPubListener;

    /** 服务器状态监测 */
//	private final GameServerCheckTimer gameServerCheckTimer;
    /**
     * 游戏前端消息服务 （消息直接从手机前端发来,如果没有直接注释掉，不经过大厅网关转发,暂时用engine封装类）
     */
    private ClientServerService bydrTcpServer;

    /**
     * mina、netty通信服务组件都实现，通过配置netty配置文件<code>NettyClientConfig.info</code> NettyFirst 选择使用netty还是mina
     *
     * @param configPath
     */
    public FishServer(String configPath) {

        //线程池配置
        ThreadPoolExecutorConfig threadPoolExecutorConfig = YamlUtil.read(configPath + Configs.threadPoolExecutorConfig, ThreadPoolExecutorConfig.class);
        if (threadPoolExecutorConfig == null) {
            LOGGER.error("{}{}未找到", configPath, Configs.threadPoolExecutorConfig);
            System.exit(0);
        }

        // 加载连接网关配置
        MinaClientConfig minaClientConfig_gate = YamlUtil.read(configPath + Configs.minaClientConfig_gate, MinaClientConfig.class);
        NettyClientConfig nettyClientConfig_gate = YamlUtil.read(configPath + Configs.nettyClientConfig_gate, NettyClientConfig.class);
        if (minaClientConfig_gate == null && nettyClientConfig_gate == null) {
            LOGGER.error("{}未配置网关连接客户端", configPath);
            System.exit(0);
        }

        // 加载连接集群配置
        MinaClientConfig minaClientConfig_cluster = YamlUtil.read(configPath + Configs.minaClientConfig_cluster, MinaClientConfig.class);
        NettyClientConfig nettyClientConfig_cluster = YamlUtil.read(configPath + Configs.nettyClientConfig_cluster, NettyClientConfig.class);
        if (minaClientConfig_cluster == null && nettyClientConfig_cluster == null) {
            LOGGER.error("{}未配置集群连接客户端", configPath);
            System.exit(0);
        }

        // HTTP通信
        MinaServerConfig minaServerConfig_http = YamlUtil.read(configPath + Configs.minaServerConfig_http, MinaServerConfig.class);

        fishHttpServer = new FishHttpServer(minaServerConfig_http);

        // 游戏前端消息服务 配置为空，不开启，开启后消息可以不经过网关直接发送到本服务器
        MinaServerConfig minaServerConfig = YamlUtil.read(configPath + "minaServerConfig.yml", MinaServerConfig.class);
        if (minaServerConfig != null) {
            bydrTcpServer = new ClientServerService(minaServerConfig);
        }

        // 如果netty 优先级高，使用Netty服务,一般不直接使用engine提供的类
        //网关
        if (nettyClientConfig_gate != null && "NettyFirst".equalsIgnoreCase(nettyClientConfig_gate.getInfo())) {
            //TODO 需要重写channelActive 发送服务器注册消息 ，不然相当于当前客户端和网关只有一个channel连接
            FishGateClient = new Bydr2GateClientNetty(threadPoolExecutorConfig, nettyClientConfig_gate);
        } else {
            FishGateClient = new Bydr2GateClient(threadPoolExecutorConfig, minaClientConfig_gate);
        }

        //集群
        if (nettyClientConfig_cluster != null && "NettyFirst".equalsIgnoreCase(nettyClientConfig_cluster.getInfo())) {
            FishClusterClient = new SingleNettyTcpClientService(nettyClientConfig_cluster);
        } else {
            FishClusterClient = new Bydr2ClusterClient(minaClientConfig_cluster);
        }


        // 状态监控
//        gameServerCheckTimer = new GameServerCheckTimer(bydr2ClusterClient, bydr2GateClient,
//		bydr2GateClient instanceof Bydr2GateClient ? minaClientConfig_gate : nettyClientConfig_gate);
        // 订阅发布
        jedisPubListener = new JedisPubListener(FishChannel.getChannels());

        //设置配置相关常量
        Config.SERVER_ID = minaClientConfig_gate.getId();
        Config.SERVER_NAME = minaClientConfig_gate.getName();
    }

    public static FishServer getInstance() {
        return FishApp.getBydrServer();
    }

    @Override
    public void run() {
        new Thread(FishGateClient).start();
        new Thread(FishClusterClient).start();

        if (bydrTcpServer != null) {
            new Thread(bydrTcpServer).start();
        }
//		gameServerCheckTimer.start();
        new Thread(fishHttpServer).start();
        new Thread(jedisPubListener).start();
    }


    public IMutilTcpClientService<? extends BaseServerConfig> getFishGateClient() {
        return FishGateClient;
    }

    /**
     * 获取线程 在连接网关服的service中获取
     *
     * @param threadType
     * @return Role>
     * 2017年9月14日 下午4:33:48
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends Executor> T getExecutor(ThreadType threadType) {
        return (T) ((GameService) FishGateClient).getExecutor(threadType);
    }

    public ITcpClientService<? extends BaseServerConfig> getFishClusterClient() {
        return FishClusterClient;
    }

    public GameHttpSevice getFishHttpServer() {
        return fishHttpServer;
    }

    /**
     * 更新可用网关服务器信息
     *
     * @param info
     */
    public void updateGateServerInfo(GameServerInfo info) {
        ServerInfo serverInfo = getFishGateClient().getServers().get(info.getId());
        if (serverInfo == null) {
            serverInfo = getServerInfo(info);
            if (getFishGateClient() instanceof Bydr2GateClient) {
                Bydr2GateClient service = (Bydr2GateClient) getFishGateClient();
                service.addTcpClient(serverInfo, NetPort.GATE_GAME_PORT, service.new MutilConHallHandler(serverInfo, service)); // TODO 暂时，网关服有多个tcp端口
            } else {
                getFishGateClient().addTcpClient(serverInfo, NetPort.GATE_GAME_PORT);
            }
        } else {
            serverInfo.setIp(info.getIp());
            serverInfo.setId(info.getId());
            serverInfo.setPort(info.getPort());
            serverInfo.setState(info.getState());
            serverInfo.setOnline(info.getOnline());
            serverInfo.setMaxUserCount(info.getMaxUserCount());
            serverInfo.setName(info.getName());
            serverInfo.setHttpPort(info.getHttpPort());
            serverInfo.setWwwip(info.getWwwIp());
        }
        getFishGateClient().getServers().put(info.getId(), serverInfo);
    }

    /**
     * 消息转换
     *
     * @param info
     * @return Role>
     * 2017年8月29日 下午2:21:52
     */
    private ServerInfo getServerInfo(GameServerInfo info) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(info.getIp());
        serverInfo.setId(info.getId());
        serverInfo.setPort(info.getPort());
        serverInfo.setState(info.getState());
        serverInfo.setOnline(info.getOnline());
        serverInfo.setMaxUserCount(info.getMaxUserCount());
        serverInfo.setName(info.getName());
        serverInfo.setHttpPort(info.getHttpPort());
        serverInfo.setWwwip(info.getWwwIp());
        serverInfo.setFreeMemory(info.getFreeMemory());
        serverInfo.setTotalMemory(info.getTotalMemory());
        serverInfo.setVersion(info.getVersion());
        return serverInfo;
    }

}
