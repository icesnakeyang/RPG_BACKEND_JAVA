package com.gogoyang.rpgapi.business.admin.withdraw;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.account.service.IWithdrawLedgerService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminWithdrawBusinessService implements IAdminWithdrawBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IWithdrawLedgerService iWithdrawLedgerService;
    private final IUserService iUserService;
    private final IAccountService iAccountService;

    public AdminWithdrawBusinessService(ICommonBusinessService iCommonBusinessService,
                                        IWithdrawLedgerService iWithdrawLedgerService,
                                        IUserService iUserService,
                                        IAccountService iAccountService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iWithdrawLedgerService = iWithdrawLedgerService;
        this.iUserService = iUserService;
        this.iAccountService = iAccountService;
    }

    @Override
    public Map listUserWithdrawApplys(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        Admin admin = iCommonBusinessService.getAdminByToken(token);

        Map qIn = new HashMap();
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);

        ArrayList<Map<String, Object>> withdrawLedgers = iWithdrawLedgerService.listWithdrawAdmin(qIn);

        Map out = new HashMap();
        out.put("withdrawLedgers", withdrawLedgers);

        /**
         * 用户信息+提现信息
         */

        Integer total = iWithdrawLedgerService.totalWithdraw(qIn);
        out.put("totalWithdrawApply", total);

        return out;
    }

    @Override
    public Map getWithdrawApplys(Map in) throws Exception {
        String token = in.get("token").toString();
        String withdrawLedgerId = in.get("withdrawLedgerId").toString();

        Admin admin = iCommonBusinessService.getAdminByToken(token);

        WithdrawLedger withdrawLedger = iWithdrawLedgerService.getWithdrawAdmin(withdrawLedgerId);

        Map out = new HashMap();
        out.put("withdrawLedger", withdrawLedger);

        UserInfo userInfo = iUserService.getUserByUserId(withdrawLedger.getUserId());
        out.put("userInfo", userInfo);

        return out;
    }

    /**
     * 管理员通过用户提现申请
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agreeWithdraw(Map in) throws Exception {
        String token = in.get("token").toString();
        String withdrawLedgerId = in.get("withdrawLedgerId").toString();

        /**
         * 目前，提现是一个手动过程，所谓管理员通过，只不过是手动把取现设置为成功状态，真实的转账还是通过线下进行
         * 如果开通了在线支付过后，转账是一个自动过程，则改管理员通过取现的操作可以作废
         */
        Admin admin = iCommonBusinessService.getAdminByToken(token);

        WithdrawLedger withdrawLedger = iWithdrawLedgerService.getWithdrawAdmin(withdrawLedgerId);

        if (withdrawLedger == null) {
            throw new Exception("30035");
        }

        if (!withdrawLedger.getStatus().equals(GogoStatus.PENDING.toString())) {
            throw new Exception("30034");
        }

        Map map = iCommonBusinessService.sumUserAccount(withdrawLedger.getUserId());

        Double balance = (Double) map.get("accountBalance");
        if (balance == null) {
            balance = 0.0;
        }
        if (balance < withdrawLedger.getAmount()) {
            throw new Exception("20004");
        }

        /**
         * 修改withdrawLedger表的status为agree
         * 插入account表
         * 重新计算余额，更新userInfo
         */
        Map qIn = new HashMap();
        qIn.put("status", GogoStatus.AGREE);
        qIn.put("withdrawLedgerId", withdrawLedgerId);
        qIn.put("processTime", new Date());
        iWithdrawLedgerService.updateWithdraw(qIn);

        Account account=new Account();
        account.setAccountId(GogoTools.UUID().toString());
        account.setAmount(withdrawLedger.getAmount());
        account.setUserId(withdrawLedger.getUserId());
        account.setCreatedTime(new Date());
        account.setType(AccountType.WITHDRAW.toString());
        iAccountService.createAccount(account);

        Double accountIn = (Double) map.get("accountIn");
        if (accountIn == null) {
            accountIn = 0.0;
        }
        Double accountOut = (Double) map.get("accountOut");
        if (accountOut == null) {
            accountOut = 0.0;
        }
        accountOut = +withdrawLedger.getAmount();
        balance = accountIn - accountOut;
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(withdrawLedger.getUserId());
        userInfo.setAccount(balance);
        userInfo.setAccountIn(accountIn);
        userInfo.setAccountOut(accountOut);
        iUserService.updateUserInfo(userInfo);
    }
}
