package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;

import java.util.ArrayList;

public interface ITaskService {
    Task createTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    Task loadTaskByTaskId(Integer taskId) throws Exception;

    ArrayList<Task> loadTaskByUserId(Integer userId) throws Exception;


}
