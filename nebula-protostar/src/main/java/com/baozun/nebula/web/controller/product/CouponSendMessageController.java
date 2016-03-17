/**
 * Copyright (c) 2012 Baozun All Rights Reserved.

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
 */
package com.baozun.nebula.web.controller.product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.sdk.manager.CouponMessage;
import com.baozun.nebula.web.controller.BaseController;

/** 
* @ClassName: CouponSendMessageController 
* @Description:(发送站内信) 
* @author GEWEI.LU 
* @date 2016年1月16日 上午11:10:42  
*/
@Controller
public class CouponSendMessageController extends BaseController {
	@Autowired
	public CouponMessage couponMessage;

	/** 
	* @Title: saveCouponSendMessage 
	* @Description:(记录发送站内信信息) 
	* @param @param model
	* @param @param request
	* @param @param response
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月16日 上午11:10:58 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value = "/instation/saveInstationSendMessage.json")
	@ResponseBody
	public void saveInstationSendMessage(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String,String> pmap= new HashMap<String, String>();
		String textvalue = request.getParameter("textvalue");
		String type = request.getParameter("type");
		String tmpid = request.getParameter("tmpid");
		pmap.put("textvalue", textvalue);
		pmap.put("type", type);
		pmap.put("tmpid", tmpid);
		Map<Object, Object> markmesmap= couponMessage.synthesizeInstationSendMessage(pmap);
		if(markmesmap.get("markmes").equals("1")){
			if(markmesmap.get("nullityuserlist")==null || markmesmap.get("nullityuserlist")==""){
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("操作成功");
			}else{
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("以下用户不存在或已禁用</br>"+markmesmap.get("nullityuserlist"));
			}
		}else{
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("操作失败");
		}
	}
	
	
	@RequestMapping(value = "/coupon/saveCouponSendMessage.json")
	@ResponseBody
	public void saveCouponSendMessage(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String,String> pmap= new HashMap<String, String>();
		String coupontype = request.getParameter("coupontype");
		String textvalue = request.getParameter("textvalue");
		String type = request.getParameter("type");
		String tmpid = request.getParameter("tmpid");
		pmap.put("coupontype", coupontype);
		pmap.put("textvalue", textvalue);
		pmap.put("type", type);
		pmap.put("tmpid", tmpid.equals("0") ? null:tmpid);
		Map<Object, Object> markmesmap=couponMessage.synthesizeCouponoperation(pmap);
		if(markmesmap.get("markmes").equals("1")){
			if(markmesmap.get("nullityuserlist")==null || markmesmap.get("nullityuserlist")==""){
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("操作成功");
			}else{
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("以下用户不存在或已禁用</br>"+markmesmap.get("nullityuserlist"));
			}
		}else{
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(markmesmap.get("nullityuserlist"));
		}
		
	}
}
