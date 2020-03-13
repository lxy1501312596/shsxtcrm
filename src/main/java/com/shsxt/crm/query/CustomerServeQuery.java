package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

public class CustomerServeQuery extends BaseQuery {
    private  String state;
    private  String  customer;
    private  String type;

    private  Integer assigner;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAssigner() {
        return assigner;
    }

    public void setAssigner(Integer assigner) {
        this.assigner = assigner;
    }
}
