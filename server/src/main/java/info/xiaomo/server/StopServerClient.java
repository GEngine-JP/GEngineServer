package info.xiaomo.server;

import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.client.Client;
import info.xiaomo.gameCore.protocol.client.ClientBuilder;
import info.xiaomo.gameCore.protocol.protobuf.ProtoBufMessageDecoder;
import info.xiaomo.gameCore.protocol.protobuf.ProtoBufMessageEncoder;
import info.xiaomo.server.server.MessageRouter;
import info.xiaomo.server.server.ServerOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopServerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopServerClient.class);

    public static void main(String[] args) throws Exception {

        try {
            String optionPath = args[0];

            ServerOption option = new ServerOption(optionPath);


            ClientBuilder builder = new ClientBuilder();


            builder.setHost("localhost");//只关本机的服务器
            builder.setPort(option.getBackServerPort());
            builder.setDecoder(new ProtoBufMessageDecoder(new ClientMessagePool()));
            builder.setEncoder(new ProtoBufMessageEncoder(new ClientMessagePool()));
            builder.setExecutor(new MessageRouter());

            Client client = builder.build();
            client.start();
            int count = 10;
            while (count > 0) {
                Thread.sleep(10 * 1000);
                count--;
            }
        } catch (Exception e) {
            LOGGER.error("关服发生错误", e);
        }

        System.exit(0);

    }


    private static class ClientMessagePool extends MessagePool {


    }

}
