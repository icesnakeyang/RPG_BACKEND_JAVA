package com.gogoyang.rpgapi.task.controller;

import com.gogoyang.rpgapi.task.entity.Task;
import com.gogoyang.rpgapi.task.service.ITaskService;
import com.gogoyang.rpgapi.task.vo.TaskRequest;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;

    @Autowired
    public TaskController(ITaskService iTaskService, IUserInfoService iUserInfoService) {
        this.iTaskService = iTaskService;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 创建新任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    public Response createTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);
            if (userInfo == null) {
                response.setErrorCode(10004);
                return response;
            }
            Task task = new Task();
            if(request.getTaskId()!=null) {
                task.setTaskId(request.getTaskId());
            }
            task.setCreatedUserId(userInfo.getUserId());
            task.setDetail(request.getDetail());
            task.setCreatedTime(new Date());
            task.setCode(request.getCode());
            task.setDays(request.getDays());
            task.setPid(request.getPid());
            task.setTitle(request.getTitle());
            task.setPrice(request.getPrice());
            task = iTaskService.createTask(task);
            response.setData(task);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10030);
            }
        }
        return response;
    }

    /**
     * 根据taskId读取任务
     * @param taskId
     * @return
     */
    @ResponseBody
    @GetMapping("/{taskId}")
    public Response loadTaskById(@PathVariable Integer taskId){
        Response response=new Response();
        try {
            Task task=iTaskService.loadTaskByTaskId(taskId);
            response.setData(task);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10031);
            }
        }
        return response;
    }

    /**
     * 根据userId，读取所有task
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/mytask")
    public Response loadMyTasks(HttpServletRequest servletRequest) {
        Response response = new Response();
        try {
            String token = servletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);
            ArrayList<Task> tasks = iTaskService.loadTaskByUserId(userInfo.getUserId());
            response.setData(tasks);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10031);
                return response;
            }
        }
        return response;
    }
}
