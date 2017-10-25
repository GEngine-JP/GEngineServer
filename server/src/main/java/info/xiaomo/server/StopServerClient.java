package info.xiaomo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaomo
 */
public class StopServerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopServerClient.class);

    public static void main(String[] args) throws Exception {

        try {
//            String optionPath = "config.properties";
//            BackMessageAndHandler pool = new BackMessageAndHandler();
//            ServerOption option = new ServerOption(optionPath);
//            ClientBuilder builder = new ClientBuilder();
//            builder.setHost("localhost");//只关本机的服务器
//            builder.setPort(option.getBackServerPort());
//            builder.setConsumer(new BackMessageRouter());
//            builder.setMsgPool(pool);
//
//            Client client = builder.createClient();
//            client.connect(false);
//            Thread.sleep(1000);
////            client.sendMsg(msg);
//            int count = 10;
//            while (count > 0) {
//                Thread.sleep(10 * 1000);
//                count--;
//            }
        } catch (Exception e) {
            LOGGER.error("关服发生错误", e);
        }

        System.exit(0);

    }


}
