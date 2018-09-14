package com.gogoyang.rpgapi.user.vo;

import com.gogoyang.rpgapi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    private String password;

    public User toUser(){
        User user=new User();

        user.setUsername(username);
        user.setPassword(password);

        return user;
    }
}
