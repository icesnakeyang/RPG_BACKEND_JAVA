package com.gogoyang.rpgapi.meta.job.dao;


import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDao extends JpaRepository<Job, Integer>{
//    List findAllByCategory(String category);
    Job findByJobId(Integer jobId);

    Page<Job> findAllByStatusIsNull(Pageable pageable);
}
