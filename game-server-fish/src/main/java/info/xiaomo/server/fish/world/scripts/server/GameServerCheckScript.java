//package info.xiaomo.server.world.scripts.server;
//
//
//import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
//import RoleManager;
//
///**
// * 服务器状态监测脚本
// *
// *
// *  2017年7月10日 下午4:36:25
// */
//public class GameServerCheckScript implements IGameServerCheckScript {
//
//	@Override
//	public void buildServerInfo(com.jjy.game.message.GameServerInfo.Builder builder) {
//		IGameServerCheckScript.super.buildServerInfo(builder);
//		MinaServerConfig minaServerConfig = BydrWorldServer.getInstance().getGameHttpServer().getMinaServerConfig();
//		builder.setHttpport(minaServerConfig.getHttpPort());
//		builder.setIp(minaServerConfig.getIp());
//		builder.setOnline(RoleManager.getInstance().getOnlineRoles().size());    //设置在线人数
//	}
//
//}
