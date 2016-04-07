package com.baozun.nebula.utilities.integration.oauth.alipay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.oauth.AbstractThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.ThirdPartyMemberAdaptor;
import com.baozun.nebula.utilities.integration.oauth.exception.RequestInfoException;

public class AlipayThirdPartyMember extends AbstractThirdPartyMemberAdaptor implements ThirdPartyMemberAdaptor{

	private Logger	logger	= LoggerFactory.getLogger(AlipayThirdPartyMember.class);

	/**
	 * 范围权限
	 */
	private String	service;

	/**
	 * 应用私钥
	 */
	private String	partner;

	/**
	 * 签名
	 */
	private String	sign;

	/**
	 * 回调url
	 */
	private String	return_url;

	/**
	 * 目标服务
	 */
	private String	target_service;

	/**
	 * 登录页url
	 */
	private String	thirdPartyLoginUrl;

	public AlipayThirdPartyMember(){
		Properties pro = ProfileConfigUtil.findPro("config/thirdpartymember.properties");
		service = pro.getProperty("thirdparty.alipay.service");
		partner = pro.getProperty("thirdparty.alipay.partner");
		return_url = pro.getProperty("thirdparty.alipay.return_url");
		target_service = pro.getProperty("thirdparty.alipay.target_service");
		thirdPartyLoginUrl = pro.getProperty("thirdparty.alipay.thirdPartyLoginUrl");
		sign = pro.getProperty("thirdparty.alipay.sign");

	}

	@Override
	public String generateLoginUrl(){
		Map<String, String> sParam = new HashMap<String, String>();
		sParam.put("service", service);
		sParam.put("partner", partner);
		sParam.put("_input_charset", _INPUT_CHARSET);
		sParam.put("sign", "");
		sParam.put("sign_type", SIGN_TYPE);
		sParam.put("return_url", return_url);
		sParam.put("target_service", target_service);
		Map<String, String> result = pareseFileter(sParam);
		String mySign = buildMySign(result);
		sParam.put("sign", mySign);
		String httpUrl = createRequestUrl(sParam, thirdPartyLoginUrl);
		return httpUrl;
	}

	@Override
	public ThirdPartyMember returnMember(HttpServletRequest request){
		ThirdPartyMember member = new ThirdPartyMember();
		String is_success = request.getParameter("is_success");
		String target_url = request.getParameter("target_url");
		if (is_success == null || !is_success.equals("T")) {
			logger.error("Alipay login failure.");
			throw new RequestInfoException("Alipay login failure.");
		}

		// 如果是alipay联合登录成功
		request.getSession().setAttribute("isAliPay", true);
		String alipayEmail = request.getParameter("email");
		String gmt_decay = request.getParameter("gmt_decay");
		String notify_id = request.getParameter("notify_id");
		String real_name = request.getParameter("real_name");
		String tokenStr = request.getParameter("token");
		String user_id = request.getParameter("user_id");
		String userGrade = request.getParameter("user_grade");
		String userGradeType = request.getParameter("user_grade_type");
		String sign = request.getParameter("sign");
		String avatar = request.getParameter("avatar");
		String sex = request.getParameter("gender");

		logger.debug(
				"alipayEmail:" + alipayEmail + ",gmt_decay:" + gmt_decay + ",notify_id:" + notify_id + ",real_name:" + real_name
						+ ",tokenStr:" + tokenStr + ",user_id:" + user_id + ",userGrade:" + userGrade + ",userGradeType:" + userGradeType
						+ ",sign:" + sign);

		member.setUid(user_id);
		member.setNickName(real_name);
		member.setUserName(alipayEmail);
		
		// 头像
		member.setAvatar(avatar);
		
		//性别  M为男性，F为女性
		if("M".equals(sex)){
			member.setSex("1");
		}else if("F".equals(sex)){
			member.setSex("2");
		}

		return member;
	}

	/**
	 * 去除空格和不参加签名的参数
	 * 
	 * @return
	 */
	private Map<String, String> pareseFileter(Map<String, String> sArray){
		Map<String, String> resMap = new HashMap<String, String>();
		if (sArray == null || sArray.size() < 0) {
			return sArray;
		}
		for (String key : sArray.keySet()){
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			resMap.put(key, value);
		}
		return resMap;
	}

	/**
	 * 生成签名结果
	 * 
	 * @return
	 */
	private String buildMySign(Map<String, String> sArray){
		String strSign = createLinkString(sArray);
		strSign = strSign + sign;
		String mySign = Md5Encrypt.md5(strSign, "UTF-8");
		return mySign;
	}

	/**
	 * key按照a b c排序
	 * 
	 * @param sArray
	 * @return
	 */
	private String createLinkString(Map<String, String> sArray){
		List<String> keys = new ArrayList<String>(sArray.keySet());
		Collections.sort(keys);
		String preStr = "";
		for (int i = 0; i < keys.size(); i++){
			String key = keys.get(i);
			String value = sArray.get(key);
			if (i == keys.size() - 1) {
				preStr = preStr + key + "=" + value;
			}else{
				preStr = preStr + key + "=" + value + "&";
			}
		}
		return preStr;
	}

}
