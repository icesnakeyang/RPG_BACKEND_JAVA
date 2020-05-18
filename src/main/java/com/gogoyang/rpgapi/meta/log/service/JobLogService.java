package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.dao.JobLogDao;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobLogService implements IJobLogService {
    private final JobLogDao jobLogDao;

    public JobLogService(JobLogDao jobLogDao) {
        this.jobLogDao = jobLogDao;
    }

    /**
     * 创建一个任务日志
     *
     * @param jobLog
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJobLog(JobLog jobLog) throws Exception {
        jobLogDao.createJobLog(jobLog);
    }

    @Override
    public JobLog getJobLog(String jobLogId) throws Exception {
        JobLog jobLog = jobLogDao.getJobLog(jobLogId);
        return jobLog;
    }

    /**
     * 批量查询任务日志
     *
     * @param qIn jobId
     *            createdUserId
     *            unread
     *            partyAId
     *            partyBId
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<JobLog> listJobLog(Map qIn) throws Exception {
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);
        return jobLogs;
    }

    /**
     * 读取一个任务的所有日志
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobLog> loadJobLogByJobId(String jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);

        return jobLogs;
    }

    /**
     * 读取当前用户为甲方时所有未阅读的日志
     */
    @Override
    public ArrayList<JobLog> listPartyAUnreadJobLog(String userId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("userId", userId);
        qIn.put("partyAId", userId);
        qIn.put("unread", true);
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);
        return jobLogs;
    }

    /**
     * 读取指定任务的甲方未阅读的日志
     *
     * @param userId
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobLog> listPartyAUnreadJobLogJobId(String userId, String jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("unread", true);
        qIn.put("userId", userId);
        qIn.put("partyAId", userId);
        qIn.put("jobId", jobId);
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);
        return jobLogs;
    }

    /**
     * 读取当前用户为乙方时所有未阅读的日志
     */
    @Override
    public ArrayList<JobLog> listPartyBUnreadJobLog(String userId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("unread", true);
        qIn.put("userId", userId);
        qIn.put("partyBId", userId);
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);
        return jobLogs;
    }

    /**
     * 读取一个任务里所有乙方未阅读的日志
     *
     * @param userId
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobLog> listPartyBUnreadJobLogJobId(String userId, String jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("unread", true);
        qIn.put("userId", userId);
        qIn.put("partyBId", userId);
        qIn.put("jobId", jobId);
        ArrayList<JobLog> jobLogs = jobLogDao.listJobLog(qIn);
        return jobLogs;
    }

    /**
     * 统计所有未阅读的任务日志
     *
     * @param qIn jobId:选填，指定任务
     *            userId：必填，查询readTime为null时，createdUserId不是userId
     *            partyAId：选填，查询甲方未读
     *            partyBId：选填，查询乙方未读
     * @return
     * @throws Exception
     */
    @Override
    public Integer totalUnreadLog(Map qIn) throws Exception {
        Integer totalUnreadLog = jobLogDao.totalUnreadLog(qIn);
        return totalUnreadLog;
    }

    /**
     * 设置阅读时间
     *
     * @param qIn readTime：必填
     *            userId：必填
     *            jobId：必填
     * @throws Exception
     */
    @Override
    public void setJobLogReadTime(Map qIn) throws Exception {
        jobLogDao.setJobLogReadTime(qIn);
    }

    /**
     * 统计任务日志总数
     * @param qIn
     * jobId
     * @return
     */
    @Override
    public Integer totalJobLog(Map qIn) {
        Integer total = jobLogDao.totalJobLog(qIn);
        return total;
    }
}
