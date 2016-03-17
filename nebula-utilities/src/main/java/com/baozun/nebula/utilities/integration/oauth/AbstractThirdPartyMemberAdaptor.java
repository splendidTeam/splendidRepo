package com.baozun.nebula.utilities.integration.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**第三方登录的抽象父类
 * */
public class AbstractThirdPartyMemberAdaptor {

	protected static final String _INPUT_CHARSET = "utf-8";
	protected static final String SIGN_TYPE = "MD5";
	
	/**拼URL
	 * */
	public static String createRequestUrl(Map<String, String> params, String url)
	{
		List<String> keys = new ArrayList<String>(params.keySet());
		String returnUrl = url;
		for (int i = 0; i < keys.size(); i++)
		{
			String key = keys.get(i);
			String value = params.get(key).toString();
			if (i == keys.size() - 1){
				returnUrl += key + "=" + value;
			}else{
				returnUrl += key + "=" + value + "&";
			}
		}
		return returnUrl;
	}

	
}
