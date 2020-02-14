package com.gogoyang.rpgapi.business.admin.withdraw;

import com.gogoyang.rpgapi.business.common.ICommonBusinessService;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import com.gogoyang.rpgapi.meta.account.service.IWithdrawLedgerService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminWithdrawBusinessService implements IAdminWithdrawBusinessService{
    private final ICommonBusinessService iCommonBusinessService;
    private final IWithdrawLedgerService iWithdrawLedgerService;

    public AdminWithdrawBusinessService(ICommonBusinessService iCommonBusinessService,
                                        IWithdrawLedgerService iWithdrawLedgerService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iWithdrawLedgerService = iWithdrawLedgerService;
    }

    @Override
    public Map listUserWithdrawApplys(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);

        ArrayList<Map<String, Object>> withdrawLedgers=iWithdrawLedgerService.listWithdrawAdmin(qIn);

        Map out=new HashMap();
        out.put("withdrawLedgers", withdrawLedgers);

        /**
         * 用户信息+提现信息
         */

        Integer total=iWithdrawLedgerService.totalWithdraw(qIn);
        out.put("totalWithdrawApply", total);

        return out;
    }
}
