package com.gogoyang.rpgapi.meta.resource.dao;

import com.gogoyang.rpgapi.meta.resource.entity.ResourceFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface ResourceFileDao {
    void createResourceFile(ResourceFile resourceFile);

    /**
     * 删除资源文件
     * @param qIn
     * jobId
     * taskId
     * fileId
     */
    void deleteResourceFile(Map qIn);

    ArrayList<ResourceFile> listResourceFile(Map qIn);

    /**
     * 修改资源文件
     * @param qIn
     * jobId
     * fileId
     * jobIdNull
     * taskIdNull
     */
    void updateResourceFile(Map qIn);
}
