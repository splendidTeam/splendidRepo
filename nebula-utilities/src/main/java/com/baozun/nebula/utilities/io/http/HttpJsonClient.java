package com.baozun.nebula.utilities.io.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**

 * @version 1.0
 * @since 1.0
 */

public class HttpJsonClient {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpJsonClient.class);
	static HttpClient httpclient = null;
	private static int			statusCode;

	static{
		try {
			httpclient = new HttpClient();
			Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", https);
			//httpclient.getHostConfiguration().setHost(host, port, https);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得PostMethod请求结果
	 * 
	 * @param uri
	 *            uri
	 * @param nameValuePairs
	 *            nameValuePairs
	 * @return if has error/exception,return null
	 */
	public static String getPostMethodResponseBodyAsString(String uri,NameValuePair[] nameValuePairs){
		// 使用POST方法
		HttpMethod httpMethod = getPostMethodWithParams(uri, nameValuePairs);
		return getHttpMethodResponseBodyAsString(httpMethod);
	}
	
	/**
	 * 获得HttpMethod请求结果
	 * 
	 * @param httpMethod
	 *            httpMethod
	 * @return if has error/exception,return null
	 */
	private static String getHttpMethodResponseBodyAsString(HttpMethod httpMethod){
		String requestResult = null;
		try{
			// 得到返回的数据
			requestResult = httpMethod.getResponseBodyAsString();
		}catch (Exception e){
			logger.error(httpMethod.getPath() + httpMethod.getQueryString() + ",visit error," + e.getMessage());
		}
		// 释放连接
		httpMethod.releaseConnection();
		return requestResult;
	}
	
	/**
	 * 获得带参数的 HttpMethod
	 * 
	 * @param uri
	 * @param nameValuePairs
	 *            参数和值对
	 * @return if has error/exception,return null
	 */
	public static HttpMethod getPostMethodWithParams(String uri,NameValuePair[] nameValuePairs){
		// 使用post方法
		PostMethod postMethod = new PostMethod(uri);
		postMethod.setRequestBody(nameValuePairs);
		return executeMethod(postMethod);
	}
	
	/**
	 * httpClient.executeMethod
	 * 
	 * @param httpMethod
	 *            httpMethod
	 * @return if has error/exception,return null
	 */
	private static HttpMethod executeMethod(HttpMethod httpMethod){
		try{
			logger.debug("before executeMethod");
			statusCode = httpclient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK){
				logger.error("statusCode is :" + statusCode);
			}
			logger.debug("after executeMethod");
			return httpMethod;
		}catch (HttpException e){
			logger.error("{},visit error,{}", httpMethod.getPath() + httpMethod.getQueryString(), e.getMessage());
			e.printStackTrace();
		}catch (IOException e){
			logger.error("{},visit error,{}", httpMethod.getPath() + httpMethod.getQueryString(), e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
}
