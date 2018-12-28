package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.meta.account.dao.AccountDao;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService implements IAccountService {
    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * save a new account record
     *
     * @param account
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Account insertNewAccount(Account account) throws Exception {
        if (account.getAccountId() != null) {
            throw new Exception("10042");
        }
        account = accountDao.save(account);
        return account;
    }

    /**
     * calculate user balance
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Map loadAccountBalance(Integer userId) throws Exception {
        /**
         * load all userId account record
         * calculate the total income, total outgoing, and balance
         * return map
         */
        ArrayList<Account> accounts = accountDao.findAllByUserId(userId);
        Double balance = 0.0;
        Double income = 0.0;
        Double outgoing = 0.0;
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            //支出，发布任务，取现，退还任务款
            if (account.getType() == AccountType.PUBLISH ||
                    account.getType() == AccountType.WITHDRAW ||
                    account.getType() == AccountType.REFUND_OUT) {
                outgoing -= account.getAmount();
            }
            //收入，充值，接受任务，收到任务退款
            if (account.getType() == AccountType.TOP_UP ||
                    account.getType() == AccountType.ACCEPT ||
                    account.getType() == AccountType.REFUND_IN) {
                income += account.getAmount();
            }
            Double amount = accounts.get(i).getAmount();
            balance += amount;
        }
        //余额
        balance = income - outgoing;
        Map out = new HashMap();
        out.put("balance", balance);
        out.put("income", income);
        out.put("outgoing", outgoing);
        return out;
    }

    @Override
    public Page<Account> listMyAccount(Map qIn) throws Exception {
        Integer userId = (Integer) qIn.get("userId");
        Integer pageIndex = (Integer) qIn.get("pageIndex");
        Integer pageSize = (Integer) qIn.get("pageSize");
        Sort sort = new Sort(Sort.Direction.DESC, "accountId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Account> accounts = accountDao.findAllByUserId(userId, pageable);
        return accounts;
    }
}
