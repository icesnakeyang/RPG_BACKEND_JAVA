package com.gogoyang.rpgapi.task.service;

import com.gogoyang.rpgapi.task.vo.CreateTaskRequest;
import com.gogoyang.rpgapi.vo.Response;

public interface ITaskService {
    Response createTask(CreateTaskRequest request);

    Response buildTaskInfoById(Integer id);

    Response buildTasks(Integer id);
}
