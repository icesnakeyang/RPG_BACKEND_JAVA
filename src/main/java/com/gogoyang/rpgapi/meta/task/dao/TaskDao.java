package com.gogoyang.rpgapi.meta.task.dao;

import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TaskDao extends JpaRepository<Task, Integer> {
    Task findByTaskId(Integer taskId);

//    @Query(value = "select ii.image_id from issues_images ii where issue_id = ?1", nativeQuery = true) // nativeQuery = true表示使用sql自己的方言查询，想查什么查什么， 按照字段数据类型返回就行了
//    List<BigInteger> findByIssueId(String issueId);
    ArrayList<Task> findAllByCreatedUserId(Integer userId);


}
