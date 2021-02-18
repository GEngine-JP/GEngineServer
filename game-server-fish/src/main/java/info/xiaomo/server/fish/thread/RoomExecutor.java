package info.xiaomo.server.fish.thread;

import java.util.*;

import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.thread.ExecutorPool;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.entity.room.Room;
import info.xiaomo.server.fish.manager.ConfigManager;
import info.xiaomo.server.fish.thread.timer.RoomTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 逻辑执行线程池，将玩家的逻辑操作分配到同一个线程中执行，避免并发数据异常
 * <p>
 * <p>
 * 2017-03-24
 */
public class RoomExecutor implements ExecutorPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomExecutor.class);
    private static final ThreadGroup threadGroup = new ThreadGroup("房间线程组");

    // key:房间ID
    private final Map<Long, RoomThread> roomThreads = new HashMap<>();

    /**
     * 房间线程
     */
    final List<RoomThread> threads = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void execute(Runnable command) {
        if (command instanceof TcpHandler) {
            TcpHandler handler = (TcpHandler) command;

            UserRole role = RoleManager.getInstance().getRole(handler.getRid());
            if (role == null) {
                LOGGER.warn("角色{}不在线", handler.getRid());
                return;
            }
            handler.setPerson(role);
            ServerThread serverThread = roomThreads.get(role.getRoomId());
            if (serverThread == null) {
                LOGGER.warn("房间{}已经销毁", role.getRoomId());
                return;
            }
            serverThread.execute(handler);
        }
    }

    /**
     * 添加逻辑线程
     *
     * @return
     */
    public RoomThread addRoom(Room room) {
        if (roomThreads.containsKey(room.getId())) {
            LOGGER.warn("房间{}已在线程中", room.getId());
            return roomThreads.get(room.getId());
        }

        Optional<RoomThread> findAny = threads.stream().filter(
                thread -> thread.getRooms().size() < ConfigManager.getInstance().getGameConfig().getThreadRoomNum())
                .findAny();
        RoomThread roomThread;
        if (findAny.isPresent()) {
            roomThread = findAny.get();
            roomThread.getRooms().add(room);
            roomThread.getRoomTimer().addRoom(room);
        } else {
            roomThread = new RoomThread(threadGroup, room);
            threads.add(roomThread);
            roomThread.start();

            RoomTimer roomFishTimer = new RoomTimer(room, roomThread);
            roomThread.setRoomTimer(roomFishTimer);
            roomThread.addTimerEvent(roomFishTimer);
        }
//		room.setRoomThread(roomThread);
        roomThreads.put(room.getId(), roomThread);

        return roomThread;
    }

    /***
     * 移除线程
     *
     * @param room
     * @return
     */
    public RoomThread removeRoom(Room room) {
        RoomThread roomThread = roomThreads.remove(room.getId());
        roomThread.getRooms().remove(room);
        roomThread.getRoomTimer().removeRoom(room);
        return roomThread;
    }

    public void stop() {
        threads.forEach(thread -> thread.stop(true));
    }

    public RoomThread getRoomThread(long roomId) {
        return roomThreads.get(roomId);
    }

}
