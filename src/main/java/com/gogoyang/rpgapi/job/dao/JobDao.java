package com.gogoyang.rpgapi.job.dao;

import com.gogoyang.rpgapi.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDao extends JpaRepository<Job, Integer>{
//    List findAllByCategory(String category);
    Job findByJobId(Integer jobId);
    Page<Job> findAllByMatchLogIdIsNull(Pageable pageable);
}
