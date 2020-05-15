package com.gogoyang.rpgapi.meta.honor.entity;

import com.gogoyang.rpgapi.framework.constant.HonorType;
import lombok.Data;

import java.util.Date;

@Data
public class Honor {
    private Integer ids;
    private String honorId;
    private String userId;
    private Integer point;
    private HonorType type;
    private String jobId;
    private Date createdTime;
    private String createdUserId;
    private String remark;

    private String jobTitle;
    private String userName;
}
