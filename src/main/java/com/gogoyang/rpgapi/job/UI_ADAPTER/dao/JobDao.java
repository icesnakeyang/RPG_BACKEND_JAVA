package com.gogoyang.rpgapi.job.UI_ADAPTER.dao;


import com.gogoyang.rpgapi.job.ENTITY.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDao extends JpaRepository<Job, Integer>{
//    List findAllByCategory(String category);
    Job findByJobId(Integer jobId);

    Page<Job> findAllByStatusIsNull(Pageable pageable);
}
