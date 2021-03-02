package info.xiaomo.server.rpg.map;

import io.protostuff.Tag;
import lombok.Data;

@Data
public class Attribute {
    /** 物理攻击上限 */
    @Tag(1)
    protected int phyAttMax = 0;

    /** 物理攻击下限 */
    @Tag(2)
    protected int phyAttMin = 0;

    /** 魔法攻击上限 */
    @Tag(3)
    protected int magicAttMax = 0;
    /** 魔法攻击上限 */
    @Tag(4)
    protected int magicAttMin = 0;
    /** 道术攻击上限 */
    @Tag(5)
    protected int taoAttMax = 0;
    /** 道术攻击下限 */
    @Tag(6)
    protected int taoAttMin = 0;
    /** 物理防御上限 */
    @Tag(7)
    protected int phyDefMax = 0;
    /** 物理防御下限 */
    @Tag(8)
    protected int phyDefMin = 0;
    /** 魔法防御上限 */
    @Tag(9)
    protected int magicDefMax = 0;
    /** 魔法防御下限 */
    @Tag(10)
    protected int magicDefMin = 0;

    /** 精准 */
    @Tag(11)
    protected int accurate = 0;

    /** 闪避 */
    @Tag(12)
    protected int dodge = 0;

    /** 攻击速度 */
    @Tag(13)
    protected int attackSpeed = 0;

    /** 最大HP */
    @Tag(14)
    protected int maxHp = 0;

    /** 最大MP */
    @Tag(15)
    protected int maxMp = 0;

    /** 幸运 */
    @Tag(16)
    protected int luck = 0;

    /** 暴击概率 */
    @Tag(17)
    protected int critical = 0;

    /** 暴击伤害（万分比） */
    @Tag(18)
    protected int critDmg = 0;

    /** 暴击固定增加伤害 */
    @Tag(19)
    protected int critFix = 0; // 暴击增加的固定伤害

    /** 最大内力 */
    @Tag(20)
    protected int maxInnerPower = 0;

    /** 战斗力 */
    @Tag(21)
    protected int fightPower = 0;

    /** 非战斗状态生命恢复 */
    @Tag(22)
    protected int lifeRec = 0;

    /** 战斗状态生命恢复 */
    @Tag(23)
    protected int fightRec = 0;

    /** 对战士伤害增加 */
    @Tag(24)
    protected int zsHurt = 0;

    /** 对法师伤害增加 */
    @Tag(25)
    protected int fsHurt = 0;

    /** 对道士伤害增加 */
    @Tag(26)
    protected int dsHurt = 0;

    /** 受战士伤害减免 */
    @Tag(27)
    protected int zsReliefHurt = 0;

    /** 受法师伤害减免 */
    @Tag(28)
    protected int fsReliefHurt = 0;

    /** 受道士伤害减免 */
    @Tag(29)
    protected int dsReliefHurt = 0;

    /** 对怪物伤害增加 */
    @Tag(30)
    protected int monsterHurt = 0;

    /** 受怪物伤害减免 */
    @Tag(31)
    protected int monsterReliefHurt = 0;

    public void fixAdd(Attribute attr) {

        phyAttMax += attr.phyAttMax;

        // 物理攻击下限 */
        phyAttMin += attr.phyAttMin;

        // 魔法攻击上限 */
        magicAttMax += attr.magicAttMax;
        // 魔法攻击上限 */
        magicAttMin += attr.magicAttMin;
        // 道术攻击上限 */
        taoAttMax += attr.taoAttMax;
        // 道术攻击下限 */
        taoAttMin += attr.taoAttMin;
        // 物理防御上限 */
        phyDefMax += attr.phyDefMax;
        // 物理防御下限 */
        phyDefMin += attr.phyDefMin;
        // 魔法防御上限 */
        magicDefMax += attr.magicDefMax;
        // 魔法防御下限 */
        magicDefMin += attr.magicDefMin;

        // 精准 */
        accurate += attr.accurate;

        // 闪避 */
        dodge += attr.dodge;

        // 攻击速度 */
        attackSpeed += attr.attackSpeed;

        // 最大HP */
        maxHp += attr.maxHp;

        // 最大MP */
        maxMp += attr.maxMp;

        luck += attr.luck;

        // 暴击概率 */
        critical += attr.critical;

        maxInnerPower += attr.maxInnerPower;

        critFix += attr.critFix;

        fightPower += attr.fightPower;

        critDmg += attr.critDmg;

        lifeRec += attr.lifeRec;

        fightRec += attr.fightRec;

        // 对战士伤害增加 */
        zsHurt += attr.zsHurt;

        // 对法师伤害增加 */
        fsHurt += attr.fsHurt;

        // 对道士伤害增加 */
        dsHurt += attr.dsHurt;

        // 受战士伤害减免 */
        zsReliefHurt += attr.zsReliefHurt;

        // 受法师伤害减免 */
        fsReliefHurt += attr.fsReliefHurt;

        // 受道士伤害减免 */
        dsReliefHurt += attr.dsReliefHurt;

        // 对怪物伤害增加 */
        monsterHurt += attr.monsterHurt;
        // 受怪物伤害减免 */
        monsterReliefHurt += attr.monsterReliefHurt;
    }

