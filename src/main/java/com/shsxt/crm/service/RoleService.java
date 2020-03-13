package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dao.RoleMapper;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.Role;
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
public class RoleService  extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String,Object>> queryAllRoles(){
        return  roleMapper.queryAllRoles();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public  void  saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        Role temp=roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null!=temp,"该角色已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"角色记录添加失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        Role temp=roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(role.getId())),"该角色已存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败");
    }


    public void deleteRole(Integer roleId){
        Role temp=selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待删除的记录不存在");
        temp.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"角色记录删除失败");
    }

    public void addGrant(Integer[] mids,Integer roleId){
         Role temp=selectByPrimaryKey(roleId);
         AssertUtil.isTrue(null==roleId||null==temp,"待授权的角色不存在");
         int count=permissionMapper.countPermissionByRoleId(roleId);
         if(count>0){
             AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败");
         }
         if(null!=mids && mids.length>0){
             List<Permission> permissions=new ArrayList<>();
             for(Integer mid:mids) {
                 Permission permission = new Permission();
                 permission.setCreateDate(new Date());
                 permission.setUpdateDate(new Date());
                 permission.setModuleId(mid);
                 permission.setRoleId(roleId);
                 permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                 permissions.add(permission);
             }
             permissionMapper.insertBatch(permissions);
         }
    }
}
