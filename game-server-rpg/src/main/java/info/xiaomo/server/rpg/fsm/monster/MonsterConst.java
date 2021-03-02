package info.xiaomo.server.rpg.fsm.monster;

/** 
 * 怪物类型
 * @author zhangli
 * 2017年6月6日 下午9:33:51   
 */
public interface MonsterConst {


	interface MonsterType{

		/**
		 * 普通怪
		 */
		int NORMAL = 1;

		/**
		 * BOSS
		 */
		int BOSS = 2;

		/**
		 * 月灵
		 */
		int YUELING = 3;
	}


	interface TargetChooseType{
		/**
		 * 仇恨机制
		 */
		int CHOU = 1;
		/**
		 * 点名机制
		 */
		int RANDOM= 2;
		/**
		 * 就近机制
		 */
		int NEAR= 3;
		/**
		 * 跟随某个目标的攻击对象
		 */
		int FOLLOW = 4;

	}

	/**
	 * 怪物复活类型
	 *
	 * @author zhangli 2017年6月6日 下午9:33:42
	 */
	public interface MonsterReliveType {

		/** 固定时间 */
		int FIX_TIME = 1;

		/** 每天定点复活 */
		int DAY_TIME = 2;
	}

}
