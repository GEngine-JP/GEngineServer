package info.xiaomo.server.rpg.fsm.monster;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.server.rpg.map.Attribute;
import info.xiaomo.server.rpg.map.Buff;
import info.xiaomo.server.rpg.system.skill.MonsterSkill;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Monster extends AbstractMonster {

    private Attribute finalAttribute;

    private Map<Integer, Buff> bufferMap = new HashMap<>();

    private MonsterSkill[] skillArray = new MonsterSkill[0];

    @Override
    public int getLastX() {
        return 0;
    }

    @Override
    public void setLastX(int lastX) {}

    @Override
    public int getLastY() {
        return 0;
    }

    @Override
    public void setLastY(int lastY) {}

    @Override
    public int getLastMapId() {
        return 0;
    }

    @Override
    public void setLastMapId(int lastMapId) {}
}
