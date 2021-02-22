package info.xiaomo.server.rpg.db.mapper;

import info.xiaomo.gengine.persist.mysql.jdbc.RowMapper;
import info.xiaomo.server.rpg.entify.User;
import info.xiaomo.server.rpg.system.user.field.UserField;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 玩家数据的 映射器
 *
 * @author 小莫
 *         2017年6月6日 下午9:30:20
 */
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapping(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(UserField.ID));
        user.setLoginName(rs.getString(UserField.LOGIN_NAME));
        user.setServerId(rs.getInt(UserField.SERVER_ID));
        user.setPlatformId(rs.getInt(UserField.PLATFORM_ID));
        user.setGmLevel(rs.getInt(UserField.GM_LEVEL));
        user.setIdNumber(rs.getString(UserField.ID_NUMBER));
        user.setRegisterTime(rs.getInt(UserField.REGISTER_TIME));
        return user;
    }

    @Override
    public void release() {

    }

}
