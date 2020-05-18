package com.gogoyang.rpgapi.business.job.myJob.log.vo;

import lombok.Data;

@Data
public class LogRequest {
    private String jobId;
    private String content;
    private Integer pageIndex;
    private Integer pageSize;
}
