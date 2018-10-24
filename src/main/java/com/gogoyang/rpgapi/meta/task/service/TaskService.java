package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.dao.TaskDao;
import com.gogoyang.rpgapi.meta.task.dao.TaskDetailDao;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.entity.TaskDetail;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TaskService implements ITaskService {
    private final TaskDao taskDao;
    private final TaskDetailDao taskDetailDao;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public TaskService(TaskDao taskDao, TaskDetailDao taskDetailDao, IUserInfoService iUserInfoService) {
        this.taskDao = taskDao;
        this.taskDetailDao = taskDetailDao;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 创建一个task
     *
     * @param task
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Task insertTask(Task task) throws Exception {
        /**
         * 首先创建一个task
         * 然后创建一个task_detail
         */
        if (task.getTaskId() != null) {
            throw new Exception("10030");
        }
        task = (taskDao.save(task));
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(task.getTaskId());
        taskDetail.setDetail(task.getDetail());
        taskDetailDao.save(taskDetail);
        return task;
    }

    /**
     * 保存Task
     * 如果用户发布任务时修改了Job，那同样也会保存对应的Task
     * 为了节约存储开销，降低服务器和网络压力，task 和 job 的detail字段，只保存在taskDetail表里。
     *
     * @param task
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateTask(Task task) throws Exception {
        if (task.getTaskId() == null) {
            throw new Exception("10093");
        }
        //先保存Task
        taskDao.save(task);

        //再保存TaskDetail
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(task.getTaskId());
        taskDetail.setDetail(task.getDetail());
        taskDetailDao.save(taskDetail);
    }

    @Override
    public Task getTaskByTaskId(Integer taskId) throws Exception {
        Task task = taskDao.findByTaskId(taskId);
        TaskDetail taskDetail = taskDetailDao.findByTaskId(taskId);
        task.setDetail(taskDetail.getDetail());
        return task;
    }

    /**
     * 读取用户的所有task记录。不包括任务详情。
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Page<Task> listTaskByUserId(Integer userId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "taskId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Task> tasks = taskDao.findAllByCreatedUserId(userId, pageable);
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
    @Transactional(rollbackOn = Exception.class)
    public void deleteTask(Integer taskId) throws Exception {
        taskDao.delete(taskId);
        taskDetailDao.deleteByTaskId(taskId);
    }


}
