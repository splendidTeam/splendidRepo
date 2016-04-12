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
package com.baozun.nebula.manager.captcha.entity;

import java.io.Serializable;

/**
 * The Class CaptchaContainer.
 * 
 * <p>
 * 参见 {@link com.baozun.nebula.web.taglib.CaptchaContainerTag}
 * </p>
 *
 * @author feilong
 * @version 1.5.3 2016年4月7日 下午8:11:54
 * @since 1.5.3
 */
public class CaptchaContainer implements Serializable{

    private static final long serialVersionUID = 8400201075591072027L;

    /** 标识容器功能,不同的验证码 容器id应该不一样. */
    private String            id;

    /**
     * 容错次数.
     * <p>
     * 定义为 <=0 表示无论何时,都需要显示验证码.<br>
     * 1标识表单提交,出错一次(比如用户名密码不匹配) 需要显示.<br>
     * 以此类推
     * </p>
     */
    private int               countThreshold   = 0;

    /**
     * 获得 标识容器功能,不同的验证码 容器id应该不一样.
     *
     * @return the 标识容器功能,不同的验证码 容器id应该不一样
     */
    public String getId(){
        return id;
    }

    /**
     * 设置 标识容器功能,不同的验证码 容器id应该不一样.
     *
     * @param id
     *            the new 标识容器功能,不同的验证码 容器id应该不一样
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * 容错次数.
     * <p>
     * 定义为 <=0 表示无论何时,都需要显示验证码.<br>
     * 1标识表单提交,出错一次(比如用户名密码不匹配) 需要显示.<br>
     * 以此类推
     * </p>
     *
     * @return the countThreshold
     */
    public int getCountThreshold(){
        return countThreshold;
    }

    /**
     * 容错次数.
     * <p>
     * 定义为 <=0 表示无论何时,都需要显示验证码.<br>
     * 1标识表单提交,出错一次(比如用户名密码不匹配) 需要显示.<br>
     * 以此类推
     * </p>
     * 
     * @param countThreshold
     *            the countThreshold to set
     */
    public void setCountThreshold(int countThreshold){
        this.countThreshold = countThreshold;
    }
}
