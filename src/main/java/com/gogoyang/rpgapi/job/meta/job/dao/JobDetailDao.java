package com.gogoyang.rpgapi.job.meta.job.dao;

import com.gogoyang.rpgapi.job.meta.job.entity.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDetailDao extends JpaRepository<JobDetail, Integer> {
}
