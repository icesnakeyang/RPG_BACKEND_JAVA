package com.gogoyang.rpgapi.business.task.service;

import com.gogoyang.rpgapi.meta.task.entity.Task;

import java.util.ArrayList;
import java.util.Map;

public interface ITaskBusinessService {
    void createTask(Map in) throws Exception;
    void createSubTask(Map in) throws Exception;
    void updateTask(Map in) throws Exception;
    void deleteTask(Map in) throws Exception;
    Map getTaskDetailByTaskId(Map in) throws Exception;
    Map getTaskTinyByTaskId(Map in) throws Exception;
    Map listTaskByUserId(Map in) throws Exception;
    Map totalSubTask(Map in) throws Exception;
    Map listTaskByPid(Map in) throws Exception;
    Map listTaskBreadcrumb(Map in) throws Exception;
    void publishNewJob(Map in) throws Exception;
}
