package com.baozun.nebula.utils.http;

import com.baozun.nebula.utilities.common.EncryptUtil;

/**
 * 用于https 登录相关的签名工具类
 * @author Justin Hu
 *
 */
public class HttpsLoginUtil {

	
	/**
	 * 生成签名
	 * @param sessionId 
	 * @param memberId 会员id
	 * @param secret 密钥
	 * @return
	 */
	public static String makeSign(String sessionId,String memberId,String secret){
		
		StringBuffer sb=new StringBuffer();
		sb.append(sessionId);
		sb.append(memberId);
		
		
		String result=EncryptUtil.getInstance().hash(sb.toString(), secret);
		
		return result;
		
	}
	
	public static void main(String[] args)throws Exception{
		
		System.out.println(makeSign("aaaaadfdf","2233","test"));
	}
	
	
}
