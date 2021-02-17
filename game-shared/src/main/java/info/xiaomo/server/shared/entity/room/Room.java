package info.xiaomo.server.shared.entity.room;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.server.shared.entity.Fish;
import info.xiaomo.server.shared.entity.UserRole;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.*;


/**
 * 房间信息
 * <p>
 * <p>
 * 2017-02-28
 */
@Data
@Entity(value = "room", noClassnameStored = true)
public class Room {
    @Id
    private long id;

    /**
     * 类型
     */
    private int type;
    /**
     * 级别
     */
    private int rank;

    /**
     * 所在线程
     */
//    private transient RoomThread roomThread;
    private transient long state; // 房间状态
    private transient long robotCreateTime; // 机器人创建时间
    /**
     * 房间位置、角色ID
     */
    private transient Map<Integer, UserRole> roles = new HashMap<>();
    private transient Map<Long, Fish> fishMap = new HashMap<>(); // 鱼
    // 多个刷新可能同时存在 value 单条鱼为List<Fish> 鱼群List<List<Fish>>
    private transient Map<Integer, Object> groupFishMap = new HashMap<>();
    private transient Set<Integer> refreshedBoom = new HashSet<>(); // 已经刷新的鱼潮ID
    private transient Map<Integer, Long> refreshTimes = new HashMap<>(); // 鱼刷新时间记录

    private transient long frozenEndTime; // 冰冻结束时间
    private transient long bossStartTime; // boss结束时间
    private transient long bossEndTime; // boss结束时间
    private transient long boomEndTime; // 鱼潮结束时间
    private transient long boomStartTime; // 鱼潮开始时间
    private transient int boomConfigId; // 鱼潮配置ID
    private transient int formationIndex; // 待刷新鱼潮阵型下标

    // 测试统计用
    private long allFireCount; // 房间总共开炮数
    private long hitFireCount; // 房间真实命中鱼数
    private long fireResultCount; // 房间结果请求数


    public Room() {
        id = Config.getId();
    }


    /**
     * 更新刷新时间
     *
     * @param refreshId 刷新ID
     * @param addTime   下次刷新增加时间
     */
    public void updateRefreshTime(int refreshId, int addTime) {
        refreshTimes.put(refreshId, System.currentTimeMillis() + addTime);
    }


    /**
     * 添加房间中的鱼
     */
    @SuppressWarnings("unchecked")
    public void addFish(Object obj) {
        if (obj instanceof Fish) {
            Fish fish = (Fish) obj;
            fishMap.put(fish.getId(), fish);
            if (!groupFishMap.containsKey(fish.getRefreshId())) {
                List<Fish> list = new ArrayList<>();
                list.add(fish);
                groupFishMap.put(fish.getRefreshId(), list);
            } else {
                ((List<Fish>) groupFishMap.get(fish.getRefreshId())).add(fish);
            }
        } else if (obj instanceof List) {
            List<Fish> fishs = (List<Fish>) obj;
            if (fishs.size() < 1) {
                return;
            }

            if (!groupFishMap.containsKey(fishs.get(0).getRefreshId())) {
                List<List<Fish>> list = new ArrayList<>();
                list.add(fishs);
                groupFishMap.put(fishs.get(0).getRefreshId(), list);
            } else {
                ((List<List<Fish>>) groupFishMap.get(fishs.get(0).getRefreshId())).add(fishs);
            }
            fishs.forEach(f -> fishMap.put(f.getId(), f));
        }
    }


}