package com.gogoyang.rpgapi.business.account.service;


import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountBusinessService implements IAccountBusinessService {
    private final IAccountService iAccountService;

    @Autowired
    public AccountBusinessService(IAccountService iAccountService) {
        this.iAccountService = iAccountService;
    }

    @Override
    public Page<Account> listMyAccount(Map in) throws Exception {
        String token=in.get("token").toString();

        UserInfo user=iUserInfoService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }
        Map qIn=new HashMap();
        qIn.put("userId", user.getUserId());
        qIn.put("pageIndex", in.get("pageIndex"));
        qIn.put("pageSize", in.get("pageSize"));
        Page<Account> accounts=iAccountService.listMyAccount(qIn);

        for(int i=0;i<accounts.getContent().size();i++){
            Date d1=(Date)accounts.getContent().get(i).getCreatedTime();
            String dd=(new SimpleDateFormat("yyyy-MM-dd")).format(d1);
            accounts.getContent().get(i).setCratedTimeStr(dd);
        }
        return accounts;
    }
}
