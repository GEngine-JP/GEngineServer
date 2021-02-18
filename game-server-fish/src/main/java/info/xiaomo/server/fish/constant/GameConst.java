package info.xiaomo.server.fish.constant;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/16 9:47
 * desc  : 游戲常量類
 * Copyright(©) 2017 by xiaomo.
 */
public interface GameConst {

    interface QueueId {

        /**
         * 登录和下线队列
         */
        int LOGIN_LOGOUT = 1;

        /**
         * 业务队列
         */
        int LOGIC = 2;
    }

}
