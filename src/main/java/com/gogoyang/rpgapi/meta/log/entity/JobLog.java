package com.gogoyang.rpgapi.meta.log.entity;

import lombok.Data;

import java.util.Date;

@Data
public class JobLog {
    private Integer ids;
    private String jobLogId;
    private String jobId;
    private String createdUserId;
    private Date createdTime;
    private String content;
    private Date readTime;
    private String partyAId;
    private String partyBId;
    private String partyAName;
    private String partyBName;
    private String createdUserName;
}
