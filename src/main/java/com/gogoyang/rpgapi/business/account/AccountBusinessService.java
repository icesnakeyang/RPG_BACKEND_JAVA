package com.gogoyang.rpgapi.business.account;


import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.account.service.IWithdrawLedgerService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountBusinessService implements IAccountBusinessService {
    private final IAccountService iAccountService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IWithdrawLedgerService iWithdrawLedgerService;

    @Autowired
    public AccountBusinessService(IAccountService iAccountService,
                                  ICommonBusinessService iCommonBusinessService,
                                  IWithdrawLedgerService iWithdrawLedgerService) {
        this.iAccountService = iAccountService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iWithdrawLedgerService = iWithdrawLedgerService;
    }

    @Override
    public Map listMyAccount(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        UserInfo user = iCommonBusinessService.getUserByToken(token);
        Map qIn = new HashMap();
        qIn.put("userId", user.getUserId());
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Account> accounts = iAccountService.listAccount(qIn);

        Map out=new HashMap();
        out.put("accounts", accounts);

        //查询记录总数
        Integer total=iAccountService.totalAccount(qIn);
        out.put("totalAccounts", total);

        return out;
    }

    @Override
    public Map loadAccountBalance(Map in) throws Exception {
        String token = in.get("token").toString();
        in.put("token", token);
        UserInfo user = iCommonBusinessService.getUserByToken(token);
        Map out = iCommonBusinessService.sumUserAccount(user.getUserId());
        return out;
    }

    /**
     * 用户申请取现
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(Map in) throws Exception {
        String token = in.get("token").toString();
        Double amount = (Double) in.get("amount");
        String remark = (String) in.get("remark");

        /**
         * 1、检查用户是否登录
         * 2、读取用户的account，是否有足够余额提现
         * 3、读取用户的申诉事件，用户提现额度应该是（余额-申诉任务金额）
         * 4、创建提现日志，等待管理员处理
         */

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //检查用户余额
        Map accountMap = iCommonBusinessService.sumUserAccount(user.getUserId());
        Double balance = (Double) accountMap.get("accountBalance");
        if (balance < amount) {
            throw new Exception("20004");
        }

        //todo 检查被申诉任务的金额

        //提现申请
        WithdrawLedger withdrawLedger = new WithdrawLedger();
        withdrawLedger.setAmount(amount);
        withdrawLedger.setCreateTime(new Date());
        withdrawLedger.setRemark(remark);
        withdrawLedger.setStatus(LogStatus.PENDING.toString());
        withdrawLedger.setUserId(user.getUserId());
        withdrawLedger.setWithdrawLedgerId(GogoTools.UUID());

        iWithdrawLedgerService.createWithdrawLedger(withdrawLedger);
    }

    /**
     * 查询用户取现记录
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listWithdraw(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", user.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<WithdrawLedger> withdrawLedgers = iWithdrawLedgerService.listWithdraw(qIn);

        Map out = new HashMap();
        out.put("withdrawLedgers", withdrawLedgers);

        Integer total = iWithdrawLedgerService.totalWithdraw(qIn);
        out.put("totalWithdrawLedger", total);

        return out;
    }
}
