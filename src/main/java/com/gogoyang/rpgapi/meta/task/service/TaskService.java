package com.gogoyang.rpgapi.meta.task.service;

import com.gogoyang.rpgapi.meta.task.dao.TaskDao;
import com.gogoyang.rpgapi.meta.task.dao.TaskDetailDao;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.entity.TaskDetail;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

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
     * @param task
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Task createTask(Task task) throws Exception{
        /**
         * 首先创建一个task
         * 然后创建一个task_detail
         */
        task.setTaskId(taskDao.save(task).getTaskId());
        TaskDetail taskDetail=new TaskDetail();
        taskDetail.setTaskId(task.getTaskId());
        taskDetail.setDetail(task.getDetail());
        taskDetailDao.save(taskDetail);
        return task;
    }

    /**
     * 保存Task
     * 如果用户发布任务时修改了Job，那同样也会保存对应的Task
     * 为了节约存储开销，降低服务器和网络压力，task 和 job 的detail字段，只保存在taskDetail表里。
     * @param task
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateTask(Task task) throws Exception{
        //先保存Task
        Task fixTask=taskDao.findByTaskId(task.getTaskId());
        if(!fixTask.getTitle().equals(task.getTitle())){
            fixTask.setTitle(task.getTitle());
        }
        if(!fixTask.getCode().equals(task.getCode())){
            fixTask.setCode(task.getCode());
        }
        if(fixTask.getDays()!=task.getDays()){
            fixTask.setDays(task.getDays());
        }
        taskDao.save(fixTask);

        //再保存TaskDetail
//        TaskDetail newTaskDetail=taskDetailDao.findByTaskId(task.getTaskId());
//        if(!newTaskDetail.getDetail().equals(task.getDetail())){
//            newTaskDetail.setDetail(task.getDetail());
//            taskDetailDao.save(newTaskDetail);
//        }
    }

    @Override
    public Task loadTaskByTaskId(Integer taskId) throws Exception{
        Task task=taskDao.findByTaskId(taskId);
        TaskDetail taskDetail=taskDetailDao.findByTaskId(taskId);
        task.setDetail(taskDetail.getDetail());
        UserInfo userInfo=iUserInfoService.loadUserByUserId(task.getCreatedUserId());
        task.setCreatedUserName(userInfo.getUsername());
        return task;
    }

    /**
     * 读取用户的所有task记录。不包括任务详情。
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Task> loadTaskByUserId(Integer userId) throws Exception{
        ArrayList<Task> tasks=taskDao.findAllByCreatedUserId(userId);
        for(int i=0;i<tasks.size();i++){
            UserInfo userInfo=iUserInfoService.loadUserByUserId(tasks.get(i).getCreatedUserId());
            tasks.get(i).setCreatedUserName(iUserInfoService.getUserName(tasks.get(i).getCreatedUserId()));
        }
        return tasks;
    }


}
