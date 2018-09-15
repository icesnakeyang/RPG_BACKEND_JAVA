package com.gogoyang.rpgapi.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TaskDetail {
    @Id
    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "detail")
    private String detail;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
