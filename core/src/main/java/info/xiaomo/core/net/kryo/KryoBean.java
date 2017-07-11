package info.xiaomo.core.net.kryo;

/**
 * 消息Bean的基类
 * 
 * @author Administrator
 * 
 */
public abstract class KryoBean {

	/**
	 * 将属性字段写入buffer中,该方法有具体的消息实现
	 * 
	 * @param output
	 * @return
	 */
	public abstract boolean write(KryoOutput output);

	/**
	 * 读取属性字段，该方法有具体的消息实现
	 * 
	 * @param input
	 * @return
	 */
	public abstract boolean read(KryoInput input);

	/**
	 * 向IOBuff中写入一个优化过的int（压缩过的）
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeInt(KryoOutput output, int value, boolean positive) {
		output.writeInt(value, positive);
	}

	/**
	 * 向IOBuff中写入一个int
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeInt(KryoOutput output, int value) {
		output.writeInt(value);
	}

	/**
	 * 向IOBuffer中写入一个字符串
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeString(KryoOutput output, String value) {
		output.writeString(value);
	}

	/**
	 * 向IOBuff中写入一个优化过的long（压缩过的）
	 * 
	 * @param output
	 * @param positive
	 * @param value
	 */
	protected void writeLong(KryoOutput output, long value, boolean positive) {
		output.writeLong(value, positive);
	}

	/**
	 * 写入一个long（未优化）
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeLong(KryoOutput output, long value) {
		output.writeLong(value);
	}

	/**
	 * 写入一个bean
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeBean(KryoOutput output, KryoBean value) {
		if (value == null) {
			output.writeByte(0);
		} else {
			output.writeByte(1);
			value.write(output);
		}

	}

	/**
	 * 写入一个short
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeShort(KryoOutput output, int value) {
		output.writeShort((short) value);
	}

	/**
	 * 写入一个short
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeShort(KryoOutput output, short value) {
		output.writeShort(value);
	}

	/**
	 * 写入一个byte
	 * 
	 * @param output
	 * @param value
	 */
	protected void writeByte(KryoOutput output, byte value) {
		output.writeByte(value);
	}

	/**
	 * 写入一个字节数组
	 * 
	 * @param output
	 * @param bytes
	 */
	protected void writeBytes(KryoOutput output, byte[] bytes) {
		output.write(bytes);
	}

	protected void writeBoolean(KryoOutput output, boolean value) {
		output.writeBoolean(value);
	}

	/**
	 * 读取一个优化过的int（压缩过的）
	 * 
	 * @param input
	 * @return
	 */
	protected int readInt(KryoInput input, boolean positive) {
		return input.readInt(positive);
	}

	/**
	 * 读取一个int值（未优化过的int）
	 * 
	 * @param input
	 * @return
	 */
	protected int readInt(KryoInput input) {
		return input.readInt();
	}

	/**
	 * 读取字符串
	 * 
	 * @param input
	 * @return
	 */
	protected String readString(KryoInput input) {
		return input.readString();
	}

	/**
	 * 读取一个优化过Long（压缩过的）
	 * 
	 * @param input
	 * @return
	 */
	protected long readLong(KryoInput input, boolean positive) {
		return input.readLong(positive);
	}

	/**
	 * 读取一个Long（未优化过的）
	 * 
	 * @param input
	 * @return
	 */
	protected long readLong(KryoInput input) {
		return input.readLong();
	}

	/**
	 * 读取一个Bean
	 * 
	 * @param input
	 * @param clazz
	 * @return
	 */
	protected KryoBean readBean(KryoInput input, Class<? extends KryoBean> clazz) {
		byte isNull = input.readByte();
		if (isNull == 0) {
			return null;
		}
		try {
			// 首先反射建立一个Bean
			KryoBean bean = (KryoBean) clazz.newInstance();
			// 读取Bean
			bean.read(input);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取short
	 * 
	 * @param input
	 * @return
	 */
	protected short readShort(KryoInput input) {
		return input.readShort();
	}

	/**
	 * 读取byte
	 * 
	 * @param input
	 * @return
	 */
	protected byte readByte(KryoInput input) {
		return input.readByte();
	}

	protected boolean readBoolean(KryoInput input) {
		return input.readBoolean();
	}
}