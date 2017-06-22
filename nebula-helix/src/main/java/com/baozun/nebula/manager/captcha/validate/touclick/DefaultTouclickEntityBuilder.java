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

import org.apache.commons.lang3.Validate;

import com.feilong.core.bean.PropertyUtil;

/**
 * 默认的
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class DefaultTouclickEntityBuilder implements TouclickEntityBuilder{

    /** 私钥. */
    private String priKey;

    /**
     * 公钥.
     */
    private String pubKey;

    //------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.captcha.manager.captcha.validate.touclick.TouclickEntityBuilder#build(com.feilong.captcha.manager.captcha.validate.
     * touclick.TouclickForm, java.lang.Object)
     */
    @Override
    public TouclickEntity build(TouclickForm touclickForm,Object customerData){
        Validate.notNull(touclickForm, "touclickForm can't be null!");
        Validate.notBlank(touclickForm.getCheckAddress(), "touclickForm checkAddress can't be blank!");
        Validate.notBlank(touclickForm.getSid(), "touclickForm sid can't be blank!");
        Validate.notBlank(touclickForm.getToken(), "touclickForm token can't be blank!");

        //---------------------------------------------------------------------
        TouclickEntity touclickEntity = new TouclickEntity(priKey, pubKey);

        PropertyUtil.copyProperties(touclickEntity, touclickForm);

        touclickEntity.setCustomerData(customerData);
        return touclickEntity;
    }

    //------------------------------------------------------------------------------

    /**
     * 设置 公钥.
     *
     * @param pubKey
     *            the pubKey to set
     */
    public void setPubKey(String pubKey){
        this.pubKey = pubKey;
    }

    /**
     * 设置 私钥.
     *
     * @param priKey
     *            the priKey to set
     */
    public void setPriKey(String priKey){
        this.priKey = priKey;
    }

}
