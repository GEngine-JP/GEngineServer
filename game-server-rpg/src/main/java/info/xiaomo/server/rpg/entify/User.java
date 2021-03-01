package info.xiaomo.server.rpg.entify;

import info.xiaomo.gengine.persist.mysql.persist.PersistAble;
import info.xiaomo.server.rpg.db.DataType;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

/** Date : 2017/7/11 13:42 desc : 玩家 Copyright(©) 2017 by xiaomo. */
@Data
public class User implements PersistAble {

  @Exclude private boolean dirty;

  @Tag(1)
  private long id;
  /** 0为普通玩家 */
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

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public int dataType() {
    return DataType.USER;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
}
