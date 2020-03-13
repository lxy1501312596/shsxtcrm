package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.ModuleService;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.util.LoginUserUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.jnlp.PersistenceService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController  extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService persistenceService;
    @Resource
    private ModuleService moduleService;
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    @RequestMapping("main")
    public String  main(HttpServletRequest request){
        Integer id= LoginUserUtil.releaseUserIdFromCookie(request);
        List<String> permissions=persistenceService.queryUserHasRolesHasPermissions(id);
        request.getSession().setAttribute("permissions",permissions);
        request.getSession().setAttribute("modules",moduleService.queryUserHasRoleHasModuleDtos(id));
        request.setAttribute("user",userService.selectByPrimaryKey(id));
        return "main_2.0";
    }
}
