package info.xiaomo.server.rpg.client;

import info.xiaomo.gengine.network.client.Client;
import info.xiaomo.gengine.network.client.ClientBuilder;
import info.xiaomo.gengine.network.pool.MessageRouter;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.rpg.constant.GameConst;
import info.xiaomo.server.rpg.processor.LoginProcessor;
import info.xiaomo.server.rpg.server.game.NetworkListener;
import info.xiaomo.server.rpg.server.game.ServerOption;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class TestLoginClient {

    public static void main(String[] args) {

        try {
            TestMessagePool pool = new TestMessagePool();
            ServerOption option =
                    YamlUtil.read(PathUtil.getConfigPath() + "config.yml", ServerOption.class);
            if (option == null) {
                log.error("没有找到config.yml配置文件");
                return;
            }
            MessageRouter router = new MessageRouter(pool);
            ClientBuilder builder = new ClientBuilder();
            builder.setHost("localhost");
            //            builder.setHost("106.15.188.160");
            builder.setPort(option.getGameServerPort());
            builder.setConsumer(router);
            builder.setEventListener(new NetworkListener());
            builder.setMsgPool(pool);

            router.registerProcessor(GameConst.QueueId.LOGIN_LOGOUT, new LoginProcessor());

            Client client = builder.createClient();
            client.connect(false);
            Thread.sleep(1000);
            ReqUserLogin request =
                    ReqUserLogin.newBuilder()
                            .setLoginName("xiaomo")
                            .build();

            client.sendMsg(request);

            // 等待10秒后关闭客户端
            int count = 10;
            while (count > 0) {
                Thread.sleep(10 * 1000);
                count--;
            }
            client.stop();
        } catch (Exception e) {
            log.error("请求登陆出错", e);
        }

        System.exit(0);
    }
}