    public void percentAdd(Attribute attr) {

        double percent = 100.0;
        phyAttMax += phyAttMax * (attr.phyAttMax) / percent;

        // 物理攻击下限 */
        phyAttMin += phyAttMin * (attr.phyAttMin) / percent;

        // 魔法攻击上限 */
        magicAttMax += magicAttMax * (attr.magicAttMax) / percent;
        // 魔法攻击上限 */
        magicAttMin += magicAttMin * (attr.magicAttMin) / percent;
        // 道术攻击上限 */
        taoAttMax += taoAttMax * (attr.taoAttMax) / percent;
        // 道术攻击下限 */
        taoAttMin += taoAttMin * (attr.taoAttMin) / percent;
        // 物理防御上限 */
        phyDefMax += phyDefMax * (attr.phyDefMax) / percent;
        // 物理防御下限 */
        phyDefMin += phyDefMin * (attr.phyDefMin) / percent;
        // 魔法防御上限 */
        magicDefMax += magicDefMax * (attr.magicDefMax) / percent;
        // 魔法防御下限 */
        magicDefMin += magicDefMin * (attr.magicDefMin) / percent;

        // 精准 */
        accurate += accurate * (attr.accurate) / percent;

        // 闪避 */
        dodge += dodge * (attr.dodge) / percent;

        // 攻击速度 */
        attackSpeed += attackSpeed * (attr.attackSpeed) / percent;

        // 最大HP */
        maxHp += maxHp * (attr.maxHp) / percent;

        // 最大MP */
        maxMp += maxMp * (attr.maxMp) / percent;

        luck += luck * (attr.luck) / percent;

        // 暴击概率 */
        critical += critical * (attr.critical) / percent;

        maxInnerPower += maxInnerPower * (attr.maxInnerPower) / percent;

        critFix += critFix * (attr.critFix) / percent;

        critDmg += critDmg * (attr.critDmg) / percent;

        lifeRec += lifeRec * (attr.lifeRec) / percent;

        fightRec += fightRec * (attr.fightRec) / percent;

        fightPower += fightPower * (attr.fightPower) / percent;

        // -----------------------只对怪 生效---------------------------------

        // 对战士伤害增加 */
        zsHurt += zsHurt * (attr.zsHurt) / percent;

        // 对法师伤害增加 */
        fsHurt += fsHurt * (attr.fsHurt) / percent;

        // 对道士伤害增加 */
        dsHurt += dsHurt * (attr.dsHurt) / percent;

        // 受战士伤害减免 */
        zsReliefHurt += zsReliefHurt * (attr.zsReliefHurt) / percent;

        // 受法师伤害减免 */
        fsReliefHurt += fsReliefHurt * (attr.fsReliefHurt) / percent;

        // 受道士伤害减免 */
        dsReliefHurt += dsReliefHurt * (attr.dsReliefHurt) / percent;

        // 对怪物伤害增加 */
        monsterHurt += monsterHurt * (attr.monsterHurt) / percent;
        // 受怪物伤害减免 */
        monsterReliefHurt += monsterReliefHurt * (attr.monsterReliefHurt) / percent;
    }
}
