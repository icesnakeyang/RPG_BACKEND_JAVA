package com.gogoyang.rpgapi.task.dao;

import com.gogoyang.rpgapi.task.entity.TaskDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDetailDao extends JpaRepository<TaskDetail, Integer>{
    TaskDetail findByTaskId(Integer id);
}
