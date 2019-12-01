package com.gogoyang.rpgapi.business.job.publicJob.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicJobRequest {
    private String code;
    private Integer days;
    private String detail;
    private Double price;
    private Integer taskId;
    private String title;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer jobId;
}
