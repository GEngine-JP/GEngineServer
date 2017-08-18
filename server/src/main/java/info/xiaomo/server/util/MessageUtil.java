package info.xiaomo.server.util;


import com.google.protobuf.MessageOrBuilder;
import info.xiaomo.server.server.SessionManager;
import info.xiaomo.server.server.Session;

import java.util.Collection;

public class MessageUtil {

    public static void sendMsg(long id, MessageOrBuilder msg) {
        Session session = SessionManager.getInstance().getSession(id);
        if (session == null) {
            return;
        }
//        session.sendMessage(msg);
    }

    public static void sendMsg(MessageOrBuilder msg) {

    }


    public static void sendMsgToRids(MessageOrBuilder msg, long... rids) {
        for (long rid : rids) {
            sendMsg(rid, msg);
        }
    }

    public static void sendMsgToRids(MessageOrBuilder msg, Collection<Long> rids) {
        for (Long rid : rids) {
            if (rid != null) {
                sendMsg(rid, msg);
            }
        }
    }

    public static void sendMsgToRids(MessageOrBuilder msg, Collection<Long> rids, Long exceptRoleId) {
        for (Long rid : rids) {
            if (rid != null && (!rid.equals(exceptRoleId))) {
                sendMsg(rid, msg);
            }
        }
    }


}
