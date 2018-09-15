package com.gogoyang.rpgapi.task.vo;

import com.gogoyang.rpgapi.task.entity.Task;
import com.gogoyang.rpgapi.task.entity.TaskDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    private Integer taskId;
    private String title;
    private String detail;
    private String days;
    private String price;
    private String pid;
    private Integer createdUserId;
    private String code;

    public Task toTask() {
        Task task = new Task();

        task.setTaskId(taskId);
        task.setTitle(title);
        if(days==null){
            days="3";
        }
        task.setDays(Integer.parseInt(days));
        if(pid==null){
            pid="0";
        }
        task.setPid(Integer.parseInt(pid));
        task.setCode(code);

        task.setCreatedTime(new Date());

        task.setCreatedUserId(createdUserId);

        TaskDetail detail=new TaskDetail();

        return task;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }
}
