package com.gogoyang.rpgapi.meta.log.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
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

    @Column(name = "partya_id")
    private Integer partyAId;

    @Column(name = "partyb_id")
    private Integer partyBId;

    @Transient
    private String partyAName;

    @Transient
    private String partyBName;

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

    public Integer getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Integer partyAId) {
        this.partyAId = partyAId;
    }

    public Integer getPartyBId() {
        return partyBId;
    }

    public void setPartyBId(Integer partyBId) {
        this.partyBId = partyBId;
    }

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }
}
