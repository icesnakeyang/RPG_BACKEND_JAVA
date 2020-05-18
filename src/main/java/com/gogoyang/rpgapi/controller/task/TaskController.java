package com.gogoyang.rpgapi.controller.task;

import com.gogoyang.rpgapi.business.task.ITaskBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人任务 Task Api接口
 */
@RestController
@RequestMapping("/rpgapi/task")
public class TaskController {
    private final ITaskBusinessService iTaskBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    public TaskController(ITaskBusinessService iTaskBusinessService,
                          ICommonBusinessService iCommonBusinessService) {
        this.iTaskBusinessService = iTaskBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建任务
     */
    @ResponseBody
    @PostMapping("/createTask")
    public Response createTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("title", request.getTitle());
            in.put("detail", request.getDetail());
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("pid", request.getPid());
            in.put("price", request.getPrice());

            logMap.put("GogoActType", GogoActType.CREATE_TASK);
            logMap.put("token", token);
            memoMap.put("title", request.getTitle());
            iTaskBusinessService.createTask(in);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 创建子任务
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createSubTask")
    public Response createSubTask(@RequestBody TaskRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pid", request.getPid());
            in.put("code", request.getCode());
            in.put("title", request.getTitle());
            in.put("price", request.getPrice());

            logMap.put("GogoActType", GogoActType.CREATE_SUB_TASK);
            logMap.put("token", token);
            memoMap.put("pid", request.getPid());
            memoMap.put("title", request.getTitle());
            iTaskBusinessService.createSubTask(in);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
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
            String token = httpServletRequest.getHeader("token");
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
            String token = httpServletRequest.getHeader("token");
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
    @PostMapping("/listMyTask")
    public Response listMyTasks(@RequestBody TaskRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_MY_TASK);
            Map out = iTaskBusinessService.listTaskByUserId(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
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
    @PostMapping("/updateTask")
    public Response updateTask(@RequestBody TaskRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            in.put("title", request.getTitle());
            in.put("code", request.getCode());
            in.put("detail", request.getDetail());
            in.put("days", request.getDays());
            in.put("price", request.getPrice());

            logMap.put("GogoActType", GogoActType.UPDATE_TASK);
            logMap.put("token", token);
            memoMap.put("title", request.getTitle());
            iTaskBusinessService.updateTask(in);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
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
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("taskId", request.getTaskId());

            logMap.put("GogoActType", GogoActType.DELETE_TASK);
            logMap.put("token", token);
            memoMap.put("taskId", request.getTaskId());
            iTaskBusinessService.deleteTask(in);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 统计子任务的数量
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/countSubTask")
    public Response countSubTask(@RequestBody TaskRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pid", request.getPid());
            Map out = iTaskBusinessService.totalSubTask(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10095);
            }
        }
        return response;
    }

    /**
     * 读取子任务列表
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listSubTask")
    public Response listSubTask(@RequestBody TaskRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("pid", request.getPid());
            Map out = iTaskBusinessService.listTaskByPid(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10095);
            }
        }
        return response;
    }

    /**
     * 查询一个私有任务的所有父任务的标题，返回一个面包屑导航组
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listTaskBreadcrumb")
    public Response listTaskBreadcrumb(@RequestBody TaskRequest request,
                                       HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("taskId", request.getTaskId());
            Map out = iTaskBusinessService.listTaskBreadcrumb(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10095);
            }
        }
        return response;
    }

    /**
     * 用户发布任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/publishNewJob")
    public Response publishNewJob(@RequestBody TaskRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("detail", request.getDetail());
            in.put("price", request.getPrice());
            in.put("taskId", request.getTaskId());
            in.put("title", request.getTitle());
            in.put("detail", request.getDetail());

            logMap.put("GogoActType", GogoActType.PUBLISH_TASK);
            logMap.put("token", token);
            logMap.put("taskId", request.getTaskId());
            logMap.put("title", request.getTitle());
            Map out=iTaskBusinessService.publishNewJob(in);
            logMap.put("result", GogoStatus.SUCCESS);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }
}
