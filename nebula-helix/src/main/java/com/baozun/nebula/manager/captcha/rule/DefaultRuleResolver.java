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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.manager.captcha.RuleResolver;
import com.feilong.servlet.http.RequestUtil;

/**
 * 默认黑白名单形式的实现.
 *
 * @author feilong
 * @version 1.5.3 2016年4月2日 下午5:13:42
 * @since 1.5.3
 */
public class DefaultRuleResolver implements RuleResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleResolver.class);

    /** The rule config. */
    private RuleConfig          ruleConfig;

    /* (non-Javadoc)
     * @see com.baozun.nebula.manager.captcha.RuleResolver#needShowCaptcha(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Boolean needShowCaptcha(HttpServletRequest request){
        String clientIp = RequestUtil.getClientIp(request);
        //白名单 如果属于 百名单  那么就认为不需要显示验证码
        if (isInWhiteIplist(clientIp, ruleConfig)){
            return false;
        }

        //如果是在黑名单之内,那么直接就认为需要显示验证码
        if (isInBlackIpList(clientIp, ruleConfig)){
            return true;
        }
        return null;
    }

    /**
     * 是否在ip黑名单之内.
     *
     * @param clientIp
     *            the client ip
     * @param ruleConfig
     *            the bot rule config
     * @return true, if checks if is in black ip list
     */
    private static boolean isInBlackIpList(String clientIp,RuleConfig ruleConfig){
        List<String> blackIpList = ruleConfig.getBlackIpList();
        if (null != blackIpList && blackIpList.contains(clientIp)){
            LOGGER.debug("clientIp:{} in blackIpList:{}", blackIpList, blackIpList.toString());
            return true;
        }
        return false;
    }

    /**
     * 是否在ip白名单之内.
     *
     * @param clientIp
     *            the client ip
     * @param ruleConfig
     *            the bot rule config
     * @return true, if checks if is in white iplist
     */
    private static boolean isInWhiteIplist(String clientIp,RuleConfig ruleConfig){
        List<String> whiteIplist = ruleConfig.getWhiteIplist();
        if (null != whiteIplist && whiteIplist.contains(clientIp)){
            LOGGER.debug("clientIp:{} in whiteIplist:{}", clientIp, whiteIplist.toString());
            return true;
        }
        return false;
    }

    /**
     * 设置 the bot rule config.
     *
     * @param defaultRuleConfig
     *            the defaultRuleConfig to set
     */
    public void setRuleConfig(RuleConfig defaultRuleConfig){
        this.ruleConfig = defaultRuleConfig;
    }
}
