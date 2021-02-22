package info.xiaomo.server.rpg.server;

import io.netty.util.AttributeKey;

/**
 * @author xiaomo
 */
public class SessionKey {
    public static final AttributeKey<Session> SESSION = AttributeKey.newInstance("SESSION");

    public static final AttributeKey<Boolean> LOGOUT_HANDLED = AttributeKey.newInstance("LOGOUT_HANDLED");
}
