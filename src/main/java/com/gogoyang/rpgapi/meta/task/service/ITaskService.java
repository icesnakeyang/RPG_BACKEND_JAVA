package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.data.domain.Page;

public interface ITaskService {
    Task insertTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    Task getTaskByTaskId(Integer taskId) throws Exception;

    Page<Task> listTaskByUserId(Integer userId, Integer pageIndex, Integer pageSize) throws Exception;

    void deleteTask(Integer taskId) throws Exception;


}
