package com.gogoyang.rpgapi.job.meta.jobMatch.entity;

import com.gogoyang.rpgapi.constant.LogStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JobMatch {
    @Id
    @GeneratedValue
    @Column(name = "job_match_id")
    private Integer jobMatchId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "match_time")
    private Date matchTime;

    @Column(name = "match_user_id")
    private Integer matchUserId;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "process_result")
    private LogStatus processResult;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getJobMatchId() {
        return jobMatchId;
    }

    public void setJobMatchId(Integer jobMatchId) {
        this.jobMatchId = jobMatchId;
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

    public LogStatus getProcessResult() {
        return processResult;
    }

    public void setProcessResult(LogStatus processResult) {
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
