package com.gogoyang.rpgapi.meta.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.dao.RepealDao;
import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

/**
 * 撤诉申请说明
 * Repeal，撤销申诉的申请
 * 当任务处于申诉中时，甲方双方都可以发起撤销申请。
 * 当乙方发起了撤诉申请，另乙方同意了撤销申请，申诉事件即刻撤销。
 * 只有当前没有等待处理的撤诉申请时，才能发起新的撤诉申请
 */

@Service
public class RepealService implements IRepealService{
    private final RepealDao repealDao;

    @Autowired
    public RepealService(RepealDao repealDao) {
        this.repealDao = repealDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertRepeal(Repeal repeal) throws Exception {
        if(repeal.getRepealId()!=null){
            throw new Exception("10079");
        }
        repealDao.save(repeal);
    }

    @Override
    public Page<Repeal> listRepealByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        /**
         * 读取所有撤诉申请
         * 分页
         * 把所有的userId都加上userName
         */
        Sort sort=new Sort(Sort.Direction.DESC, "repealId");
        Pageable pageable=new PageRequest(pageIndex, pageSize, sort);
        Page<Repeal> repeals=repealDao.findallby
        return null;
    }

    @Override
    public Repeal loadMyUnReadRepeal(Integer jobId, Integer userId) throws Exception {
        /**
         * 读取当前任务我未读的撤诉申请
         */
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateRepeal(Repeal repeal) throws Exception {
        /**
         * 修改撤诉申请
         */
        if(repeal.getRepealId()==null){
            throw new Exception("10078");
        }
        repealDao.save(repeal);
    }
}
