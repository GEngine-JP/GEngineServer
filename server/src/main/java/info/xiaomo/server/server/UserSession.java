package info.xiaomo.server.server;

import com.google.protobuf.MessageOrBuilder;
import info.xiaomo.gameCore.protocol.entity.Session;
import info.xiaomo.server.entify.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
 * Date  : 2017/7/11 16:59
 * desc  : session
 * Copyright(©) 2017 by xiaomo.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSession extends Session {

    private User user;

    public void sendMessage(MessageOrBuilder msg) {
        channel.writeAndFlush(msg);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
