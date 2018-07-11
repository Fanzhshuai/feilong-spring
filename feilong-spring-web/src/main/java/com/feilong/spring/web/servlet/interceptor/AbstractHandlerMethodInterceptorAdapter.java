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
package com.feilong.spring.web.servlet.interceptor;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.servlet.http.RequestUtil.getRequestFullURL;
import static com.feilong.tools.slf4j.Slf4jUtil.format;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.spring.BeanLogMessageBuilder;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 所有 HandlerMethodInterceptor 的父类.
 * 
 * <h3>作用以及说明:</h3>
 * <blockquote>
 * <ol>
 * <li>必须是 HandlerMethod 方法,才能进入相关方法;<br>
 * 如果不是 HandlerMethod,将会以 warn级别日志输出</li>
 * <li>提供了记录方法耗时记录日志功能</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see HandlerInterceptorAdapter
 * @see org.springframework.web.servlet.handler.MappedInterceptor
 * @see com.feilong.spring.web.event.builder.MappedInterceptorBeanToMapBuilder
 * @since 1.10.4
 */
public abstract class AbstractHandlerMethodInterceptorAdapter extends HandlerInterceptorAdapter implements Ordered{

    /** The Constant log. */
    private static final Logger LOGGER                                        = LoggerFactory
                    .getLogger(AbstractHandlerMethodInterceptorAdapter.class);

    //---------------------------------------------------------------

    /**
     * The Constant METHOD_NAME_PRE_HANDLE.
     * 
     * @since 1.12.7
     */
    private static final String METHOD_NAME_PRE_HANDLE                        = "doPreHandle";

    /**
     * The Constant METHOD_NAME_POST_HANDLE.
     * 
     * @since 1.12.7
     */
    private static final String METHOD_NAME_POST_HANDLE                       = "doPostHandle";

    /**
     * The Constant METHOD_NAME_AFTER_COMPLETION.
     * 
     * @since 1.12.7
     */
    private static final String METHOD_NAME_AFTER_COMPLETION                  = "doAfterCompletion";

    /**
     * The Constant METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED.
     * 
     * @since 1.12.7
     */
    private static final String METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED = "doAfterConcurrentHandlingStarted";
    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(BeanLogMessageBuilder.buildFieldsSimpleMessage(this));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return true;//容错
        }
        //---------------------------------------------------------------
        Date beginDate = new Date();

        logBegin(request, METHOD_NAME_PRE_HANDLE);

        boolean doPreHandle = doPreHandle(request, response, (HandlerMethod) handler);

        //---------------------------------------------------------------

        logEnd(request, METHOD_NAME_PRE_HANDLE, beginDate);

        return doPreHandle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }
        //---------------------------------------------------------------
        Date beginDate = new Date();
        logBegin(request, METHOD_NAME_POST_HANDLE);

        doPostHandle(request, response, (HandlerMethod) handler, modelAndView);

        logEnd(request, METHOD_NAME_POST_HANDLE, beginDate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }

        //---------------------------------------------------------------
        Date beginDate = new Date();

        logBegin(request, METHOD_NAME_AFTER_COMPLETION);

        doAfterCompletion(request, response, (HandlerMethod) handler, ex);

        logEnd(request, METHOD_NAME_AFTER_COMPLETION, beginDate);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }

        //---------------------------------------------------------------
        Date beginDate = new Date();

        logBegin(request, METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED);

        doAfterConcurrentHandlingStarted(request, response, (HandlerMethod) handler);

        logEnd(request, METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED, beginDate);
    }

    //---------------------------------------------------------------

    /**
     * Log no handler method.
     *
     * @param request
     *            the request
     * @param handler
     *            the handler
     */
    private static void logNoHandlerMethod(HttpServletRequest request,Object handler){
        if (LOGGER.isWarnEnabled()){
            String message = "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,";
            LOGGER.warn(message, getRequestFullURL(request, UTF8), handler.getClass().getName());
        }
    }

    //---------------------------------------------------------------

    /**
     * Log begin.
     *
     * @param request
     *            the request
     * @param methodName
     *            the method name
     * @since 1.12.6
     */
    private void logBegin(HttpServletRequest request,String methodName){
        if (METHOD_NAME_AFTER_COMPLETION.equals(methodName) || METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED.equals(methodName)){
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace(buildBeginMessage(request, methodName));
            }
            return;
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(buildBeginMessage(request, methodName));
        }
    }

    //---------------------------------------------------------------

    /**
     * Log end.
     *
     * @param request
     *            the request
     * @param methodName
     *            the method name
     * @param beginDate
     *            the begin date
     * @since 1.12.6
     */
    private void logEnd(HttpServletRequest request,String methodName,Date beginDate){
        if (METHOD_NAME_AFTER_COMPLETION.equals(methodName) || METHOD_NAME_AFTER_CONCURRENT_HANDLING_STARTED.equals(methodName)){
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace(buildEndMessage(request, methodName, beginDate));
            }
            return;
        }

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(buildEndMessage(request, methodName, beginDate));
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the begin message.
     *
     * @param request
     *            the request
     * @param methodName
     *            the method name
     * @return the string
     * @since 1.12.7
     */
    private String buildBeginMessage(HttpServletRequest request,String methodName){
        return Slf4jUtil.format("will [{}.{}],[{}]", getClass().getSimpleName(), methodName, getRequestFullURL(request, UTF8));
    }

    //---------------------------------------------------------------

    /**
     * Builds the end message.
     *
     * @param request
     *            the request
     * @param methodName
     *            the method name
     * @param beginDate
     *            the begin date
     * @return the string
     * @since 1.12.7
     */
    private String buildEndMessage(HttpServletRequest request,String methodName,Date beginDate){
        String pattern = "end [{}.{}],use time: [{}],[{}]";
        return format(pattern, getClass().getSimpleName(), methodName, formatDuration(beginDate), getRequestFullURL(request, UTF8));
    }

    //---------------------------------------------------------------

    /**
     * Do pre handle.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @return true, if successful
     */
    @SuppressWarnings({ "static-method", "unused" })
    public boolean doPreHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
        return true;
    }

    /**
     * Do post handle.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param modelAndView
     *            the model and view
     */
    @SuppressWarnings("unused")
    public void doPostHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,ModelAndView modelAndView){
    }

    /**
     * Do after completion.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param ex
     *            the ex
     */
    @SuppressWarnings("unused")
    public void doAfterCompletion(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,Exception ex){
    }

    /**
     * Do after concurrent handling started.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     */
    @SuppressWarnings("unused")
    public void doAfterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder(){
        return 0;
    }

}