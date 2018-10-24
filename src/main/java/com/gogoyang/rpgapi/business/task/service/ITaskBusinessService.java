package com.gogoyang.rpgapi.business.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ITaskBusinessService {
    Task createTask(Map in) throws Exception;
    void updateTask(Map in) throws Exception;
    void deleteTask(Map in) throws Exception;
    Map getTaskByTaskId(Map in) throws Exception;
    Page<Task> listTaskByUserId(Map in) throws Exception;
}
