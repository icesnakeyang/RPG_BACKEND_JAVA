package com.gogoyang.rpgapi.business.honor.service;

import com.gogoyang.rpgapi.meta.honor.service.IHonorService;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HonorBusinessService implements IHonorBusinessService{
    private final IHonorService iHonorService;
    private final IUserService iUserService;

    @Autowired
    public HonorBusinessService(IHonorService iHonorService,
                                IUserService iUserService) {
        this.iHonorService = iHonorService;
        this.iUserService = iUserService;
    }

    @Override
    public Map listMyHonor(Map in) throws Exception {
        iHonorService.insertHonor();
        return null;
    }
}
