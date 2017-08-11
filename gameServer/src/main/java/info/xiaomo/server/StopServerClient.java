package info.xiaomo.server;

import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import info.xiaomo.gameCore.protocol.client.Client;
import info.xiaomo.gameCore.protocol.client.ClientBuilder;
import info.xiaomo.server.server.ServerOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class StopServerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopServerClient.class);

    public static void main(String[] args) throws Exception {

        try {
            String optionPath = args[0];

            ServerOption option = new ServerOption(optionPath);


            ClientBuilder builder = new ClientBuilder();


            builder.setHost("localhost");//只关本机的服务器
            builder.setPort(option.getBackServerPort());
            builder.setListener(new Listener());
            builder.setConsumer(new Consumer());
            builder.setMessagePool(new Pool());

            Client client = builder.build();
            client.connect();

//            ReqCloseServerMessage req = new ReqCloseServerMessage();
//
//            client.sendMsg(req);
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

    static class Listener implements NetworkEventListener {

        @Override
        public void onConnected(ChannelHandlerContext ctx) {

        }

        @Override
        public void onDisconnected(ChannelHandlerContext ctx) {

        }

        @Override
        public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

        }

    }

    static class Consumer implements NetworkConsumer {

        @Override
        public void consume(Channel channel, Message msg) {
//            ResCloseServerMessage res = (ResCloseServerMessage) msg;
//            LOGGER.info(res.getInfo());
//            if (res.getCode() == -1) {
//                LOGGER.info("local exit...");
//                System.exit(0);
//            }
        }

    }

    static class Pool implements MessagePool {

        private Map<Integer, Class<? extends Message>> pool = new HashMap<>();

        public Pool() {
        }


        @Override
        public Message get(int messageId) {
            Class<? extends Message> clazz = pool.get(messageId);
            if (clazz != null) {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }

        public void register(int messageId, Class<? extends Message> clazz) {
            pool.put(messageId, clazz);
        }

    }
}
