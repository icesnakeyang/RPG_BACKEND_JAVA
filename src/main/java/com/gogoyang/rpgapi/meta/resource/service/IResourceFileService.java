package com.gogoyang.rpgapi.meta.resource.service;

import com.gogoyang.rpgapi.meta.resource.entity.ResourceFile;

import java.util.ArrayList;
import java.util.Map;

public interface IResourceFileService {
    void createResourceFile(ResourceFile resourceFile) throws Exception;

    /**
     * 删除资源文件
     * @param qIn
     * jobId
     * taskId
     * @throws Exception
     */
    void deleteResourceFile(Map qIn) throws Exception;

    ArrayList<ResourceFile> listResourceFile(Map qIn) throws Exception;

    /**
     * 修改资源文件
     * @param qIn
     * jobId
     * fileId
     * jobIdNull
     * taskIdNull
     * @throws Exception
     */
    void updateResourceFile(Map qIn) throws Exception;
}
