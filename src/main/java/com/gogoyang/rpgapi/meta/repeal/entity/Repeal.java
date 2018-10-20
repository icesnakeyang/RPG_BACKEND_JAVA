package com.gogoyang.rpgapi.meta.repeal.entity;

import com.gogoyang.rpgapi.framework.constant.LogStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Repeal {
    @Id
    @GeneratedValue
    @Column(name = "repeal_id")
    private Integer repealId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "spot_id")
    private Integer spotId;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "process_result")
    private LogStatus processResult;

    @Column(name = "process_remark")
    private String processRemark;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_user_id")
    private Integer processUserId;

    @Transient
    private String createdUserName;

    @Transient
    private String processUserName;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getRepealId() {
        return repealId;
    }

    public void setRepealId(Integer repealId) {
        this.repealId = repealId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getSpotId() {
        return spotId;
    }

    public void setSpotId(Integer spotId) {
        this.spotId = spotId;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public LogStatus getProcessResult() {
        return processResult;
    }

    public void setProcessResult(LogStatus processResult) {
        this.processResult = processResult;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Integer getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(Integer processUserId) {
        this.processUserId = processUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName;
    }
}
