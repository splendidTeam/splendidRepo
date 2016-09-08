package com.baozun.nebula.manager.member.entity;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * SendEmailConfig 配置发送邮件的一些参数
 * <ol>
 * <li>isValidIntervalTime 是否开启时间间隔的验证开关</li>
 * <li>isValidMaxSendNumber 是否开启最大发送数的验证开关</li>
 * <li>maxSendNumber 获取有效期内的允许最大发送数</li>
 * <li>intervalTime 获取有效期内的发送时间间隔</li>
 * <li>emailCacheExpireTime 获取缓存有效期</li>
 * </ol>
 * 
 * @author yufei.kong 2016年4月5日 16:13:35
 */
public class SendEmailConfig{

    private boolean isValidIntervalTime;//是否开启时间间隔的验证开关

    private boolean isValidMaxSendNumber ;//是否开启最大发送数的验证开关

    private Long maxSendNumber ;//获取有效期内的允许最大发送数

    private Long intervalTime;//获取有效期内的发送时间间隔

    private Long emailCacheExpireTime;//获取缓存有效期
    
    public SendEmailConfig(){
        Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");
        maxSendNumber=Long.valueOf(StringUtils.trim(pro.getProperty("send.mail.maxSendNumber","5")));
        intervalTime=Long.valueOf(StringUtils.trim(pro.getProperty("send.mail.intervalTime","30")));
        emailCacheExpireTime=Long.valueOf(StringUtils.trim(pro.getProperty("send.mail.emailCacheExpireTime","86400")));
        isValidIntervalTime=Boolean.valueOf(StringUtils.trim(pro.getProperty("send.mail.isValidIntervalTime","false")));
        isValidMaxSendNumber=Boolean.valueOf(StringUtils.trim(pro.getProperty("send.mail.isValidMaxSendNumber","false")));
    }
    public boolean getIsValidIntervalTime(){
        return isValidIntervalTime;
    }

    public void setIsValidIntervalTime(boolean isValidIntervalTime){
        this.isValidIntervalTime = isValidIntervalTime;
    }

    public boolean getIsValidMaxSendNumber(){
        return isValidMaxSendNumber;
    }

    public void setIsValidMaxSendNumber(boolean isValidMaxSendNumber){
        this.isValidMaxSendNumber = isValidMaxSendNumber;
    }

    public Long getMaxSendNumber(){
        return maxSendNumber;
    }

    public void setMaxSendNumber(Long maxSendNumber){
        this.maxSendNumber = maxSendNumber;
    }

    public Long getIntervalTime(){
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime){
        this.intervalTime = intervalTime;
    }

    public Long getEmailCacheExpireTime(){
        return emailCacheExpireTime;
    }

    public void setEmailCacheExpireTime(Long emailCacheExpireTime){
        this.emailCacheExpireTime = emailCacheExpireTime;
    }

}
