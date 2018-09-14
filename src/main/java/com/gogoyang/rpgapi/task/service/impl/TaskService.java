package com.gogoyang.rpgapi.task.service.impl;

import com.gogoyang.rpgapi.task.dao.TaskDao;
import com.gogoyang.rpgapi.task.entity.Task;
import com.gogoyang.rpgapi.task.service.ITaskService;
import com.gogoyang.rpgapi.task.vo.CreateTaskRequest;
import com.gogoyang.rpgapi.task.vo.CreateTaskResponse;
import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    private final UserDao userDao;
    private final TaskDao taskDao;

    @Autowired
    public TaskService(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public Response createTask(CreateTaskRequest request) {
        Response response=new Response();
        CreateTaskResponse createTaskResponse=new CreateTaskResponse();
        createTaskResponse.setId(taskDao.save(request.toTask()).getTaskId());
        response.setData(createTaskResponse);
        return response;
    }

    @Override
    public Response buildTaskInfoById(Integer id) {
        Response response=new Response();
        Task task=taskDao.findByTaskId(id);
        User user=userDao.findByUserId(task.getCreatedUserId());
        task.setCreatedUserName(user.getUsername());
        response.setData(task);
        return response;
    }

    @Override
    public Response buildTasks(Integer id) {
        Response response=new Response();
        List tasks=taskDao.findAllByCreatedUserId(id);
        response.setData(tasks);
        return response;
    }
}
