package info.xiaomo.server.rpg.client;

import info.xiaomo.gengine.network.client.Client;
import info.xiaomo.gengine.network.client.ClientBuilder;
import info.xiaomo.gengine.network.pool.MessageRouter;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.rpg.server.back.BackMessagePool;
import info.xiaomo.server.rpg.server.game.NetworkListener;
import info.xiaomo.server.rpg.server.game.ServerOption;
import info.xiaomo.server.shared.protocol.gm.ReqGMCloseServer;
import info.xiaomo.server.shared.protocol.msg.GMMsgId;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class StopServerClient {

    public static void main(String[] args) {

        try {
            BackMessagePool pool = new BackMessagePool();
            ServerOption option =
                    YamlUtil.read(PathUtil.getConfigPath() + "config.yml", ServerOption.class);
            if (option == null) {
                log.error("没有找到config.yml配置文件");
                return;
            }
            ClientBuilder builder = new ClientBuilder();
            builder.setHost("localhost"); // 只关本机的服务器
            builder.setPort(option.getBackServerPort());
            builder.setConsumer(new MessageRouter(pool));
            builder.setEventListener(new NetworkListener());
            builder.setMsgPool(pool);

            Client client = builder.createClient();
            client.connect(false);
            Thread.sleep(1000);
            ReqGMCloseServer request =
                    ReqGMCloseServer.newBuilder()
                            .setMsgId(GMMsgId.CloseServerRequest)
                            .setResMsg("1")
                            .build();
            client.sendMsg(request);

            // 等待10秒后关闭客户端
            int count = 10;
            while (count > 0) {
                Thread.sleep(1000);
                count--;
            }
            client.stop();
        } catch (Exception e) {
            log.error("关服发生错误", e);
        }

        System.exit(0);
    }
}
