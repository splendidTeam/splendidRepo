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
package com.baozun.nebula.web.interceptor;

import java.io.Serializable;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class ClientInfo implements Serializable{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** ua. */
    private String userAgent;

    /** 客户端识别码. */
    private String fingerPrint;

    /**
     * 
     */
    public ClientInfo(){
        super();
    }

    /**
     * @param userAgent
     * @param fingerPrint
     */
    public ClientInfo(String userAgent, String fingerPrint){
        super();
        this.userAgent = userAgent;
        this.fingerPrint = fingerPrint;
    }

    /**
     * 获得 ua.
     *
     * @return the userAgent
     */
    public String getUserAgent(){
        return userAgent;
    }

    /**
     * 设置 ua.
     *
     * @param userAgent
     *            the userAgent to set
     */
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }

    /**
     * 获得 客户端识别码.
     *
     * @return the fingerPrint
     */
    public String getFingerPrint(){
        return fingerPrint;
    }

    /**
     * 设置 客户端识别码.
     *
     * @param fingerPrint
     *            the fingerPrint to set
     */
    public void setFingerPrint(String fingerPrint){
        this.fingerPrint = fingerPrint;
    }

}
