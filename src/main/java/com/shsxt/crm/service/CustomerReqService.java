package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CustomerRepMapper;
import com.shsxt.crm.vo.CustomerRep;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomerReqService extends BaseService<CustomerRep,Integer> {
    @Resource
    private CustomerRepMapper customerRepMapper;

    public void saveCustomerReq(CustomerRep customerRep){

    }
}
