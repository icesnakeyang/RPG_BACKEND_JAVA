package com.gogoyang.rpgapi.business.task;

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

    /**
     * 查询我创建的任务
     * @param in
     * @return
     * @throws Exception
     */
    Map listTaskByUserId(Map in) throws Exception;

    Map listTaskPartyA(Map in) throws Exception;

    Map listTaskPartyB(Map in) throws Exception;

    Map totalSubTask(Map in) throws Exception;
    Map listTaskByPid(Map in) throws Exception;
    Map listTaskBreadcrumb(Map in) throws Exception;

    /**
     * 发布任务，并返回jobId
     * @param in
     * @return
     * @throws Exception
     */
    Map publishNewJob(Map in) throws Exception;
}
