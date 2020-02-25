package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.util.Md5Util;
import com.shsxt.crm.util.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private  UserMapper userMapper;

    public UserModel login(String username,String userpwd){
        checkLoginParams(username,userpwd);
        User user= userMapper.queryUserByUsername(username);
        AssertUtil.isTrue(null==user,"用户不存在");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userpwd))),"密码不正确");
        return  buildUserModel(user);
    }
    public UserModel buildUserModel(User user){
        return  new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
    }

    public void checkLoginParams(String username,String userpwd){
        AssertUtil.isTrue(StringUtils.isBlank(username),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userpwd),"密码不能为空");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldpassword,String newpassword,String configpasswrod){
        checkParams(userId,oldpassword,newpassword,configpasswrod);
        User user=selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newpassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"密码更新失败");
    }

    public void checkParams(Integer userId,String oldpassword,String newpassword,String configpasswrod){
        User user=selectByPrimaryKey(userId);
        AssertUtil.isTrue(null==userId ||null==user,"用户未登录或不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldpassword),"请输入原始密码!");
        AssertUtil.isTrue(StringUtils.isBlank(newpassword),"请输入新密码!");
        AssertUtil.isTrue(StringUtils.isBlank(configpasswrod),"请输入确认密码!");
        AssertUtil.isTrue(!(newpassword.equals(configpasswrod)),"新密码输入不一致!");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldpassword))),"原始密码不正确!");
        AssertUtil.isTrue(oldpassword.equals(newpassword),"新密码不能与旧密码相同!");
    }
}
