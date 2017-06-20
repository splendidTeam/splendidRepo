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
package com.baozun.nebula.manager.captcha.validate.touclick;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.manager.captcha.CaptchaValidate;
import com.feilong.tools.jsonlib.JsonUtil;
import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.model.Status;
import com.touclick.captcha.service.TouClick;

/**
 * The Class TouclickValidate.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 * @deprecated 请使用 feilong-captch ,进行了框架的升级
 */
@Deprecated
public class TouclickCaptchaValidate implements CaptchaValidate{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER   = LoggerFactory.getLogger(TouclickCaptchaValidate.class);

    /** The Constant TOUCLICK. */
    private static final TouClick TOUCLICK = new TouClick();

    /**
     * Validate.
     *
     * @param touclickEntity
     *            the touclick entity
     * @param touclickCustomerValidator
     *            the touclick customer validator
     * @return true, if successful
     */
    public boolean validate(TouclickEntity touclickEntity,TouclickCustomerValidator touclickCustomerValidator){
        Validate.notNull(touclickEntity, "touclickEntity can't be null!");

        String checkAddress = touclickEntity.getCheckAddress();
        Validate.notEmpty(checkAddress, "checkAddress can't be null/empty!");

        String token = touclickEntity.getToken();
        Validate.notEmpty(token, "token can't be null/empty!");

        String sid = touclickEntity.getSid();
        Validate.notEmpty(sid, "sid can't be null/empty!");

        //-------------------------------------------------------------------------------------

        String pubKey = touclickEntity.getPubKey();
        Validate.notEmpty(pubKey, "pubKey can't be null/empty!");

        String priKey = touclickEntity.getPriKey();
        Validate.notEmpty(priKey, "priKey can't be null/empty!");

        //-------------------------------------------------------------------------------------
        try{
            Status touclickStatus = TOUCLICK.check(checkAddress, sid, token, pubKey, priKey);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("touclickEntity:{},touclickStatus :{}", JsonUtil.format(touclickEntity), JsonUtil.format(touclickStatus));
            }

            boolean codeValidate = 0 == touclickStatus.getCode();

            return null == touclickCustomerValidator ? //
                            codeValidate : //
                            touclickCustomerValidator.validator(touclickStatus, touclickEntity.getCustomerData());

        }catch (TouclickException e){
            LOGGER.error("", e);
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.captcha.manager.captcha.CaptchaValidate#validate(java.lang.String, java.lang.String,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean validate(String captchaId,String userInput,HttpServletRequest request){
        //TODO
        throw new NotImplementedException("validate is not implemented!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.captcha.manager.captcha.CaptchaValidate#validateAjax(java.lang.String, java.lang.String, java.lang.String,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean validateAjax(String captchaId,String userInput,String instanceId,HttpServletRequest request){
        //TODO
        throw new NotImplementedException("validate is not implemented!");
    }
}
