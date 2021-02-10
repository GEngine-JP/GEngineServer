package info.xiaomo.server.test;

import info.xiaomo.server.config.ConfigDataManager;
import info.xiaomo.server.config.beans.ItemConfig;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/24 13:09
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class TestExcelConfig {

    public static void main(String[] args) throws Exception {
        ConfigDataManager.getInstance().init();
        ConfigDataManager.getInstance().getConfigs(ItemConfig.class).forEach(System.out::println);
    }

}
