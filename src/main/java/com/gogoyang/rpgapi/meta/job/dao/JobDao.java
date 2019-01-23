package com.gogoyang.rpgapi.meta.job.dao;


import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobDao extends JpaRepository<Job, Integer>{
//    List findAllByCategory(String category);
    Job findByJobId(Integer jobId);

    Job findByTaskId(Integer taskId);

    //find jobs waiting for match
    Page<Job> findAllByStatus(JobStatus jobStatus,Pageable pageable);

    //find all party a jobs
    Page<Job> findAllByPartyAIdAndStatus(Integer userId, JobStatus jobStatus, Pageable pageable);
//    ArrayList<Job> findAllByPartyAIdAndStatus(Integer userId, JobStatus jobStatus);

    Page<Job> findAllByPartyBIdAndStatus(Integer userId, JobStatus jobStatus, Pageable pageable);

    Page<Job> findAllByStatusOrStatus(JobStatus jobStatus1, JobStatus jobStatus2, Pageable pageable);

    Page<Job> findAllByPartyAIdAndStatusOrStatus(Integer userId, JobStatus jobStatus1, JobStatus jobStatus2, Pageable pageable);

    /**
     * read party a accepted job
     */
    Page<Job> findAllByStatusAndPartyAId(JobStatus jobStatus, Integer partyAId, Pageable pageable);

    /**
     * read party b accepted job
     */
    Page<Job> findAllByStatusAndPartyBId(JobStatus jobStatus, Integer partyBId, Pageable pageable);
}
