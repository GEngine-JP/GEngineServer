package info.xiaomo.server.db.msyql;

import info.xiaomo.core.db.persist.PersistFactory;
import info.xiaomo.core.db.persist.Persistable;
import info.xiaomo.server.db.DbDataType;
import info.xiaomo.server.entify.User;

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
 * Date  : 2017/7/13 17:25
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class UserPersistFactory implements PersistFactory {
    private static final String INSERT = "insert into p_user (" +
            "id," +
            " login_name," +
            " server_id," +
            " platform_id," +
            " gm_level," +
            " id_number," +
            " register_time" +
            ") values (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE p_user set gm_level = ?, id_number = ? where id = ?";

    private static final String DELETE = "delete from p_user where id = ?";


    @Override
    public String name() {
        return "用户";
    }

    @Override
    public int dataType() {
        return DbDataType.USER;
    }

    @Override
    public String createInsertSql() {
        return INSERT;
    }

    @Override
    public String createUpdateSql() {
        return UPDATE;
    }

    @Override
    public String createDeleteSql() {
        return DELETE;
    }

    @Override
    public Object[] createInsertParameters(Persistable obj) {
        User user = (User) obj;
        return createParameters(user);
    }

    @Override
    public Object[] createUpdateParameters(Persistable obj) {
        User user = (User) obj;
        return createParameters(user);
    }

    @Override
    public Object[] createDeleteParameters(Persistable obj) {
        return new Object[]{obj.getId()};
    }

    @Override
    public long taskPeriod() {
        return 60 * 1000;
    }

    private Object[] createParameters(User user) {
        return new Object[]{
                user.getId(),
                user.getLoginName(),
                user.getNickName(),
                user.getServerId(),
                user.getPlatformId(),
                user.getGmLevel(),
                user.getIdNumber(),
                user.getRegisterTime()
        };
    }
}
