package info.xiaomo.server.constant;

public enum LogAction {

    /**
     * gm
     */
    GM(1, "gm产生"),;

    private int code;

    private String comment;

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
