package com.gogoyang.rpgapi.job.jobStop.entity;

import com.gogoyang.rpgapi.constant.LogStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
}
