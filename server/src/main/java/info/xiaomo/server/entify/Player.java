package info.xiaomo.server.entify;

import info.xiaomo.core.db.persist.Persistable;
import info.xiaomo.server.constant.DataType;
import io.protostuff.Exclude;
import io.protostuff.Tag;

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
 * Date  : 2017/7/11 13:42
 * desc  : 玩家
 * Copyright(©) 2017 by xiaomo.
 */
public class Player implements Persistable {

    @Exclude
    private boolean dirty;

    @Tag(1)
    private long id;

    @Tag(2)
    private String name;


    @Override
    public long getId() {
        return id;
    }

    @Override
    public int dataType() {
        return DataType.PLAYER;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
