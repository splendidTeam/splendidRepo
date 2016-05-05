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
package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;

/**
 * 用户收藏
 *
 */
public class NebulaMemberFavoriteController extends BaseController {

	/**
	 * log 定义
	 */
	private static final Logger LOG = LoggerFactory.getLogger(NebulaMemberFavoriteController.class);

	/**
	 * 加入收藏
	 * 
	 * 如果收藏到商品，传入itemId；如果收藏到sku，传入skuId。
	 * 如果itemId和skuId都传入，则以skuId为准
	 * 
	 * @RequestMapping(value = "/favorite/add", method = RequestMethod.POST)
	 * @ResponseBody
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult addFavorite(@LoginMember MemberDetails memberDetails, 
			@RequestParam Long itemId, @RequestParam(required = false) Long skuId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		//TODO
		return new DefaultReturnResult();
	}
}
