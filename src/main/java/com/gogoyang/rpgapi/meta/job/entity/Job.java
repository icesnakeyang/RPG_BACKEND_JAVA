package com.gogoyang.rpgapi.meta.job.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Job {
    private Integer ids;
    private String jobId;
    private String code;
    private String taskId;
    private String title;
    private Integer days;
    private Double price;
    private String partyAId;
    private String partyAName;
    private Date createdTime;
    private String category;
    /**
     * 任务详情保存在jobDetail表里
     */
    private String detail;
    /**
     * job当前状态
     */
    private String status;

    /**
     * 签约时间
     */
    private Date contractTime;

    private String matchId;

    /**
     * partyB id
     */
    private String partyBId;

    /**
     * 乙方的姓名
     */
    private String partyBName;

    /**
     * 申请的总人数
     */
    private Integer totalApply;
    private Integer totalApplyUnread;

    /**
     * 任务被分配的人数
     */
    private Integer totalMatch;
    private Integer totalMatchUnread;

    /**
     * 我未阅读的日志总数
     */
    private Integer totalLog;
    private Integer totalLogUnread;

    /**
     * 统计完成日志
     */
    private Integer totalComplete;
    private Integer totalCompleteUnread;

    /**
     * 统计终止日志
     */
    private Integer totalStop;
    private Integer totalStopUnread;

    /**
     * 申诉次数
     */
    private Integer totalSpotlight;
    private Integer totalSpotlightUnread;
}
