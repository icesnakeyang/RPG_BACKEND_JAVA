package com.gogoyang.rpgapi.job.job.vo;

import com.gogoyang.rpgapi.job.job.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private String code;
    private Integer taskId;
    private String title;
    private Integer days;
    private Double reward;
    private Integer createdUserId;
    private String category;
    private String jobDetail;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Job toJob(){
        Job job=new Job();
        job.setCategory(category);
        job.setCode(code);
        job.setCreatedTime(new Date());
        job.setCreatedUserId(createdUserId);
        job.setDays(days);
        job.setDetail(jobDetail);
        job.setTaskId(taskId);
        job.setReward(reward);
        job.setTitle(title);

        return job;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(String jobDetail) {
        this.jobDetail = jobDetail;
    }
}
