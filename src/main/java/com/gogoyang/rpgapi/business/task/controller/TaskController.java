package com.gogoyang.rpgapi.business.task.controller;

import com.gogoyang.rpgapi.business.task.service.ITaskBusinessService;
import com.gogoyang.rpgapi.business.task.vo.TaskRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final ITaskBusinessService iTaskBusinessService;

    @Autowired
    public TaskController(ITaskBusinessService iTaskBusinessService) {
        this.iTaskBusinessService = iTaskBusinessService;
    }

    /**
     * 创建任务
     */
    @ResponseBody
    @PostMapping("/create")
    public Response createTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("title", request.getTitle());
            in.put("detail", request.getDetail());
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("pid", request.getPid());
            Task task = iTaskBusinessService.createTask(in);
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
     * 根据taskId读取任务，包括任务详情
     */
    @ResponseBody
    @GetMapping("/{taskId}")
    public Response getTaskById(@PathVariable Integer taskId) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            in.put("taskId", taskId);
            Map out = iTaskBusinessService.getTaskDetailByTaskId(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10031);
            }
        }
        return response;
    }

    /**
     * 根据taskId读取任务，不包括任务详情
     */
    @ResponseBody
    @PostMapping("/getTaskTinyById")
    public Response getTaskTinyById(@PathVariable Integer taskId) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            in.put("taskId", taskId);
            Map out = iTaskBusinessService.getTaskDetailByTaskId(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10031);
            }
        }
        return response;
    }

    /**
     * 根据userId，读取所有task
     */
    @ResponseBody
    @PostMapping("/mytask")
    public Response loadMyTasks(@RequestBody TaskRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<Task> tasks = iTaskBusinessService.listTaskByUserId(in);
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

    /**
     * 修改
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/update")
    public Response updateTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("title", request.getTitle());
            in.put("code", request.getCode());
            in.put("detail", request.getDetail());
            in.put("days", request.getDays());
            iTaskBusinessService.updateTask(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10093);
                return response;
            }
        }
        return response;
    }

    /**
     * 删除
     * 真的删除
     */
    @ResponseBody
    @PostMapping("/delete")
    public Response deleteTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            iTaskBusinessService.deleteTask(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10094);
                return response;
            }
        }
        return response;
    }

    /**
     * 统计子任务的数量
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/countSubTask")
    public Response countSubTask(@RequestBody TaskRequest request,
                                 HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            Map in=new HashMap();
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pid", request.getPid());
            Map out=iTaskBusinessService.totalSubTask(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10095);
            }
        }
        return response;
    }


}
