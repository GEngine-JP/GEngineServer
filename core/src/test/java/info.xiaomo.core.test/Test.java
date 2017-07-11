package info.xiaomo.core.test;

import info.xiaomo.core.config.ConfigDataManager;

import java.util.List;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 10:49
 * desc  : 测试
 * Copyright(©) 2017 by xiaomo.
 */
public class Test {
    public static void main(String[] args) {
        ConfigDataManager.getInstance().init("F:\\ChessGame\\data\\");
        List<TestConfig> list = ConfigDataManager.getInstance().getList(TestConfig.class);
        for (TestConfig testConfig : list) {
            System.out.println(testConfig.getName());
        }
    }
}
