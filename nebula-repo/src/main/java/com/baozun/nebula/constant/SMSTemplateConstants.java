/**
 * 
 */
package com.baozun.nebula.constant;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 短信模板code 常量
 * 
 * @author Viktor Huang
 * @date 2016年3月30日 上午11:07:10
 */
public class SMSTemplateConstants{

	/** 注册发送验证码短信模板 */
        public static final String	SMS_REGISTER_CAPTCHA;
	
	/* 验证码的生存时间 */
        public static final Integer     MAX_EXIST_TIME;

        /* 验证码的位数 */
        public static final Integer     VALIDATE_CODE_LENGTH;

        static{
                Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");

                MAX_EXIST_TIME = Integer.valueOf(StringUtils.trim(pro.getProperty("sms.vcode.max.exist.time")));

                VALIDATE_CODE_LENGTH = Integer.valueOf(StringUtils.trim(pro.getProperty("sms.vcode.length")));

                SMS_REGISTER_CAPTCHA = StringUtils.trim(pro.getProperty("sms.register.captcha"));

        }
}
