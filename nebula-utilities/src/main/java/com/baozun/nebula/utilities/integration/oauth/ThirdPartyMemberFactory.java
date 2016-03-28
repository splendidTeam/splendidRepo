package com.baozun.nebula.utilities.integration.oauth;

import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.integration.oauth.alipay.AlipayThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.qq.QqThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.weChat.WeChatThirdPartyMember;
import com.baozun.nebula.utilities.integration.oauth.weibo.WeiboThirdPartyMember;

/**
 * 用于分配当前所使用的类型
 * @author Justin Hu
 *
 */
public class ThirdPartyMemberFactory {

	public static final String TYPE_ALIPAY="ALIPAY";
	
	public static final String TYPE_QQ="QQ";
	
	public static final String TYPE_WEIBO="WEIBO";
	
	public static final String TYPE_WECHAT="WECHAT";
	
	private Map<String,ThirdPartyMemberAdaptor> tpMemberAdaptorMap=new HashMap<String,ThirdPartyMemberAdaptor>();
	
	private static ThirdPartyMemberFactory thirdPartyMemberFactory=new ThirdPartyMemberFactory();
	
	public static ThirdPartyMemberFactory getInstance(){
		
		return thirdPartyMemberFactory;
	}
	
	private ThirdPartyMemberFactory(){
		
		tpMemberAdaptorMap.put(TYPE_ALIPAY, new AlipayThirdPartyMember());
		tpMemberAdaptorMap.put(TYPE_QQ, new QqThirdPartyMember());
		tpMemberAdaptorMap.put(TYPE_WEIBO, new WeiboThirdPartyMember());
		tpMemberAdaptorMap.put(TYPE_WECHAT, new WeChatThirdPartyMember());
	}
	
	public ThirdPartyMemberAdaptor getThirdPartyMemberAdaptor(String type){
		
		return tpMemberAdaptorMap.get(type);
	}
	
}
