package com.gogoyang.rpgapi.meta.job.dao;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobMapper {

    ArrayList<Job> listMyPendingJob(Map qIn);
}
