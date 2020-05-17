package com.gogoyang.rpgapi.meta.stop.service;

import com.gogoyang.rpgapi.meta.stop.dao.JobStopDao;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StopService implements IJobStopService {
    private final JobStopDao jobStopDao;

    @Autowired
    public StopService(JobStopDao jobStopDao) {
        this.jobStopDao = jobStopDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertJobStop(JobStop jobStop) throws Exception {
        jobStopDao.createJobStop(jobStop);
    }

    @Override
    public ArrayList<JobStop> loadJobStopByJobId(String jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        Integer offset =(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobStop> jobStops=jobStopDao.listJobStop(qIn);
        return jobStops;
    }

    @Override
    public JobStop loadJobStopUnProcess(String jobId) throws Exception {
        JobStop jobStop=jobStopDao.getJobStop(jobId);

        return jobStop;
    }

    @Override
    public ArrayList<JobStop> loadMyUnReadStop(String jobId, String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("unread", true);
        qIn.put("userId", userId);
        ArrayList<JobStop> jobStops = jobStopDao.listJobStop(qIn);
        return jobStops;
    }

    @Override
    public void updateJobStop(JobStop jobStop) throws Exception {
        if(jobStop.getStopId()==null){
            throw new Exception("10072");
        }
        jobStopDao.updateJobStop(jobStop);
    }
}
