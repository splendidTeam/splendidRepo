/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.web.controller.bundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;

/**
 * @author yue.ch
 *
 */
public class NebulaBundleController extends NebulaAbstractBundleController {

	private static final Logger LOG = LoggerFactory.getLogger(NebulaBundleController.class);

	private static final String VIEW_BUNDLE_LIST = "bundle.list";

	private static final String VIEW_BUNDLE_DETAIL = "bundle.detail";

	/**
	 * 查看捆绑类商品详细信息
	 * 
	 * @RequestMapping(value = "/bundle/{bundleId}", method = RequestMethod.GET)
	 * 
	 * @param bundleId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBundleDetail(@PathVariable("bundleId") Long bundleId, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		return VIEW_BUNDLE_DETAIL;
	}

	/**
	 * 查看捆绑类商品列表
	 * 
	 * @RequestMapping(value = "/bundle/list", method = RequestMethod.GET)
	 * 
	 * @param pageForm
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showBundleList(@ModelAttribute("page") PageForm pageForm, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		return VIEW_BUNDLE_LIST;
	}
	
	/**
	 * PDP页面异步加载bundle信息
	 * 
	 * @RequestMapping(value = "/bundle/loadBundles.json", method = RequestMethod.GET)
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult loadBundleInfo(@RequestParam("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response, Model model){
		
		DefaultReturnResult result = new DefaultReturnResult();
		
		return result;
	}
	
	 
}
