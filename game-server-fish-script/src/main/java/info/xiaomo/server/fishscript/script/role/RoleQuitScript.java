package info.xiaomo.server.fishscript.script.role;


import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.fish.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;

/**
 * 退出
 *
 *
 * 2017年8月4日 下午2:14:32
 */
public class RoleQuitScript implements IRoleScript {

	@Override
	public void quit(UserRole role, GlobalReason reason) {
		RoleManager roleManager = RoleManager.getInstance();
		
		role.setGameId(0);
		roleManager.getOnlineRoles().remove(role.getId());
//		role.syncGold(Reason.UserQuit);
		roleManager.saveRoleData(role);
	}

	
}
