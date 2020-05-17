package com.gogoyang.rpgapi.business.job.myJob.stop.service;


import java.util.Map;

public interface IStopBusinessService {

    void createJobStop(Map in) throws Exception;

    Map loadStopList(Map in) throws Exception;

    void setStopReadTime(Map in) throws Exception;

    /**
     * 拒绝终止
     * @param in
     * @throws Exception
     */
    void rejectStop(Map in) throws Exception;

    /**
     * 同意终止
     * @param in
     * @throws Exception
     */
    void acceptStop(Map in) throws Exception;
}
