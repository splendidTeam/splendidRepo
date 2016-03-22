/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.interceptor.clientcache;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.baozun.nebula.web.interceptor.AbstractHandlerInterceptorAdapter;
import com.feilong.servlet.http.ResponseUtil;
import com.feilong.servlet.http.entity.HttpHeaders;

/**
 * 用来拦截所有 标识有 {@link ClientCache}的 请求方法.
 * 
 * <h3>作用及原理:</h3>
 * <blockquote>
 * 
 * <ol>
 * <li>如果没有标识{@link ClientCache}，那么自动通过拦截器，不进行任何处理</li>
 * <li>如果标识的{@link ClientCache}，{@link ClientCache#value()} <=0,那么标识不设置缓存，参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}</li>
 * <li>否则，会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头，value值为 {@code "max-age=" + value}
 * </li>
 * </ol>
 * </blockquote>
 * 
 * 
 * <h3>使用方式:</h3>
 * 
 * <blockquote>
 * Example 1:标识请求不需要 浏览器端缓存
 * <p>
 * <code>@ClientCache(0)</code>
 * </p>
 * 
 * Example 2:标识请求需要5分钟 浏览器端缓存
 * <p>
 * <code>@ClientCache(value = TimeInterval.SECONDS_PER_MINUTE * 5)</code>
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月17日 上午12:45:06
 * @see ResponseUtil#setNoCacheHeader(HttpServletResponse)
 * @see javax.servlet.http.HttpServletResponse#setHeader(String, String)
 * @since 1.2.2
 */
public class ClientCacheInterceptor extends AbstractHandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCacheInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            ClientCache clientCache = handlerMethod.getMethodAnnotation(ClientCache.class);

            //如果没有标识{@link ClientCache}，那么自动通过拦截器，不进行任何处理
            if (clientCache != null){
                int value = clientCache.value();

                Method method = handlerMethod.getMethod();

                //如果标识的{@link ClientCache}，{@link ClientCache#value()} <=0,那么标识不设置缓存，参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}
                if (value <= 0){
                    ResponseUtil.setNoCacheHeader(response);

                    LOGGER.debug("[{}.{}()],setNoCacheHeader", method.getDeclaringClass().getSimpleName(), method.getName());
                }
                //否则，会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头，value值为 {@code "max-age=" + value}
                else{
                    String cacheControlValue = "max-age=" + value;
                    response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControlValue);

                    LOGGER.debug(
                                    "[{}.{}()],set response setHeader:[{}],value is :[{}]",
                                    method.getDeclaringClass().getSimpleName(),
                                    method.getName(),
                                    HttpHeaders.CACHE_CONTROL,
                                    cacheControlValue);
                }
            }
        }
    }
}