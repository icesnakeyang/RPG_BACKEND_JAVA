package com.gogoyang.rpgapi.meta.apply.entity;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import lombok.Data;

import java.util.Date;

@Data
public class JobApply {
    private Integer ids;
    private String jobApplyId;
    private String jobId;
    private String applyUserId;
    private Date applyTime;
    private String content;
    private String status;
    private String processUserId;
    private Date readTime;
    private Date processTime;
    private String processRemark;
    private Date processReadTime;
}
