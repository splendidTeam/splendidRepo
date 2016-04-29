package com.baozun.nebula.utilities.integration.oauth.weChat;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.AbstractThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.exception.RequestInfoException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;

public class WeChatThirdPartyMember extends AbstractThirdPartyMemberAdaptor implements ThirdPartyMemberAdaptor {

	private Logger logger = LoggerFactory.getLogger(WeChatThirdPartyMember.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 应用私钥
	 */
	private String secret;

	private String scope;
	
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
	
	/**
	 * 刷新tokenUrl
	 */
	private String thirdPartyRefreshTokenUrl;

	public WeChatThirdPartyMember() {
		Properties pro = ProfileConfigUtil.findPro("config/thirdpartymember.properties");

		appId = pro.getProperty("thirdparty.weixin.appID");
		scope = pro.getProperty("thirdparty.weixin.scope");
		secret = pro.getProperty("thirdparty.weixin.appsecret");
		thirdPartyLoginUrl = pro.getProperty("thirdparty.weixin.thirdPartyLoginUrl");
		thirdPartyCallbackUrl = pro.getProperty("thirdparty.weixin.thirdPartyCallbackUrl");
		thirdPartyTokenUrl = pro.getProperty("thirdparty.weixin.thirdPartyTokenUrl");
		thirdPartyUserInfoUrl = pro.getProperty("thirdparty.weixin.thirdPartyUserInfoUrl");
		thirdPartyRefreshTokenUrl = pro.getProperty("thirdparty.weixin.thirdPartyRefreshTokenUrl");
	}

	@Override
	public String generateLoginUrl() {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("appid", appId);
		params.put("redirect_uri", URLEncoder.encode(thirdPartyCallbackUrl));
		params.put("response_type", "code");
		params.put("scope", "snsapi_login");
		params.put("state", UUID.randomUUID().toString());
		String redirectUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(params, thirdPartyLoginUrl) + "#wechat_redirec";
		
		logger.info("引导授权的URL为：" + redirectUrl);			
		return redirectUrl;
	}

	@Override
	public ThirdPartyMember returnMember(HttpServletRequest request) {
		ThirdPartyMember member = new ThirdPartyMember();
		String code = request.getParameter("code");
		// token信息
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("grant_type", "authorization_code");
		tokenMap.put("secret", secret);
		tokenMap.put("code", code);
		tokenMap.put("appid", appId);
		String tokenUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(tokenMap, thirdPartyTokenUrl);
		String tokenInfo = "";
		//String openid = "";
		Map<String, String> jsonMap = null;
		try {
			tokenInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(tokenUrl, HttpMethodType.GET);

			// 通过code获取access_token
			// String accessToken =
			// getAccessToken(tokenInfo).get("access_token");
			TypeReference<Map<String, String>> idReference = new TypeReference<Map<String, String>>() {};

			Map<String, String> openIdMap = objectMapper.readValue(tokenInfo.substring(tokenInfo.indexOf('{'),tokenInfo.indexOf('}') + 1), idReference);

			if (!openIdMap.isEmpty()) {
				if (Validator.isNotNullOrEmpty(openIdMap.get("errcode"))) {
						member.setErrorCode(openIdMap.get("errcode"));
						member.setErrorDescription(openIdMap.get("errmsg"));						
						return member;
				}
				String accessToken = openIdMap.get("access_token");
				logger.debug("info:{}", accessToken);
				// 用户信息
				Map<String, String> userInfoMap = new HashMap<String, String>();
				String userUrl = "";
				String userInfo = "";
				TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

				userInfoMap.put("openid", openIdMap.get("openid"));
				userInfoMap.put("access_token", accessToken);
				userUrl = AbstractThirdPartyMemberAdaptor.createRequestUrl(userInfoMap, thirdPartyUserInfoUrl);
				logger.debug("userUrl:{}", userUrl);
				userInfo = HttpClientUtil.getHttpMethodResponseBodyAsString(userUrl, HttpMethodType.GET);
				jsonMap = objectMapper.readValue(new String(userInfo.getBytes("iso-8859-1")), typeReference);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RequestInfoException("WenXin login failure " + tokenInfo, e);
		}
		// 获取用户信息失败
		if (jsonMap.isEmpty()) {
			throw new RequestInfoException("WenXin login failure with " +  jsonMap.get("errmsg"));
		}
		if (Validator.isNotNullOrEmpty(jsonMap.get("errcode"))) {
			member.setErrorCode(jsonMap.get("errcode")+"");
			member.setErrorDescription(jsonMap.get("errmsg"));
			return member;
		}

		member = new ThirdPartyMember();
		member.setNickName(jsonMap.get("nickname"));
		member.setUid(jsonMap.get("openid"));
		
		// 头像
		member.setAvatar(jsonMap.get("headimgurl"));
		
		//性别  普通用户性别，1为男性，2为女性
		//member.setSex(jsonMap.get("sex")+"");
		return member;

	}

	/** 获取wenxin 的 AccessToken */
	private Map<String, String> getAccessToken(String str) {
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = str.split("&");
		for (String s : arr) {
			String[] t = s.split("=");
			map.put(t[0], t[1]);
		}
		return map;
	}

}
