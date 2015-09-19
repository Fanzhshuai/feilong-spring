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
package com.feilong.spring.context;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.feilong.core.util.Validator;

/**
 * The Class ApplicationContextUtil.
 *
 * @author feilong
 * @version 1.0.8 2014年11月29日 下午7:38:13
 * @since 1.0.8
 */
public final class ApplicationContextUtil{

    /** Don't let anyone instantiate this class. */
    private ApplicationContextUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得 application context for log map.
     *
     * @param applicationContext
     *            the application context
     * @return the application context for log map
     */
    public static Map<String, Object> getApplicationContextForLogMap(ApplicationContext applicationContext){

        if (Validator.isNullOrEmpty(applicationContext)){
            return null;
        }

        Map<String, Object> applicationContextForLogMap = new LinkedHashMap<String, Object>();

        applicationContextForLogMap.put("applicationContext.getBeanDefinitionCount()", applicationContext.getBeanDefinitionCount());

        applicationContextForLogMap.put("applicationContext.getApplicationName()", applicationContext.getApplicationName());
        applicationContextForLogMap.put("applicationContext.getDisplayName()", applicationContext.getDisplayName());

        applicationContextForLogMap.put("applicationContext.getClass()", applicationContext.getClass());

        applicationContextForLogMap.put("applicationContext.getId()", applicationContext.getId());
        applicationContextForLogMap.put("applicationContext.getStartupDate()", applicationContext.getStartupDate());

        applicationContextForLogMap.put("ApplicationContext.CLASSPATH_ALL_URL_PREFIX", ApplicationContext.CLASSPATH_ALL_URL_PREFIX);
        applicationContextForLogMap.put("ApplicationContext.CLASSPATH_URL_PREFIX", ApplicationContext.CLASSPATH_URL_PREFIX);
        applicationContextForLogMap.put("ApplicationContext.FACTORY_BEAN_PREFIX", ApplicationContext.FACTORY_BEAN_PREFIX);
        applicationContextForLogMap.put(
                        "applicationContext.getParent() info",
                        getApplicationContextForLogMap(applicationContext.getParent()));

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanDefinitionNames);

        applicationContextForLogMap.put("applicationContext.getBeanDefinitionNames()", beanDefinitionNames);

        Map<String, Object> beanDefinitionNamesAndClassMap = new TreeMap<String, Object>();
        for (String beanDefinitionName : beanDefinitionNames){
            try{
                Object bean = applicationContext.getBean(beanDefinitionName);
                String canonicalName = bean.getClass().getCanonicalName();
                Object vObject = canonicalName
                                + (applicationContext.isPrototype(beanDefinitionName) ? "[Prototype]" : (applicationContext
                                                .isSingleton(beanDefinitionName) ? "[Singleton]" : ""));
                beanDefinitionNamesAndClassMap.put(beanDefinitionName, vObject);
            }catch (BeansException e){
                beanDefinitionNamesAndClassMap.put(beanDefinitionName, e.getMessage());
            }
        }

        applicationContextForLogMap.put("beanDefinitionNamesAndClassMap", beanDefinitionNamesAndClassMap);

        Environment environment = applicationContext.getEnvironment();
        applicationContextForLogMap.put("applicationContext.getEnvironment()", environment);

        return applicationContextForLogMap;
    }
}
