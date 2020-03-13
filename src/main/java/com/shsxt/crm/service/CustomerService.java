package com.shsxt.crm.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CustomerLossMapper;
import com.shsxt.crm.dao.CustomerMapper;
import com.shsxt.crm.dao.CustomerOrderMapper;
import com.shsxt.crm.query.CustomerQuery;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.vo.Customer;
import com.shsxt.crm.vo.CustomerLoss;
import com.shsxt.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerOrderMapper customerOrderMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCustomer(Customer customer){
          checkParmas(customer.getName(),customer.getPhone(),customer.getFr());
          AssertUtil.isTrue(null!=customerMapper.queryCustomerByName(customer.getName()),"该客户已存在");
          customer.setIsValid(1);
          customer.setState(0);
          customer.setCreateDate(new Date());
          customer.setUpdateDate(new Date());
          String khno="KH_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
          customer.setKhno(khno);
          AssertUtil.isTrue(insertSelective(customer)<1,"客户添加失败");
    }

    private void checkParmas(String name, String phone, String fr) {
        AssertUtil.isTrue(StringUtils.isBlank(name),"请指定客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号格式非法！");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"请指定公司法人");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public  void updateCustomer(Customer customer){
        AssertUtil.isTrue(null==customer.getId()||null==selectByPrimaryKey(customer.getId()),"待更新记录不存在");
        checkParmas(customer.getName(),customer.getPhone(),customer.getFr());
        Customer temp=customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(null!=temp&&!(temp.getId().equals(customer.getId())),"该客户已存在");
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(customer)<1,"客户更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer cid){
        Customer customer=selectByPrimaryKey(cid);
        AssertUtil.isTrue(null==cid||null==customer,"待删除记录不存在");
        customer.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(customer)<1,"客户删除失败");
    }

    public  void updateCustomerState(){
        List<Customer> lossCutomers=customerMapper.queryLossCustomers();
        if(null !=lossCutomers && lossCutomers.size()>0){
            List<CustomerLoss> customerLosses= new ArrayList<CustomerLoss>();
            List<Integer> lossCusIds=new ArrayList<Integer>();
            lossCutomers.forEach(customer->{
                CustomerLoss customerLoss=new CustomerLoss();
                // 设置最后下单时间
                CustomerOrder lastCustomerOrder = customerOrderMapper.queryLastCustomerOrderByCusId(customer.getId());
                if(null !=lastCustomerOrder){
                    customerLoss.setLastOrderTime(lastCustomerOrder.getOrderDate());
                }
                customerLoss.setCreateDate(new Date());
                customerLoss.setCusManager(customer.getCusManager());
                customerLoss.setCusName(customer.getName());
                customerLoss.setCusNo(customer.getKhno());
                customerLoss.setIsValid(1);
                //  设置客户流失状态为暂缓流失状态
                customerLoss.setState(0);
                customerLoss.setUpdateDate(new Date());
                customerLosses.add(customerLoss);
                lossCusIds.add(customer.getId());
            });
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLosses)<customerLosses.size(),"客户流失数据流转失败!");
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCusIds)<lossCusIds.size(),"客户流失数据流转失败!");
        }
    }

    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        Map<String,Object> result=new HashMap<>();
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getRows());
        List<Map<String,Object>>  list=customerMapper.queryCustomerContributionByParams(customerQuery);
        PageInfo<Map<String,Object>> pageInfo=new PageInfo<>(list);
        result.put("total",pageInfo.getTotal());
        result.put("rows",pageInfo.getList());
        return  result;
    }

    public Map<String, Object> countCustomerMake() {
        Map<String,Object> result=new HashMap<String,Object>();
        List<Map<String,Object>> list =customerMapper.countCustomerMake();
        List<String> data1List=new ArrayList<String>();
        List<Integer> data2List=new ArrayList<Integer>();
        list.forEach(m->{
            data1List.add(m.get("level").toString());
            data2List.add(Integer.parseInt(m.get("total")+""));
        });
        result.put("data1",data1List);
        result.put("data2",data2List);
        return result;
    }
}
