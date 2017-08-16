package info.xiaomo.server;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.client.Client;
import info.xiaomo.gameCore.protocol.client.ClientBuilder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import info.xiaomo.server.server.ServerOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
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

            builder.setMessagePool(new ClientMessagePool());

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

    static class Consumer implements MessageExecutor {


        @Override
        public void doCommand(Channel channel, AbstractHandler handler) throws Exception {

        }

        @Override
        public void connected(ChannelHandlerContext ctx) {

        }

        @Override
        public void disconnected(ChannelHandlerContext ctx) {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable paramThrowable) {

        }
    }


    static class ClientMessagePool extends MessagePool{


    }

}
