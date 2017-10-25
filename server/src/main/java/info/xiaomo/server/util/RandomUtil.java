/**
 * 文   件  名：  RandomUtil.java
 * 工   程  名：  MainServer
 * 创建日期：  2015年2月5日 下午2:38:48
 * 创建作者：  杨  强 <281455776@qq.com>
 */
package info.xiaomo.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 * 
 * @author 杨 强
 *
 */
public class RandomUtil {

	private static final Map<String, Double> CUR_PRIVATE_RATE = new ConcurrentHashMap<>();
	private static final Map<String, Integer> LAST_PRIVATE_RATE = new ConcurrentHashMap<>();
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);
    
    private static final Map<Integer, Double> PRIVATE_RATE_INIT_MAP = new HashMap<>();
    
    /**
     * 随机产生min到max之间的一个小数值，包含min和不包含max
     * 
     * @param min
     * @param max
     *            不包含
     * @return
     */
    public static double random(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("传入的范围不合法!最小值不能大于最大值！");
        }
		return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * 随机产生min到max之间的一个整数值，包含min和max
     * 
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("传入的范围不合法!最小值不能大于最大值！");
        }
		return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

	public static boolean randomBooleanByType(long rid, int type, int rate) {
		if (rate > 10000) {
			rate = 10000;
		}
		double realRate = PRIVATE_RATE_INIT_MAP.get(rate);
		if (realRate == 0.0) {
			return false;
		}

		String key = rid + "_" + type;
		Double curRate = CUR_PRIVATE_RATE.get(key);  //取出当前的实际概率
		if (curRate == null) {
			curRate = realRate;
		}
		Integer lastRate = LAST_PRIVATE_RATE.get(key);
		if (lastRate == null || lastRate != rate) {
			curRate = realRate;
		}

		LAST_PRIVATE_RATE.put(key, rate);
		if (ThreadLocalRandom.current().nextDouble() < curRate) {
			//成功重置
			CUR_PRIVATE_RATE.put(key, realRate);
			return true;
		} else {//失败翻倍
			CUR_PRIVATE_RATE.put(key, curRate * 2);
			return false;
		}
	}


    /**
     * 随机一个double类型的数
     * @param min
     * @param max
     * @return
     */
    public static Double randomDouble(Double min, Double max){
        if (min > max) {
            throw new IllegalArgumentException("传入的范围不合法!最小值不能大于最大值！");
        }
        DecimalFormat df = new DecimalFormat("#.0");
        Double num = ThreadLocalRandom.current().nextDouble(max - min )+ min;
        return Double.valueOf(df.format(num));
    }

    /**
     * 根据几率计算是否生成，成功几率是sucRange/maxRange
     * 
     * @param maxRange
     *            最大范围，随机范围是[1,maxRange]
     * @param sucRange
     *            成功范围，成功范围是[1,sucRange]
     * @return 成功true失败false
     */
    public static boolean isGenerate(int maxRange, int sucRange) {
		return maxRange == sucRange || sucRange > 0 && random(1, maxRange) <= sucRange;
	}

    /**
     * 从指定的的元素集中随机一个元素
     * 
     * @param collection
     *            元素集
     * @return
     */
    public static <T> T randomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("元素集不能为空！");
        }
        int index = random(0, collection.size() - 1);
        Iterator<T> it = collection.iterator();
        for (int i = 0; i <= index && it.hasNext(); i++) {
            T t = it.next();
            if (i == index) {
                return t;
            }
        }
        return null;
    }

    /**
     * 从指定的元素数组中随机出一个元素
     * 
     * @param array
     *            元素数组
     * @return T
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("元素数组不能为空！");
        }
        return randomElement(Arrays.asList(array));
    }

    /**
     * 根据每个几率返回随机的一个索引
     * 
     * @param probs
     * @return -1失败or随机的索引
     */
    public static int randomIndexByProb(List<Integer> probs) {
        LinkedList<Integer> newProbs = new LinkedList<Integer>();
        int lastTotalProb = 0;
		for (Integer prob : probs) {
			int currentTotalProb = lastTotalProb + prob;
			newProbs.add(currentTotalProb);
			lastTotalProb = currentTotalProb;
		}
        if (newProbs.isEmpty()) {
            return -1;
        }
        int totalProb = newProbs.getLast();
        if (totalProb == 0) {// 总概率为0
            return -1;
        }
        int random = random(0, totalProb - 1);
        for (int i = 0; i < newProbs.size(); i++) {
            int currentTotalProb = newProbs.get(i);
            if (currentTotalProb > random) {
                return i;
            }
        }
        LOGGER.error("计算概率错误{}", probs.toString());
        return -1;
    }

    /**
     * 根据每个几率返回随机的一个索引
     * 
     * @param array
     * @return -1失败or随机的索引
     */
    public static int randomIndexByProb(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("元素数组不能为空！");
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i : array) {
            list.add(i);
        }
        return randomIndexByProb(list);
    }

	static {

		PRIVATE_RATE_INIT_MAP.put(0, 0.0);

		for(int i = 1; i <= 100 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000000000000000000000000000000528);
		}

		for(int i = 101; i <= 200 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000000000000000594000000000000000);
		}

		for(int i = 201; i <= 300 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000000000061100000000000000000000);
		}

		for(int i = 301; i <= 400 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000000019940000000000000000000000);
		}

		for(int i = 401; i <= 500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000000637900000000000000000000000);
		}

		for(int i = 501; i <= 600 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000006389400000000000000000000000);
		}

		for(int i = 601; i <= 700 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000033190000000000000000000000000);
		}

		for(int i = 701; i <= 800 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000114055000000000000000000000000);
		}
		for(int i = 801; i <= 900 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000302707000000000000000000000000);
		}

		for(int i = 901; i <= 1000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.000656500000000000000000000000000);
		}

		for(int i = 1001; i <= 1250 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.002656510000000000000000000000000);
		}

		for(int i = 1251; i <= 1500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.006767750000000000000000000000000);
		}

		for(int i = 1501; i <= 1750 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.013452300000000000000000000000000);
		}

		for(int i = 1751; i <= 2000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.02296035);
		}

		for(int i = 2001; i <= 2500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.04931000);
		}

		for(int i = 2501; i <= 3000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.08520000);
		}

		for(int i = 3001; i <= 3500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.12531000);
		}

		for(int i = 3501; i <= 4000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.17863200);
		}

		for(int i = 4001; i <= 4500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.2293580);
		}
		for(int i = 4501; i <= 5000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.2929000);
		}
		for(int i = 5001; i <= 5500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.3603980);
		}
		for(int i = 5501; i <= 6000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.4226500);
		}
		for(int i = 6001; i <= 6500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.4811260);
		}

		for(int i = 6501; i <= 7000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.5714400);
		}

		for(int i = 7001; i <= 7500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.6666680);
		}
		for(int i = 7501; i <= 8000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.7500000);
		}
		for(int i = 8001; i <= 8500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.8235300);
		}
		for(int i = 8501; i <= 9000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.8889000);
		}
		for(int i = 9001; i <= 9500 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 0.947369);
		}

		for(int i = 9501; i <= 10000 ; i++) {
			PRIVATE_RATE_INIT_MAP.put(i, 1.0);
		}



	}

}
