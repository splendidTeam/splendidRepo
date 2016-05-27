package com.baozun.nebula.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.constant.EmailType;
import com.baozun.nebula.sdk.utils.StringUtil;

/**
 * 邮件工具类
 */
public final class EmailUtil{

    /**
     * 通过邮件获取其相关信息
     * 
     * @param email
     *            邮件
     * @return MailBoxEntity
     */
    public static EmailType getEmailType(String email){
        String postfix = getEmailPostfix(email);
        return EmailType.getEmailTypeByPostfix(postfix);
    }

    /**
     * 獲取郵件發送次數過期時間
     * 
     * @return
     */
    public static Integer getEmailExpireSeconds(){
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.MILLISECOND, 0);
        Date now = new Date();
        Long expireSeconds = (todayEnd.getTime().getTime() - now.getTime()) / 1000;
        return expireSeconds.intValue();
    }

    public static List<String> getRequestParams(String requestConfirm){
        List<String> list = new ArrayList<String>();
        String[] strArr = StringUtils.split(requestConfirm, "&");
        if (strArr != null){
            for (String key : strArr){
                String[] subArr = key.split("=", 2);
                list.add(subArr[1]);
            }
        }
        return list;
    }

    /**
     * 获得邮件的后缀名
     * 
     * @param email
     *            邮件
     * @return
     */
    public static String getEmailPostfix(String email){
        return StringUtil.substring(email, "@", 1);
    }

}