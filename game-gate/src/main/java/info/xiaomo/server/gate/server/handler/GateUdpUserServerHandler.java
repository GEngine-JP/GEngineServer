package info.xiaomo.server.gate.server.handler;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.handler.ClientProtocolHandler;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.server.gate.struct.UserSession;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * udp消息处理器
 *
 * <p>2017年9月1日 下午2:06:36
 */
public class GateUdpUserServerHandler extends ClientProtocolHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GateUdpUserServerHandler.class);

  public GateUdpUserServerHandler() {
    super(8);
  }

  public GateUdpUserServerHandler(GameService<MinaServerConfig> service) {
    this();
    this.gameService = service;
  }

  /**
   * 消息转发到大厅服或游戏服务器
   *
   * @param bytes 前8字节分别为消息ID、protobuf长度
   */
  @Override
  protected void forward(IoSession session, int msgID, byte[] bytes) {
    // 转发到大厅服
    if (msgID < Config.HALL_MAX_MID) {
      forwardToHall(session, msgID, bytes);
      return;
    }
    // 转发到游戏服
    Object attribute = session.getAttribute(Config.USER_SESSION);
    if (attribute != null) {
      UserSession userSession = (UserSession) attribute;
      if (userSession.getRoleId() > 0) {
        if (userSession.sendToGame(MsgUtil.clientToGame(msgID, bytes))) {
          return;
        } else {
          LOGGER.warn("角色[{}]没有连接游戏服务器,消息{}发送失败", userSession.getRoleId(), msgID);
          return;
        }
      }
    }
    LOGGER.warn("{}消息[{}]未找到玩家", MsgUtil.getIp(session), msgID);
  }

  /**
   * 消息转发到大厅服务器
   *
   * <p>2017年7月21日 上午10:14:44
   *
   * @param session
   * @param msgID
   * @param bytes
   */
  private void forwardToHall(IoSession session, int msgID, byte[] bytes) {
    Object attribute = session.getAttribute(Config.USER_SESSION);
    if (attribute != null) {
      UserSession userSession = (UserSession) attribute;
      if (userSession.getRoleId() > 0) {
        if (!userSession.sendToHall(MsgUtil.clientToGame(msgID, bytes))) {
          LOGGER.warn("角色[{}]没有连接大厅服务器", userSession.getRoleId());
          return;
        }
      }
    }
    LOGGER.warn("[{}]消息未找到对应的处理方式", msgID);
  }

  @Override
  public GameService<MinaServerConfig> getService() {
    return gameService;
  }

  @Override
  public void sessionOpened(IoSession session) {
    super.sessionOpened(session);
    //		UserSession userSession = new UserSession(session);
    //		session.setAttribute(Config.USER_SESSION, userSession);
    // TODO
  }

  @Override
  public void sessionClosed(IoSession session) {
    super.sessionClosed(session);
    //		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class,
    //				script -> script.quit(session, Reason.SessionClosed));
    session.closeNow(); // TODO ?
  }

  @Override
  public void sessionIdle(IoSession session, IdleStatus idleStatus) {
    super.sessionIdle(session, idleStatus);
    //		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class,
    //				script -> script.quit(session, Reason.SessionIdle));
    session.closeNow(); // TODO ?
  }
}
