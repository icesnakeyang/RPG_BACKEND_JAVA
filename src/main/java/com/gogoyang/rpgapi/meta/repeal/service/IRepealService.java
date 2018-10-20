package com.gogoyang.rpgapi.meta.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.RepositoryQuery;

import java.util.ArrayList;

public interface IRepealService {
    /**
     * 新增
     * @param repeal
     * @throws Exception
     */
    void insertRepeal(Repeal repeal) throws Exception;

    /**
     * 读取所有
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Repeal> listRepealByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取任务的未处理的撤销申诉
     * @param jobId
     * @return
     * @throws Exception
     */
    Page<Repeal> listRepealUnProcess(Integer jobId) throws Exception;

    /**
     * 读取未读
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    ArrayList<Repeal> loadMyUnReadRepeal(Integer jobId, Integer userId) throws Exception;

    /**
     * 修改
     * @param repeal
     * @throws Exception
     */
    void updateRepeal(Repeal repeal) throws Exception;
}
