package com.baozun.nebula.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.constant.EmailType;
import com.baozun.nebula.sdk.utils.StringUtil;




/**
 * 飞龙邮件工具类
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2010-3-22 上午11:59:09
 * @since 1.0
 */
public final class EmailUtil{

	/**
	 * 获得邮件的中文名称
	 * 
	 * @param email
	 *            邮件
	 * @return 中文名称
	 */
	/*public static String getEmailChineseName(String email){
		EmailType emailType = getEmailType(email);
		if (null != emailType){
			return emailType.getChineseName();
		}
		return ExceptionConstants.exception_unknown_type_email;
	}*/

	/**
	 * 通过邮件获取其相关信息
	 * 
	 * @param email
	 *            邮件
	 * @return MailBoxEntity
	 */
	public static EmailType getEmailType(String email){
		String postfix = getEmailPostfix(email);
		return EmailType.getEmailTypeByPostfix(postfix);
	}
	
	
	/**
	 *	獲取郵件發送次數過期時間
	 * @return
	 */
	public static Integer getEmailExpireSeconds(){
		 Calendar todayEnd = Calendar.getInstance();  
	     todayEnd.set(Calendar.HOUR_OF_DAY, 23);
	     todayEnd.set(Calendar.SECOND, 59);
	     todayEnd.set(Calendar.MINUTE, 59);
	     todayEnd.set(Calendar.MILLISECOND, 0);
	     Date now = new  Date();
	     Long expireSeconds = (todayEnd.getTime().getTime()-now.getTime())/1000;
	     return expireSeconds.intValue() ;  
	}
	
	
	public static List<String> getRequestParams(String requestConfirm) {
		List<String> list = new ArrayList<String>();
		String[] strArr = StringUtils.split(requestConfirm, "&");
		if (strArr != null) {
			for (String key : strArr) {
				String[] subArr = key.split("=", 2);
				list.add(subArr[1]);
			}
		}
		return list;
	}

	/**
	 * 获得邮件的后缀名
	 * 
	 * @param email
	 *            邮件
	 * @return
	 */
	public static String getEmailPostfix(String email){
		return StringUtil.substring(email, "@", 1);
	}

	// /**
	// * 获得错误邮件标题
	// *
	// * @param servletContext
	// * @param customerTitle
	// * @param request
	// * @return 获得错误邮件标题
	// */
	// public static String getErrorEmailTitle(ServletContext servletContext,String customerTitle,HttpServletRequest request){
	// String emailTitle = "错误页面";
	// if (Validator.isNotNull(customerTitle)){
	// emailTitle = customerTitle;
	// }
	// if (FeiLongHTTP.isLocalHost(request)){
	// emailTitle = "[本地测试]" + emailTitle;
	// }else{
	// emailTitle = "[" + FeiLongPropertiesConfigure.getPropertiesFeiLongValueWithServletContext(servletContext, "projectChineseName") + "]"
	// + emailTitle;
	// }
	// return emailTitle;
	// }

	// /**
	// * 通过邮件获得邮箱的登录路径
	// *
	// * @param email
	// * 邮件
	// * @return 获得邮箱的登录路径
	// */
	// public static String getEmailLoginHrefByEmail(String email){
	// EmailType emailType = getEmailType(email);
	// // if (null != emailType){
	// // HtmlAEntity htmla = new HtmlAEntity();
	// // htmla.setHref(emailType.getWebsite());
	// // htmla.setText(emailType.getWebsite());
	// // htmla.setTitle("登陆" + emailType.getChineseName());
	// // htmla.setTarget(HtmlAEntity.target_blank);
	// // return HTMLA.createA(htmla);
	// // }
	// return ExceptionConstants.exception_unknown_type_email;
	// }

}