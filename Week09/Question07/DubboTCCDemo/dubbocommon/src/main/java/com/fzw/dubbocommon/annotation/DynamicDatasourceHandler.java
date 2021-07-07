package com.fzw.dubbocommon.annotation;

import com.fzw.dubbocommon.config.DynamicRoutingDatasource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author fzw
 * @description
 * @date 2021-07-07
 **/
@Slf4j
@Aspect
@Component
public class DynamicDatasourceHandler implements Ordered {

    @Before(value = "@annotation(com.fzw.dubbocommon.annotation.DynamicDatasource)")
    public void datasourceInject(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        DynamicDatasource dynamicDatasource = method.getAnnotation(DynamicDatasource.class);
        String value = dynamicDatasource.value();
        log.info("datasourceInject: {}", value);
        DynamicRoutingDatasource.setKeyContextContent(value);
    }

    @AfterReturning(value = "@annotation(com.fzw.dubbocommon.annotation.DynamicDatasource)")
    public void datasourceClear(JoinPoint point) throws Throwable {
        log.info("datasourceClear");
        DynamicRoutingDatasource.clearKeyContextContent();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 3;
    }
}
