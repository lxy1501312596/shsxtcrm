package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.util.Md5Util;
import com.shsxt.crm.util.PhoneUtil;
import com.shsxt.crm.util.UserIDBase64;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserService extends BaseService<User,Integer> {


    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel login(String username, String userpwd) {
        checkLoginParams(username, userpwd);
        User user = userMapper.queryUserByUsername(username);
        AssertUtil.isTrue(null == user, "用户不存在");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userpwd))), "密码不正确");
        return buildUserModel(user);
    }

    public UserModel buildUserModel(User user) {
        return new UserModel(UserIDBase64.encoderUserID(user.getId()), user.getUserName(), user.getTrueName());
    }

    public void checkLoginParams(String username, String userpwd) {
        AssertUtil.isTrue(StringUtils.isBlank(username), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userpwd), "密码不能为空");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId, String oldpassword, String newpassword, String configpasswrod) {
        checkParams(userId, oldpassword, newpassword, configpasswrod);
        User user = selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newpassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "密码更新失败");
    }

    public void checkParams(Integer userId, String oldpassword, String newpassword, String configpasswrod) {
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == userId || null == user, "用户未登录或不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldpassword), "请输入原始密码!");
        AssertUtil.isTrue(StringUtils.isBlank(newpassword), "请输入新密码!");
        AssertUtil.isTrue(StringUtils.isBlank(configpasswrod), "请输入确认密码!");
        AssertUtil.isTrue(!(newpassword.equals(configpasswrod)), "新密码输入不一致!");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldpassword))), "原始密码不正确!");
        AssertUtil.isTrue(oldpassword.equals(newpassword), "新密码不能与旧密码相同!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        User temp=userMapper.queryUserByUsername(user.getUserName());
        AssertUtil.isTrue(null!=temp &&(temp.getIsValid()==1),"该用户存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(insertHasKey(user)==null,"用户添加失败");
        int userId=user.getId();
        relaionUserRole(userId,user.getRoleIds());

    }

    public void relaionUserRole(Integer userId, List<Integer> roleIds){
        int count=userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"角色用户分配失败");
        }

        if(null!=roleIds && roleIds.size()>0){
            List<UserRole> userRoles=new ArrayList<>();
            roleIds.forEach(roleId->{
                UserRole userRole=new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            });
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles)<userRoles.size(),"用户角色分配失败");
        }
    }


    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址!");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)), "手机号格式不合法!");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
          AssertUtil.isTrue(null==user.getId()||null==selectByPrimaryKey(user.getId()) ,"待更新记录不存在");
          checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        User temp=userMapper.queryUserByUsername(user.getUserName());
        if(null!=temp &&(temp.getIsValid()==1)){
            AssertUtil.isTrue(!(user.getId().equals(temp.getId())),"该用户已存在");
        }
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户更新失败");
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    public void deleteUser(Integer id){
        User user=selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id||null==user,"待删除记录不存在");
        int count=userRoleMapper.countUserRoleByUserId(id);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(id)!=count,"用户角色分配失败");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"删除失败");
    }


    public List<Map<String, Object>> queryAllCustomerManager() {
        return userMapper.queryAllCustomerManager();
    }
}
