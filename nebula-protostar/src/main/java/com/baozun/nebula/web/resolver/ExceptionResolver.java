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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.feilong.servlet.http.RequestUtil;

/**
 * @author dianchao.song
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ModelAndView getModelAndView(String viewName,Exception exception,HttpServletRequest request){

        LOGGER.error("", exception);

        ModelAndView mv = new ModelAndView(viewName);
        Map<String, Object> exceptionMap = new HashMap<String, Object>();
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>(1);
        if (exception instanceof BusinessException){
            BusinessException businessException = (BusinessException) exception;
            exception = encode(businessException);
            exceptionMap.put("statusCode", ((BusinessException) exception).getErrorCode());
            exceptionMap.put("message", exception.getMessage());
            exceptionMap.put("linkedException", ((BusinessException) exception).getLinkedException());
        }else{
            BusinessException runtimeException = encode(new BusinessException(ErrorCodes.SYSTEM_ERROR));
            exceptionMap.put("statusCode", runtimeException.getErrorCode());
            exceptionMap.put("message", runtimeException.getMessage());
            Writer w = new StringWriter();
            exception.printStackTrace(new PrintWriter(w));
            exceptionMap.put("stackTrace", w.toString());

        }
        if (RequestUtil.isAjaxRequest(request)){
            mv.setView(new MappingJacksonJsonView());
        }
        result.put("exception", exceptionMap);
        mv.addAllObjects(result);
        exception.printStackTrace();
        return mv;
    }

    private BusinessException encode(BusinessException businessException){
        String key = ErrorCodes.BUSINESS_EXCEPTION_PREFIX + businessException.getErrorCode();
        int errorCode = businessException.getErrorCode();
        Object[] args = businessException.getArgs();
        String message = messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
        Object[] objects = { errorCode, key, args, message };
        LOGGER.error("errorCode:{},key:{},args:{},message:{}", objects);
        BusinessException result = new BusinessException(errorCode, message);
        result.setArgs(args);
        if (businessException.getLinkedException() != null){
            result.setLinkedException(encode(businessException.getLinkedException()));
        }
        return result;
    }
}
