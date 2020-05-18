package com.gogoyang.rpgapi.meta.complete.entity;

import lombok.Data;

import java.util.Date;

@Data
public class JobComplete {
    private Integer ids;
    private String completeId;
    private String jobId;
    private String createdUserId;
    private String content;
    private Date createdTime;
    private Date readTime;
    private String status;
    private Date processTime;
    private String processRemark;
    private String processUserId;
    private Date processReadTime;
    private String createdUserName;
    private String processUserName;
}
