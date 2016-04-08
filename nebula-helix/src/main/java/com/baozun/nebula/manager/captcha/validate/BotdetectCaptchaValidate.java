/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.manager.captcha.validate;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.manager.captcha.CaptchaValidate;

import botdetect.web.Captcha;

/**
 * Botdetect Captcha 校验.
 * 
 * <h3>需要使用以下jar:</h3>
 * <blockquote>
 * 
 * <pre>
 * {@code
 *         <dependency>
 *             <groupId>com.captcha</groupId>
 *             <artifactId>botdetect</artifactId>
 *             <version>4.0.baozun.RELEASE</version>
 *         </dependency>
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * <h3>web.xml:</h3>
 * <blockquote>
 * 
 * <pre>
{@code
    <servlet>
        <servlet-name>BotDetect Captcha</servlet-name>
        <servlet-class>botdetect.web.http.CaptchaServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>BotDetect Captcha</servlet-name>
        <url-pattern>/botdetectcaptcha</url-pattern>
    </servlet-mapping>
    <context-param>
        <param-name>LBD_helpLinkMode</param-name>
        <param-value>image</param-value>
    </context-param>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * <h3>JSP使用:</h3>
 * <blockquote>
 * 
 * <pre>
{@code
    <%@ taglib prefix="botDetect" uri="botDetect"%>
    
    <botDetect:captcha 
    id="captcha_pc_login" 
    imageWidth="80" 
    imageHeight="26" 
    codeLength="4" 
    imageStyles="Halo,CrossShadow,Lego" 
    soundEnabled="false"
    reloadEnabled="false" />
}

具体属性参数,可以参见官方文档
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>about Botdetect</h3>
 * 
 * <blockquote>
 * <p>
 * see {@link <a href="https://captcha.com/">Botdetect</a>}
 * </p>
 * 
 * <p>
 * BotDetect™ CAPTCHA generator is a form security solution using <b>Captcha challenges</b>,<br>
 * that are easy for humans but hard for bots, to <b>prevent automated page posting</b>.
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.5.3 2016年3月28日 下午5:12:09
 * @see <a href="https://captcha.com/">Botdetect</a>
 * @see <a href="https://captcha.com/captcha-examples.html">captcha-examples.html</a>
 * @see <a href="https://captcha.com/doc/java/jsp-captcha-quickstart.html">jsp-captcha-quickstart</a>
 * @see <a href="https://captcha.com/doc/java/examples/jsp-captcha-tag-example.html">jsp-captcha-tag-example</a>
 * @see <a href="https://captcha.com/doc/java/api/captcha-web-reference.html">captcha-web-reference</a>
 * @since 1.5.3
 */
public class BotdetectCaptchaValidate implements CaptchaValidate{

    /* (non-Javadoc)
     * @see com.baozun.nebula.manager.captcha.CaptchaValidate#validate(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean validate(String captchaId,String userInput,HttpServletRequest request){
        Captcha captcha = Captcha.load(request, captchaId);
        return captcha.validate(request, userInput);
    }

    /* (non-Javadoc)
     * @see com.baozun.nebula.manager.captcha.CaptchaValidate#validateAjax(java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean validateAjax(String captchaId,String userInput,String instanceId,HttpServletRequest request){
        Captcha captcha = Captcha.load(request, captchaId);
        return captcha.validateAjax(userInput, instanceId);
    }
}
