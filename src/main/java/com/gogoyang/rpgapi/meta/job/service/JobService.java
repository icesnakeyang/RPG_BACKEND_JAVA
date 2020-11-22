package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.dao.JobDao;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void insertJob(Job job) throws Exception {
        jobDao.createJob(job);

        //保存jobDetail表
        jobDao.createJobDetail(job);
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
        Job jobDetail = jobDao.getJobDetail(jobId);
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
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Job job) throws Exception {
        jobDao.updateJobTiny(job);
        if (job.getDetail() != null) {
            jobDao.updateJobDetail(job);
        }
    }

    /**
     * 读取所有我是甲方的任务
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Map listPartyAJob(Map qIn) throws Exception {
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        Integer totalJobs=jobDao.totalJob(qIn);

        Map out=new HashMap();
        out.put("totalJobs", totalJobs);
        out.put("jobs", jobs);
        return out;
    }

    /**
     * 读取我是乙方的所有任务
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    @Override
    public Map listPartyBJob(Map qIn) throws Exception {

        ArrayList<Job> jobs=jobDao.listJob(qIn);
        Integer totalJobs=jobDao.totalJob(qIn);

        Map out=new HashMap();
        out.put("jobs", jobs);
        out.put("totalJobs", totalJobs);
        return out;
    }

    @Override
    public Map listMyPendingJob(String partyAId,
                                      Integer pageIndex, Integer pageSize) throws Exception {
        ArrayList statusList=new ArrayList();
        statusList.add(JobStatus.PENDING);
        statusList.add(JobStatus.MATCHING);
        Map qIn=new HashMap();
        qIn.put("statusList", statusList);
        qIn.put("partyAId", partyAId);
        Integer offset =(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Job> jobs=jobDao.listJob(qIn);

        Integer totalJob=jobDao.totalJob(qIn);

        Map out=new HashMap();
        out.put("jobs", jobs);
        out.put("totalJob", totalJob);
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(String jobId) throws Exception {
        jobDao.deleteJob(jobId);
        jobDao.deleteJobDetail(jobId);
    }

    @Override
    public Map listPublicJob(Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList statusList=new ArrayList();
        statusList.add(JobStatus.PENDING);
        statusList.add(JobStatus.MATCHING);
        qIn.put("statusList", statusList);
        ArrayList<Job> jobs=jobDao.listJob(qIn);

        Integer total= jobDao.totalJob(qIn);

        Map out=new HashMap();
        out.put("jobs", jobs);
        out.put("totalJobs", total);
        Integer pages=total/pageSize;
        if(total%pageSize>0){
            pages++;
        }
        out.put("totalJobs", total);
        out.put("totalJobPages", pages);
        return out;
    }

    @Override
    public Map listMyPartyAAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("partyAId", userId);
        qIn.put("status", JobStatus.ACCEPTANCE);
        ArrayList<Job> jobs=jobDao.listJob(qIn);
        Integer total=jobDao.totalJob(qIn);
        Map out=new HashMap();
        out.put("totalJobs", total);
        out.put("jobList", jobs);
        return out;
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
