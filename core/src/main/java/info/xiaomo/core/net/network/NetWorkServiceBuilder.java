package info.xiaomo.core.net.network;

import info.xiaomo.core.net.message.MessagePool;
import info.xiaomo.core.net.network.NetworkConsumer;
import info.xiaomo.core.net.network.NetworkEventListener;
import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 15:18
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class NetWorkServiceBuilder {
    private int bossLoopGroupCount;

    private int workerLoopGroupCount;

    private int port;

    private MessagePool msgPool;

    /**
     * 网络消费者
     */
    private NetworkConsumer consumer;

    /**
     * 事件监听器
     */
    private NetworkEventListener networkEventListener;

    /**
     * 额外的handler
     */
    private List<ChannelHandler> channelHandlerList = new ArrayList<>();


    public NetWorkService createService() {
        return new NetWorkService(this);
    }
    public int getBossLoopGroupCount() {
        return bossLoopGroupCount;
    }

    public void setBossLoopGroupCount(int bossLoopGroupCount) {
        this.bossLoopGroupCount = bossLoopGroupCount;
    }

    public int getWorkerLoopGroupCount() {
        return workerLoopGroupCount;
    }

    public void setWorkerLoopGroupCount(int workerLoopGroupCount) {
        this.workerLoopGroupCount = workerLoopGroupCount;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MessagePool getMsgPool() {
        return msgPool;
    }

    public void setMsgPool(MessagePool msgPool) {
        this.msgPool = msgPool;
    }

    public NetworkConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(NetworkConsumer consumer) {
        this.consumer = consumer;
    }

    public NetworkEventListener getNetworkEventListener() {
        return networkEventListener;
    }

    public void setNetworkEventListener(NetworkEventListener networkEventListener) {
        this.networkEventListener = networkEventListener;
    }

    public List<ChannelHandler> getChannelHandlerList() {
        return channelHandlerList;
    }

    public void setChannelHandlerList(List<ChannelHandler> channelHandlerList) {
        this.channelHandlerList = channelHandlerList;
    }
}
