package info.xiaomo.server.rpg.config.tables;

import java.util.Map;
import info.xiaomo.gengine.config.IConfig;
import info.xiaomo.gengine.config.annotation.Column;
import info.xiaomo.gengine.config.annotation.Config;
import info.xiaomo.gengine.config.annotation.Table;
import info.xiaomo.gengine.config.converters.IntegerArrayConverter;
import info.xiaomo.gengine.config.converters.IntegerMapConverter;
import info.xiaomo.gengine.config.converters.Matrix3IntConverter;
import lombok.Data;
import lombok.ToString;

/** @author xiaomo */
@ToString
@Data
@Config
@Table(
        name = "cfg_item",
        primaryKey = {"id", "secondId"})
public class ItemConfig implements IConfig {
    @Column(notNull = true)
    private int id;

    @Column(name = "id2")
    private String secondId;

    // 多个转换器共同使用 先转成int数组 然后将int数组转为map
    @Column({IntegerArrayConverter.class, IntegerMapConverter.class})
    private Map<Integer, Integer> map;

    @Column(name = "ints")
    private int[] arrays;

    @Column(Matrix3IntConverter.class)
    private int[][][] intss;
}
