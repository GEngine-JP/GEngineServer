package info.xiaomo.core.util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class AttributeUtil {
	
	public static <T> T get(Channel channel, AttributeKey<T> key){
		return channel.attr(key).get();
	}
	
	public static <T> void set(Channel channel, AttributeKey<T> key, T value){
		channel.attr(key).set(value);
	}
}
