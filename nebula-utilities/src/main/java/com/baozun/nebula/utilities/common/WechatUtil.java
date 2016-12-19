package com.baozun.nebula.utilities.common;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WechatUtil {

	private final static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	/**
	 * 获取request body中的内容
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestBody(HttpServletRequest request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = request.getReader();
		char[] charBuffer = new char[1024];
		int readCount = -1;
		while ((readCount = reader.read(charBuffer)) > 0) {
			stringBuilder.append(charBuffer, 0, readCount);
		}
		return stringBuilder.toString();
	}

	/**
	 * 商户生成的随机字符串，32个字节以下
	 * 
	 * @return
	 */
	public static String generateRandomString() {
		String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int maxPos = allChars.length();
		StringBuffer nonceSb = new StringBuffer();
		for (int i = 0; i < 32; i++) {
			nonceSb.append(allChars.charAt((int) (Math.random() * maxPos)));
		}
		return nonceSb.toString();
	}
	
	/**
	 * post 请求,传送xml数据
	 * @param urlStr
	 * @param xmlInfo
	 * @return
	 */
	public static String post(String urlStr, String xmlInfo) {  
		StringBuffer sb = new StringBuffer();
        try {  
            URL url = new URL(urlStr);  
            URLConnection con = url.openConnection();  
            con.setDoOutput(true);  
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");  
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            out.write(new String(xmlInfo.getBytes("UTF-8")));  
            out.flush();  
            out.close();  
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));  
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {  
            	sb.append(line);
            }  
        } catch (MalformedURLException e) {  
        	logger.error(e.getMessage(), e);
        } catch (IOException e) {  
        	logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }  
  
	/**
	 * 生成xml文件
	 * @param params
	 * @return
	 */
    public static String getXmlInfo(Map<String, String> params) {  
        StringBuilder sb = new StringBuilder();  
        sb.append("<xml>");  
        for(Map.Entry<String, String> entry : params.entrySet()){
        	sb.append("<").append(entry.getKey()).append("><![CDATA[").append(entry.getValue()).append("]]></").append(entry.getKey()).append(">");
        }
        sb.append("</xml>"); 
        if(logger.isDebugEnabled()){
        	logger.debug("xml is [{}]", sb.toString());
        }
        return sb.toString();  
    }  
    
	/**
	 * @param srcMap
	 * @param excludes
	 * @param partnerKey
	 * @return
	 * @throws WechatException
	 */
	public static String makeSign(Map<String, String> srcMap, Collection<String> excludes, String partnerKey){
		Map<String, String> tmpMap = new TreeMap<String, String>();
		tmpMap.putAll(srcMap);
		if (null != excludes) {
			for (String exclude : excludes) {
				tmpMap.remove(exclude);
			}
		}
		StringBuilder sb = new StringBuilder();
		String value = "";
		for (String key : tmpMap.keySet()) {
			value = tmpMap.get(key);
			if (StringUtils.isBlank(value)) {
				continue;
			} else {
				sb.append(key).append("=").append(value).append("&");
			}
		}
		String stringSignTemp = sb.append("key=").append(partnerKey).toString();
		logger.debug("## stringSignTemp:{}", stringSignTemp);
		String sign = Md5Encrypt.md5(stringSignTemp).toUpperCase();
		return sign;
	}
	
	public static String getSignature(Map<String, String> params, String signType) {
		String signature = null;
		try {
			StringBuffer signatureBuffer = new StringBuffer();
			if (params != null) {
				String key = null;
				for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
					key = iterator.next();
					signatureBuffer.append(key).append("=").append(params.get(key));
					if (iterator.hasNext()) {
						signatureBuffer.append("&");
					}
				}
			}

			if (signatureBuffer.length() > 0) {
				MessageDigest crypt = MessageDigest.getInstance(signType);
				crypt.reset();
				logger.debug("signatureBuffer is {}", signatureBuffer.toString());
				crypt.update(signatureBuffer.toString().getBytes("UTF-8"));
				signature = byteToHex(crypt.digest());
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		logger.debug("signature is {}", signature);
		return signature;
	}
	
	private static String byteToHex(final byte[] hash) {
		StringBuilder str = new StringBuilder();
		for (byte b : hash) {
			String hv = Integer.toHexString(b & 0xFF);
			if (hv.length() < 2) {
				str.append("0");
			}
			str.append(hv);
		}
		return str.toString();
	}
	
	public static String getNonceStr() {
		return UUID.randomUUID().toString();
	}

	public static String getTimeStamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

    
}
