package com.gogoyang.rpgapi.meta.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.dao.SpotContentDao;
import com.gogoyang.rpgapi.meta.spotlight.dao.SpotDao;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.spotlight.entity.SpotContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SpotService implements ISpotService {
    private final SpotDao spotDao;
    private final SpotContentDao spotContentDao;

    @Autowired
    public SpotService(SpotDao spotDao, SpotContentDao spotContentDao) {
        this.spotDao = spotDao;
        this.spotContentDao = spotContentDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Spot insertSpotlight(Spot spot) throws Exception {
        if (spot.getSpotId() != null) {
            throw new Exception("10076");
        }
        SpotContent spotContent = new SpotContent();
        spotContent.setContent(spot.getContent());
        spot = spotDao.save(spot);
        spotContent.setSpotId(spot.getSpotId());
        spotContentDao.save(spotContent);
        return spot;
    }

    /**
     * 读取所有正在申诉中的申诉事件
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<Spot> listSpotlight(Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "spotId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Spot> spots = spotDao.findAll(pageable);
        return spots;
    }

    /**
     * 读取一个任务的所有申诉记录
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Page<Spot> listSpotlightByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort=new Sort(Sort.Direction.DESC, "spotId");
        Pageable pageable=new PageRequest(pageIndex, pageSize, sort);
        Page<Spot> spots = spotDao.findAllByJobId(jobId, pageable);
        return spots;
    }

    @Override
    public Spot getSpotlightByJobId(Integer spotId) throws Exception {
        Spot spot = spotDao.findBySpotId(spotId);
        return spot;
    }
}
