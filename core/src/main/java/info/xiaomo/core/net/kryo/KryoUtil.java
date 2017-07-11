package info.xiaomo.core.net.kryo;

public class KryoUtil {

	public static int CACHESIZE = 1024 * 1024;

	private final static ThreadLocal<KryoOutput> cacheOutputs = new ThreadLocal<KryoOutput>() {
		protected KryoOutput initialValue() {
			KryoOutput Output = new KryoOutput(CACHESIZE);
			return Output;
		}
	};

	public static KryoOutput getOutput() {
		KryoOutput output = cacheOutputs.get();
		if (output.position() > 0) {
			output.setPosition(0);
			throw new RuntimeException("output出现迭代调用，或者使用之后没有清空");
		}
		return output;
	}

	private final static ThreadLocal<KryoInput> cacheInputs = new ThreadLocal<KryoInput>() {
		protected KryoInput initialValue() {
			KryoInput Input = new KryoInput();
			return Input;
		}
	};

	
	public static KryoInput getInput() {
		return cacheInputs.get();
	}

	/**
	 * 该方法请不要调用，只有一个地方（数据库持久化）需要调用这个方法。
	 */
	public static void clear() {
		KryoOutput output = cacheOutputs.get();
		output.clear();
	}

}
