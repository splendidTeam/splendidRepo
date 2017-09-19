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
package com.baozun.nebula.web.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.ValidateException;
import com.feilong.servlet.http.RequestUtil;

/**
 * 这个ExceptionResolver原本设计是供PTS使用的，现在已经被大量复制并散落到各个项目，大家也扩充了很多业务类型的异常
 * 目前存在的问题是，关键的异常信息没有打印，大量的StackTrace信息会占用大量的内存，目前这两个问题已经解决
 * 请各位记住一个原则，被ExceptionResolver捕获的异常信息已经打印出来了，在各自的业务方法中切勿再次打印。
 * @author dianchao.song
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    @Autowired
    private MessageSource messageSource;

    //---------------------------------------------------------------

    @Override
    protected ModelAndView getModelAndView(String viewName, Exception exception, HttpServletRequest request) {
        LOGGER.error("", exception);

        Map<String, Object> exceptionMap = new HashMap<>(3);

        if (exception instanceof BusinessException){
            BusinessException businessException = (BusinessException) exception;
            exception = encode(businessException);
            exceptionMap.put("statusCode", ((BusinessException) exception).getErrorCode());
            exceptionMap.put("message", exception.getMessage());
            exceptionMap.put("linkedException", ((BusinessException) exception).getLinkedException());
        }else if (exception instanceof ValidateException){//如果是通用服务端验证的异常
            ValidateException ve = (ValidateException) exception;
            exceptionMap.put("statusCode", ErrorCodes.PARAMS_ERROR);
            String message = null;
            if (ve.getFullKey() != null){
                Object[] args = ve.getArgs();
                message = messageSource.getMessage(ve.getFullKey(), args, LocaleContextHolder.getLocale());
            }else{
                message = messageSource.getMessage(ve.getFieldNameKey(), null, LocaleContextHolder.getLocale());
                Object[] args = ve.getArgs();
                message += messageSource.getMessage(ve.getErrorTipKey(), args, LocaleContextHolder.getLocale());
            }
            exceptionMap.put("message", message);

        }else if (exception instanceof BindException){
            BindException be = (BindException) exception;
            List<ObjectError> allErrors = be.getAllErrors();
            ObjectError err = allErrors.get(0);
            String msg = err.getDefaultMessage();

            exceptionMap.put("statusCode", ErrorCodes.DATA_BIND_EXCEPTION);
            exceptionMap.put("message", msg);

            /*
             * 移除stacktrace信息
             * Writer writer = new StringWriter();
             * exception.printStackTrace(new PrintWriter(writer));
             * exceptionMap.put("stackTrace", writer.toString());
             */
        }else{
            BusinessException runtimeException = encode(new BusinessException(ErrorCodes.SYSTEM_ERROR));
            exceptionMap.put("statusCode", runtimeException.getErrorCode());
            exceptionMap.put("message", runtimeException.getMessage());

            /*
             *  移除stacktrace信息
             * Writer writer = new StringWriter();
             * exception.printStackTrace(new PrintWriter(writer));
             * exceptionMap.put("stackTrace", writer.toString());
             */
        }

        //---------------------------------------------------------------
        ModelAndView modelAndView = new ModelAndView(viewName);
        if (RequestUtil.isAjaxRequest(request)){
            modelAndView.setView(new MappingJacksonJsonView());
        }
        Map<String, Map<String, Object>> result = new HashMap<>(1);
        result.put("exception", exceptionMap);
        modelAndView.addAllObjects(result);

        return modelAndView;
    }

    private BusinessException encode(BusinessException businessException) {
        String key = ErrorCodes.BUSINESS_EXCEPTION_PREFIX + businessException.getErrorCode();
        int errorCode = businessException.getErrorCode();
        Object[] args = businessException.getArgs();

        String message = messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("errorCode:{},key:{},args:{},message:{}", errorCode, key, args, message);
        }
        BusinessException result = new BusinessException(errorCode, message);
        result.setArgs(args);
        if (businessException.getLinkedException() != null){
            result.setLinkedException(encode(businessException.getLinkedException()));
        }
        return result;
    }
}
