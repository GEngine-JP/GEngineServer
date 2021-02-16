/**
 * 创建日期:  2017年08月19日 9:57
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.server.config.converters;


import info.xiaomo.gengine.config.IConverter;

/**
 * @author YangQiang
 */
public class IntegerArrayConverter implements IConverter<String, int[]> {
    @Override
    public int[] convert(String s) {
        String[] strs = s.split("#");
        int[] ret = new int[strs.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Integer.parseInt(strs[i]);
        }
        return ret;
    }
}
