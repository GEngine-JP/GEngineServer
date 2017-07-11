package info.xiaomo.server.util;

public enum LogAction {

    GM(1, "gm产生"),
    PICK_UP(2, "拾取"),
    GIVE_UP(3, "放弃"),
    USE(4, "使用"),
    UP_SKILL_COST(5, "升级技能消耗"),
    BOX(6, "开宝箱"),
    ACCEPT_TASK(7, "接受任务获得任务道具"),
    TASK_COST(8, "任务消耗"),
    DELIVER(9, "传送"),
    TASK_REWARD(10, "任务奖励"),
    MONSTER_DIE(11, "怪物死亡"),
    PUT_ON_EQUIP(12, "穿装备"),
    PUT_OFF_EQUIP(13, "脱装备"),
    ITEM_COMPOUND(14, "合成道具"),
    RECYCLE_EQUIP(15, "装备回收"),
    STORE_BUY(16, "商城购买获得"),
    COST_BY_STORE_BUY(17, "商城购买消耗"),
    ENTER_INSTANCE(18, "进入副本"),
    DONATE_TO_WORLD_WAREHOUSE(19, "世界仓库捐献"),
    TAKEOUT_FROM_WORLD_WAREHOUSE(20, "世界仓库提取"),
    BIAOCHE_COST(21, "押镖消耗"),
    BIAOCHE_REWARD(22, "押镖奖励"),
    RELIVE(23, "复活"),
    MULTI_BUBBLE_GET(24, "多倍泡点获得"),
    MULTI_BUBBLE_COST(25, "多倍泡点消耗"),
    ROAST_PIGS_COST(26, "烧猪消耗"),
    INSTANCE_REWARD(27, "副本奖励"),
    BOSS_DIG_REWARD(28, "boss挖掘"),
    BOSS_DIG_COST(29, "Boss挖掘消耗"),
    SLOT_SKILL_COST(30, "连击技能消耗"),
    CREATE_UNION_COST(31, "创建行会消耗"),
    ACTIVATE_BREAK_COST(32, "激活突破消耗"),
    COMPLETE_BREAK_REWARD(33, "完成突破奖励"),
    LEVEL_UP_GIFT_COST(34, "升级天赋消耗"),
    RESET_GIFT_COST(35, "重置天赋消耗"),
    ;

    private int code;

    private String comment;

    LogAction(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }


    public int getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
