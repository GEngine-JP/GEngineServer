package info.xiaomo.server.fish.world;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.server.fish.manager.ConfigManager;
import info.xiaomo.server.shared.entity.Team;

/**
 * 组队管理
 *
 *
 *  2017年8月3日 上午11:42:22
 */
public class TeamManager {
	private static volatile TeamManager teamManager;
	private final Map<Long, Team> teams = new ConcurrentHashMap<>();

	private TeamManager() {

	}

	public static TeamManager getInstance() {
		if (teamManager == null) {
			synchronized (TeamManager.class) {
				if (teamManager == null) {
					teamManager = new TeamManager();
				}
			}
		}
		return teamManager;
	}

	public Team getIdleTeam(int rank) {
		Optional<Team> optional = teams.values().stream().filter(t -> t.getRank() == rank && t.getStatus() == 0
				&& t.getRoleIds().size() < ConfigManager.getInstance().getGameConfig().getRoomSize()).findAny();
		return optional.orElse(null);
	}

	public Map<Long, Team> getTeams() {
		return teams;
	}

}
