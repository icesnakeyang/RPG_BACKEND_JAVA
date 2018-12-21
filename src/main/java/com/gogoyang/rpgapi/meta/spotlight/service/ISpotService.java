package com.gogoyang.rpgapi.meta.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface ISpotService {
    /**
     * 创建一个申诉
     * @param spot
     * @throws Exception
     */
    Spot insertSpotlight(Spot spot) throws Exception;

    /**
     * 读取所有申诉事件
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Spot> listSpotlight(Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取一个任务的所有申诉
     * @param jobId
     * @return
     * @throws Exception
     */
    Page<Spot> listSpotlightByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取一个任务当前正在申诉的事件
     * @param jobId
     * @return
     * @throws Exception
     */
    Spot getSpotlightByJobId(Integer jobId) throws Exception;
}
