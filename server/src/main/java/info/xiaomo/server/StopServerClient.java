package info.xiaomo.server;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.client.Client;
import info.xiaomo.gameCore.protocol.client.ClientBuilder;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.back.BackMessagePool;
import info.xiaomo.server.protocol.gm.message.ReqCloseServerMessage;
import info.xiaomo.server.server.ServerOption;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionManager;
import io.netty.channel.Channel;
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
            builder.setConsumer(new Consumer(new BackMessagePool()));
            builder.setMsgPool(new BackMessagePool());

            Client client = builder.createClient();
            client.connect(false);

            client.sendMsg((new ReqCloseServerMessage()));

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


    static class Consumer implements NetworkConsumer {
        private BackMessagePool msgPool;

        public Consumer(BackMessagePool msgPool) {
            this.msgPool = msgPool;
        }

        @Override
        public void consume(AbstractMessage message, Channel channel) {
            AbstractHandler handler = msgPool.getHandler(message.getId());
            if (handler != null) {
                handler.setMessage(message);
                Session session = new Session(channel);
                handler.setParam(session);
                handler.doAction();
            }
        }

        @Override
        public void connected(Channel channel) {
            LOGGER.info(channel + " connected to back server");
        }

        @Override
        public void disconnected(Channel channel) {
            LOGGER.info(channel + " disconnected to back server");
        }

        @Override
        public void exceptionOccurred(Channel channel, Throwable error) {
            LOGGER.error(channel + " exception occurred", error);
        }
    }


}
