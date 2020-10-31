package com.gogoyang.rpgapi.meta.team.entity;

import lombok.Data;

import java.util.Date;

/**
 * 团队
 */
@Data
public class Team {
    private Integer ids;
    private String teamId;
    private String teamName;
    private String city;
    /**
     * 团队技能树Id
     */
    private String description;
    private String createUserId;
    private Date createTime;
    private String status;
    private String managerId;
    /**
     * 团队技能树Id
     */
    private String skillTreeId;
}
