package com.gogoyang.rpgapi.user.controller;

import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.CreateUserRequest;
import com.gogoyang.rpgapi.user.vo.LoginRequest;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/create")
    public Response createUser(@RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Response buildUser(@PathVariable Integer id){
        return userService.buildUserById(id);
    }

    @ResponseBody
    @PostMapping("/login")
    public Response loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request);
    }
}
