package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.util.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    public Map<String,Object>   querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object>  result=new HashMap<String, Object>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        PageInfo<SaleChance> pageInfo=new PageInfo<>(selectByParams(saleChanceQuery));
        result.put("total",pageInfo.getTotal());
        result.put("rows",pageInfo.getList());
        return  result;
    }

    public  void SavesaleChance(SaleChance saleChance){
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getStatus());
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(saleChance)<1,"机会数据添加失败");
    }



    public  void  checkParams(String customerName,String linkMan,String linkPhone){
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系电话!");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号格式不合法!");
    }


    public void updateSaleChance(SaleChance saleChance){
        AssertUtil.isTrue(null==saleChance.getId(),"记录不存在");
        SaleChance temp=selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null==temp,"记录不存在");
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
         if(StringUtils.isBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan())){
             saleChance.setState(StateStatus.STATED.getType());
             saleChance.setAssignTime(new Date());
             saleChance.setDevResult(DevResult.DEVING.getStatus());
         }else if(StringUtils.isNotBlank(temp.getAssignMan())&&StringUtils.isBlank(saleChance.getAssignMan())){
             saleChance.setAssignMan("");
             saleChance.setState(StateStatus.UNSTATE.getType());
             saleChance.setAssignTime(null);
             saleChance.setDevResult(DevResult.UNDEV.getStatus());
         }
         saleChance.setUpdateDate(new Date());
         AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"机会数据更新失败");
    }


    public void deleteSaleChanceByids(Integer[] its){
        AssertUtil.isTrue(null==its||its.length==0,"请选择待删除的机会数据");
        AssertUtil.isTrue(deleteBatch(its)<its.length,"机会数据删除失败");
    }
}
