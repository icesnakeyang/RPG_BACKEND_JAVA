package com.gogoyang.rpgapi.business.task;

import com.gogoyang.rpgapi.business.job.common.IJobCommonBusinessService;
import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskBusinessService implements ITaskBusinessService {
    private final ITaskService iTaskService;
    private final IJobService iJobService;
    private final IAccountService iAccountService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IJobCommonBusinessService iJobCommonBusinessService;

    @Autowired
    public TaskBusinessService(ITaskService iTaskService,
                               IJobService iJobService, IAccountService iAccountService,
                               ICommonBusinessService iCommonBusinessService,
                               IJobCommonBusinessService iJobCommonBusinessService) {
        this.iTaskService = iTaskService;
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iJobCommonBusinessService = iJobCommonBusinessService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String detail = (String) in.get("detail");
        String code = (String) in.get("code");
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Task task = new Task();
        task.setTaskId(GogoTools.UUID());
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
    @Transactional(rollbackFor = Exception.class)
    public void createSubTask(Map in) throws Exception {
        String token = in.get("token").toString();
        String code = in.get("code").toString();
        String pid = in.get("pid").toString();
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        /**
         * 读取父任务
         */
        Task pTask = iTaskService.getTaskTinyByTaskId(pid);
        if (pTask == null) {
            throw new Exception("30006");
        }

        Task task = new Task();
        task.setTaskId(GogoTools.UUID());
        task.setCreatedUserId(user.getUserId());
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setPid(pid);
        task.setTitle(title);
        task.setPrice(price);
        iTaskService.insertTask(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(Map in) throws Exception {
        String taskId = in.get("taskId").toString();
        String detail = (String) in.get("detail");
        String code = (String) in.get("code");
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
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Map in) throws Exception {
        /**
         * check user
         * check sub task
         * the task only can be deleted when there is no sub task
         */
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //检查是否有子任务
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
        String taskId = in.get("taskId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);
        Task task = iTaskService.getTaskDetailByTaskId(taskId);

        /**
         * 增加一个jobId, 如果task已经发布，就返回这个发布的jobId
         */
        Map jobIn = new HashMap();
        jobIn.put("taskId", taskId);

//        Map jobOut = iJobCommonBusinessService.getJobTinyByJobId(in);
        Job job = iJobService.getJobByTaskId(taskId);

        Map out = new HashMap();

        if (job != null) {
            out.put("job", job);
        }

        out.put("task", task);
        return out;
    }

    @Override
    public Map getTaskTinyByTaskId(Map in) throws Exception {
        /**
         * 检查token，检查userInfo
         */
        String token = in.get("token").toString();
        String taskId = in.get("taskId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Task task = iTaskService.getTaskTinyByTaskId(taskId);
        Map out = new HashMap();
        out.put("task", task);
        return out;
    }

    @Override
    public Map listTaskByUserId(Map in) throws Exception {
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        String token = in.get("token").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Task> tasks = iTaskService.listTaskByUserId(user.getUserId(), pageIndex, pageSize);

        Map out = new HashMap();
        out.put("tasks", tasks);

        return out;
    }

    @Override
    public Map totalSubTask(Map in) throws Exception {
        String pid = in.get("pid").toString();
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
        String token = in.get("token").toString();
        String pid = in.get("pid").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        ArrayList list = loadTaskTree(pid);

        Map out = new HashMap();
        out.put("tasks", list);

        return out;
    }

    @Override
    public Map listTaskBreadcrumb(Map in) throws Exception {
        String taskId = in.get("taskId").toString();

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
    @Transactional(rollbackFor = Exception.class)
    public Map publishNewJob(Map in) throws Exception {
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
        String taskId = in.get("taskId").toString();
        String title = in.get("title").toString();

        //读取当前用户
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //根据taskId读取job
        Job job = iJobService.getJobByTaskId(taskId);
        if (job != null) {
            //该task已经发布过了，且，发布的任务正在进行中
            if (job.getStatus().equals(JobStatus.PROGRESS.toString())) {
                throw new Exception("10092");
            }
            //该Task已经发布过了在，且，目前该任务正在匹配中
            if (job.getStatus().equals(JobStatus.MATCHING.toString())) {
                throw new Exception("10092");
            }
            //该Task已经发布过了，且，正在等待匹配中
            if (job.getStatus().equals(JobStatus.PENDING.toString())) {
                throw new Exception("10092");
            }
        }

        //创建Job
        job = new Job();
        job.setJobId(GogoTools.UUID());
        job.setPartyAId(user.getUserId());
        job.setCreatedTime(new Date());
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setStatus(JobStatus.PENDING.toString());
        job.setTaskId(taskId);
        job.setTitle(title);
        job.setDetail(detail);
        iJobService.insertJob(job);

        //新增account记录，publish任务时在甲方账户扣除对应的任务金额
        Account account = new Account();
        account.setAccountId(GogoTools.UUID());
        account.setUserId(job.getPartyAId());
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.PUBLISH.toString());
        iAccountService.createAccount(account);

        //刷新甲方账户的balance余额
        Map qIn = new HashMap();
        qIn.put("userId", job.getPartyAId());
        iCommonBusinessService.sumUserAccount(job.getPartyAId());

        Map out = new HashMap();
        out.put("jobId", job.getJobId());

        return out;
    }

    private ArrayList loadTaskTree(String taskId) throws Exception {
        //读取子任务
        ArrayList<Task> subTaskList = iTaskService.listTaskByPid(taskId);
        ArrayList list = new ArrayList();
        for (int i = 0; i < subTaskList.size(); i++) {
            Map map = new HashMap();
            Task task = subTaskList.get(i);
            map.put("taskId", task.getTaskId());

            String title=task.getTitle();
            if(task.getJobId()!=null){
                title+=" && "+task.getJobTitle();
            }
            if(task.getPartyBId()!=null){
                title+=" && "+task.getPartyBName();
            }

            map.put("title", title);
            if (task.getJobId() != null) {
                map.put("publish", true);
            } else {
                map.put("publish", false);
            }
            ArrayList<Task> childTasks = loadTaskTree(task.getTaskId());
            if (childTasks.size() > 0) {
                map.put("expand", true);
                map.put("children", childTasks);
            }
            list.add(map);
        }
        return list;
    }
}
