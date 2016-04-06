package com.baozun.nebula.utilities.integration.oauth.weibo;

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
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.integration.oauth.AbstractThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.exception.RequestInfoException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;

public class WeiboThirdPartyMember extends AbstractThirdPartyMemberAdaptor  implements ThirdPartyMemberAdaptor{

	private Logger logger = LoggerFactory.getLogger(WeiboThirdPartyMember.class);
	
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
	 * Token url
	 */
	private String thirdPartyTokenUrl;
	
	/**
	 * 用户信息url
	 */
	private String thirdPartyUserInfoUrl;
	
	public WeiboThirdPartyMember() {
		Properties pro=ProfileConfigUtil.findPro("config/thirdpartymember.properties");
		
		clientId = pro.getProperty("thirdparty.weibo.clientId");
		secret = pro.getProperty("thirdparty.weibo.secret");
		thirdPartyLoginUrl = pro.getProperty("thirdparty.weibo.thirdPartyLoginUrl");
		thirdPartyCallbackUrl = pro.getProperty("thirdparty.weibo.thirdPartyCallbackUrl");
		thirdPartyTokenUrl = pro.getProperty("thirdparty.weibo.thirdPartyTokenUrl");
		thirdPartyUserInfoUrl = pro.getProperty("thirdparty.weibo.thirdPartyUserInfoUrl");
		
	}
	
	@Override
	public String generateLoginUrl() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", clientId);
		params.put("redirect_uri", thirdPartyCallbackUrl);
		String redirectUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(params, thirdPartyLoginUrl);
		logger.info("引导授权的URL为："+redirectUrl);
		return redirectUrl;
	}

	@Override
	public ThirdPartyMember returnMember(HttpServletRequest request) {
		ThirdPartyMember member = new ThirdPartyMember();
		//token信息
		String code = request.getParameter("code");
		if(Validator.isNullOrEmpty(code)){
			String errorCode = request.getParameter("error_code");
			if(Validator.isNotNullOrEmpty(errorCode)) {
				logger.error("Sina Weibo login cancel.");
				member.setErrorCode(errorCode);
				member.setErrorDescription((String) request.getParameter("error_description"));
				return member;
			}
		}
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("grant_type", "authorization_code");
		tokenMap.put("client_id", clientId);
		tokenMap.put("client_secret", secret);
		tokenMap.put("code", code);
		tokenMap.put("state", "nebulaWeibo");
		tokenMap.put("redirect_uri", thirdPartyCallbackUrl);
		String tokenUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(tokenMap, thirdPartyTokenUrl);
		String tokenInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(
				tokenUrl, HttpMethodType.POST);
		TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
		TypeReference<Map<String, String>> userReference = new TypeReference<Map<String, String>>() {};
		Map<String, String> jsonMap = null;
		Map<String, Object> userjsonMap = null;
		String uid = "";
		String access_token = "";
		//用户信息
		Map<String, String> userMap = new HashMap<String, String>();
		String userUrl = "";
		String userInfo ="";
		
		try {
			jsonMap = objectMapper.readValue(tokenInfo, typeReference);
			if(jsonMap != null){
				uid = (String) jsonMap.get("uid");
				access_token = (String) jsonMap.get("access_token");
				
				userMap.put("access_token", access_token);
				userMap.put("uid", uid);
				userUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(userMap, thirdPartyUserInfoUrl);
				userInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(
						userUrl, HttpMethodType.GET);
				if(userInfo.indexOf("error_code") != -1) {
					userjsonMap = objectMapper.readValue(userInfo, userReference);
				}else{
					userInfo = userInfo.substring(0, userInfo.indexOf("status")-2)+"}";
					userjsonMap = objectMapper.readValue(userInfo, userReference);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error("Sina Weibo login failure.");
			throw new RequestInfoException("Sina Weibo login failure.",e1);
		}
		
		if(userInfo.indexOf("error_code") != -1) {
			logger.error("Sina Weibo login failure.");
			throw new RequestInfoException((String) userjsonMap.get("error"));
		}
		
		if(userjsonMap.isEmpty()){
			logger.error("Sina Weibo login failure.");
			throw new RequestInfoException("Sina Weibo login failure.");
		}
		
		member.setUid(uid);
		member.setNickName((String) userjsonMap.get("screen_name"));
		member.setAvatar((String) userjsonMap.get("profile_image_url"));
		member.setSex(jsonMap.get("gender"));
		
		// 性别，m：男、f：女、n：未知
		if("m".equals(jsonMap.get("gender"))){
			member.setSex("1");
		}else if("f".equals(jsonMap.get("gender"))){
			member.setSex("2");
		}
		return member;
		
	}

}
