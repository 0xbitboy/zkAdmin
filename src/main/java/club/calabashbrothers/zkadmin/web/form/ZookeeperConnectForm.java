package club.calabashbrothers.zkadmin.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by liaojiacan on 2017/7/6.
 */
public class ZookeeperConnectForm {
    @NotEmpty(message = "连接字符串不能为空")
    private  String connectString;
    private  Integer sessionTimeout;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
