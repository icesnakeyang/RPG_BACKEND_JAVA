package com.gogoyang.rpgapi.task.dao;

import com.gogoyang.rpgapi.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDao extends JpaRepository<Task, Integer> {
    Task findByTaskId(Integer id);
    List findAllByCreatedUserId(Integer id);
}
