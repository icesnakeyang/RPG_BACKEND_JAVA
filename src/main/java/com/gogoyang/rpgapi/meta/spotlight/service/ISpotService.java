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
    void insertSpotlight(Spot spot) throws Exception;

    /**
     * 读取所有申诉事件
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Spot> loadSpotlight(Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取一个任务的所有申诉
     * @param jobId
     * @return
     * @throws Exception
     */
    ArrayList<Spot> loadSpotlightByJobId(Integer jobId) throws Exception;

    /**
     * 修改验收日志/处理验收日志申请
     * @param spot
     * @throws Exception
     */
    void updateSpotlight(Spot spot) throws Exception;
}