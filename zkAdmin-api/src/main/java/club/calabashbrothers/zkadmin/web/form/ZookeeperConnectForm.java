package club.calabashbrothers.zkadmin.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by liaojiacan on 2017/7/6.
 */
public class ZookeeperConnectForm {
    @NotEmpty(message = "连接字符串不能为空")
    private  String connectString;
    private  Integer sessionTimeout;
    private String acl;
    private String remark;


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

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
