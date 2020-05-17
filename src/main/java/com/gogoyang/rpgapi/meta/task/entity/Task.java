package com.gogoyang.rpgapi.meta.task.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Task {
    private Integer ids;
    private String taskId;
    private String title;
    private Integer days;
    private Double price;
    private Date createdTime;
    private String createdUserId;
    private String pid;
    private String code;
    private String createdUserName;
    private String detail;
}
