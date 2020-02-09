package com.gogoyang.rpgapi.business.account;


import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
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
    private final IUserService iUserService;

    @Autowired
    public AccountBusinessService(IAccountService iAccountService,
                                  IUserService iUserService) {
        this.iAccountService = iAccountService;
        this.iUserService = iUserService;
    }

    @Override
    public Page<Account> listMyAccount(Map in) throws Exception {
        String token=in.get("token").toString();

        User user=iUserService.getUserByToken(token);
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

    @Override
    public Map loadAccountBalance(Map in) throws Exception {
        String token=in.get("token").toString();
        in.put("token", token);
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }
        Map out=iAccountService.loadAccountBalance(user.getUserId());
        return out;
    }
}
