package com.gogoyang.rpgapi.meta.job.dao;

import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDetailDao extends JpaRepository<JobDetail, Integer> {
}
