package com.gogoyang.rpgapi.meta.honor.service;


import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import org.springframework.data.domain.Page;

import java.util.Map;


public interface IHonorService {
    void insertHonor(Honor honor) throws Exception;
    Page<Honor> listMyHonor(Integer userId, Integer pageIndex, Integer pageSize);
    Map loadUserHonorBalance(Integer userId) throws Exception;
}
