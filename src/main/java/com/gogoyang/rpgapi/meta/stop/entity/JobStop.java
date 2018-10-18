package com.gogoyang.rpgapi.meta.stop.entity;

import com.gogoyang.rpgapi.framework.constant.LogStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class JobStop {
    @Id
    @GeneratedValue
    @Column(name = "stop_id")
    private Integer stopId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "content")
    private String content;

    @Column(name = "refund")
    private Double refund;

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

    @Transient
    private String createdUserName;

    @Transient
    private String processUserName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Integer getStopId() {
        return stopId;
    }

    public void setStopId(Integer stopId) {
        this.stopId = stopId;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getRefund() {
        return refund;
    }

    public void setRefund(Double refund) {
        this.refund = refund;
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

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName;
    }
}
