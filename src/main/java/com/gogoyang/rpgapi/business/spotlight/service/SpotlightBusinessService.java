package com.gogoyang.rpgapi.business.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.spotlight.entity.SpotBook;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotlightBusinessService implements ISpotlightBusinessService{
    private final ISpotService iSpotService;

    @Autowired
    public SpotlightBusinessService(ISpotService iSpotService) {
        this.iSpotService = iSpotService;
    }

    @Override
    public Map listSpotlight(Map in) throws Exception {
        /**
         * 分页读取所有当前有效的申诉，用于申诉广场的列表显示
         * 把用户id的用户名加上
         * 统计下阅读人数
         */
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Page<Spot> spots=iSpotService.listSpotlight(pageIndex, pageSize);

        for(int i=0;i<spots.getContent().size();i++){
            UserInfo user=iUserInfoService.getUserByUserId(spots.getContent().get(i).getCreatedUserId());
            if(user.getRealName()!=null) {
                spots.getContent().get(i).setCreatedUserName(user.getRealName());
            }else{
                spots.getContent().get(i).setCreatedUserName(user.getEmail());
            }
        }

        Map out=new HashMap();
        out.put("spotlightList", spots);
        return out;
    }

    @Override
    public Map getSpotlightTiny(Map in) throws Exception {
        /**
         * 根据jobId读取一个简要的申诉信息，不包含详情
         */
        return null;
    }

    @Override
    public Map getSpotlightDetail(Map in) throws Exception {
        /**
         * 根据jobId读取详细的申诉，包括详情，用于申诉详情页显示
         * 根据token获取当前用户，如果不是创建用户，就增加一次views。
         * views并不判断一个用户的重复访问，只要访问一次就加一次
         */
        Integer spotId=(Integer)in.get("spotId");

        Spot spot=iSpotService.getSpotlightBySpotId(spotId);
        Map out=new HashMap();
        out.put("spot", spot);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createSpotBook(Map in) throws Exception {
        /**
         * 创建一个申诉事件的用户留言
         * 用户必须登录
         */
        String token=in.get("token").toString();
        Integer spotId=(Integer)in.get("spotId");
        String content=in.get("content").toString();

        UserInfo userInfo=iUserInfoService.getUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        SpotBook spotBook=new SpotBook();
        spotBook.setContent(content);
        spotBook.setCreatedTime(new Date());
        spotBook.setCreatedUserId(userInfo.getUserId());
        spotBook.setSpotId(spotId);
        iSpotService.insertSpotBook(spotBook);
    }
}
