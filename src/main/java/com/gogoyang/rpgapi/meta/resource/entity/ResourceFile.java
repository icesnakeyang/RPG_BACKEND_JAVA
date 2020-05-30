package com.gogoyang.rpgapi.meta.resource.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ResourceFile {
    private Integer ids;
    private String fileId;
    private String url;
    private String fileName;
    private Date createTime;
    private String taskId;
    private String jobId;
    private String usageId;
}
