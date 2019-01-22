package com.gogoyang.rpgapi.meta.complete.entity;

import com.gogoyang.rpgapi.framework.constant.LogStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class JobComplete {
    @Id
    @GeneratedValue
    @Column(name = "complete_id")
    private Integer completeId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "result")
    private LogStatus result;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    @Column(name = "process_user_id")
    private Integer processUserId;

    @Column(name = "process_read_time")
    private Date processReadTime;

    @Transient
    private String createdUserName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getCompleteId() {
        return completeId;
    }

    public void setCompleteId(Integer completeId) {
        this.completeId = completeId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

    public LogStatus getResult() {
        return result;
    }

    public void setResult(LogStatus result) {
        this.result = result;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
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

    public Date getProcessReadTime() {
        return processReadTime;
    }

    public void setProcessReadTime(Date processReadTime) {
        this.processReadTime = processReadTime;
    }
}
