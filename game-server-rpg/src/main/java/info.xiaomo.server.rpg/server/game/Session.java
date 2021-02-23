package info.xiaomo.server.rpg.server.game;

import com.google.protobuf.AbstractMessage;

import java.net.InetSocketAddress;

import info.xiaomo.gengine.network.ISession;
import info.xiaomo.gengine.network.Packet;
import info.xiaomo.gengine.utils.AttributeUtil;
import info.xiaomo.server.rpg.entify.Role;
import info.xiaomo.server.rpg.entify.User;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo
 */
@Slf4j
@Data
public class Session implements ISession {

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

    public void sendMessage(Packet msg) {
        channel.writeAndFlush(msg);
    }
}
