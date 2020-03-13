package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CustomerMapper;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.enums.CustomerServeStatus;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerSevceService extends BaseService<CustomerServe,Integer> {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private UserMapper userMapper;

    public void saveOrUpdateCustomerServe(CustomerServe customerServe){
        if(null==customerServe.getId()){
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()),"请指定客户");
            AssertUtil.isTrue(null==customerMapper.queryCustomerByName(customerServe.getCustomer()),"当前客户暂不存在");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()),"请指定服务类型");
            customerServe.setIsValid(1);
            customerServe.setCreateDate(new Date());
            customerServe.setUpdateDate(new Date());
            customerServe.setState(CustomerServeStatus.CREATED.getState());
            AssertUtil.isTrue(insertSelective(customerServe)<1,"服务记录添加失败");
        }else {
            CustomerServe temp = selectByPrimaryKey(customerServe.getId());
            AssertUtil.isTrue(null==temp,"待处理的服务记录不存在");
            if (customerServe.getState().equals(CustomerServeStatus.ASSIGNED.getState())){
                AssertUtil.isTrue(StringUtils.isBlank(customerServe.getAssigner())||(null==userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner()))),"待分配用户不存在");
                customerServe.setAssignTime(new Date());
                customerServe.setUpdateDate(new Date());
                AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe)<1,"服务分配失败");
            }if(customerServe.getState().equals(CustomerServeStatus.PROCED.getState())){
                AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()),"请指定处理内容");
                customerServe.setServiceProceTime(new Date());
                customerServe.setUpdateDate(new Date());
                AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe)<1,"服务处理失败");
            }if(customerServe.getState().equals(CustomerServeStatus.FEED_BACK.getState())){
                AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()),"请指定反馈内容");
                AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()),"请指定反馈满意度");
                customerServe.setUpdateDate(new Date());
                customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
                AssertUtil.isTrue(updateByPrimaryKeySelective(customerServe)<1,"服务反馈失败");
            }
        }

    }
}
