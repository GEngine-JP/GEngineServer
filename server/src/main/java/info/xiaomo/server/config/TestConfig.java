package info.xiaomo.server.config;


import info.xiaomo.gameCore.config.annotation.Column;
import info.xiaomo.gameCore.logger.annotation.Table;
import lombok.Data;

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
 * Date  : 2017/7/11 10:52
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@Data
@Table(tableName = "test")
public class TestConfig {

    @Column(notNull = true)
    private int id;

    @Column(name = "name")
    private String name;

}
