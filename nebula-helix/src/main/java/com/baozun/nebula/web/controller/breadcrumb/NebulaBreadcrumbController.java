/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.breadcrumb;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.web.controller.breadcrumb.viewcommand.BreadcrumbsViewCommand;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;

/**   
 * 面包屑
 * <h3>获取页面的面包屑(分为plp、pdp两种场景)</h3>
 * <p>一、用法实例:</p>
 * <ul>
 * 		<li>PLP:<jsp:include page="/item/breadcrumb.htm?navId =1" /></li>
 * 		<li>PDP:<jsp:include page="/item/breadcrumb.htm?itemId =2" /></li>
 * </ul>
 * <p>二、具体实现:</p>
 * <p>
 * 		<h2>1. 导航navId不为空,</h2>
 * 			<li>itemId不为空时，可构造[导航树结构+商品名称]作为面包屑</li>
 * 			<li>itemId为空时，可构造[导航树结构]作为面包屑</li>
 * </p>
 * <p>
 * 		<h2>2. 导航navId为空时,</h2>
 * 			<li>itemId不为空时,</br>
 * 				<p style="padding-left:10px;">
 * 					a.获取所有的itemCollection集合,构造该itemId对应的ItemCollectionContext
 * 				</p>
 * 				<p style="padding-left:10px;">
 * 					b.遍历itemCollection,利用AutoCollection的apply方法获取最长、最早的itemCollection,
 * 				</p>
 * 				<p style="padding-left:10px;">
 * 					c.根据此itemCollection找出对应的Navication，可构造[导航树结构+商品名称]作为面包屑
 * 				</p>
 * 			</li>
 * 			<li>itemId为空时，抛异常</li>
 * </p>
 * @Description 
 * @author dongliang ma
 * @date 2016年5月13日 下午6:14:47 
 * @version   
 */
public class NebulaBreadcrumbController extends NebulaAbstractBreadcrumbController{
	
	private static final Logger	LOG									= LoggerFactory.getLogger(NebulaBreadcrumbController.class);
	
	//面包屑对应的jsp
	/** 列表页*/
	private static final String VIEW_BREADCRUMB_PLP 				="breadcrumb.plp";
	
	/** 详情页*/
	private static final String VIEW_BREADCRUMB_PDP 				="breadcrumb.pdp";
	
	//Model key的定义
	/** 面包屑的model key*/
	private static final String	MODEL_KEY_BREADCRUMB				="breadcrumb";
	/**
	 * 获取页面的面包
	 * 
	 * @RequestMapping(value = "/item/breadcrumb.htm", method = RequestMethod.GET)
	 * @param navId 导航id
	 * @param itemId 商品id 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBreadcrumb(@RequestParam("navId") Long navId,
			@RequestParam("itemId") Long itemId, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			List<BreadcrumbsViewCommand> breadcrumbsViewCommands = bulidCurmbViewCommandsWithCache(navId, itemId, request);
			model.addAttribute(MODEL_KEY_BREADCRUMB, breadcrumbsViewCommands);
		} catch (IllegalItemStateException e) {
			LOG.error("[PDP_BUILD_BREADCRUMB] breadcrumb state illegal. itemId:{}, {}",itemId, e.getState().name());
			//异常也不能影响pdp页面的渲染,面包屑地方显示空白即可
			return getView(navId,itemId);
		}
		return getView(navId,itemId);
	}




	/**
	 * 导航不为空->列表页面包屑，
	 * 其他，商品详情页
	 * @param navId
	 * @param itemId
	 * @return
	 */
	private String getView(Long navId, Long itemId) {
		if(Validator.isNotNullOrEmpty(navId)){
			return VIEW_BREADCRUMB_PLP;
		}
		return VIEW_BREADCRUMB_PDP;
	}
	
	/* 
	 * @see com.baozun.nebula.web.controller.breadcrumb.NebulaAbstractBreadcrumbController#getBreadcrumbCacheExpireSeconds()
	 */
	@Override
	protected Integer getBreadcrumbCacheExpireSeconds() {
		// 1天
		return TimeInterval.SECONDS_PER_DAY;
	}
	
	
	
	
	
	
	
	
	
	
	
}
