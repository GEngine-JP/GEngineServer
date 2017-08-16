package info.xiaomo.server.server;

import io.netty.util.AttributeKey;

public class SessionKey {
	public static final AttributeKey<UserSession> SESSION = AttributeKey.newInstance("SESSION");
	
	public static final AttributeKey<Boolean> LOGOUT_HANDLED = AttributeKey.newInstance("LOGOUT_HANDLED");
}
