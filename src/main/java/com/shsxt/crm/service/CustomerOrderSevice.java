package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CustomerOrderMapper;
import com.shsxt.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class CustomerOrderSevice extends BaseService<CustomerOrder,Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    public Map<String, Object> queryOrderDetailByOrderId(Integer orderId) {
       return customerOrderMapper.queryOrderDetailByOrderId(orderId);
    }
}
