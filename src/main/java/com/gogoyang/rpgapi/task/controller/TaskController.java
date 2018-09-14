package com.gogoyang.rpgapi.task.controller;

import com.gogoyang.rpgapi.task.service.ITaskService;
import com.gogoyang.rpgapi.task.vo.CreateTaskRequest;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final IUserService userService;

    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService iTaskService, IUserService userService) {
        this.taskService = iTaskService;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/create")
    public Response createTask(@RequestBody CreateTaskRequest request,
                               HttpServletRequest servletRequest){
        String token=servletRequest.getHeader("token");
        User user=userService.buildUserByToken(token);
        request.setCreatedUserId(user.getUserId());
        return taskService.createTask(request);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Response buildTaskInfo(@PathVariable Integer id){
        return taskService.buildTaskInfoById(id);
    }

    @ResponseBody
    @PostMapping("/mytask")
    public Response publicTasks(HttpServletRequest servletRequest){
        String token=servletRequest.getHeader("token");
        User user=userService.buildUserByToken(token);
        return taskService.buildTasks(user.getUserId());
    }
}
