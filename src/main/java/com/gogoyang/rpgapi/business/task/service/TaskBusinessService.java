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
        Integer pid = (Integer) in.get("pid");
        String title = in.get("title").toString();

        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Task task = new Task();
        task.setCreatedUserId(userInfo.getUserId());
        task.setDetail(detail);
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setDays(days);
        task.setPid(pid);
        task.setTitle(title);
        task = iTaskService.insertTask(task);
        return task;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateTask(Map in) throws Exception {
        Integer taskId = (Integer)in.get("taskId");
        String detail = in.get("detail").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();

        Task task=iTaskService.getTaskByTaskId(taskId);
        task.setCode(code);
        task.setTitle(title);
        task.setDetail(detail);
        task.setDays(days);
        iTaskService.updateTask(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteTask(Map in) throws Exception {
        Integer taskId=(Integer)in.get("taskId");
        iTaskService.deleteTask(taskId);
    }

    @Override
    public Map getTaskByTaskId(Map in) throws Exception {
        Integer taskId=(Integer)in.get("taskId");
        Task task=iTaskService.getTaskByTaskId(taskId);

        UserInfo userInfo=iUserInfoService.loadUserByUserId(task.getCreatedUserId());
        task.setCreatedUserName(userInfo.getUsername());

        /**
         * 增加一个jobId, 如果task已经发布，就返回这个发布的jobId
         */
        Map jobIn=new HashMap();
        jobIn.put("taskId", taskId);

        Map jobOut=iJobDetailBusinessService.getJobTinyByTaskId(in);
        Map out=new HashMap();
        out.put("job", jobOut.get("job"));
        out.put("task", task);
        return out;
    }

    @Override
    public Page<Task> listTaskByUserId(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Page<Task> tasks=iTaskService.listTaskByUserId(userInfo.getUserId(),pageIndex, pageSize);
        for(int i=0;i<tasks.getContent().size();i++){
            tasks.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                    tasks.getContent().get(i).getCreatedUserId()));
        }
        return tasks;
    }
}
