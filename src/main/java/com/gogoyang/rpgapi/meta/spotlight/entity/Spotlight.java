package com.gogoyang.rpgapi.meta.spotlight.entity;


import lombok.Data;

import java.util.Date;

@Data
public class Spotlight {
    private Integer ids;
    private String spotlightId;
    private String jobId;
    private String createdUserId;
    private Date createdTime;
    private String title;
    private String content;
    private String status;
    private Integer views;
    private Integer comments;
    private Integer upvote;
    private Integer downvote;
    private String partyAId;
    private String partyBId;
    private String partyAName;
    private String partyBname;
    private String createdUserName;
}
