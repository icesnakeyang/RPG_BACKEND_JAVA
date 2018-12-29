package com.gogoyang.rpgapi.meta.honor.entity;

import com.gogoyang.rpgapi.framework.constant.HonorType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Honor {
    @Id
    @GeneratedValue
    @Column(name = "honor_id")
    private Integer honorId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "point")
    private Double point;

    @Column(name = "type")
    private HonorType type;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "remark")
    private String remark;
    ////////////////////////////////////////////////////////////////////////////////
    public Integer getHonorId() {
        return honorId;
    }

    public void setHonorId(Integer honorId) {
        this.honorId = honorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public HonorType getType() {
        return type;
    }

    public void setType(HonorType type) {
        this.type = type;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
