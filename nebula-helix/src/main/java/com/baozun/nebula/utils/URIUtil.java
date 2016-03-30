package com.baozun.nebula.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理url uri 等
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2010-6-11 上午02:06:43
 * @since 1.0
 * @deprecated pls use {@link com.feilong.core.net.URIUtil}
 */
@Deprecated
public final class URIUtil{

    private static final Logger log      = LoggerFactory.getLogger(URIUtil.class);

    /** 查询片段 <code>{@value}</code> */
    public static final String  fragment = "#";

    /**
     * URI uri = new URI(path);<br>
     * 如果String对象的URI违反了RFC 2396的语法规则，将会产生一个java.net.URISyntaxException。
     * 
     * @param path
     * @return
     */
    public static URI getURI(String path){
        try{
            // 如果String对象的URI违反了RFC 2396的语法规则，将会产生一个java.net.URISyntaxException。
            URI uri = new URI(path);
            return uri;
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证path是不是绝对路径
     * 
     * @param path
     *            待验证的字符串
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isAbsolutePath(String path){
        URI uri = getURI(path);

        if (null == uri){
            return false;
        }else{
            return uri.isAbsolute();
        }
    }

    /**
     * 拼接url(如果charsetType 是null,则原样拼接,如果不是空,则返回安全的url)
     * 
     * @param before
     *            ?前面的部分
     * @param paramMap
     *            参数map value将会被 toString
     * @param charsetType
     *            编码,如果为空 不name 和value 不进行编码
     * @return
     */
    public static String getEncodedUrl(String before,Map<String, ?> paramMap,String charsetType){
        // map 不是空 表示 有参数
        if (Validator.isNotNullOrEmpty(paramMap)){
            StringBuilder builder = new StringBuilder("");
            builder.append(before);
            builder.append("?");

            int i = 0;
            int size = paramMap.size();
            for (Map.Entry<String, ?> entry : paramMap.entrySet()){
                String key = entry.getKey();
                // 兼容特殊情况
                Object value = entry.getValue();
                if (null == value){
                    value = "";
                    log.warn("the param key:[{}] value is null", key);
                }

                if (Validator.isNotNullOrEmpty(charsetType)){
                    // 统统先强制 decode 再 encode
                    // 浏览器兼容问题
                    key = encode(decode(key, charsetType), charsetType);
                    if (!"".equals(value)){
                        value = encode(decode(value.toString(), charsetType), charsetType);
                    }
                }

                builder.append(key);
                builder.append("=");
                builder.append(value);

                // 最后一个& 不拼接
                if (i != size - 1){
                    builder.append("&");
                }
                ++i;
            }

            return builder.toString();
        }
        return before;
    }

    /**
     * 将a=1&b=2这样格式的数据转换成map,这个方法不会处理 特殊符号 中文等不兼容情况
     * 
     * @param query
     *            a=1&b=2类型的数据
     * @return map value的处理,原始的key和value
     * @see #parseQueryToMap(String, String)
     */
    public static Map<String, String> parseQueryToMap(String query){
        return parseQueryToMap(query, null);
    }

    /**
     * 将a=1&b=2这样格式的数据转换成map (如果charsetType 不是null或者empty 返回安全的 key和value)
     * 
     * @param query
     *            a=1&b=2类型的数据
     * @param charsetType
     *            何种编码，如果是null或者 empty,那么不参数部分原样返回,自己去处理兼容性问题<br>
     *            否则会先解码,再加码,因为ie浏览器和chrome 浏览器 url中访问路径 ,带有中文情况下 不一致
     * @return map value的处理
     *         <ul>
     *         <li>没有Validator.isNullOrEmpty(bianma) 那么就原样返回</li>
     *         <li>如果有编码,统统先强制 decode 再 encode</li>
     *         </ul>
     */
    public static Map<String, String> parseQueryToMap(String query,String charsetType){
        if (Validator.isNotNullOrEmpty(query)){
            String[] nameAndValueArray = query.split("&");
            if (Validator.isNotNullOrEmpty(nameAndValueArray)){
                Map<String, String> map = new LinkedHashMap<String, String>();
                for (int i = 0, j = nameAndValueArray.length; i < j; ++i){

                    String nameAndValue = nameAndValueArray[i];
                    String[] tempArray = nameAndValue.split("=", 2);

                    if (tempArray != null && tempArray.length == 2){
                        String key = tempArray[0];
                        String value = tempArray[1];

                        if (Validator.isNullOrEmpty(charsetType)){
                            // 没有编码 原样返回
                            map.put(key, value);
                        }else{
                            // 统统先强制 decode 再 encode
                            // 浏览器兼容问题
                            key = encode(decode(key, charsetType), charsetType);
                            value = encode(decode(value, charsetType), charsetType);
                            map.put(key, value);
                        }
                    }
                }
                return map;
            }
        }
        return null;
    }

    /**
     * 获取链接?前面的连接(不包含?)
     * 
     * @param url
     * @return 如果 url 为空 返回 ""
     */
    public static String getBefore(String url){
        if (Validator.isNullOrEmpty(url)){
            return "";
        }else{
            String before = "";
            // 判断url中是否含有?
            int index = url.indexOf('?');
            if (index == -1){
                before = url;
            }else{
                before = url.substring(0, index);
            }
            return before;
        }
    }

    /**
     * 获取联合url,通过在指定的上下文中对给定的 spec 进行解析创建 URL。 新的 URL 从给定的上下文 URL 和 spec 参数创建<br>
     * 网站地址拼接,请使用{@link #getUnionUrl(URL, String)}
     * <p>
     * 示例: URIUtil.getUnionUrl("E:\\test", "sanguo")------------->file:/E:/test/sanguo
     * 
     * @param context
     *            要解析规范的上下文
     * @param spec
     *            the <code>String</code> to parse as a URL.
     * @return 获取联合url
     */
    public static String getUnionUrl(String context,String spec){
        URL url_parent = getURL(context);
        return getUnionUrl(url_parent, spec);
    }

    /**
     * 获取联合url,通过在指定的上下文中对给定的 spec 进行解析创建 URL。 新的 URL 从给定的上下文 URL 和 spec 参数创建<br>
     * 网站地址拼接,请使用这个method
     * <p>
     * 示例: URIUtil.getUnionUrl("E:\\test", "sanguo")------------->file:/E:/test/sanguo<br>
     * URL url = new URL("http://www.exiaoshuo.com/jinyiyexing/");<br>
     * result = URIUtil.getUnionUrl(url, "/jinyiyexing/1173348/");<br>
     * http://www.exiaoshuo.com/jinyiyexing/1173348/
     * 
     * @param context
     *            要解析规范的上下文
     * @param spec
     *            the <code>String</code> to parse as a URL.
     * @return 获取联合url
     */
    public static String getUnionUrl(URL context,String spec){
        try{
            URL url_union = new URL(context, spec);
            return url_union.toString();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串路径转成url
     * 
     * @param pathName
     *            字符串路径
     * @return url
     */
    public static URL getURL(String pathName){
        File file = new File(pathName);
        try{
            // file.toURL() 已经过时,它不会自动转义 URL 中的非法字符
            return file.toURI().toURL();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ******************************************** 去除乱码 *************************************************
     */
    /**
     * iso-8859的方式去除乱码(默认采用Constants.bianma)
     * 
     * @param str
     *            字符串
     * @return 原来的字符串
     * @deprecated
     */
    public static String decodeLuanMa_ISO8859(String str){
        return decodeLuanMa_ISO8859(str, CharsetType.GB2312);
    }

    // /**
    // * iso-8859的方式去除乱码
    // *
    // * @param str
    // * 字符串
    // * @return 原来的字符串
    // * @deprecated
    // */
    // public static String decodeLuanMa_ISO8859_UTF(String str){
    // return decodeLuanMa_ISO8859(str, CharsetType.UTF8);
    // }

    /**
     * iso-8859的方式去除乱码
     * 
     * @param str
     *            字符串
     * @param bianma
     *            使用的编码
     * @return 原来的字符串
     * @deprecated
     */
    public static String decodeLuanMa_ISO8859(String str,String bianma){
        if (Validator.isNotNullOrEmpty(str)){
            try{
                return new String(str.trim().getBytes(CharsetType.ISO_8859_1), bianma);
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return "";
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
     * Decodes a <code>application/x-www-form-urlencoded</code> string using a specific encoding scheme. The supplied encoding is used to
     * determine what
     * characters are represented by any consecutive sequences of the form "<code>%<i>xy</i></code>".
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
     *            The name of a supported <a href="../lang/package-summary.html#charenc">character encoding</a>.
     * @return the newly decoded <code>String</code>
     * @exception UnsupportedEncodingException
     *                If character encoding needs to be consulted, but named character encoding is not supported
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

    /**
     * url中的特殊字符转为16进制代码,用于url传递
     * 
     * @param specialCharacter
     *            特殊字符
     * @return 特殊字符url编码
     */
    @Deprecated
    public static String specialCharToHexString(String specialCharacter){

        Map<String, String> specialCharacterMap = new HashMap<String, String>();

        specialCharacterMap.put("+", "%2B");// URL 中+号表示空格
        specialCharacterMap.put(" ", "%20");// URL中的空格可以用+号或者编码
        specialCharacterMap.put("/", "%2F");// 分隔目录和子目录
        specialCharacterMap.put("?", "%3F");// 分隔实际的 URL 和参数
        specialCharacterMap.put("%", "%25");// 指定特殊字符
        specialCharacterMap.put("#", "%23");// 表示书签
        specialCharacterMap.put("&", "%26");// URL 中指定的参数间的分隔符
        specialCharacterMap.put("=", "%3D");// URL 中指定参数的值

        if (specialCharacterMap.containsKey(specialCharacter)){
            return specialCharacterMap.get(specialCharacter);
        }
        // 不是 url 特殊字符 原样输出
        return specialCharacter;
    }

}
