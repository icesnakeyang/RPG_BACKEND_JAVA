package com.gogoyang.rpgapi.business.admin.secretary.realname;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.mysql.cj.PreparedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecretaryRealnameBusinessService implements ISecretaryRealnameBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final IRealNameService iRealNameService;

    public SecretaryRealnameBusinessService(ICommonBusinessService iCommonBusinessService,
                                            IRealNameService iRealNameService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iRealNameService = iRealNameService;
    }

    @Override
    public Map listRealnamePending(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        Admin admin = iCommonBusinessService.getAdminByToken(token);
        Map qIn = new HashMap();
        qIn.put("verifyStatus", "PENDING");
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<RealName> realNames = iRealNameService.listRealName(qIn);

        Map out = new HashMap();
        out.put("realnameList", realNames);

        /**
         * 统计实名认证申请总数
         */
        Integer total = iRealNameService.totalRealName(qIn);
        out.put("totalRealname", total);

        return out;

    }

    @Override
    public Map getRealnamePending(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();

        Admin admin = iCommonBusinessService.getAdminByToken(token);

        RealName realName = iRealNameService.getRealNameByUserId(userId);

        Map out = new HashMap();
        out.put("realName", realName);

        return out;
    }

    @Override
    public void agreeRealname(Map in) throws Exception {
        String token=in.get("token").toString();
        String userId=in.get("userId").toString();

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        RealName realName=iRealNameService.getRealNameByUserId(userId);
        if(realName==null){
            throw new Exception("30033");
        }
        Map qIn=new HashMap();
        qIn.put("verifyResult", GogoStatus.AGREE);
        qIn.put("userId", userId);
        iRealNameService.update(qIn);
    }

    @Override
    public void rejectRealname(Map in) throws Exception {
        String token=in.get("token").toString();
        String userId=in.get("userId").toString();
        String remark=in.get("remark").toString();

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        RealName realName=iRealNameService.getRealNameByUserId(userId);
        if(realName==null){
            throw new Exception("30033");
        }
        Map qIn=new HashMap();
        qIn.put("verifyResult", GogoStatus.REJECT);
        qIn.put("userId", userId);
        qIn.put("remark", remark);
        iRealNameService.update(qIn);
    }
}
