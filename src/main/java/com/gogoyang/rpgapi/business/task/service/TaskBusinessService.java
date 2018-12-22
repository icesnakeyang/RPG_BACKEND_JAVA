package com.gogoyang.rpgapi.business.task.service;

import com.gogoyang.rpgapi.business.job.detail.service.IJobDetailBusinessService;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskBusinessService implements ITaskBusinessService {
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;
    private final IJobDetailBusinessService iJobDetailBusinessService;

    @Autowired
    public TaskBusinessService(IUserInfoService iUserInfoService, ITaskService iTaskService, IJobDetailBusinessService iJobDetailBusinessService) {
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iJobDetailBusinessService = iJobDetailBusinessService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Task createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = in.get("detail").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();

        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Task task = new Task();
        task.setCreatedUserId(userInfo.getUserId());
        task.setDetail(detail);
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setDays(days);
        task.setTitle(title);
        task = iTaskService.insertTask(task);
        return task;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Task createSubTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String code = in.get("code").toString();
        Integer pid = (Integer) in.get("pid");
        String title = in.get("title").toString();

        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Task task = new Task();
        task.setCreatedUserId(userInfo.getUserId());
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setPid(pid);
        task.setTitle(title);
        task = iTaskService.insertTask(task);
        return task;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateTask(Map in) throws Exception {
        Integer taskId = (Integer) in.get("taskId");
        String detail = in.get("detail").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();

        Task task = iTaskService.getTaskDetailByTaskId(taskId);
        task.setCode(code);
        task.setTitle(title);
        task.setDetail(detail);
        task.setDays(days);
        iTaskService.updateTask(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteTask(Map in) throws Exception {
        /**
         * check user
         * check sub task
         * the task only can be deleted when there is no sub task
         */
        String token = in.get("token").toString();
        Integer taskId = (Integer) in.get("taskId");
        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        ArrayList<Task> tasks = iTaskService.listTaskByPid(taskId);
        if (tasks.size() > 0) {
            throw new Exception("10097");
        }
        iTaskService.deleteTask(taskId);
    }

    @Override
    public Map getTaskDetailByTaskId(Map in) throws Exception {
        /**
         * 检查token，检查userInfo
         */
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Integer taskId = (Integer) in.get("taskId");
        Task task = iTaskService.getTaskDetailByTaskId(taskId);

        UserInfo createUser = iUserInfoService.getUserByUserId(task.getCreatedUserId());
        if(createUser.getRealName()!=null){
            task.setCreatedUserName(createUser.getRealName());
        }else {
            task.setCreatedUserName(createUser.getUsername());
        }

        /**
         * 增加一个jobId, 如果task已经发布，就返回这个发布的jobId
         */
        Map jobIn = new HashMap();
        jobIn.put("taskId", taskId);

        Map jobOut = iJobDetailBusinessService.getJobTinyByTaskId(in);
        Map out = new HashMap();
        out.put("job", jobOut.get("job"));
        out.put("task", task);
        return out;
    }

    @Override
    public Map getTaskTinyByTaskId(Map in) throws Exception {
        /**
         * 检查token，检查userInfo
         */
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Integer taskId = (Integer) in.get("taskId");
        Task task = iTaskService.getTaskTinyByTaskId(taskId);
        Map out = new HashMap();
        out.put("task", task);
        return out;
    }

    @Override
    public Page<Task> listTaskByUserId(Map in) throws Exception {
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        Page<Task> tasks = iTaskService.listTaskByUserId(userInfo.getUserId(), pageIndex, pageSize);
        for (int i = 0; i < tasks.getContent().size(); i++) {
            tasks.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                    tasks.getContent().get(i).getCreatedUserId()));
        }
        return tasks;
    }

    @Override
    public Map totalSubTask(Map in) throws Exception {
        Integer pid = (Integer) in.get("pid");
        if (pid == null) {
            throw new Exception("10095");
        }
        ArrayList<Task> tasks = iTaskService.listTaskByPid(pid);
        Integer totalSub = tasks.size();
        Map out = new HashMap();
        out.put("totalSub", totalSub);
        return out;
    }

    @Override
    public Map listTaskByPid(Map in) throws Exception {
        Integer pid = (Integer) in.get("pid");
        if (pid == null) {
            throw new Exception("10095");
        }
        ArrayList<Task> tasks = iTaskService.listTaskByPid(pid);
        Map out = new HashMap();
        out.put("task", tasks);
        return out;
    }

    @Override
    public Map listTaskBreadcrumb(Map in) throws Exception {
        Integer taskId = (Integer) in.get("taskId");
        Task task = iTaskService.getTaskTinyByTaskId(taskId);
        ArrayList breadList = new ArrayList();
        while (task.getPid() != null) {
            Map map = new HashMap();
            map.put("taskId", task.getTaskId());
            map.put("title", task.getTitle());
            breadList.add(map);
            task = iTaskService.getTaskTinyByTaskId(task.getPid());
        }
        if (task != null) {
            Map map = new HashMap();
            map.put("taskId", task.getTaskId());
            map.put("title", task.getTitle());
            breadList.add(map);
        }
        ArrayList list = new ArrayList();
        for (int i = breadList.size() - 1; i >= 0; i--) {
            list.add(breadList.get(i));
        }
        Map out = new HashMap();
        out.put("breadList", list);
        return out;
    }
}
