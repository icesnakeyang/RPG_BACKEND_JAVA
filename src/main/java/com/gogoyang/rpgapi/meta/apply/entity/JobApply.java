package com.gogoyang.rpgapi.meta.apply.entity;

import com.gogoyang.rpgapi.framework.constant.LogStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JobApply {
    @Id
    @GeneratedValue
    @Column(name = "job_apply_id")
    private Integer jobApplyId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "apply_user_id")
    private Integer applyUserId;

    @Column(name = "apply_time")
    private Date applyTime;

    @Column(name = "process_user_id")
    private Integer processUserId;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "process_result")
    private LogStatus processResult;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    @Column(name = "content")
    private String content;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getJobApplyId() {
        return jobApplyId;
    }

    public void setJobApplyId(Integer jobApplyId) {
        this.jobApplyId = jobApplyId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(Integer processUserId) {
        this.processUserId = processUserId;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
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

    public LogStatus getProcessResult() {
        return processResult;
    }

    public void setProcessResult(LogStatus processResult) {
        this.processResult = processResult;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
