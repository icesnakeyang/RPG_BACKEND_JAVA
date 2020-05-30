package com.gogoyang.rpgapi.meta.resource.service;

import com.gogoyang.rpgapi.meta.resource.dao.ResourceFileDao;
import com.gogoyang.rpgapi.meta.resource.entity.ResourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class ResourceFileService implements IResourceFileService{
    private final ResourceFileDao resourceFileDao;

    public ResourceFileService(ResourceFileDao resourceFileDao) throws Exception{
        this.resourceFileDao = resourceFileDao;
    }

    @Override
    public void createResourceFile(ResourceFile resourceFile) throws Exception{
        resourceFileDao.createResourceFile(resourceFile);
    }

    /**
     * 删除资源文件
     * @param qIn
     * @throws Exception
     */
    @Override
    public void deleteResourceFile(Map qIn) throws Exception{
        String taskId=(String)qIn.get("taskId");
        String jobId=(String)qIn.get("jobId");
        String fileId=(String)qIn.get("fileId");

        /**
         * 删除资源文件时，至少需要指定一个taskId，或者jobId，
         * 否则，所有的资源文件都会被删除
         */

        if(taskId==null && jobId==null && fileId==null){
            throw new Exception("30019");
        }

        resourceFileDao.deleteResourceFile(qIn);
    }

    @Override
    public ArrayList<ResourceFile> listResourceFile(Map qIn) throws Exception {
        ArrayList<ResourceFile> resourceFiles=resourceFileDao.listResourceFile(qIn);
        return resourceFiles;
    }

    @Override
    public void updateResourceFile(Map qIn) throws Exception {
        resourceFileDao.updateResourceFile(qIn);
    }
}
