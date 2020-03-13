package com.shsxt.proxy;

import com.shsxt.crm.annotaions.RequirePermission;
import com.shsxt.crm.exceptions.AuthFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import java.util.List;

@Configuration
@Aspect
public class PermissionProxy {

    @Autowired
    private HttpSession httpSession;

    @Around(value = "@annotation(com.shsxt.crm.annotaions.RequirePermission)")
    public  Object around(ProceedingJoinPoint pjp) throws Throwable{
        List<String> permissions=(List<String>) httpSession.getAttribute("permissions");
        if(null==permissions ||permissions.size()==0){
            throw  new AuthFailedException();
        }
        Object result=null;
        MethodSignature methodSignature= (MethodSignature) pjp.getSignature();
        RequirePermission requirePermission=methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        if(!(permissions.contains(requirePermission.code()))){
              throw new  AuthFailedException();
        }
        result=pjp.proceed();
        return  result;
    }
}
