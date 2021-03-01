package info.xiaomo.server.rpg.constant;

/** @author xiaomo */
public enum LogAction {

    /** gm */
    GM(1, "gm产生"),
    ;

    private final int code;

    private final String comment;

    LogAction(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public int getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
