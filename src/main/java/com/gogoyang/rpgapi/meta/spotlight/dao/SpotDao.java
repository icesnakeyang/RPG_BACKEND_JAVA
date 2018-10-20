package com.gogoyang.rpgapi.meta.spotlight.dao;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SpotDao extends JpaRepository<Spot, Integer> {
    Page<Spot> findAllByJobStatus(JobStatus jobStatus, Pageable pageable);

    ArrayList<Spot> findAllByJobId(Integer jobId);
}
