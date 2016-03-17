/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.member.MemberFrontendManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.utils.CookieUtil;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.utils.cookie.HttpUtilities;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.utils.cookie.HttpUtilities;
/**
 * BaseController
 * 
 * @author songdianchao
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
public class BaseController{

	@Autowired
	private MemberFrontendManager memberManager;
	
	@Resource
	protected ApplicationContext	context;

	/**
	 * ajax http header
	 */
	public static final String		HEADER_WITH_AJAX_SPRINGMVC	= "X-Requested-With=XMLHttpRequest";
	
	/**
	 * 默认的成功状态
	 * @return
	 */
	protected static final BackWarnEntity SUCCESS=new BackWarnEntity(true,"");
	
	
	/**
	 * 默认的失败状态
	 * @return
	 */
	protected static final BackWarnEntity FAILTRUE=new BackWarnEntity(false,"");
	
	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	protected MemberDetails getUserDetails(HttpServletRequest request){
		return (MemberDetails) request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
	}

	/**
	 * 生成spring 的跳转路径<br>
	 * e.g. getSpringRedirectPath("/shoppingcart") <br>
	 * 注:不需要你手工的 拼接request.getContextPath()
	 * 
	 * @param targetUrl
	 *            如果是相对根目录路径 只需要传递 如"/shoppingcart" spring会自动添加request.getContextPath() <br>
	 *            也可以传入绝对路径 如:http://www.baidu.com
	 * @return
	 */
	protected String getSpringRedirectPath(String targetUrl){
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + targetUrl;
	}

	/**
	 * 生成 spring Forward 路径
	 * 
	 * @param forwardUrl
	 * @return
	 */
	protected String getSpringForwardPath(String forwardUrl){
		return UrlBasedViewResolver.FORWARD_URL_PREFIX + forwardUrl;
	}

	// **********************************************************************************************

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(Integer errorCode){
		Object[] args = null;
		return getMessage(errorCode, args);
	}

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(Integer errorCode,Object...args){
		if (Validator.isNotNullOrEmpty(errorCode)){
			return getMessage(ErrorCodes.BUSINESS_EXCEPTION_PREFIX + errorCode, args);
		}
		return null;
	}

	/**
	 * 获得消息信息
	 * 
	 * @param code
	 *            code
	 * @param args
	 *            args
	 * @return
	 */
	protected String getMessage(String code,Object...args){
		if (Validator.isNotNullOrEmpty(code)){
			return context.getMessage(code, args, LocaleContextHolder.getLocale());
		}
		return null;
	}

	/**
	 * 获得消息信息
	 * 
	 * @param messageSourceResolvable
	 *            适用于 ObjectError 以及 FieldError
	 * @return
	 */
	protected String getMessage(MessageSourceResolvable messageSourceResolvable){
		return context.getMessage(messageSourceResolvable, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 获取session 信息
	 * 
	 * @param session
	 * @return UserDetails
	 */
	protected MemberFrontendCommand getSessionMember(HttpSession session){
		return (MemberFrontendCommand) session.getAttribute(Constants.BAOZUN_SEESSION_MEMBER);
	}
	
	/**
	 * 判断session中某key的值是否与参数值相同
	 */
	protected boolean valueInSession(HttpSession session, String key, Object value) {
		Object obj = session.getAttribute(key);
		if(obj != null)
			return obj.equals(value);
		return false;
	}
	
	/**
	 * 获取当前登录用户信息
	 * 
	 * @return
	 */
	protected Long getMemberId(HttpServletRequest request){
		MemberDetails memberDetails = (MemberDetails) request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
		if(null != memberDetails)
			return memberDetails.getMemberId();
		return null;
	}
	
	/**
	 * 获取cookie中的购物车行集合
	 * @param request
	 * @return
	 * @throws EncryptionException 
	 */
	protected List<CookieShoppingCartLine> getCookieShoppingCartLines(HttpServletRequest request) throws EncryptionException{
		Cookie cookie = WebUtils.getCookie(request,CookieKeyConstants.GUEST_COOKIE_GC);
		if(null != cookie){
			if(StringUtils.isBlank(cookie.getValue()))
				return null;
			else
				return JSON.parseObject(EncryptUtil.getInstance().decrypt(cookie.getValue()), new TypeReference<ArrayList<CookieShoppingCartLine>>(){});
		}
		return null;
	}
	
	/**
	 * 获取cookie中的购物车行信息.将cookie中的购物车 转换为 shoppingCartLineCommand
	 * @param callType
	 * @param validedLines
	 * @param effectEngine
	 * @param memberContext
	 * @param cartLineList
	 * @return
	 */
	private List<ShoppingCartLineCommand> getCookieShoppingCartLine(Integer callType,
			List<CookieShoppingCartLine> cartLineList) {
		List<ShoppingCartLineCommand> shoppingCartLines = new ArrayList<ShoppingCartLineCommand>();
		for(CookieShoppingCartLine cookieCartLine : cartLineList){
			//if(callType == Constants.CHECKED_CHOOSE_STATE){//查询选中状态的购物车数据
				//if(String.valueOf(cookieCartLine.getSettlementState()).equals(String.valueOf(Constants.CHECKED_CHOOSE_STATE)))
					shoppingCartLines.add(parseCookieCartLineToShoppingCartLine(cookieCartLine));
			//}
		}
		return shoppingCartLines;
	}
	
	/**
	 * 将cookie中的购物车 转换为 shoppingCartLineCommand
	 * @param cookieCartLine
	 * @return
	 */
	private ShoppingCartLineCommand parseCookieCartLineToShoppingCartLine(CookieShoppingCartLine cookieCartLine){
		ShoppingCartLineCommand shoppingLineCommand = new ShoppingCartLineCommand();
		//将cookie中的购物车 转换为 shoppingCartLineCommand
		shoppingLineCommand.setCreateTime(cookieCartLine.getCreateTime());
		shoppingLineCommand.setSkuId(cookieCartLine.getSkuId());
		shoppingLineCommand.setQuantity(cookieCartLine.getQuantity());
		shoppingLineCommand.setExtentionCode(cookieCartLine.getExtentionCode());
		shoppingLineCommand.setSettlementState(cookieCartLine.getSettlementState());
		shoppingLineCommand.setShopId(cookieCartLine.getShopId());
		shoppingLineCommand.setGift(null == cookieCartLine.getIsGift()?false : cookieCartLine.getIsGift());
		shoppingLineCommand.setPromotionId(cookieCartLine.getPromotionId());
		shoppingLineCommand.setLineGroup(cookieCartLine.getLineGroup());
		return shoppingLineCommand;
	}
	
	/**
	 * 将cookie中的购物车行集合转换为ShoppingCartLineCommand集合
	 * @param callType
	 * @param request
	 * @param shoppingCartLines
	 * @throws EncryptionException 
	 */
	protected List<ShoppingCartLineCommand> getCookieShoppingCartLines(HttpServletRequest request,Integer callType){
		List<ShoppingCartLineCommand> shoppingCartLines = new ArrayList<ShoppingCartLineCommand>();
		//获取cookie中的购物车行集合
		List<CookieShoppingCartLine> cartLineList = null;
		try {
			cartLineList = getCookieShoppingCartLines(request);
		} catch (EncryptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(null != cartLineList && cartLineList.size() > 0){
			shoppingCartLines =	getCookieShoppingCartLine(callType,cartLineList);
		}
		return shoppingCartLines;
	}
	
	/**
	 * 把cookie购物车行对象加入cookie当中
	 * @param guestIndentify
	 * @param response
	 * @param cartLineList
	 */
	protected void addGuestIndentifyCartCookie(
			HttpServletResponse response,List<CookieShoppingCartLine> cartLineList) {
		String json = JSON.toJSONString(cartLineList);
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName(CookieKeyConstants.GUEST_COOKIE_GC);
		cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
		try {
			String encrypt = EncryptUtil.getInstance().encrypt(json);
			cookieGenerator.addCookie(response,encrypt);
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把cookie购物车行对象加入cookie当中
	 * @param guestIndentify
	 * @param response
	 * @param cartLineList
	 */
	protected void addGuestIndentifyCartCookie(
			HttpServletResponse response,HttpServletRequest request,List<CookieShoppingCartLine> cartLineList) {
		String json = JSON.toJSONString(cartLineList);

		try {
			String encrypt = EncryptUtil.getInstance().encrypt(json);
			
			HttpUtilities.setCookieValue(CookieKeyConstants.GUEST_COOKIE_GC, encrypt, request, response, Integer.MAX_VALUE, true);
			
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
	}
	
	protected Integer getCookieShoppingCartLinesQty(List<CookieShoppingCartLine> cartLineList){
		Integer qty = 0;
		//获取cookie中的购物车行集合
		if(null != cartLineList && cartLineList.size() > 0){
			for (Iterator iterator = cartLineList.iterator(); iterator
					.hasNext();) {
				CookieShoppingCartLine cookieShoppingCartLine = (CookieShoppingCartLine) iterator
						.next();
				qty += cookieShoppingCartLine.getQuantity();
			}
		}
		return qty;
	}
	
	/**
	 * 设置购物车后的头部购物车商品数量
	 * @param request
	 * @param response
	 * @param totalQty
	 */
	protected void emptyCookieShoppingCart(HttpServletRequest request,HttpServletResponse response,Integer totalQty){
		CookieUtil.deleteCookie(request, response,CookieKeyConstants.GUEST_COOKIE_GC);
		CookieUtil.setCookie(request, response, CookieKeyConstants.GUEST_COOKIE_GC_CNT,String.valueOf(totalQty),Integer.MAX_VALUE);
	}
	
	/**
	 * 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
	 * @param shoppingCartLine
	 * @return
	 */
	protected List<CookieShoppingCartLine> getCookieCartLines(
			List<ShoppingCartLineCommand> shoppingCartLines) {
		//将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
		List<CookieShoppingCartLine> cookieLines = new ArrayList<CookieShoppingCartLine>();
		if(null != shoppingCartLines && shoppingCartLines.size() > 0){
			for(ShoppingCartLineCommand shoppingCartLine : shoppingCartLines){
				CookieShoppingCartLine cookieCartLine = new CookieShoppingCartLine();
				cookieCartLine.setExtentionCode(shoppingCartLine.getExtentionCode());
				cookieCartLine.setSkuId(shoppingCartLine.getSkuId());
				cookieCartLine.setQuantity(shoppingCartLine.getQuantity());
				cookieCartLine.setCreateTime(shoppingCartLine.getCreateTime());
				cookieCartLine.setSettlementState(shoppingCartLine.getSettlementState());
				cookieCartLine.setShopId(shoppingCartLine.getShopId());
				cookieCartLine.setIsGift(shoppingCartLine.isGift());
				cookieCartLine.setPromotionId(shoppingCartLine.getPromotionId());
				cookieCartLine.setLineGroup(shoppingCartLine.getLineGroup());
				cookieLines.add(cookieCartLine);
			}
		}
		return cookieLines;
	}
	
	/**
	 * 查找最新session中的用户信息
	 * @param request
	 */
	protected MemberDetails findLastMemeberDetailsInSesion(HttpServletRequest request) {
		MemberDetails memberDetails = getUserDetails(request);
		if(null != memberDetails){
			Member member = new Member();
			member.setId(memberDetails.getMemberId());
			member.setLoginName(memberDetails.getLoginName());
			member.setLoginEmail(memberDetails.getLoginEmail());
			member.setLoginMobile(memberDetails.getLoginMobile());
			
			memberDetails = memberManager.getMemberDetails(member);
			
			//重新设置session中的用户信息
			request.getSession().setAttribute(SessionKeyConstants.MEMBER_CONTEXT,memberDetails);
		}
		return memberDetails;
	}
}
