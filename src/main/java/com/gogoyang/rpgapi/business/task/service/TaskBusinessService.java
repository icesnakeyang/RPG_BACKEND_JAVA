package com.gogoyang.rpgapi.business.task.service;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
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
    private final ITaskService iTaskService;
    private final IUserService iUserService;
    private final IMyJobCommonBusinessService iMyJobCommonBusinessService;
    private final IJobService iJobService;
    private final IAccountService iAccountService;

    @Autowired
    public TaskBusinessService(ITaskService iTaskService,
                               IUserService iUserService,
                               IMyJobCommonBusinessService iMyJobCommonBusinessService,
                               IJobService iJobService, IAccountService iAccountService) {
        this.iTaskService = iTaskService;
        this.iUserService = iUserService;
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = in.get("detail").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        Task task = new Task();
        task.setCreatedUserId(user.getUserId());
        task.setDetail(detail);
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setDays(days);
        task.setTitle(title);
        task.setPrice(price);
        iTaskService.insertTask(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createSubTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String code = in.get("code").toString();
        Integer pid = (Integer) in.get("pid");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        Task task = new Task();
        task.setCreatedUserId(user.getUserId());
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setPid(pid);
        task.setTitle(title);
        task.setPrice(price);
        iTaskService.insertTask(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateTask(Map in) throws Exception {
        Integer taskId = (Integer) in.get("taskId");
        String detail = in.get("detail").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        Task task = iTaskService.getTaskDetailByTaskId(taskId);
        task.setCode(code);
        task.setTitle(title);
        task.setDetail(detail);
        task.setDays(days);
        task.setPrice(price);
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
        User user = iUserService.getUserByToken(token);
        if (user == null) {
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
        User userInfo = iUserService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        Integer taskId = (Integer) in.get("taskId");
        Task task = iTaskService.getTaskDetailByTaskId(taskId);

        User createUser = iUserService.getUserByUserId(task.getCreatedUserId());
        if (createUser.getRealName() != null) {
            task.setCreatedUserName(createUser.getRealName());
        } else {
            task.setCreatedUserName(createUser.getEmail());
        }

        /**
         * 增加一个jobId, 如果task已经发布，就返回这个发布的jobId
         */
        Map jobIn = new HashMap();
        jobIn.put("taskId", taskId);

        Map jobOut = iMyJobCommonBusinessService.getJobTinyByTaskId(in);
        Map out = new HashMap();
        out.put("job", jobOut.get("common"));
        out.put("task", task);
        return out;
    }

    @Override
    public Map getTaskTinyByTaskId(Map in) throws Exception {
        /**
         * 检查token，检查userInfo
         */
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
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
        User user = iUserService.getUserByToken(token);
        Page<Task> tasks = iTaskService.listTaskByUserId(user.getUserId(), pageIndex, pageSize);
        for (int i = 0; i < tasks.getContent().size(); i++) {
            User createdUser = iUserService.getUserByUserId(tasks.getContent().get(i).getCreatedUserId());
            if (createdUser.getRealName() != null) {
                tasks.getContent().get(i).setCreatedUserName(createdUser.getRealName());
            } else {
                tasks.getContent().get(i).setCreatedUserName(createdUser.getEmail());
            }
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void publishNewJob(Map in) throws Exception {
        /**
         *  1、根据token读取用户，检查用户
         *  2、根据taskId读取job，检查job是否已经发布
         *  3、创建job
         *  4、创建甲方账户，更新甲方账户统计信息
         */
        String token = in.get("token").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        String detail = in.get("detail").toString();
        Double price = (Double) in.get("price");
        Integer taskId = (Integer) in.get("taskId");
        String title = in.get("title").toString();

        //读取当前用户
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            //当前用户不存在
            throw new Exception("10004");
        }

        //根据taskId读取job
        Job job = iJobService.getJobByTaskId(taskId);
        if (job != null) {
            //该task已经发布过了，且，发布的任务正在进行中
            if (job.getStatus() == JobStatus.PROGRESS) {
                throw new Exception("10092");
            }
            //该Task已经发布过了在，且，目前该任务正在匹配中
            if (job.getStatus() == JobStatus.MATCHING) {
                throw new Exception("10092");
            }
            //该Task已经发布过了，且，正在等待匹配中
            if (job.getStatus() == JobStatus.PENDING) {
                throw new Exception("10092");
            }
        }

        //创建Job
        job = new Job();
        job.setPartyAId(user.getUserId());
        job.setCreatedTime(new Date());
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setStatus(JobStatus.PENDING);
        job.setTaskId(taskId);
        job.setTitle(title);
        job.setDetail(detail);
        job = iJobService.insertJob(job);

        //新增account记录，publish任务时在甲方账户扣除对应的任务金额
        Account account = new Account();
        account.setUserId(job.getPartyAId());
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.PUBLISH);
        iAccountService.insertNewAccount(account);

        //计算甲方账户的balance余额
        Map accountMap = iAccountService.loadAccountBalance(job.getPartyAId());
        Double balance = (Double) accountMap.get("balance");
        Double income = (Double) accountMap.get("income");
        Double outgoing = (Double) accountMap.get("outgoing");

        //更新甲方的账户统计信息
        user.setAccount(balance);
        user.setAccountIn(income);
        user.setAccountOut(outgoing);
        iUserService.update(user);
    }
}
