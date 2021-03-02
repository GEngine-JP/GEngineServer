package info.xiaomo.server.rpg.system.move;

import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.IMove;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.constant.MapConst.Speed;
import info.xiaomo.gengine.map.obj.Performer;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.server.rpg.map.GameMap;
import info.xiaomo.server.rpg.map.MapManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoveManager implements IMove {

    private static final MoveManager INSTANCE = new MoveManager();

    public static MoveManager getInstance() {
        return INSTANCE;
    }

    public void playerWalk(Performer player, int x, int y) {

        // 检查一些buff不能移动的情况

        GameMap map = MapManager.getInstance().getMap(player.getMapId(), player.getLine());

        if (map == null) {
            return;
        }

        MapPoint point = player.getPoint();

        MapPoint targetPoint = map.getPoint(x, y);
        if (targetPoint == null) {
            log.info("{}|{}步行目标点为空[{},{}]", player.getId(), player.getName(), x, y);
            return;
        }
        if (player.getMovingPoint() != point) {
            // 移动失败，通知客户端拉回玩家
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());
            log.info("{}|{}步行速度过快", player.getId(), player.getName());
            return; // 步子太大
        }

        Dir dir = GeomUtil.getDir(point, targetPoint);

        if (GeomUtil.distance(point, targetPoint) > 1) {
            // 移动失败，通知客户端拉回玩家
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());
            log.info("{}|{}步行步子太大", player.getId(), player.getName());
            return;
        }

        MapPoint next = GeomUtil.nextDirPoint(point, dir);
        if (next == null) {
            // 移动失败，通知客户端拉回玩家
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());
            log.info("{}|{}步行路径中格子为空", player.getId(), player.getName());
            return;
        }
        if (moveToPoint(map, player, next)) {

            player.setLastPoint(point);
            player.setLastMoveSpeed(Speed.WALK);
            player.setLastMoveTime(System.currentTimeMillis());

            //			ResWalkMessage res = new ResWalkMessage();
            //			res.setX(next.x);
            //			res.setY(next.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendRoundMessage(res, player);
            map.getAoi().updateObject(player, point, next);
            map.getAoi().updateWatcher(player, point, next);

        } else {
            // 移动失败，通知客户端拉回玩家
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());
        }
    }

    public void playerRun(Performer player, int x, int y) {
        // 检查一些buff不能移动的情况

        // LOGGER.info(player.getName() + " 移动：" + x + "," + y);

        GameMap map = MapManager.getInstance().getMap(player.getMapId(), player.getLine());

        if (map == null) {
            return;
        }

        MapPoint targetPoint = map.getPoint(x, y);
        if (targetPoint == null) {
            log.info("{}|{}跑步目标点为空[{},{}]", player.getId(), player.getName(), x, y);
            return;
        }

        MapPoint point = player.getPoint();
        if (player.getMovingPoint() != point) {
            // 移动失败，通知客户端拉回玩家
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());

            log.info(
                    "{}|{}跑步速度过快, time:{}, speed:{}",
                    player.getId(),
                    player.getName(),
                    System.currentTimeMillis() - player.getLastMoveTime(),
                    player.getLastMoveSpeed());
            return; // 步子太大
        }

        Dir dir = GeomUtil.getDir(point, targetPoint);

        if (GeomUtil.distance(point, targetPoint) > 2) {
            //			ResChangePosMessage res = new ResChangePosMessage();
            //			res.setX(point.x);
            //			res.setY(point.y);
            //			res.setLid(player.getId());
            //			MessageUtil.sendMsg(res, player.getId());
            log.info("{}|{}跑步步子太大", player.getId(), player.getName());
            return;
        }
        MapPoint next = GeomUtil.nextDirPoint(point, dir);
        if (moveToPoint(map, player, next)) {
            next = GeomUtil.nextDirPoint(next, dir);
            if (moveToPoint(map, player, next)) {

                player.setLastPoint(point);
                player.setLastMoveSpeed(Speed.RUN);
                player.setLastMoveTime(System.currentTimeMillis());

                //                ResRunMessage res = new ResRunMessage();
                //                res.setX(next.x);
                //                res.setY(next.y);
                //                res.setLid(player.getId());
                //                MessageUtil.sendRoundMessage(res, player);
            } else {

                player.setLastPoint(point);
                player.setLastMoveSpeed(Speed.WALK);
                player.setLastMoveTime(System.currentTimeMillis());

                //                ResWalkMessage res = new ResWalkMessage();
                //                res.setX(next.x);
                //                res.setY(next.y);
                //                res.setLid(player.getId());
                //                MessageUtil.sendRoundMessage(res, player);
            }
            map.getAoi().updateObject(player, point, next);
            map.getAoi().updateWatcher(player, point, next);
        } else {
            // 移动失败，通知客户端拉回玩家
            //            ResChangePosMessage res = new ResChangePosMessage();
            //            res.setX(point.x);
            //            res.setY(point.y);
            //            res.setLid(player.getId());
            //            MessageUtil.sendMsg(res, player.getId());
        }
    }

    public void playerHorse(Performer player, int x, int y) {
        // 检查一些buff不能移动的情况

        GameMap map = MapManager.getInstance().getMap(player.getMapId(), player.getLine());

        if (map == null) {
            return;
        }

        MapPoint targetPoint = map.getPoint(x, y);
        if (targetPoint == null) {
            log.info("{}|{}骑马目标点为空[{},{}]", player.getId(), player.getName(), x, y);
            return;
        }

        MapPoint point = player.getPoint();
        if (player.getMovingPoint() != point) {

            // 移动失败，通知客户端拉回玩家
            //            ResChangePosMessage res = new ResChangePosMessage();
            //            res.setX(point.x);
            //            res.setY(point.y);
            //            res.setLid(player.getId());
            //            MessageUtil.sendMsg(res, player.getId());
            log.info(
                    "{}|{}骑马速度过快, time:{}, speed:{}",
                    player.getId(),
                    player.getName(),
                    System.currentTimeMillis() - player.getLastMoveTime(),
                    player.getLastMoveSpeed());
            return; // 步子太大
        }

        Dir dir = GeomUtil.getDir(point, targetPoint);

        if (GeomUtil.distance(point, targetPoint) > 3) {
            //            ResChangePosMessage res = new ResChangePosMessage();
            //            res.setX(point.x);
            //            res.setY(point.y);
            //            res.setLid(player.getId());
            //            MessageUtil.sendMsg(res, player.getId());
            log.info("{}|{}骑马步子太大", player.getId(), player.getName());
            return;
        }
        MapPoint next = GeomUtil.nextDirPoint(point, dir);
        if (moveToPoint(map, player, next)) { // 1步
            next = GeomUtil.nextDirPoint(next, dir);
            if (moveToPoint(map, player, next)) { // 2步
                next = GeomUtil.nextDirPoint(next, dir);
                if (moveToPoint(map, player, next)) { // 3步

                    player.setLastPoint(point);
                    player.setLastMoveSpeed(Speed.HORSE);
                    player.setLastMoveTime(System.currentTimeMillis());

                    //                    ResHorseMessage res = new ResHorseMessage();
                    //                    res.setX(next.x);
                    //                    res.setY(next.y);
                    //                    res.setLid(player.getId());
                    //                    MessageUtil.sendRoundMessage(res, player);

                } else {

                    player.setLastPoint(point);
                    player.setLastMoveSpeed(Speed.RUN);
                    player.setLastMoveTime(System.currentTimeMillis());

                    //                    ResRunMessage res = new ResRunMessage();
                    //                    res.setX(next.x);
                    //                    res.setY(next.y);
                    //                    res.setLid(player.getId());
                    //                    MessageUtil.sendRoundMessage(res, player);
                }
            } else {

                player.setLastPoint(point);
                player.setLastMoveSpeed(Speed.WALK);
                player.setLastMoveTime(System.currentTimeMillis());

                //                ResWalkMessage res = new ResWalkMessage();
                //                res.setX(next.x);
                //                res.setY(next.y);
                //                res.setLid(player.getId());
                //                MessageUtil.sendRoundMessage(res, player);
            }
            map.getAoi().updateObject(player, point, next);
            map.getAoi().updateWatcher(player, point, next);
        } else {
            // 移动失败，通知客户端拉回玩家
            //            ResChangePosMessage res = new ResChangePosMessage();
            //            res.setX(point.x);
            //            res.setY(point.y);
            //            MessageUtil.sendMsg(res, player.getId());
        }
    }

    /**
     * 这个主要用来给AI用的移动，玩家不允许调用该方法
     *
     * @param map
     * @param performer
     * @param point
     */
    public void performerMove(AbstractGameMap map, Performer performer, MapPoint point) {

        MapPoint oldPoint = performer.getPoint();
        map.stand(performer, point);
        map.getAoi().updateObject(performer, oldPoint, point);
        //        Message msg = buildMoveMsg(performer, oldPoint, point);
        //        MessageUtil.sendRoundMessage(msg, performer);
    }

    public void changeDir(Performer player, byte dir) {
        Dir enumDir = Dir.getByIndex(dir);
        if (enumDir != null && enumDir != Dir.NONE) {
            player.setDir(dir);
            //            ResChangeDirMessage res = new ResChangeDirMessage();
            //            res.setDir(dir);
            //            res.setLid(player.getId());
            //            MessageUtil.sendMsg(res, player.getId());
        }
    }

    private boolean moveToPoint(GameMap map, Performer player, MapPoint point) {

        if (point == null) {
            log.info("{}|{}移动中路径中格子为空", player.getId(), player.getName());
            return false;
        }
        if (point.canWalk(player, false)) {
            map.stand(player, point);
            if (point.isHasEvent()) {
                player.setMoved(true);
            }
            return true;
        } else {
            log.info("{}|{}移动中路径中格子不能站立->{}", player.getId(), player.getName(), point);
        }
        return false;
    }
}
