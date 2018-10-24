package com.gogoyang.rpgapi.meta.task.dao;

import com.gogoyang.rpgapi.meta.task.entity.TaskDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDetailDao extends JpaRepository<TaskDetail, Integer>{
    TaskDetail findByTaskId(Integer id);

    void deleteByTaskId(Integer taskId);
}
