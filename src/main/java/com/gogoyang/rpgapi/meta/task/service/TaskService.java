package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.dao.TaskDao;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService implements ITaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * 创建一个task
     *
     * @param task
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTask(Task task) throws Exception {
        taskDao.createTask(task);
        taskDao.createTaskDetail(task);
    }

    /**
     * 保存Task
     * 如果用户发布任务时修改了Job，那同样也会保存对应的Task
     * 为了节约存储开销，降低服务器和网络压力，task 和 common 的detail字段，只保存在taskDetail表里。
     *
     * @param task
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(Task task) throws Exception {
        if (task.getTaskId() == null) {
            throw new Exception("10093");
        }
        //先保存Task
        taskDao.updateTaskTiny(task);
        taskDao.updateTaskDetail(task);
    }

    /**
     * read task from db, not contain task detail
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public Task getTaskTinyByTaskId(String taskId) throws Exception {
        Task task=taskDao.getTaskTiny(taskId);
        return task;
    }

    /**
     * read task from db, contain task detail
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public Task getTaskDetailByTaskId(String taskId) throws Exception {
        Task task = taskDao.getTaskDetail(taskId);
        return task;
    }

    /**
     * 读取用户的所有父task记录。不包括任务详情， 不包括子任务。
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Task> listTaskByUserId(String userId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("createdUserId", userId);
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Task> tasks=taskDao.listTask(qIn);

        return tasks;
    }

    /**
     * 删除task和task detail
     * 真的删除
     *
     * @param taskId
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(String taskId) throws Exception {
        taskDao.deleteTaskTiny(taskId);
        taskDao.deleteTaskDetail(taskId);
    }

    /**
     * 根据父任务taskId查询所有下级子任务
     * @param pid
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Task> listTaskByPid(String pid) throws Exception {
        Map qIn=new HashMap();
        qIn.put("pid", pid);
        ArrayList<Task> tasks=taskDao.listTask(qIn);
        return tasks;
    }

    @Override
    public Integer totalTask(Map qIn) {
        Integer total=taskDao.totalTask(qIn);
        return total;
    }


}
