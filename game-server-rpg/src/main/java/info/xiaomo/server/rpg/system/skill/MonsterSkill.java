package info.xiaomo.server.rpg.system.skill;

import lombok.Data;

@Data
public class MonsterSkill {

    private int skillId;
    /** 技能配置的CD时间 */
    private int configCDTime;

    /** 使用该技能的最大血量百分比 */
    private int hpPercentMax;

    /** 使用该技能的最小血量百分比 */
    private int hpPercentMin;

    /** 最大使用距离 */
    private int attackDistance;

    /** 技能使用优先级 */
    private int priority;
}
