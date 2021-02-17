package info.xiaomo.server.shared.entity;

import info.xiaomo.gengine.entity.AbsPerson;
import info.xiaomo.gengine.utils.ReflectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;


/**
 * 角色信息
 * <p>
 * <p>
 * 2017-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "role", noClassnameStored = true)
public class UserRole extends AbsPerson {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRole.class);

    /**
     * setter 方法集合
     */
    public static final transient Map<String, Method> WRITEMETHODS = ReflectUtil.getReadMethod(UserRole.class);

    private String head;
    private int gender;
    private boolean is_vip;
    private String sign; // 签名
    private Date update_date;
    private Date cdate;
    private String roleRedisKey;
    private boolean is_sync_from_hall; // 是否已经和大厅同步

    private int giftGold; // 新手赠送金币
    private volatile long dayAwardGold; // 每日领奖金币
    private volatile long dayFireGold; // 每日开炮金币
    private int pay_rmb; // 充值额
    private long winGold; // 输赢的钱，需要同步更新到大厅数据库
    private long roomId; // 房间ID
    private int seatNo; // 座位编号
    private int roomType; // 房间类型
    @Embedded
    private List<Integer> presentFishs = new ArrayList<>(); // 赠送鱼，百分之百命中

    @Embedded
    private List<Integer> presentAccumulates = new ArrayList<>(); // 赠送累积奖，值为
    // 房间类型*10+0|1
    // 0从真池获取，1从假池获取
    @Embedded
    private List<Integer> fireGolds = new ArrayList<>(); // 开炮金币数
    private int betGold; // 当前下注金币总和
    private long enterRoomGold; // 进入房间时玩家的金币
    private long fireTime; // 玩家最后开炮时间

    // 玩家习惯统计
    private transient int fireCount; // 开炮次数
    private transient Map<Integer, Integer> fishHits = new HashMap<>(); // 命中鱼统计
    private transient Map<Integer, Integer> fishDies = new HashMap<>(); // 死亡鱼统计

    private transient int ownerId; // 机器人所属玩家ID
    private transient long leaveTime; // 机器人离开房间时间


}