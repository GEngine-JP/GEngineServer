package info.xiaomo.server.util;


import info.xiaomo.core.base.common.Cast;
import info.xiaomo.core.base.common.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * ip正则表达式
	 */
	public static final String IP_REGEX = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\."
			+ "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

	/**
	 * 是否是空字符串
	 *
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object obj, T defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		return (T) obj;
	}

	/**
	 * 格式化字符串，如果没有对应的参数则按照原样输出
	 * <p>
	 * <ul>
	 * 例如:
	 * <li>"获得{0}元宝,20"输出"获得20元宝"</li>
	 * <li>"{0}获得{1}元宝,XX"输出"XX获得{1}元宝"</li>
	 * <li>"{0}获得{1}元宝,XX,20"输出"XX获得20元宝"</li>
	 * </ul>
	 *
	 * @param str
	 * @param params
	 * @return
	 */
	public static String format(String str, Object... params) {
		if (isBlank(str)) {
			return str;
		}
		if (params == null || params.length == 0) {
			return str;
		}
		Pattern p = Pattern.compile("\\{(\\d+)}");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String param = m.group();
			int index = Integer.parseInt(m.group(1));
			if (params.length > index) {
				Object obj = params[index];
				if (obj != null) {
					param = obj.toString();
				}
			}
			m.appendReplacement(sb, param);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 是否包含有HTML标签
	 *
	 * @param str
	 * @return
	 */
	public static boolean containsHTMLTag(String str) {
		if (StringUtil.isBlank(str)) {
			return false;
		}

		String pattern = "<\\s*(\\S+)(\\s[^>]*)?>[\\s\\S]*<\\s*/\\1\\s*>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * String数组转成int数组
	 *
	 * @param numbers
	 * @return
	 */
	public static List<Integer> strArrToIntList(String[] numbers) {
		List<Integer> intArr = new ArrayList<>();
		for (String number : numbers) {
			intArr.add(Integer.parseInt(number));
		}
		return intArr;
	}

	/**
	 * String数组转成int数组
	 *
	 * @param numbers
	 * @return
	 */
	public static int[] strArrToIntArr(String[] numbers) {
		int[] intArr = new int[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			intArr[i] = Integer.parseInt(numbers[i]);
		}
		return intArr;
	}

	/**
	 * 根据指定的分隔符将字符串转为int数组
	 *
	 * @param source
	 * @param split
	 * @return
	 */
	public static int[] strToIntArr(String source, String split) {
		if (isBlank(source)) {
			return new int[0];
		}
		String[] numbers = source.split(split);
		return strArrToIntArr(numbers);
	}

	/**
	 * 字符串转换为Map<Integer, Integer>，用&和#号隔开
	 *
	 * @param str
	 * @return
	 */
	public static Map<Integer, Integer> strToIntMap(String str) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (isBlank(str)) {
			return map;
		} else {
			String[] allArray = str.split(Symbol.AND);
			if (allArray != null) {
				for (int i = 0; i < allArray.length; i++) {
					String[] strIntArr = allArray[i].split(Symbol.JINHAO);
					if (strIntArr != null && strIntArr.length == 2) {
						map.put(Cast.toInteger(strIntArr[0]), Cast.toInteger(strIntArr[1]));
					}
				}
			}
		}
		return map;
	}

	/**
	 * 字符串转换为Map<Integer, Integer>，用;和#号隔开
	 *
	 * @param str
	 * @return
	 */
	public static Map<Integer, Integer> strToIntMap2(String str) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (isBlank(str)) {
			return map;
		} else {
			String[] allArray = str.split(Symbol.FENHAO);
			if (allArray != null) {
				for (int i = 0; i < allArray.length; i++) {
					String[] strIntArr = allArray[i].split(Symbol.JINHAO);
					if (strIntArr != null && strIntArr.length == 2) {
						map.put(Cast.toInteger(strIntArr[0]), Cast.toInteger(strIntArr[1]));
					}
				}
			}
		}
		return map;
	}
}
