package com.gogoyang.rpgapi.job.vo;

import com.gogoyang.rpgapi.job.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {
    private String code;
    private Integer taskId;
    private String title;
    private Integer days;
    private Double reward;
    private Integer createdUserId;

    public Job toJob(){
        Job job=new Job();
        job.setCode(code);
        job.setTaskId(taskId);
        job.setTitle(title);
        job.setDays(days);
        job.setReward(reward);
        job.setCreatedUserId(createdUserId);
        job.setCreatedTime(new Date());
        job.setCategory("RPG");

        return job;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }
}
