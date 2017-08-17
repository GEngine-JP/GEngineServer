package info.xiaomo.server;

import com.google.protobuf.MessageOrBuilder;
import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import info.xiaomo.gameCore.protocol.client.Client;
import info.xiaomo.gameCore.protocol.client.ClientBuilder;
import info.xiaomo.server.back.BackMessagePool;
import info.xiaomo.server.back.BackMessageRouter;
import info.xiaomo.server.server.EventListener;
import info.xiaomo.server.server.MessageRouter;
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
            builder.setConsumer(new Consumer());
            builder.setMessagePool(new BackMessagePool());
            builder.setNetworkEventListener(new Listener());

            Client client = builder.build();
            client.start();
//
//            ReqCloseServerMessage.Builder newBuilder = ReqCloseServerMessage.newBuilder();
//            newBuilder.setSex(1);
//            ReqCloseServerMessage msg = newBuilder.build();
//            client.sendMsg(msg);

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


        @Override
        public void consume(Channel channel, AbstractHandler handler) {

       }
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

}
