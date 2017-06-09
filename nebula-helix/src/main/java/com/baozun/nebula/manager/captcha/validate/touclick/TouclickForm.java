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

/**
 * 提交表单时自动封装的对象.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class TouclickForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 288232184048495608L;

    /** 二次验证地址，二级域名. */
    private String            checkAddress;

    /** session id. */
    private String            sid;

    /** 二次验证口令，单次有效. */
    private String            token;

    //-------------------------------------------------------------------

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

}
