package info.xiaomo.server.system.user;

import info.xiaomo.server.db.DataCenter;
import info.xiaomo.server.db.DbDataType;
import info.xiaomo.server.entify.User;
import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.EventUtil;
import info.xiaomo.server.server.UserSession;
import info.xiaomo.server.server.SessionManager;
import info.xiaomo.server.util.IDUtil;
import info.xiaomo.server.util.MessageUtil;
import info.xiaomo.server.util.TimeUtil;
import org.slf4j.Logger;
import info.xiaomo.server.message.User.*;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 15:02
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class UserManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);
    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
    }


    /**
     * 登录游戏
     * @param session session
     */
    public void login(UserSession session, ReqLoginMessage msg) {
        if (msg.getLoginName().isEmpty()){
            return;
        }
        User user = DataCenter.getUser(msg.getLoginName());
        if (user == null) {
            // 新建用户
            user = createUser(msg.getLoginName());
            if (user == null) {
                LOGGER.error("用户创建失败:{},{},{}", msg.getLoginName());
                session.close();
                return;
            }
        }

        session.setUser(user); // 注册账户
        SessionManager.getInstance().register(user.getId(), session);// 注册session

        ResLoginMessage.Builder builder = ResLoginMessage.newBuilder();
        builder.setLoginName(msg.getLoginName());
        builder.setSex(msg.getSex());
        ResLoginMessage res = builder.build();
        MessageUtil.sendMsg(session.getUser().getId(),res);

        EventUtil.executeEvent(EventType.LOGIN, user);
    }


    /**
     * 创建角色
     * @param loginName loginName
     * @return User
     */
    private User createUser(String loginName) {
        User user = new User();
        long id = IDUtil.getId();
        user.setId(id);
        user.setLoginName(loginName);
        user.setServerId(1);
        user.setGmLevel(1);
        user.setPlatformId(1);
        user.setRegisterTime(TimeUtil.getNowOfSeconds());
        DataCenter.insertData(user, true);
        DataCenter.registerUser(user);
        return user;
    }


    /**
     * 退出
     * @param user user
     */
    public void logout(User user) {
        DataCenter.updateData(user.getId(), DbDataType.USER, false);

        EventUtil.executeEvent(EventType.LOGOUT, user);
    }
}
