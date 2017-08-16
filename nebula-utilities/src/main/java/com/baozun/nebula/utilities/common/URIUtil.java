package com.baozun.nebula.utilities.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.core.net.ParamUtil;
import com.feilong.core.util.MapUtil;

import static com.feilong.core.bean.ConvertUtil.toMap;

/**
 * 处理url uri 等
 * 
 * @deprecated pls use {@link com.feilong.core.net.URIUtil}
 */
@Deprecated
public final class URIUtil{

    private static final Logger log = LoggerFactory.getLogger(URIUtil.class);

    /**
     * 拼接url
     * 
     * @param before
     *            ?前面的部分
     * @param paramMap
     *            参数map value将会被 toString
     * @param charsetType
     *            编码
     * @return
     */
    public static String getEncodedUrl(Map<String, ?> paramMap,String charsetType){
        Validate.notEmpty(paramMap, "paramMap can't be null/empty!");
        Validate.notEmpty(charsetType, "charsetType can't be null/empty!");

        //---------------------------------------------------------------

        String action = (String) paramMap.get("action");
        Map<String, String> subMapExcludeKeysa = toMap(MapUtil.getSubMapExcludeKeys(paramMap, "action"), String.class, String.class);
        return ParamUtil.addParameterSingleValueMap(action, subMapExcludeKeysa, charsetType);

    }

    /**
     * 拼接html
     * 
     * @param before
     *            ?前面的部分
     * @param paramMap
     *            参数map value将会被 toString
     * @param charsetType
     *            编码,如果为空 不name 和value 不进行编码
     * @return
     */
    public static String generateAutoSubmitForm(Map<String, String> paramMap){
        StringBuilder html = new StringBuilder();
        html.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">");
        html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
        html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(paramMap.get("action")).append("\" method=\"post\">\n");

        for (String key : paramMap.keySet()){
            if (!key.equalsIgnoreCase("action")){
                html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
            }
        }
        html.append("</form>\n</html>");
        return html.toString();
    }

    /**
     * 加码,对参数值进行编码 <br>
     * 使用以下规则：
     * <ul>
     * <li>字母数字字符 "a" 到 "z"、"A" 到 "Z" 和 "0" 到 "9" 保持不变。</li>
     * <li>特殊字符 "."、"-"、"*" 和 "_" 保持不变。</li>
     * <li>空格字符 " " 转换为一个加号 "+"。</li>
     * <li>所有其他字符都是不安全的，因此首先使用一些编码机制将它们转换为一个或多个字节。<br>
     * 然后每个字节用一个包含 3 个字符的字符串 "%xy" 表示，其中 xy 为该字节的两位十六进制表示形式。<br>
     * 推荐的编码机制是 UTF-8。<br>
     * 但是，出于兼容性考虑，如果未指定一种编码，则使用相应平台的默认编码。</li>
     * </ul>
     * 
     * @param value
     * @param charsetType
     *            charsetType {@link CharsetType}
     * @see CharsetType
     * @return 加码之后的值<br>
     *         如果 charsetType 是空 原样返回 value
     */
    public static String encode(String value,String charsetType){
        if (Validator.isNullOrEmpty(charsetType)){
            return value;
        }

        try{
            return URLEncoder.encode(value, charsetType);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码,对参数值进行解码 <br>
     * Decodes a <code>application/x-www-form-urlencoded</code> string using a
     * specific encoding scheme. The supplied encoding is used to determine what
     * characters are represented by any consecutive sequences of the form "
     * <code>%<i>xy</i></code>".
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     * 
     * @param s
     *            the <code>String</code> to decode
     * @param enc
     *            The name of a supported <a
     *            href="../lang/package-summary.html#charenc">character
     *            encoding</a>.
     * @return the newly decoded <code>String</code>
     * @exception UnsupportedEncodingException
     *                If character encoding needs to be consulted, but named
     *                character encoding is not supported
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     * @param value
     *            需要被解码的值
     * @param charsetType
     *            charsetType {@link CharsetType}
     * @see CharsetType
     * @return 解码之后的值<br>
     *         如果 charsetType 是空 原样返回 value
     */
    public static String decode(String value,String charsetType){
        if (Validator.isNullOrEmpty(charsetType)){
            return value;
        }
        try{
            return URLDecoder.decode(value, charsetType);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getResArr(String str){
        String regex = "(.*?cupReserved\\=)(\\{[^}]+\\})(.*)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);

        String reserved = "";
        if (matcher.find()){
            reserved = matcher.group(2);
        }

        String result = str.replaceFirst(regex, "$1$3");
        String[] resArr = result.split("&");
        for (int i = 0; i < resArr.length; i++){
            if ("cupReserved=".equals(resArr[i])){
                resArr[i] += reserved;
            }
        }
        return resArr;
    }
}
