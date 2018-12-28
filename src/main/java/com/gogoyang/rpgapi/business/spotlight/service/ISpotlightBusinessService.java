package com.gogoyang.rpgapi.business.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ISpotlightBusinessService {

    /**
     * 申诉广场读取所有当前申诉
     * @param in
     * @return
     * @throws Exception
     */
    Page<Spot> listSpotlight(Map in) throws Exception;

    /**
     * 读取一个简要的申诉
     * @param in
     * @return
     * @throws Exception
     */
    Map getSpotlightTiny(Map in) throws Exception;

    /**
     * 读取一个详细的申诉
     * @param in
     * @return
     * @throws Exception
     */
    Map getSpotlightDetail(Map in) throws Exception;

    void createSpotBook(Map in) throws Exception;
}
