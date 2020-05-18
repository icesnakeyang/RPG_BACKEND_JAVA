package com.gogoyang.rpgapi.meta.honor.service;


import com.gogoyang.rpgapi.meta.honor.entity.Honor;

import java.util.ArrayList;
import java.util.Map;


public interface IHonorService {
    /**
     * 创建一个荣誉值记录
     * @param honor
     */
    void createHonor(Honor honor);

    /**
     *
     * @param qIn
     * userId
     * offset
     * size
     * @return
     */
    ArrayList<Honor> listHonor(Map qIn);

    /**
     * 统计一个用户的荣誉值，可按type分类统计
     * @param qIn
     * userId
     * type
     * @return
     */
    Double sumHonor(Map qIn);

    /**
     * 统计荣誉记录总数
     * @param qIn
     * userId
     * @return
     */
    Integer totalHonor(Map qIn);
}
