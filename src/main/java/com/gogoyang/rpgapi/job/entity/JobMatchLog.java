package com.gogoyang.rpgapi.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JobMatchLog {
    @Id
    @GeneratedValue
    @Column(name = "job_match_log_id")
    private Integer jobMatchLogId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "match_time")
    private Date matchTime;

    @Column(name = "match_user_id")
    private Integer matchUserId;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "process_result")
    private boolean processResult;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    public Integer getJobMatchLogId() {
        return jobMatchLogId;
    }

    public void setJobMatchLogId(Integer jobMatchLogId) {
        this.jobMatchLogId = jobMatchLogId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Integer getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Integer matchUserId) {
        this.matchUserId = matchUserId;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public boolean isProcessResult() {
        return processResult;
    }

    public void setProcessResult(boolean processResult) {
        this.processResult = processResult;
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
}
