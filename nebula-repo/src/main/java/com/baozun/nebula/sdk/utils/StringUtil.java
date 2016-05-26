package com.baozun.nebula.sdk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.Validator;

/**
 * @author jun.lu
 * @creattime 2013-11-20
 * @deprecated pls use {@link com.feilong.core.lang.StringUtil}
 */
@Deprecated
public class StringUtil{

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isNull(String str){
        boolean flag = false;
        if (null == str){
            flag = true;
        }else if (str.trim().length() <= 0){
            flag = true;
        }

        return flag;
    }

    public static boolean isNotNull(String str){
        boolean flag = false;
        if (null != str && str.trim().length() > 0){
            flag = true;
        }

        return flag;
    }

    /**
     * [截取]:从第一次出现字符串位置开始(包含)截取到最后,shift表示向前或者向后挪动位数,<br>
     * beginIndex= text.indexOf(beginString) + shift;<br>
     * return text.substring(beginIndex);
     * 
     * <pre>
     * substring(&quot;jinxin.feilong&quot;,&quot;.&quot;,0)======&gt;&quot;.feilong&quot;
     * substring(&quot;jinxin.feilong&quot;,&quot;.&quot;,1)======&gt;&quot;feilong&quot;
     * </pre>
     * 
     * @param text
     *            text
     * @param beginString
     *            beginString
     * @param shift
     *            负数表示向前,整数表示向后,0表示依旧从自己的位置开始算起
     * @return
     *         <ul>
     *         <li>if isNullOrEmpty(text),return null</li>
     *         <li>if isNullOrEmpty(beginString),return null</li>
     *         <li>if text.indexOf(beginString)==-1,return null</li>
     *         <li>{@code  beginIndex + shift > text.length()},return null</li>
     *         <li>else,return text.substring(beginIndex + shift)</li>
     *         </ul>
     * @throws IllegalArgumentException
     *             {@code  if beginIndex + shift<0}
     * 
     */
    public static final String substring(String text,String beginString,int shift) throws IllegalArgumentException{
        if (Validator.isNullOrEmpty(text)){
            return null;
        }else if (Validator.isNullOrEmpty(beginString)){
            return null;
        }
        //****************************************************
        int beginIndex = text.indexOf(beginString);
        // 查不到指定的字符串
        if (beginIndex == -1){
            return null;
        }
        //****************************************************
        int startIndex = beginIndex + shift;
        if (startIndex < 0){
            String logInfo = StringBuilderUtil.append(
                            "beginIndex + shift <0,",
                            "beginIndex:",
                            beginIndex,
                            ",shift:" + shift,
                            ",text:" + text,
                            ",text.length:",
                            text.length());

            throw new IllegalArgumentException(logInfo);
        }else if (startIndex > text.length()){

            if (log.isInfoEnabled()){
                String logInfo = StringBuilderUtil.append(
                                "beginIndex + shift > text.length(),",
                                "beginIndex:",
                                beginIndex,
                                ",shift:" + shift,
                                ",text:" + text,
                                ",text.length:",
                                text.length());
                log.info(logInfo);
            }

            return null;
        }
        // 索引从0 开始
        return text.substring(startIndex);
    }

}
