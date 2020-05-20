package com.gogoyang.rpgapi.meta.task.dao;

import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TaskDao {

    void createTask(Task task);
    void createTaskDetail(Task task);

    /**
     * 读取一个任务的简要信息
     * @param taskId
     * @return
     */
    Task getTaskTiny(String taskId);

    /**
     * 读取一个任务详情
     * @param taskId
     */
    Task getTaskDetail(String taskId);

    /**
     * 批量读取任务
     * @param qIn
     * titleKey:任务标题关键字模糊查询
     * pid：父任务jobId
     * createdUserId:任务创建人id
     * offset
     * size
     * @return
     */
    ArrayList<Task> listTask(Map qIn);

    /**
     * 根据taskId修改task
     * @param task
     */
    void updateTaskTiny(Task task);

    /**
     * 修改一个任务的详情
     * @param task
     */
    void updateTaskDetail(Task task);

    /**
     * 删除task
     * @param taskId
     */
    void deleteTaskTiny(String taskId);

    /**
     * 删除task_detail
     * @param taskId
     */
    void deleteTaskDetail(String taskId);

    /**
     * 统计任务数量
     * @param qIn
     * pid
     * @return
     */
    Integer totalTask(Map qIn);
}
