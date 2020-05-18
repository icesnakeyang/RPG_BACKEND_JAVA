package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.dao.JobCompleteDao;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobCompleteService implements IJobCompleteService {
    private final JobCompleteDao jobCompleteDao;

    @Autowired
    public JobCompleteService(JobCompleteDao jobCompleteDao) {
        this.jobCompleteDao = jobCompleteDao;
    }

    /**
     * 创建验收任务
     *
     * @param jobComplete
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertJobComplete(JobComplete jobComplete) throws Exception {
        jobCompleteDao.createJobComplete(jobComplete);
    }

    @Override
    public ArrayList<JobComplete> listJobComplete(Map qIn) throws Exception {
        ArrayList<JobComplete> jobCompletes=jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    /**
     * 读取一个任务的所有验收任务
     *
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobComplete> loadJobCompleteByJobId(String jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobComplete> jobCompletes=jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    /**
     * 读取甲方未阅读的验收日志
     */
    @Override
    public ArrayList<JobComplete> listPartyAUnread(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("processUserUnread", true);
        qIn.put("processUserId", userId);
        ArrayList<JobComplete> jobCompletes = jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    /**
     * 甲方（process user）未阅读的某个任务的未读完成日志
     * read the jobid unread complete log by party a
     */
    @Override
    public ArrayList<JobComplete> listPartyAUnreadJobId(String userId, String jobId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("processUserUnread", true);
        qIn.put("processUserId", userId);
        qIn.put("jobId", jobId);
        ArrayList<JobComplete> jobCompletes = jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    /**
     * 读取乙方未阅读的，甲方已处理的验收日志
     */
    @Override
    public ArrayList<JobComplete> listPartyBUnread(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("createUserUnread", true);
        qIn.put("createdUserId", userId);
        ArrayList<JobComplete> jobCompletes = jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    @Override
    public ArrayList<JobComplete> listPartyBUnreadAccept(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("createUserUnread",true);
        qIn.put("createdUserId", userId);
        ArrayList<JobComplete> jobCompletes=jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    /**
     * read jobId unread processed complete by party b
     */
    @Override
    public ArrayList<JobComplete> listPartyBUnreadJobId(String userId, String jobId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("createUserUnread", true);
        qIn.put("createdUserId", userId);
        qIn.put("jobId", jobId);
        ArrayList<JobComplete> jobCompletes = jobCompleteDao.listJobComplete(qIn);
        return jobCompletes;
    }

    @Override
    public void updateJobComplete(JobComplete jobComplete) throws Exception {
        jobCompleteDao.updateJobComplete(jobComplete);
    }

    /**
     * 查询一个任务的未处理完成日志
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public JobComplete getUnprocessComplete(String jobId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING);
        ArrayList<JobComplete> completes = jobCompleteDao.listJobComplete(qIn);
        JobComplete jobComplete=null;
        if(completes.size()==1){
            jobComplete=completes.get(0);
        }
        return jobComplete;
    }

    @Override
    public JobComplete getCompleteByStatus(String jobId, LogStatus logStatus) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING);
        JobComplete jobComplete=jobCompleteDao.getJobComplete(qIn);
        return jobComplete;
    }

    /**
     * 统计所有未阅读的任务完成日志
     * @param qIn
     * userId
     * jobId
     * @return
     * @throws Exception
     */
    @Override
    public Integer totalUnreadComplete(Map qIn) throws Exception {
        Integer totalUnreadComplete = jobCompleteDao.totalUnreadComplete(qIn);
        return totalUnreadComplete;
    }

    @Override
    public void setJobCompleteReadTime(Map qIn) throws Exception {
        jobCompleteDao.setJobCompleteReadTime(qIn);
    }

    /**
     * 设置甲方对处理结果的阅读时间
     * @param qIn
     * jobId
     * processReadTime
     * userId
     * @throws Exception
     */
    @Override
    public void setJobCompleteProcessReadTime(Map qIn) throws Exception {
        jobCompleteDao.setJobCompleteProcessReadTime(qIn);
    }

    /**
     * 统计任务的完成验收日志数量
     * @param qIn
     * jobId
     * @return
     */
    @Override
    public Integer totalJobComplete(Map qIn) {
        Integer total=jobCompleteDao.totalJobComplete(qIn);
        return total;
    }
}
