//package info.xiaomo.server.gameserverscript.bydr.script.server;
//
//
//import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
//import RoleManager;
//import BydrServer;
//import info.xiaomo.server.shared.protocol.server.GameServerInfo;
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
//	public void buildServerInfo(GameServerInfo.Builder builder) {
//		IGameServerCheckScript.super.buildServerInfo(builder);
//		MinaServerConfig minaServerConfig = BydrServer.getInstance().getGameHttpServer().getMinaServerConfig();
//		builder.setHttpPort(minaServerConfig.getHttpPort());
//		builder.setIp(minaServerConfig.getIp());
//		builder.setOnline(RoleManager.getInstance().getOnlineRoles().size());	//设置在线人数
//	}
//
//}
