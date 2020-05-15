package com.gogoyang.rpgapi.meta.stop.entity;

import lombok.Data;

import java.util.Date;

@Data
public class JobStop {
    private Integer ids;
    private String stopId;
    private String jobId;
    private String createdUserId;
    private Date createdTime;
    private String content;
    private Double refund;
    private Date readTime;
    private String status;
    private Date processTime;
    private String processRemark;
    private String processUserId;
    private Date processReadTime;
    private String createdUserName;
    private String processUserName;
}
