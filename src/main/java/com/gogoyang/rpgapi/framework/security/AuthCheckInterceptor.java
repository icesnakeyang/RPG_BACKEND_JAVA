package com.gogoyang.rpgapi.framework.security;

import com.gogoyang.rpgapi.meta.user.userInfo.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthCheckInterceptor implements HandlerInterceptor {
    private final UserInfoDao userInfoDao;

    @Autowired
    public AuthCheckInterceptor(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token=httpServletRequest.getHeader("token");
//        if(StringUtils.isEmpty(token)){
//            throw new Exception("No token in header");
//        }
//        User user=userDao.findByToken(token);
//        if(user==null){
//            throw new Exception("Token error");
//        }
        if(token!=null) {
            AccessContext.setToken(token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        AccessContext.clearAccessKey();
    }
}
