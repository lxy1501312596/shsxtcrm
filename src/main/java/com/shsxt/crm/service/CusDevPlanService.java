package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    public  void   saveCusDevPlan(CusDevPlan cusDevPlan){
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"计划项添加失败");
    }

    public void checkParams(Integer saleChanceId, String planItem, Date planDate){
        AssertUtil.isTrue(null==saleChanceId ||null==saleChanceMapper.selectByPrimaryKey(saleChanceId),"请设置营销机会id" );
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容");
        AssertUtil.isTrue(null==planDate,"请指定项目时间");
    }

    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null==cusDevPlan.getId() || null==selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不存在");
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划更新失败");
    }

    public void deleteCusDevPlan(Integer id){
        CusDevPlan cusDevPlan=selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id ||null==cusDevPlan,"待删除的记录不存在");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划删除失败");
    }

}
