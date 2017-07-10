package club.calabashbrothers.zkadmin.domain.entity;

import java.util.Date;

/**
 * Created by liaojiacan on 2017/6/28.
 */
public class BaseEntity {
    private Date createdDate;

    private Date updatedDate;

    private String opUser;


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser == null ? null : opUser.trim();
    }
}
