package com.gogoyang.rpgapi.meta.stop.service;

import com.gogoyang.rpgapi.meta.stop.dao.JobStopDao;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class StopService implements IJobStopService {
    private final JobStopDao jobStopDao;

    @Autowired
    public StopService(JobStopDao jobStopDao) {
        this.jobStopDao = jobStopDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertJobStop(JobStop jobStop) throws Exception {
        if (jobStop.getStopId() != null) {
            throw new Exception("10064");
        }
        jobStopDao.save(jobStop);
    }

    @Override
    public Page<JobStop> loadJobStopByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "stopId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<JobStop> jobStops = jobStopDao.findAllByJobId(jobId, pageable);
        return jobStops;
    }

    @Override
    public JobStop loadJobStopUnProcess(Integer jobId) throws Exception {
        JobStop jobStop=jobStopDao.findByJobIdAndResultIsNull(jobId);
        return jobStop;
    }

    @Override
    public ArrayList<JobStop> loadMyUnReadStop(Integer jobId, Integer userId) throws Exception {
        ArrayList<JobStop> jobStops = jobStopDao.findAllByJobIdAndReadTimeIsNullAndCreatedUserIdIsNot(jobId, userId);
        return jobStops;
    }

    @Override
    public void updateJobStop(JobStop jobStop) throws Exception {
        if(jobStop.getStopId()==null){
            throw new Exception("10072");
        }
        jobStopDao.save(jobStop);
    }
}
