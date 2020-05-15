package com.gogoyang.rpgapi.business.job.myJob.complete.vo;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import lombok.Data;

@Data
public class JobCompleteRequest {
    private String jobId;
    private String content;
    private LogStatus result;
    private String processRemark;
    private Integer pageIndex;
    private Integer pageSize;
    private String completeId;
}
