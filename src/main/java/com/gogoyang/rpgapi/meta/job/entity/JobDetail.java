package com.gogoyang.rpgapi.meta.job.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class JobDetail{
    private Integer ids;
    private String jobId;
    private String detail;
}
