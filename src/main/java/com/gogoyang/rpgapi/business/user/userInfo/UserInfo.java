package com.gogoyang.rpgapi.business.user.userInfo;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.user.entity.User;

public class UserInfo {
    private User user;
    private Email email;
    private Phone phone;
    private RealName realName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public RealName getRealName() {
        return realName;
    }

    public void setRealName(RealName realName) {
        this.realName = realName;
    }
}
