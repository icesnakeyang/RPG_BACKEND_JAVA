package com.gogoyang.rpgapi.meta.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobDetail{
    @Id
    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "detail")
    private String detail;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
