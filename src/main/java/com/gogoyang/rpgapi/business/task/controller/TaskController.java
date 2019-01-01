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
    @PostMapping("/createTask")
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
     * 创建子任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createSubTask")
    public Response createSubTask(@RequestBody TaskRequest request,
                                   HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);
            in.put("pid", request.getPid());
            in.put("code", request.getCode());
            in.put("title", request.getTitle());
            iTaskBusinessService.createSubTask(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10096);
            }
        }
        return response;
    }

    /**
     * 根据taskId读取任务，包括任务详情
     */
    @ResponseBody
    @PostMapping("/getTaskDetailByTaskId")
    public Response getTaskDetailByTaskId(@RequestBody TaskRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("taskId", request.getTaskId());
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
    @PostMapping("/getTaskTinyByTaskId")
    public Response getTaskTinyById(@RequestBody TaskRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            Map out = iTaskBusinessService.getTaskTinyByTaskId(in);
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
            in.put("taskId", request.getTaskId());
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
    @PostMapping("/deleteTask")
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

    /**
     * 读取子任务列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listSubTask")
    public Response listSubTask(@RequestBody TaskRequest request,
                                HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pid", request.getPid());
            Map out=iTaskBusinessService.listTaskByPid(in);
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

    /**
     * 查询一个私有任务的所有父任务的标题，返回一个面包屑导航组
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTaskBreadcrumb")
    public Response listTaskBreadcrumb(@RequestBody TaskRequest request,
                                       HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("taskId", request.getTaskId());
            Map out=iTaskBusinessService.listTaskBreadcrumb(in);
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
