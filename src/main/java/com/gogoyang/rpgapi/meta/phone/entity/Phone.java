package com.gogoyang.rpgapi.meta.phone.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Phone {
    @Id
    @GeneratedValue
    @Column(name = "phone_id")
    private Integer phoneId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_verify")
    private Boolean isVerify;

    @Column(name = "verify_time")
    private Date verifyTime;

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getVerify() {
        return isVerify;
    }

    public void setVerify(Boolean verify) {
        isVerify = verify;
    }
}
