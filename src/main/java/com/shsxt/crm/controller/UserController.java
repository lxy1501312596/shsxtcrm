package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.util.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @GetMapping("user/queryUserId")
    @ResponseBody
    public User queryUserId(Integer userId){
       return   userService.selectByPrimaryKey(userId);
    }

    @RequestMapping("user/login")
    @ResponseBody
    public  ResultInfo login(String userName,String userPwd){
            UserModel userModel = userService.login(userName,userPwd);
            return  success("用户登录成功",userModel);
    }

    @PostMapping("user/updatePassword")
    @ResponseBody
    public  ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword){
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request),oldPassword,newPassword,confirmPassword);
        return  success("密码更新成功");
    }

    @RequestMapping("user/index")
    public  String  index(){
        return  "user";
    }

    @RequestMapping("user/list")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
       return   userService.queryByParamsForDataGrid(userQuery);
    }

    @RequestMapping("user/save")
    @ResponseBody
    public  ResultInfo saveUser(User user){
        user.getRoleIds().forEach(roleId->{
            System.out.println(roleId);
        });
        userService.saveUser(user);
        return  success("用户添加成功");
    }


    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
     return  success("用户更新成功");
    }

    @RequestMapping("user/delete")
    @ResponseBody
    public  ResultInfo deleteUser(Integer userId){
        userService.deleteUser(userId);
        return  success("用户删除成功");
    }

    @RequestMapping("user/queryAllCustomerManager")
    @ResponseBody
    public List<Map<String,Object>>  queryAllCustomerManager(){
       return userService.queryAllCustomerManager();
    }
}
