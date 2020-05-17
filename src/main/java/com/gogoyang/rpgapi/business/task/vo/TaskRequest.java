package com.gogoyang.rpgapi.business.task.vo;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String detail;
    private String Code;
    private Integer days;
    private String pid;
    private Integer pageIndex;
    private Integer pageSize;
    private String taskId;
    private Double price;
}
