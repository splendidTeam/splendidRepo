/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.ui.Model;

import com.baozun.nebula.event.EventPublisher;

/**
 * BaseController
 * 
 * @author D.C
 * @author feilong
 */
public abstract class BaseController{

    @Resource
    protected ApplicationContext context;

    @Resource
    protected EventPublisher     eventPublisher;
    
    private static final DeviceResolver deviceResolver = new LiteDeviceResolver();
    

    // TODO未实现
    /**
     * 使用RSA非对称加解密 默认使用全局的public key，使用servlet初始化，此处保持空实现 如果安全上要求每个用户使用不同public
     * key时需要商城重写
     * 
     * @param request
     * @param model
     */
    protected void init4SensitiveDataEncryptedByJs(HttpServletRequest request,Model model){
    }

    /**
     * 敏感数据解密过程
     * 
     * @param sensitiveData
     * @return
     */
    protected String decryptSensitiveDataEncryptedByJs(String sensitiveData,HttpServletRequest request){
        String result = sensitiveData;
        // TODO 解密动作
        try{

        }catch (Exception e){} // 解密出错原样使用
        return result;
    }

    /**
     * 获取用户环境上下文，如ip agent等信息，默认是空实现
     * 
     * @param request
     * @param response
     * @return
     */
    protected Map<String, String> getClientContext(HttpServletRequest request,HttpServletResponse response){
        return new HashMap<String, String>();
    }

    /**
     * 基于spring mobile 解析获得 {@link Device},以便区分 {@link Device#isMobile()},{@link Device#isNormal()},{@link Device#isTablet()}.
     * 
     * <p>
     * 目前 spring mobile的默认实现是 {@link LiteDeviceResolver},参见
     * {@link org.springframework.mobile.device.DeviceResolverHandlerInterceptor#DeviceResolverHandlerInterceptor()}
     * </p>
     * 
     * @param request
     *            request
     * @return {@link Device}
     * @since 5.3.0
     * @see Device
     * @see org.springframework.mobile.device.DeviceResolverHandlerInterceptor#DeviceResolverHandlerInterceptor()
     * @see org.springframework.mobile.device.LiteDeviceResolver
     */
    protected Device getDevice(HttpServletRequest request){
        return deviceResolver.resolveDevice(request);
    }

}
