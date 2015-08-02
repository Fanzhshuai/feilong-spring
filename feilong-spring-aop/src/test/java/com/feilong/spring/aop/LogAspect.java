/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.spring.aop;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.lang.StringUtil;

/**
 * 日志切面 aspect.
 * 
 * @author feilong
 * @version 1.0 2012-3-30 上午2:49:12
 */
@Aspect
public class LogAspect extends AbstractAspect{

    // log4j
    /** The LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(LogAspect.class);

    /** The _LOGGER. */
    private Log                 log;

    // && @annotation(com.feilong.core.aop.Log)
    /**
     * Pointcut.
     */
    @Pointcut("execution(* com.feilong.spring.aspects.**.*(..))")
    // @Pointcut("execution(* @annotation(com.feilong.core.aop.Log))")
    private void pointcut(){
    }

    // com.feilong.spring.aspects.UserManager
    /**
     * Around.
     * 
     * @param proceedingJoinPoint
     *            the join point
     * @throws Throwable
     *             the throwable
     */
    @Around(value = "pointcut()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        // LoggerFactory.
        // LOGGER.

        Method method = getMethod(proceedingJoinPoint, Log.class);
        String methodName = method.getName();
        Date begin = new Date();
        // 通过反射执行目标对象的连接点处的方法
        proceedingJoinPoint.proceed();
        // 在来得到方法名吧，就是通知所要织入目标对象中的方法名称
        Date end = new Date();
        Object[] args = proceedingJoinPoint.getArgs();
        // for (Object arg : args){
        // LOGGER.info("arg:{}", arg.toString());
        // }
        // LOGGER.info("{},{}", begin, end);

        log = getAnnotation(proceedingJoinPoint, Log.class);
        String level = log.level();
        // LOGGER.debug("level:{}", level);
        // 输出的日志 怪怪的 02:13:10 INFO (NativeMethodAccessorImpl.java:?) [invoke0()] method:addUser([1018, Jummy]),耗时:0
        // ReflectUtil.invokeMethod(log, level, "method:{}({}),耗时:{}", objects);

        String format = "method:%s(%s),耗时:%s";
        Object[] objects = { methodName, args, DateExtensionUtil.getIntervalForView(begin, end) };

        Object message = StringUtil.format(format, objects);
        LOGGER.log(Level.toLevel(level), message);
    }
}
