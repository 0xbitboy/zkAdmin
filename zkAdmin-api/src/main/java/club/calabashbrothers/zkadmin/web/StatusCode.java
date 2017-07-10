package club.calabashbrothers.zkadmin.web;

/**
 * Created by liaojiacan on 3/7/16.
 */
public enum StatusCode {
    SUCCESS(200,1),
    FAIL(-200,2);


    // 成员变量
    private int code;
    private int index;

    StatusCode(int code, int index) {
        this.code = code;
        this.index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
