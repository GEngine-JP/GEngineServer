package info.xiaomo.server.util;

import info.xiaomo.gameCore.base.tuple.TwoTuple;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息统计工具类
 *
 * @author Administrator
 */
public class PackageCountUtil {

    private static ConcurrentHashMap<Integer, AtomicLong> packageCount = new ConcurrentHashMap<>();

    private static boolean open = false;


    /**
     * 增加一个消息的计数
     *
     * @param msgId
     */
    public static void count(int msgId) {

        if (!open) {
            return;
        }
        AtomicLong count = packageCount.get(msgId);
        if (count == null) {
            count = new AtomicLong();
            AtomicLong oldCount = packageCount.putIfAbsent(msgId, count);
            if (oldCount != null) {
                count = oldCount;
            }
        }
        count.incrementAndGet();
    }

    /**
     * 获取指定ID的消息发送次数
     *
     * @param msgId
     * @return
     */
    public static long getCountById(int msgId) {
        AtomicLong count = packageCount.get(msgId);
        if (count == null) {
            return 0;
        }
        return count.get();
    }

    /**
     * 获取所有消息的次数，按照发送量排序
     *
     * @param num 获取排序前多少的消息
     * @return
     */
    public static List<TwoTuple<Integer, Long>> getTop(int num) {

        List<TwoTuple<Integer, Long>> ret = new ArrayList<>();

        for (int id : packageCount.keySet()) {
            ret.add(new TwoTuple<>(id, packageCount.get(id).get()));
        }

        ret.sort((o1, o2) -> Long.compare(o2.second, o1.second));
        if (num >= ret.size()) {
            return ret;
        } else {
            return ret.subList(0, num);
        }
    }

    /**
     * 开启或关闭
     *
     * @param open
     */
    public static void open(boolean open) {
        PackageCountUtil.open = open;
    }

    public static boolean isOpen() {
        return open;
    }

    @Sharable
    public static class PackageCountHandler extends ChannelOutboundHandlerAdapter {

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

            AbstractMessage amsg = (AbstractMessage) msg;
            if (amsg != null) {
                count(amsg.getId());
            }
            super.write(ctx, msg, promise);
        }

    }

}
