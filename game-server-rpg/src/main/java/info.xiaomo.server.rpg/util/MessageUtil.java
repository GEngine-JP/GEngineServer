package info.xiaomo.server.rpg.util;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Descriptors;
import java.util.Collection;
import java.util.Map;
import info.xiaomo.gengine.network.MsgPack;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.rpg.server.game.SessionManager;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class MessageUtil {

    public static void sendMsg(AbstractMessage msg, long id) {
        Session session = SessionManager.getInstance().getSession(id);
        if (session == null) {
            return;
        }
        sendMsg(session, msg);
    }

    public static void sendMsg(Session session, AbstractMessage msg) {
        int msgId = getMessageID(msg);
        MsgPack packet = new MsgPack(MsgPack.HEAD_TCP, msgId, msg.toByteArray());
        session.sendMessage(packet);
    }

    public static void sendMsgToRids(AbstractMessage msg, long... rids) {
        for (long rid : rids) {
            sendMsg(msg, rid);
        }
    }

    public static void sendMsgToRids(AbstractMessage msg, Collection<Long> rids) {
        for (Long rid : rids) {
            if (rid != null) {
                sendMsg(msg, rid);
            }
        }
    }

    public static void sendMsgToRids(
            AbstractMessage msg, Collection<Long> rids, Long exceptRoleId) {
        for (Long rid : rids) {
            if (rid != null && (!rid.equals(exceptRoleId))) {
                sendMsg(msg, rid);
            }
        }
    }

    private static int getMessageID(AbstractMessage msg) {
        for (Map.Entry<Descriptors.FieldDescriptor, Object> fieldDescriptorObjectEntry :
                msg.getAllFields().entrySet()) {
            if (fieldDescriptorObjectEntry.getKey().getName().equals("msgId")) {
                return ((Descriptors.EnumValueDescriptor) fieldDescriptorObjectEntry.getValue())
                        .getNumber();
            }
        }
        log.error("【{}】中未设置msgId, 内容：{}", msg.getClass().getSimpleName(), msg);
        return 0;
    }
}
