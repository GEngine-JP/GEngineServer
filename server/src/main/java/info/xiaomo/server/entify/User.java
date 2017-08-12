package info.xiaomo.server.entify;

import info.xiaomo.gameCore.persist.persist.Persistable;
import info.xiaomo.server.db.DbDataType;
import io.protostuff.Exclude;
import io.protostuff.Tag;
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
 * Date  : 2017/7/11 13:42
 * desc  : 玩家
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class User implements Persistable {

    @Exclude
    private boolean dirty;

    @Tag(1)
    private long id;
    /**
     * 0为普通玩家
     */
    @Tag(2)
    private int gmLevel;

    @Tag(3)
    private String loginName;

    @Tag(4)
    private int serverId;

    @Tag(5)
    private int platformId;

    @Tag(6)
    private String idNumber;

    @Tag(7)
    private int registerTime;


    @Override
    public long getId() {
        return id;
    }

    @Override
    public int dataType() {
        return DbDataType.USER;
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

}
