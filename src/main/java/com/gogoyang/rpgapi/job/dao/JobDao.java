package com.gogoyang.rpgapi.job.dao;

import com.gogoyang.rpgapi.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDao extends JpaRepository<Job, Integer>{
    List findAllByCategory(String category);
}
