package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.util.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController  extends BaseController {
    @Resource
    private UserService userService;
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    @RequestMapping("main")
    public String  main(HttpServletRequest request){
        Integer id= LoginUserUtil.releaseUserIdFromCookie(request);
        request.setAttribute("user",userService.selectByPrimaryKey(id));
        return "main";
    }
}
