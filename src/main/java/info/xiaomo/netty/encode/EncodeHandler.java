package info.xiaomo.netty.encode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * @author: xiaomo
 * @github: https://github.com/qq83387856
 * @email: hupengbest@163.com
 * @QQ_NO: 83387856
 * @Date: 2016/11/25 10:18
 * @Copyright(©) 2015 by xiaomo.
 * 相当于 jar包中的StringEncoder
 **/

public class EncodeHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] bytes = ((String) msg).getBytes("utf-8");
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(bytes.length);
        buf.writeBytes(bytes);
        ctx.writeAndFlush(buf);

    }
}
