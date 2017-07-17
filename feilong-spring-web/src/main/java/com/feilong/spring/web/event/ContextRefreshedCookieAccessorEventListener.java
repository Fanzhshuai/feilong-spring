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
package com.feilong.spring.web.event;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.util.SortUtil.sortListByPropertyNamesValue;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.servlet.http.entity.CookieEntity;
import com.feilong.spring.event.AbstractContextRefreshedEventListener;

/**
 * The listener interface for receiving contextStartedLogging events.
 * The class that is interested in processing a contextStartedLogging
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addContextStartedLoggingListener</code> method. When
 * the contextStartedLogging event occurs, that object's appropriate
 * method is invoked.
 * 
 * <p>
 * 
 * 只有一个ApplicationContextEvent,表示ApplicationContext容器事件,且其又有如下实现：
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * <th align="left">备注</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextStartedEvent</td>
 * <td>ApplicationContext启动后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextStoppedEvent</td>
 * <td>ApplicationContext停止后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextRefreshedEvent</td>
 * <td>ApplicationContext初始化或刷新完成后触发的事件</td>
 * <td>容器初始化完成后调用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextClosedEvent</td>
 * <td>ApplicationContext关闭后触发的事件；</td>
 * <td>（如web容器关闭时自动会触发spring容器的关闭,如果是普通java应用,需要调用ctx.registerShutdownHook();注册虚拟机关闭时的钩子才行）</td>
 * </tr>
 * 
 * </table>
 * 注: {@link org.springframework.context.support.AbstractApplicationContext}
 * 抽象类实现了LifeCycle的start和stop回调并发布ContextStartedEvent和ContextStoppedEvent事件；但是无任何实现调用它,所以目前无任何作用。
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public class ContextRefreshedCookieAccessorEventListener extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedCookieAccessorEventListener.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (!LOGGER.isInfoEnabled()){
            return;
        }

        Map<String, CookieAccessor> beanNameAndCookieAccessorMap = buildHandlerMethods(contextRefreshedEvent.getApplicationContext());

        if (isNullOrEmpty(beanNameAndCookieAccessorMap)){
            LOGGER.info("can not find CookieAccessor bean");
            return;
        }

        //---------------------------------------------------------------

        List<Map<String, Object>> list = buildList(beanNameAndCookieAccessorMap);

        LOGGER.info("Cookie Accessor size:[{}], Info:{}", list.size(), formatToSimpleTable(sortListByPropertyNamesValue(list, "name")));
    }

    //---------------------------------------------------------------

    /**
     * Builds the list.
     *
     * @param handlerMethods
     *            the handler methods
     * @return the list
     */
    private List<Map<String, Object>> buildList(Map<String, CookieAccessor> handlerMethods){
        List<Map<String, Object>> list = new ArrayList<>();

        for (Map.Entry<String, CookieAccessor> entry : handlerMethods.entrySet()){
            String key = entry.getKey();
            CookieAccessor cookieAccessor = entry.getValue();

            CookieEntity cookieEntity = cookieAccessor.getCookieEntity();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("beanName", key);
            map.put("name", cookieEntity.getName());
            map.put("httpOnly", cookieEntity.getHttpOnly());
            map.put("path", cookieEntity.getPath());

            int maxAge = cookieEntity.getMaxAge();

            map.put("maxAge", toShowMaxAge(maxAge));

            map.put("domain", cookieEntity.getDomain());
            map.put("secure", cookieEntity.getSecure());
            map.put("version", cookieEntity.getVersion());
            map.put("isValueEncoding", cookieAccessor.getIsValueEncoding());

            list.add(map);
        }
        return list;
    }

    /**
     * To show max age.
     *
     * @param maxAge
     *            单位秒
     * @return the string
     */
    private String toShowMaxAge(int maxAge){
        if (maxAge <= 0){
            return String.valueOf(maxAge);
        }
        return DateExtensionUtil.formatDuration(toLong(maxAge) * 1000);
    }

    /**
     * Builds the handler methods.
     *
     * @param applicationContext
     *            the application context
     * @return 如果取不到 <code>RequestMappingHandlerMapping</code>,返回 {@link Collections#emptyMap()}<br>
     * @throws BeansException
     *             the beans exception
     */
    private static Map<String, CookieAccessor> buildHandlerMethods(ApplicationContext applicationContext){
        Map<String, CookieAccessor> beansOfType = applicationContext.getBeansOfType(CookieAccessor.class, true, true);

        if (null == beansOfType){
            return emptyMap();
        }

        return beansOfType;
    }

    //---------------------------------------------------------------

}