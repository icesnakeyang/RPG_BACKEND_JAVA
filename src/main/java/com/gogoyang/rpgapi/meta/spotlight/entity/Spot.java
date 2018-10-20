package com.gogoyang.rpgapi.meta.spotlight.entity;

import com.gogoyang.rpgapi.framework.constant.JobStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Spot {
    @Id
    @GeneratedValue
    @Column(name = "spot_id")
    private Integer spotId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Integer views;

    @Column(name = "comments")
    private Integer comments;

    @Column(name = "job_status")
    private JobStatus jobStatus;

    @Transient
    private String content;

    @Transient
    private Integer partyAId;

    @Transient
    private Integer partyBId;

    @Transient
    private String partyAName;

    @Transient
    private String partyBname;

    @Transient
    private String createdUserName;

    /////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getSpotId() {
        return spotId;
    }

    public void setSpotId(Integer spotId) {
        this.spotId = spotId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
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

    public String getPartyBname() {
        return partyBname;
    }

    public void setPartyBname(String partyBname) {
        this.partyBname = partyBname;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }
}
