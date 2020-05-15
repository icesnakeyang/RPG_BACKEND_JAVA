package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.dao.JobDao;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
class JobService implements IJobService {
    private final JobDao jobDao;

    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    /**
     * 创建一个Job
     *
     * @param job
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertJob(Job job) throws Exception {
        //确保jobId为null，否则为修改
        if (job.getJobId() != null) {
            throw new Exception("10032");
        }
        if (job.getTitle() == null) {
            throw new Exception("10032");
        }
        jobDao.createJob(job);

        //保存jobDetail表
        JobDetail jobDetail = new JobDetail();
        jobDetail.setJobId(job.getJobId());
        jobDetail.setDetail(job.getDetail());
        jobDao.createJobDetail(jobDetail);
    }

    /**
     * 读取Job的详细信息，包括jobDetail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobDetailByJobId(String jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        Job job = jobDao.getJob(qIn);
        JobDetail jobDetail = jobDao.getJobDetail(jobId);
        job.setDetail(jobDetail.getDetail());
        return job;
    }

    /**
     * 读取job，不带detail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobTinyByJobId(String jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        Job job = jobDao.getJob(qIn);
        return job;
    }

    /**
     * 根据taskId读取job
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobByTaskId(String taskId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("taskId", taskId);
        Job job = jobDao.getJob(qIn);
        return job;
    }

    /**
     * 根据jobStatus读取jobs
     * read jobs by jobStatus
     * paginate the list
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Job> listJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn = new HashMap();
        qIn.put("status", jobStatus);
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Job> jobs = jobDao.listJob(qIn);
        return jobs;
    }

    /**
     * 修改job
     *
     * @param job
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJob(Job job) throws Exception {
        if (job.getJobId() == null) {
            throw new Exception("10010");
        }
        jobDao.updateJobTiny(job);
        if (job.getDetail() != null) {
            JobDetail jobDetail = new JobDetail();
            jobDetail.setJobId(job.getJobId());
            jobDetail.setDetail(job.getDetail());
            jobDao.updateJobDetail(jobDetail);
        }
    }

    /**
     * 读取所有我是甲方的任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Job> listPartyAJob(String userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        qIn.put("partyAId",userId);
        qIn.put("status", jobStatus);
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }

    /**
     * 读取我是乙方的所有任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Job> listPartyBJob(Integer userId, JobStatus jobStatus,
                                   Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        qIn.put("partyBId",userId);
        qIn.put("status", jobStatus);
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }

    @Override
    public ArrayList<Job> listMyPendingJob(String partyAId,
                                      Integer pageIndex, Integer pageSize) throws Exception {
        ArrayList statusList=new ArrayList();
        statusList.add(JobStatus.PENDING);
        statusList.add(JobStatus.MATCHING);
        Map qIn=new HashMap();
        qIn.put("statusList", statusList);
        qIn.put("partyAId", partyAId);
        Integer offset =(pageIndex-1)*pageSize;
        qIn.put("offset", pageIndex);
        qIn.put("sieze", pageSize);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteJob(String jobId) throws Exception {
        jobDao.deleteJob(jobId);
        jobDao.deleteJobDetail(jobId);
    }

    @Override
    public ArrayList<Job> listPublicJob(Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList statusList=new ArrayList();
        statusList.add(JobStatus.PENDING);
        statusList.add(JobStatus.MATCHING);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }

    @Override
    public ArrayList<Job> listMyPartyAAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("partyAId", userId);
        qIn.put("status", JobStatus.ACCEPTANCE);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }

    @Override
    public ArrayList<Job> listMyPartyBAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("partyBId", userId);
        qIn.put("status", JobStatus.ACCEPTANCE);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        return jobs;
    }
}
