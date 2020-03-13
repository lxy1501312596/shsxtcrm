package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

public class ModuleQuery extends BaseQuery {
    private  Integer grade;
    private  String moduleName;
    private  String code;
    private  Integer pId;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }
}
