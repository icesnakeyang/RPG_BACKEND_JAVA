package com.gogoyang.rpgapi.job.meta.job.entity;

import com.gogoyang.rpgapi.constant.JobStatus;
import com.gogoyang.rpgapi.constant.LogStatus;

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

    @Column(name = "account_balance")
    private Double accountBalance;

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

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Integer partyAId) {
        this.partyAId = partyAId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
}
