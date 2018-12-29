package com.gogoyang.rpgapi.framework.aop;

import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.user.entity.User;

public class AspectVariable {
    private static final ThreadLocal<RoleType> roleType=new ThreadLocal<RoleType>();
    private static final ThreadLocal<User> currentUser=new ThreadLocal<User>();
    private static final ThreadLocal<String> token=new ThreadLocal<String>();

    public static RoleType getRoleType(){
        return roleType.get();
    }

    public static void setRoleType(RoleType value){
        roleType.set(value);
    }

    public static User getCurrentUser(){
        return currentUser.get();
    }

    public static void setCurrentUser(User value){
        currentUser.set(value);
    }

    public static String getToken(){
        return token.get();
    }

    public static void setToken(String value){
        token.set(value);
    }
}
