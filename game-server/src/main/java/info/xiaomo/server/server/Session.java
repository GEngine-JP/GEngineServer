package info.xiaomo.server.server;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.common.utils.AttributeUtil;
import info.xiaomo.server.entify.Role;
import info.xiaomo.server.entify.User;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xiaomo
 */
@Slf4j
@Data
public class Session {

    private Channel channel;

    private User user;

    private int sendDelay;

    private Role role;

    private volatile boolean offline = false;

    public Session(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void registerUser(User user) {
        AttributeUtil.set(channel, SessionAttributeKey.UID, user.getId());
        this.user = user;
    }

    public boolean isUserRegistered() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public String getIP() {
        if (channel == null) {
            return "";
        }
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        if (address == null) {
            return "";
        }
        return address.getAddress().getHostAddress();
    }

    public void clearAttribute() {
        if (channel == null) {
            return;
        }
        AttributeUtil.set(channel, SessionAttributeKey.UID, null);
    }

    public void close() {
        if (channel == null) {
            return;
        }
        clearAttribute();
        try {
            if (channel.isActive() || channel.isOpen()) {
                channel.close().sync();
            }
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    public void closeByHttp() {
        if (channel == null) {
            return;
        }
        try {
            channel.close().sync();
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    public void sendMessage(AbstractMessage msg) {
        channel.writeAndFlush(msg);
    }
}
