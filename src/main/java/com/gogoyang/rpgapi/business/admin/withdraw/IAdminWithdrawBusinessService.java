package com.gogoyang.rpgapi.business.admin.withdraw;

import java.util.Map;

public interface IAdminWithdrawBusinessService {
    Map listUserWithdrawApplys(Map in) throws Exception;

    Map getWithdrawApplys(Map in) throws Exception;

    void agreeWithdraw(Map in) throws Exception;
}
