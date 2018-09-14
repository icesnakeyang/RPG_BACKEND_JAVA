package com.gogoyang.rpgapi.task.vo;

import com.gogoyang.rpgapi.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String detail;
    private String days;
    private String price;
    private String pid;
    private Integer createdUserId;

    public Task toTask() {
        Task task = new Task();

        task.setTitle(title);
        task.setDetail(detail);
        if(days==null){
            days="3";
        }
        task.setDays(Integer.parseInt(days));
        if(pid==null){
            pid="0";
        }
        task.setPid(Integer.parseInt(pid));

        task.setCreatedTime(new Date());

        task.setCreatedUserId(createdUserId);

        return task;
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
