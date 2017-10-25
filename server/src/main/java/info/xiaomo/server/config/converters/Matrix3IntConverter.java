/**
 * 创建日期:  2017年08月24日 10:56
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.server.config.converters;

import info.xiaomo.core.config.IConverter;
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
        IntegerConverter integerConverter = new IntegerConverter(); // 基本类型
        ArrayConverter arrayConverter1 = new ArrayConverter(int[].class, integerConverter); // 一维数组 默认使用逗号分割

        ArrayConverter arrayConverter2 = new ArrayConverter(int[][].class, arrayConverter1); // 二维数组
        arrayConverter2.setDelimiter(';'); // 使用分号分割
        arrayConverter2.setAllowedChars(new char[]{','});


        ArrayConverter arrayConverter3 = new ArrayConverter(int[][].class, arrayConverter2); // 二维数组
        arrayConverter3.setDelimiter('|'); // 使用竖线分割
        arrayConverter3.setAllowedChars(new char[]{';', ','});

        return arrayConverter3.convert(int[][][].class, str);
    }
}
