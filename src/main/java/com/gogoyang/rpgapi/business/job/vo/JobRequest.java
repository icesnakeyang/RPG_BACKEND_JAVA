package com.gogoyang.rpgapi.business.job.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class JobRequest {
    private String code;
    private String taskId;
    private String title;
    private Integer days;
    private Double price;
    private String jobDetail;
    private String jobMatchId;
    private String userId;
    private String jobId;
    private Integer pageIndex;
    private Integer pageSize;
    private String remark;
    private String content;
    private String jobLogId;
    private ArrayList statusList;
}
