package com.gogoyang.rpgapi.meta.job.entity;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue
    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "code")
    private String code;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "title")
    private String title;

    @Column(name = "days")
    private Integer days;

    @Column(name = "price")
    private Double price;

    @Column(name = "partya_id")
    private Integer partyAId;

    @Transient
    private String partyAName;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "category")
    private String category;

    /**
     * 任务详情保存在jobDetail表里
     */
    @Transient
    private String detail;

    /**
     * job当前状态
     */
    @Column(name = "status")
    private JobStatus status;

    /**
     * 签约时间
     */
    @Column(name = "contract_time")
    private Date contractTime;

    @Column(name = "match_id")
    private Integer matchId;

    /**
     * partyB id
     */
    @Column(name = "partyb_id")
    private Integer partyBId;

    /**
     * 乙方的姓名
     */
    @Transient
    private String partyBName;

    /**
     * 申请的总人数
     */
    @Column(name = "job_apply_num")
    private Integer jobApplyNum;

    /**
     * 任务被分配的人数
     */
    @Column(name = "job_match_num")
    private Integer jobMatchNum;

    /**
     * 我未阅读的日志总数
     */
    @Transient
    private Integer unRead;

    @Column(name="spot_num")
    private Integer spotNum;

}
