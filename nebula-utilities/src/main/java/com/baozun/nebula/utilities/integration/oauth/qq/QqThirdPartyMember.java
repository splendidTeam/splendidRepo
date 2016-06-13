package com.baozun.nebula.utilities.integration.oauth.qq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.oauth.AbstractThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.exception.RequestInfoException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;

public class QqThirdPartyMember extends AbstractThirdPartyMemberAdaptor  implements ThirdPartyMemberAdaptor{

	private Logger logger = LoggerFactory.getLogger(QqThirdPartyMember.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	

	/**
	 * 应用id
	 */
	private String clientId;

	/**
	 * 应用私钥
	 */
	private String secret;

	/**
	 * 登录页url
	 */
	private String thirdPartyLoginUrl;

	/**
	 * 回调url
	 */
	private String thirdPartyCallbackUrl;

	/**
	 * token url
	 */
	private String thirdPartyTokenUrl;

	/**
	 * OpenId url
	 */
	private String thirdPartyOpenIDUrl;
	/**
	 * 用户信息url
	 */
	private String thirdPartyUserInfoUrl;


	public QqThirdPartyMember() {
		
		Properties pro=ProfileConfigUtil.findPro("config/thirdpartymember.properties");
		
		clientId = pro.getProperty("thirdparty.qq.clientId");
		secret = pro.getProperty("thirdparty.qq.secret");
		thirdPartyLoginUrl = pro.getProperty("thirdparty.qq.thirdPartyLoginUrl");
		thirdPartyCallbackUrl = pro.getProperty("thirdparty.qq.thirdPartyCallbackUrl");
		thirdPartyTokenUrl = pro.getProperty("thirdparty.qq.thirdPartyTokenUrl");
		thirdPartyOpenIDUrl = pro.getProperty("thirdparty.qq.thirdPartyOpenIDUrl");
		thirdPartyUserInfoUrl = pro.getProperty("thirdparty.qq.thirdPartyUserInfoUrl");	
	}

	@Override
	public String generateLoginUrl() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", clientId);
		params.put("redirect_uri", thirdPartyCallbackUrl);
		String redirectUrl = createRequestUrl(params, thirdPartyLoginUrl);
		logger.debug("info:{}", redirectUrl);
		return redirectUrl;
	}

	@Override
	public ThirdPartyMember returnMember(HttpServletRequest request) {
		ThirdPartyMember member = null;
		String code = request.getParameter("code");
		//token信息
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("grant_type", "authorization_code");
		tokenMap.put("client_id", clientId);
		tokenMap.put("client_secret", secret);
		tokenMap.put("code", code);
		tokenMap.put("state", "esprit");
		tokenMap.put("redirect_uri", thirdPartyCallbackUrl);
		String tokenUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(tokenMap, thirdPartyTokenUrl);
		String tokenInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(
				tokenUrl, HttpMethodType.GET);
		String accessToken = getAccessToken(tokenInfo).get("access_token");
		//openId信息
		Map<String, String> opendidMap = new HashMap<String, String>();
		opendidMap.put("access_token", accessToken);
		String openidUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(opendidMap,
				thirdPartyOpenIDUrl);
		String openidInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(
				openidUrl, HttpMethodType.GET);
		String openid = "";
		TypeReference<Map<String, String>> idReference = new TypeReference<Map<String, String>>() {
		};
		Map<String, String> openIdMap = null;
		logger.debug("info:{}",openidInfo);
		
		//用户信息
		Map<String, String> userInfoMap = new HashMap<String, String>();
		String userUrl ="";
		String userInfo ="";
		TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
		Map<String, String> jsonMap = null;
		try {
			openIdMap = objectMapper.readValue(openidInfo.substring(openidInfo.indexOf('{'), openidInfo.indexOf('}') + 1), idReference);
			if(!openIdMap.isEmpty()){
				openid = openIdMap.get("openid");
				userInfoMap.put("openid", openid);
				userInfoMap.put("oauth_consumer_key", clientId);
				userInfoMap.put("access_token", accessToken);
				userUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(userInfoMap,
						thirdPartyUserInfoUrl);
				logger.debug("userUrl:{}",userUrl);
				userInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(
						userUrl, HttpMethodType.GET);
				jsonMap = objectMapper.readValue(userInfo, typeReference);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RequestInfoException("QQ login failure "+openidInfo,e);
		}
			
		if(jsonMap.isEmpty() || !"0".equals((String)jsonMap.get("ret"))){
			throw new RequestInfoException("QQ login failure with "+(String)jsonMap.get("msg"));
		}
		
		member = new ThirdPartyMember();
		member.setNickName((String) jsonMap.get("nickname"));
		member.setUid(openid);
		
		// 头像
		member.setAvatar(jsonMap.get("figureurl_1"));
		
		//性别  注意：获取不到时默认返回男
		if("男".equals(jsonMap.get("gender"))){
			member.setSex("1");
		}else if("女".equals(jsonMap.get("gender"))){
			member.setSex("2");
		}
		return member;
	
	}

	/** 获取QQ 的 AccessToken*/
	private  Map<String, String> getAccessToken(String str) {
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = str.split("&");
		for (String s : arr) {
			String[] t = s.split("=");
			map.put(t[0], t[1]);
		}
		return map;
	}

}
