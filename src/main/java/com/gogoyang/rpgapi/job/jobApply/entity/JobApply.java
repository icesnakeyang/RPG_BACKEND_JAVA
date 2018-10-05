package com.gogoyang.rpgapi.job.jobApply.entity;

import com.gogoyang.rpgapi.constant.LogStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JobApply {
    @Id
    @GeneratedValue
    @Column(name = "ids")
    private Integer ids;

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

    @Column(name = "result")
    private LogStatus result;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
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

    public LogStatus getResult() {
        return result;
    }

    public void setResult(LogStatus result) {
        this.result = result;
    }
}
