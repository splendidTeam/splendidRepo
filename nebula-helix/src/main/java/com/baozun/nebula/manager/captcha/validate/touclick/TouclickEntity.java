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

import java.io.Serializable;

import com.feilong.tools.jsonlib.SensitiveWords;

/**
 * The Class TouclickEntity.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class TouclickEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5377542804894645900L;

    /** 二次验证地址，二级域名. */
    private String            checkAddress;

    /** session id. */
    private String            sid;

    /** 二次验证口令，单次有效. */
    private String            token;

    //-----------------------------------
    /** 自定义的数据,用于服务器端二次校验,如对数据安全有很高要求的时候(比如防黄牛/机器人),可以设置值,配合自定义的 {@link TouclickCustomerValidator} 使用. */
    private Object            customerData;

    //---------------------------------------

    /**
     * 公钥.
     */
    @SensitiveWords
    private String            pubKey;

    /** 私钥. */
    @SensitiveWords
    private String            priKey;

    /**
     * Instantiates a new touclick entity.
     */
    //---------------------------------------
    public TouclickEntity(){
    }

    /**
     * Instantiates a new touclick entity.
     *
     * @param pubKey
     *            the pub key
     * @param priKey
     *            the pri key
     */
    public TouclickEntity(String pubKey, String priKey){
        super();
        this.pubKey = pubKey;
        this.priKey = priKey;
    }

    /**
     * 获得 二次验证地址，二级域名.
     *
     * @return the checkAddress
     */
    public String getCheckAddress(){
        return checkAddress;
    }

    /**
     * 设置 二次验证地址，二级域名.
     *
     * @param checkAddress
     *            the checkAddress to set
     */
    public void setCheckAddress(String checkAddress){
        this.checkAddress = checkAddress;
    }

    /**
     * 获得 session id.
     *
     * @return the sid
     */
    public String getSid(){
        return sid;
    }

    /**
     * 设置 session id.
     *
     * @param sid
     *            the sid to set
     */
    public void setSid(String sid){
        this.sid = sid;
    }

    /**
     * 获得 二次验证口令，单次有效.
     *
     * @return the token
     */
    public String getToken(){
        return token;
    }

    /**
     * 设置 二次验证口令，单次有效.
     *
     * @param token
     *            the token to set
     */
    public void setToken(String token){
        this.token = token;
    }

    /**
     * 获得 公钥.
     *
     * @return the pubKey
     */
    public String getPubKey(){
        return pubKey;
    }

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
     * 获得 私钥.
     *
     * @return the priKey
     */
    public String getPriKey(){
        return priKey;
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

    /**
     * 自定义的数据,用于服务器端二次校验,如对数据安全有很高要求的时候(比如防黄牛/机器人),可以设置值,配合自定义的 {@link TouclickCustomerValidator} 使用.
     *
     * @return the customerData
     */
    public Object getCustomerData(){
        return customerData;
    }

    /**
     * 自定义的数据,用于服务器端二次校验,如对数据安全有很高要求的时候(比如防黄牛/机器人),可以设置值,配合自定义的 {@link TouclickCustomerValidator} 使用.
     *
     * @param customerData
     *            the customerData to set
     */
    public void setCustomerData(Object customerData){
        this.customerData = customerData;
    }
}