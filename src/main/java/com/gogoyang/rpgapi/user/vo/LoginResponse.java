package com.gogoyang.rpgapi.user.vo;


import com.gogoyang.rpgapi.user.entity.User;

public class LoginResponse {
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
