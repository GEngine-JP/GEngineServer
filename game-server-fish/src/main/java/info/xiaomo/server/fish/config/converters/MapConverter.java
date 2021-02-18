/**
 * 创建日期:  2017年08月19日 10:01
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.server.fish.config.converters;


import info.xiaomo.gengine.config.IConverter;

import java.util.Map;

/**
 * @author YangQiang
 */
public class MapConverter implements IConverter<String, Map<Integer, Integer>> {
    @Override
    public Map<Integer, Integer> convert(String s) {
        return new IntegerArrayConverter().andThen(new IntegerMapConverter()).convert(s);
    }
}
