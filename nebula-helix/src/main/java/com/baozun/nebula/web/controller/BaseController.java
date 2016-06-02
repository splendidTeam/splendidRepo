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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.utilities.common.encryptor.RSAEncryptor;
import com.feilong.spring.mobile.device.DeviceUtil;

/**
 * BaseController
 * 所有控制器的基类，里面有控制类必须的信息。控制类在处理流程的过程中，需要仔细规划哪些行为是在控制器中直接操作，
 * 哪些行为则是在对应时间点或者事件的情况下触发执行，而不是将所有可能通用的部分都紧耦合到基类中来。
 * 目前基类中支持的有：
 * I18n信息的获取，用于控制器返回信息时使用
 * 接收请求的来源判断，用于控制器进行相关的逻辑控制
 * 应用事件发布，请注意未来应用是一个集群类的应用，对于事件的处理必须和集群的特性兼容
 * 
 * @author D.C
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public abstract class BaseController{

    private static final Logger  LOG                  = LoggerFactory.getLogger(BaseController.class);

    @Resource
    protected ApplicationContext context;

    @Resource
    protected EventPublisher     eventPublisher;

    @Autowired
    private MessageSource        messageSource;

    //RSA加密全局的PublicKey
    private static final String  SENSITIVE_PUBLIC_KEY = "sensitivePublicKey";

    /**
     * 获取i18n信息
     * 
     * @param key
     * @return
     */
    protected String getMessage(String key){
        return getMessage(key, new Object[] {});
    }

    /**
     * 获取i18n信息
     * 
     * @param key
     * @param params
     * @return
     */
    protected String getMessage(String key,Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    /**
     * 根据BindingResult获得返回数据对象
     * 
     * @param bindingResult
     * @return
     */
    protected NebulaReturnResult getResultFromBindingResult(BindingResult bindingResult){
        DefaultReturnResult returnResult = new DefaultReturnResult();
        if (bindingResult.hasErrors()){
            returnResult.setResult(false);
            for (ObjectError error : bindingResult.getAllErrors()){
                DefaultResultMessage message = new DefaultResultMessage();
                message.setMessage(error.getCode());
                if (error.getArguments() != null)
                    message.setParams(Arrays.asList(error.getArguments()));
                returnResult.getExtraResultMessages().add(message);
            }
            returnResult.setResultMessage(returnResult.getExtraResultMessages().get(0));
        }
        return returnResult;
    }

    /**
     * 使用RSA非对称加解密 默认使用全局的public key，使用servlet初始化， 如果安全上要求每个用户使用不同public
     * key时需要商城重写
     * 
     * @param request
     * @param model
     * @author 冯明雷
     * @time 2016年3月30日下午4:43:47
     */
    protected void init4SensitiveDataEncryptedByJs(HttpServletRequest request,Model model){
        //默认的js使用的公钥
        model.addAttribute(SENSITIVE_PUBLIC_KEY, new RSAEncryptor().getStrPublicKey());
    }

    /**
     * 敏感数据解密，默认使用全局RSA解密
     * 
     * @param sensitiveData
     * @return
     */
    protected String decryptSensitiveDataEncryptedByJs(String sensitiveData,HttpServletRequest request){
        try{
            return EncryptUtil.getInstance().getEncryptor("RSA").decrypt(sensitiveData);
        }catch (EncryptionException e1){
            LOG.warn("[DECRYPTION_ERROR] [{}]", sensitiveData);
            return sensitiveData;
        }
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
     * 以往的实现方式是通过配置 {@link DeviceResolverHandlerInterceptor}或者 {@link DeviceResolverRequestFilter}或者
     * {@link DeviceHandlerMethodArgumentResolver},<br>
     * 目前的实现方式是:按需加载,当有需要的时候调用该方法
     * </p>
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
     * @see com.feilong.spring.mobile.device.DeviceUtil#getDevice(HttpServletRequest)
     */
    protected Device getDevice(HttpServletRequest request){
        return DeviceUtil.getDevice(request);
    }

}
