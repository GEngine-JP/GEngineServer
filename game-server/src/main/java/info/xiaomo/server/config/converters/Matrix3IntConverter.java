/**
 * 创建日期:  2017年08月24日 10:56
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.server.config.converters;

import info.xiaomo.gengine.config.IConverter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;

/**
 * @author YangQiang
 */
public class Matrix3IntConverter implements IConverter<String, int[][][]> {
    @Override
    public int[][][] convert(String str) {
        if (str == null || str.isEmpty()) {
            return new int[0][][];
        }
        // 格式 1,2;3,4|5,6  =>  [[[1, 2], [3, 4]], [[5,6]]]
        // 基本类型
        IntegerConverter integerConverter = new IntegerConverter();
        // 一维数组 默认使用逗号分割
        ArrayConverter arrayConverter1 = new ArrayConverter(int[].class, integerConverter);

        // 二维数组
        ArrayConverter arrayConverter2 = new ArrayConverter(int[][].class, arrayConverter1);
        // 使用分号分割
        arrayConverter2.setDelimiter(';');
        arrayConverter2.setAllowedChars(new char[]{','});

        // 二维数组
        ArrayConverter arrayConverter3 = new ArrayConverter(int[][].class, arrayConverter2);
        // 使用竖线分割
        arrayConverter3.setDelimiter('|');
        arrayConverter3.setAllowedChars(new char[]{';', ','});

        return arrayConverter3.convert(int[][][].class, str);
    }
}
