package info.xiaomo.server.rpg;

import info.xiaomo.gengine.network.client.Client;
import info.xiaomo.gengine.network.client.ClientBuilder;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.rpg.server.back.BackMessageAndHandler;
import info.xiaomo.server.rpg.server.back.BackMessageRouter;
import info.xiaomo.server.rpg.server.game.NetworkListener;
import info.xiaomo.server.rpg.server.game.ServerOption;
import info.xiaomo.server.shared.protocol.gm.CloseServerRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo
 */
@Slf4j
public class StopServerClient {

    public static void main(String[] args) {

        try {
            BackMessageAndHandler pool = new BackMessageAndHandler();
            ServerOption option = YamlUtil.read(PathUtil.getConfigPath() + "config.yml", ServerOption.class);
            ClientBuilder builder = new ClientBuilder();
            builder.setHost("localhost");//只关本机的服务器
            builder.setPort(option.getBackServerPort());
            builder.setConsumer(new BackMessageRouter());
            builder.setEventListener(new NetworkListener());
            builder.setMsgPool(pool);

            CloseServerRequest request = CloseServerRequest.newBuilder().setResMsg("1").build();
            Client client = builder.createClient();
            client.connect(false);
            Thread.sleep(1000);
            client.sendMsg(request);
            int count = 10;
            while (count > 0) {
                Thread.sleep(10 * 1000);
                count--;
            }
        } catch (Exception e) {
            log.error("关服发生错误", e);
        }

        System.exit(0);

    }


}
