package info.xiaomo.server.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.server.struct.room.Room;
import info.xiaomo.server.thread.timer.RoomTimer;

/**
 * 房间逻辑线程
 * <p>
 * 一个线程处理多个房间
 * </p>
 * 
 *
 * @date 2017-03-24
 */
public class RoomThread extends ServerThread {
	private static final AtomicInteger threadNum = new AtomicInteger(0);
	private final List<Room> rooms = new ArrayList<>();
	private RoomTimer roomTimer;

	public RoomThread(ThreadGroup threadGroup, Room room) {
		super(threadGroup, ThreadType.ROOM + "-" + threadNum.getAndIncrement(), 500, 10000); // TODO
        rooms.add(room);
	}

	public RoomTimer getRoomTimer() {
		return roomTimer;
	}

	public void setRoomTimer(RoomTimer roomFishTimer) {
        roomTimer = roomFishTimer;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	

	public Room getRoom(long roomId) {
		Optional<Room> findAny = rooms.stream().filter(r -> r.getId() == roomId).findAny();
		return findAny.orElse(null);
	}

}
