package com.gogoyang.rpgapi.job.job.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
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

    @Column(name = "reward")
    private Double reward;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "category")
    private String category;

    @Transient
    private String createdUserName;

    @Transient
    private String detail;

    /**
     * job是否已经成交
     */
    @Column(name = "is_match")
    private Boolean isMatch;

    @Column(name = "contract_time")
    private Date contractTime;

    @Column(name = "partyb_id")
    private Integer partyBId;

    /**
     * 申请的总人数
     */
    @Transient
    private Integer jobApplyNum;

    /**
     * 任务被分配的人数
     */
    @Transient
    private Integer jobMatchNum;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public Integer getJobApplyNum() {
        return jobApplyNum;
    }

    public void setJobApplyNum(Integer jobApplyNum) {
        this.jobApplyNum = jobApplyNum;
    }

    public Integer getJobMatchNum() {
        return jobMatchNum;
    }

    public void setJobMatchNum(Integer jobMatchNum) {
        this.jobMatchNum = jobMatchNum;
    }

    public Date getContractTime() {
        return contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }

    public Integer getPartyBId() {
        return partyBId;
    }

    public void setPartyBId(Integer partyBId) {
        this.partyBId = partyBId;
    }
}
