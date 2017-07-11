package info.xiaomo.core.db.jdbc;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据序列化工具类
 * @author 张力
 */
public class SerializerUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializerUtil.class);
	
	public static <T> byte[] encode(T object, Class<T> clazz){
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate();
		return ProtostuffIOUtil.toByteArray(object, schema, buffer);
	}
	
	
	
	public static <T> T decode(byte[] bytes, Class<T> clazz){ //Item.class
		T object;
		try {
			object = clazz.newInstance();
		} catch (Exception e) {
			LOGGER.error("Protostuff反序列化时创建实例失败,Class:" + clazz.getName(), e);
			return null;
		}
		return decode(bytes, clazz, object);
	}
	
	public static <T> T decode(byte[] bytes, Class<T> clazz, T object){
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		ProtostuffIOUtil.mergeFrom(bytes, object, schema);
		return object;
	}
	
}
