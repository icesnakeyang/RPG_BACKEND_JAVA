package com.gogoyang.rpgapi.meta.userAction.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class UserActionLog {
    @Id
    @GeneratedValue
    @Column(name = "ids")
    private Integer ids;

    @Column(name = "log_id")
    private String userActionLogId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "act_type")
    private String actType;

    @Column(name = "memo")
    private String memo;
}
