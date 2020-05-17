package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;

import java.util.ArrayList;

public interface ITaskService {
    void insertTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    Task getTaskTinyByTaskId(String taskId) throws Exception;

    Task getTaskDetailByTaskId(String taskId) throws Exception;

    ArrayList<Task> listTaskByUserId(String userId, Integer pageIndex, Integer pageSize) throws Exception;

    void deleteTask(String taskId) throws Exception;

    ArrayList<Task> listTaskByPid(String pid) throws Exception;
}
