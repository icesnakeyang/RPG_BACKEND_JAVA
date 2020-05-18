package com.gogoyang.rpgapi.business.admin.vo;

import com.gogoyang.rpgapi.framework.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {
    private String loginName;
    private String password;
    private RoleType roleType;
    private String jobId;
    private String userId;
    private Integer pageIndex;
    private Integer pageSize;
    private String applyId;
    private String remark;
    private String phone;
}
