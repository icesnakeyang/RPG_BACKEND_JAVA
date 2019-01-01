package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskService {
    void insertTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    Task getTaskTinyByTaskId(Integer taskId) throws Exception;

    Task getTaskDetailByTaskId(Integer taskId) throws Exception;

    Page<Task> listTaskByUserId(Integer userId, Integer pageIndex, Integer pageSize) throws Exception;

    void deleteTask(Integer taskId) throws Exception;

    ArrayList<Task> listTaskByPid(Integer pid) throws Exception;
}
