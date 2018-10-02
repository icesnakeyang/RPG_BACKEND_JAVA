package com.gogoyang.rpgapi.task.service;

import com.gogoyang.rpgapi.task.entity.Task;

import java.util.ArrayList;

public interface ITaskService {
    Task createTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    Task loadTaskByTaskId(Integer taskId) throws Exception;

    ArrayList<Task> loadTaskByUserId(Integer userId) throws Exception;


}
