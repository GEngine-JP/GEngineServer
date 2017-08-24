/**
 * 创建日期:  2017年08月19日 10:08
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.server.config.converters;


import info.xiaomo.gameCore.config.IConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YangQiang
 */
public class IntegerMapConverter implements IConverter<int[], Map<Integer, Integer>> {
    @Override
    public Map<Integer, Integer> convert(int[] ints) {
        Map<Integer, Integer> map = new HashMap<>();
        if (ints != null) {
            for (int i = 1; i < ints.length; i += 2) {
                map.put(ints[i - 1], ints[i]);
            }
        }
        return map;
    }
}
