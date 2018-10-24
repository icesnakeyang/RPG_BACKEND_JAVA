package com.gogoyang.rpgapi.meta.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.data.domain.Page;

public interface IRepealService {
    /**
     * 新增
     * @param repeal
     * @throws Exception
     */
    Repeal insertRepeal(Repeal repeal) throws Exception;

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
     * 读取未读
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    Repeal getMyUnReadRepeal(Integer jobId, Integer userId) throws Exception;

    /**
     * 读取一个任务的我未处理的撤销申诉
     * @param jobId
     * @return
     * @throws Exception
     */
    Repeal getUnProcessRepeal(Integer jobId) throws Exception;

    /**
     * 修改
     * @param repeal
     * @throws Exception
     */
    void updateRepeal(Repeal repeal) throws Exception;
}
