package info.xiaomo.server.back.msg;


import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.server.AbstractMessage;
import io.netty.buffer.ByteBuf;

/**
 * 重新加载配置文件
 */
public class ReqReloadCfgMessage extends AbstractMessage {

    @Override
    public void doAction() {
        BackManager.getInstance().reloadCfg(sequence, session, type, cfgName, cacheName);
    }

    public ReqReloadCfgMessage() {
        this.queueId = 1;
    }

    @Override
    public int getId() {
        return 1005;
    }

    /**
     * 类型（1 全部重载，2 加载指定的配置表 3 加载指定的cache）
     */
    private int type;

    /**
     * 配置表名字,type=2的时候生效
     */
    private String cfgName;

    /**
     * 缓存的类全名,type=3的时候生效
     */
    private String cacheName;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName;
    }


    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }


    @Override
    public boolean read(ByteBuf buf) {
        this.type = readInt(buf);
        this.cfgName = readString(buf);
        this.cacheName = readString(buf);

        return true;
    }

    @Override
    public boolean write(ByteBuf buf) {
        this.writeInt(buf, type);
        this.writeString(buf, cfgName);
        this.writeString(buf, cacheName);

        return true;
    }
}

