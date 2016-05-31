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
package com.baozun.nebula.manager.captcha.rule;

import java.io.Serializable;
import java.util.List;

/**
 * RuleConfig相关规则配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年4月2日 下午5:07:46
 * @since 1.5.3
 */
public class RuleConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3883469893269270496L;

    /** 白名单是设置能通过的用户,如果请求的ip属于白名单之内,那么直接过,比如测试可以直接不用输入验证码. */
    private List<String>      whiteIplist;

    /** 黑名单是设置不能通过的用户,如果用户的ip属于黑名单之内,那么肯定不能通过. */
    private List<String>      blackIpList;

    /**
     * 获得 白名单是设置能通过的用户,如果请求的ip属于白名单之内,那么直接过,比如测试可以直接不用输入验证码.
     *
     * @return the whiteIplist
     */
    public List<String> getWhiteIplist(){
        return whiteIplist;
    }

    /**
     * 设置 白名单是设置能通过的用户,如果请求的ip属于白名单之内,那么直接过,比如测试可以直接不用输入验证码.
     *
     * @param whiteIplist
     *            the whiteIplist to set
     */
    public void setWhiteIplist(List<String> whiteIplist){
        this.whiteIplist = whiteIplist;
    }

    /**
     * 获得 黑名单是设置不能通过的用户,如果用户的ip属于黑名单之内,那么肯定不能通过.
     *
     * @return the blackIpList
     */
    public List<String> getBlackIpList(){
        return blackIpList;
    }

    /**
     * 设置 黑名单是设置不能通过的用户,如果用户的ip属于黑名单之内,那么肯定不能通过.
     *
     * @param blackIpList
     *            the blackIpList to set
     */
    public void setBlackIpList(List<String> blackIpList){
        this.blackIpList = blackIpList;
    }
}
