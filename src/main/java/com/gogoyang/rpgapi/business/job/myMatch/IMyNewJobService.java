package com.gogoyang.rpgapi.business.job.myMatch;

import java.util.Map;

public interface IMyNewJobService {
    void acceptNewJob(Map in) throws Exception;

    void rejectNewJob(Map in) throws Exception;
}
