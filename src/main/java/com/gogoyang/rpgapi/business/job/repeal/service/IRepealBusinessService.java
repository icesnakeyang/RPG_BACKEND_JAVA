package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IRepealBusinessService {
    Repeal createRepeal(Map in) throws Exception;

    Page<Repeal> listRepeal(Map in) throws Exception;

    void setRepealReadTime(Map in) throws Exception;

    void rejectRepeal(Map in) throws Exception;

    void acceptRepeal(Map in) throws Exception;

    Integer countUnreadRepeal(Map in) throws Exception;
}
