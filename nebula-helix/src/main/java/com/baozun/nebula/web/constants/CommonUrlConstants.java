package com.baozun.nebula.web.constants;

import java.util.Properties;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 公共url地址常量
 * @author Justin Hu
 *
 */
public class CommonUrlConstants {

	public static final String LOGIN_URL;
	
	public static final String LOGOUT_URL;
	
	public static final String REGISTER_URL;
	
	public static final String DEFAULT_REDIRECT_URL ;
	
	public static final String MEMBERINFO_DEFAULT_REDIRECT_URL;
	
	public static final String MATCH_LOGIN_URL;
	
	public static final String MATCH_REGISTER_URL;
	
	public static final String MOBILE_NEXT_STEP_URL ;
	
	public static final String EMAIL_NEXT_STEP_URL ;
	
	public static final String PAYMENT_URL ;
	
	// 分类的url样式
	public static final String CATEGORY_URL;
	
	//搜索的样式
	public static final String SEARCH_URL;
	
	//面包屑指定的 url 样式
	public static final String ASSIGN_URL;
	
	public static final String MATCH_NO_RETURN_URL;	

	static{
		Properties pro=ProfileConfigUtil.findCommonPro("config/commUrl.properties");
		
	 	 LOGIN_URL=pro.getProperty("LOGIN_URL").trim();
			
		 LOGOUT_URL=pro.getProperty("LOGOUT_URL").trim();
		
		 REGISTER_URL=pro.getProperty("REGISTER_URL").trim();
		
		 DEFAULT_REDIRECT_URL = pro.getProperty("DEFAULT_REDIRECT_URL").trim();
		
		 MEMBERINFO_DEFAULT_REDIRECT_URL = pro.getProperty("MEMBERINFO_DEFAULT_REDIRECT_URL").trim();
		
		 MATCH_LOGIN_URL =pro.getProperty("MATCH_LOGIN_URL").trim();
		
		 MATCH_REGISTER_URL = pro.getProperty("MATCH_REGISTER_URL").trim();
		
		 MOBILE_NEXT_STEP_URL = pro.getProperty("MOBILE_NEXT_STEP_URL").trim();
		
		 EMAIL_NEXT_STEP_URL = pro.getProperty("EMAIL_NEXT_STEP_URL").trim();
		
		 PAYMENT_URL = pro.getProperty("PAYMENT_URL").trim();
		 
		 CATEGORY_URL = pro.getProperty("CATEGORY_URL").trim();
		 
		 SEARCH_URL =  pro.getProperty("SEARCH_URL").trim();
		 
		 ASSIGN_URL =  pro.getProperty("ASSIGN_URL").trim();
		 
		 MATCH_NO_RETURN_URL= pro.getProperty("MATCH_NO_RETURN_URL").trim();
		 
	}
}
