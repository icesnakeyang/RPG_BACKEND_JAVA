package com.gogoyang.rpgapi.aop;

import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;

public class AspectVariable {
    private static final ThreadLocal<RoleType> roleType=new ThreadLocal<RoleType>();
    private static final ThreadLocal<UserInfo> currentUser=new ThreadLocal<UserInfo>();
    private static final ThreadLocal<String> token=new ThreadLocal<String>();

    public static RoleType getRoleType(){
        return roleType.get();
    }

    public static void setRoleType(RoleType value){
        roleType.set(value);
    }

    public static UserInfo getCurrentUser(){
        return currentUser.get();
    }

    public static void setCurrentUser(UserInfo value){
        currentUser.set(value);
    }

    public static String getToken(){
        return token.get();
    }

    public static void setToken(String value){
        token.set(value);
    }
}
