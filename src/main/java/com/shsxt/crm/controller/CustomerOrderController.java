package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.query.CustomerOrderQuery;
import com.shsxt.crm.service.CustomerOrderSevice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderSevice customeOrderSevice;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrdersByParams(CustomerOrderQuery customerOrderQuery){
        return   customeOrderSevice.queryByParamsForDataGrid(customerOrderQuery);
    }

    @RequestMapping("queryOrderDetailByOrderId")
    @ResponseBody
    public Map<String,Object> queryOrderDetailByOrderId(Integer orderId){
        return  customeOrderSevice.queryOrderDetailByOrderId(orderId);
    }
}

