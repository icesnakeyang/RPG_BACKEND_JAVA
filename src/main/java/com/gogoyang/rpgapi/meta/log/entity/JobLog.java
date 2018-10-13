package com.gogoyang.rpgapi.meta.log.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class JobLog {
    @Id
    @GeneratedValue
    @Column(name = "job_log_id")
    private Integer jobLogId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "content")
    private String content;

    @Column(name = "read_time")
    private Date readTime;

    @Transient
    private String createdUserName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(Integer jobLogId) {
        this.jobLogId = jobLogId;
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

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }
}
