package com.gogoyang.rpgapi.business.task;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.resource.entity.ResourceFile;
import com.gogoyang.rpgapi.meta.resource.service.IResourceFileService;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import com.gogoyang.rpgapi.meta.team.service.ITeamService;
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
    private final IResourceFileService iResourceFileService;
    private final IRPGFunction irpgFunction;
    private final ITeamService iTeamService;
    private final IUserService iUserService;

    @Autowired
    public TaskBusinessService(ITaskService iTaskService,
                               IJobService iJobService, IAccountService iAccountService,
                               ICommonBusinessService iCommonBusinessService,
                               IResourceFileService iResourceFileService,
                               IRPGFunction irpgFunction,
                               ITeamService iTeamService,
                               IUserService iUserService) {
        this.iTaskService = iTaskService;
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iResourceFileService = iResourceFileService;
        this.irpgFunction = irpgFunction;
        this.iTeamService = iTeamService;
        this.iUserService = iUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTask(Map in) throws Exception {
        String token = in.get("token").toString();
        Map detailMap = (Map) in.get("detailMap");
        String code = (String) in.get("code");
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Task task = new Task();
        task.setTaskId(GogoTools.UUID());
        task.setCreatedUserId(user.getUserId());
        task.setDetail(detailMap.get("detail").toString());
        task.setCreatedTime(new Date());
        task.setCode(code);
        task.setDays(days);
        task.setTitle(title);
        task.setPrice(price);
        iTaskService.insertTask(task);

        /**
         * 保存资源文件
         */
        ArrayList fileList = (ArrayList) detailMap.get("fileList");
        for (int i = 0; i < fileList.size(); i++) {
            String url = ((Map) fileList.get(i)).get("url").toString();
            String fileName = ((Map) fileList.get(i)).get("fileName").toString();
            ResourceFile resourceFile = new ResourceFile();
            resourceFile.setCreateTime(new Date());
            resourceFile.setFileId(GogoTools.UUID());
            resourceFile.setFileName(fileName);
            resourceFile.setTaskId(task.getTaskId());
            resourceFile.setUrl(url);
            iResourceFileService.createResourceFile(resourceFile);
        }
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
        Map detailMap = (Map) in.get("detailMap");
        String code = (String) in.get("code");
        Integer days = (Integer) in.get("days");
        String title = in.get("title").toString();
        Double price = (Double) in.get("price");

        Task task = iTaskService.getTaskDetailByTaskId(taskId);
        task.setCode(code);
        task.setTitle(title);
        if (detailMap != null) {
            task.setDetail(detailMap.get("detail").toString());
        }
        task.setDays(days);
        task.setPrice(price);
        iTaskService.updateTask(task);

        /**
         * 如果有新的删除原来的资源文件，重新保存资源文件
         */

        //读取原来的资源文件
        Map qIn = new HashMap();
        qIn.put("taskId", task.getTaskId());
        ArrayList<ResourceFile> resourceFiles = iResourceFileService.listResourceFile(qIn);

        for (int i = 0; i < resourceFiles.size(); i++) {
            //判断该文件是否还在detail，没有就删除oss服务器上的文件
            String fileName = resourceFiles.get(i).getFileName();
            if (fileName != null) {
                Integer fileIndex = task.getDetail().indexOf(fileName);
                if (fileIndex == -1) {
                    //图片已经没有了，删除
                    irpgFunction.deleteOSSFile(fileName);
                    //删除资源文件库
                    qIn = new HashMap();
                    qIn.put("fileId", resourceFiles.get(i).getFileId());
                    iResourceFileService.deleteResourceFile(qIn);
                }
            }
        }

        //如果有新的图片，就添加资源库
        ArrayList fileList = (ArrayList) detailMap.get("fileList");
        for (int i = 0; i < fileList.size(); i++) {
            String url = ((Map) fileList.get(i)).get("url").toString();
            String fileName = ((Map) fileList.get(i)).get("fileName").toString();
            ResourceFile resourceFile = new ResourceFile();
            resourceFile.setCreateTime(new Date());
            resourceFile.setFileId(GogoTools.UUID());
            resourceFile.setFileName(fileName);
            resourceFile.setTaskId(task.getTaskId());
            resourceFile.setUrl(url);
            iResourceFileService.createResourceFile(resourceFile);
        }
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

        /**
         * 删除原来的资源文件，重新保存资源文件
         */
        //读取原来的资源文件
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        ArrayList<ResourceFile> resourceFiles = iResourceFileService.listResourceFile(qIn);

        for (int i = 0; i < resourceFiles.size(); i++) {
            //删除oss服务器上的文件
            String fileName = resourceFiles.get(i).getFileName();
            if (fileName != null) {
                irpgFunction.deleteOSSFile(fileName);
            }
            //删除资源文件库
            iResourceFileService.deleteResourceFile(qIn);
        }
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

        Map out = iTaskService.listTaskByUserId(user.getUserId(), pageIndex, pageSize);

        return out;
    }

    @Override
    public Map listTaskPartyA(Map in) throws Exception {
        String token = in.get("token").toString();

        Map out = new HashMap();
        return out;

    }

    @Override
    public Map listTaskPartyB(Map in) throws Exception {
        return null;
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
        Map detailMap = (Map) in.get("detailMap");
        Double price = (Double) in.get("price");
        String taskId = in.get("taskId").toString();
        String title = in.get("title").toString();
        String teamId = (String) in.get("teamId");
        String memberId = (String) in.get("memberId");

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
        job.setDetail(detailMap.get("detail").toString());
        /**
         * 如果有指定的团队和成员，直接成交任务
         * 1、检查指定的团队是否存在
         * 2、检查指定的团队成员是否有效
         * 3、设置乙方
         * 4、设置任务为PROGRESS状态
         * 5、增加乙方用户的account
         */
        if (teamId != null) {
            if (memberId == null) {
                //指定了团队就必须指定成员
                throw new Exception("30032");
            }
            Team team = iTeamService.getTeamDetail(teamId);
            if (team == null) {
                throw new Exception("30029");
            }
            UserInfo userB = iUserService.getUserByUserId(memberId);
            if (userB == null) {
                throw new Exception("30030");
            }
            job.setPartyBId(userB.getUserId());
            job.setTeamId(team.getTeamId());
            job.setStatus(JobStatus.PROGRESS.toString());
            job.setContractTime(new Date());
            //把任务金额转给乙方
            Account account = new Account();
            account.setAccountId(GogoTools.UUID());
            account.setAmount(job.getPrice());
            account.setCreatedTime(new Date());
            account.setType(AccountType.APPOINT_JOB.toString());
            account.setUserId(userB.getUserId());
            account.setJobId(job.getJobId());
            iAccountService.createAccount(account);
            //刷新乙方的账户余额
            iCommonBusinessService.sumUserAccount(userB.getUserId());
        }
        /**
         * 保存任务信息
         */
        iJobService.insertJob(job);

        /**
         * 如果有新的删除原来的资源文件，重新保存资源文件
         */

        //读取原来的资源文件
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        ArrayList<ResourceFile> resourceFiles = iResourceFileService.listResourceFile(qIn);

        for (int i = 0; i < resourceFiles.size(); i++) {
            /**
             * 这里读取的是原task detail里的资源文件表
             * 用户发布任务时，并不修改task detail，而是把修改的内容直接保存到新增的job detail里去。
             * 所以，这里有个问题，如果原来的task detail表，保存的图片时url，而此时，应该把此url也复制到job detail的资源表里去。
             * 否则，清理oss时，可能造成误删除。即，删除task时，job的detail里的url指向的oss图片可能被删除掉了。
             * 所以，在删除oss图片时，既要检查task detail，也要检查job detail，因为task detail里的url和job detail的url
             * 可能指向同一张图片，删除task时，不能也把job的图片也删除了。
             */
            String fileName = resourceFiles.get(i).getFileName();
            if (fileName != null) {
                Integer fileIndex = job.getDetail().indexOf(fileName);
                if (fileIndex != -1) {
                    //url存在，把jobId也保存到资源表里
                    qIn = new HashMap();
                    qIn.put("jobId", job.getJobId());
                    qIn.put("fileId", resourceFiles.get(i).getFileId());
                    iResourceFileService.updateResourceFile(qIn);
                }
            }
        }

        //如果有新的图片，就添加资源库
        ArrayList fileList = (ArrayList) detailMap.get("fileList");
        for (int i = 0; i < fileList.size(); i++) {
            String url = ((Map) fileList.get(i)).get("url").toString();
            String fileName = ((Map) fileList.get(i)).get("fileName").toString();
            ResourceFile resourceFile = new ResourceFile();
            resourceFile.setCreateTime(new Date());
            resourceFile.setFileId(GogoTools.UUID());
            resourceFile.setFileName(fileName);
            resourceFile.setJobId(job.getJobId());
            resourceFile.setUrl(url);
            iResourceFileService.createResourceFile(resourceFile);
        }


        //新增account记录，publish任务时在甲方账户扣除对应的任务金额
        Account account = new Account();
        account.setAccountId(GogoTools.UUID());
        account.setUserId(job.getPartyAId());
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.PUBLISH.toString());
        iAccountService.createAccount(account);

        //刷新甲方账户的balance余额
        qIn = new HashMap();
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

            String title = task.getTitle();
            if (task.getJobId() != null) {
                title += " && " + task.getJobTitle();
            }
            if (task.getPartyBId() != null) {
                title += " && " + task.getPartyBName();
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
