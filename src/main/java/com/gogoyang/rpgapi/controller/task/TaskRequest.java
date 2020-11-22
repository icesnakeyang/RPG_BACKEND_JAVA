package com.gogoyang.rpgapi.controller.task;

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
    private String teamId;
    private String memberId;
}
