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
 */
package com.baozun.nebula.web.controller.promotion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.constant.Constants;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.promotion.PromotionManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * @author: pengfei.fang
 * @Description: 促销信息controller
 * @date:2016年01月06日
 */
@Controller
public class PromotionInfomationController extends BaseController{

	@Autowired
	private PromotionManager	promotionManager;

	@Autowired
	private CategoryManager		categoryManager;

	@RequestMapping(value = "/promotion/information/listPrmInfo.htm")
	public String listPromotionInformation(Model model){
		List<Category> prmInforList = categoryManager.findSubCategoryListByParentCode(Constants.PROMOTION_INFORMATION_CATEGORY_CODE);
		model.addAttribute("promotionList", prmInforList);
		return "/promotion/promotion-information";
	}

	@RequestMapping(value = "/promotion/information/findByCategoryCode.json")
	@ResponseBody
	public Object findByCategoryCode(@RequestParam(value = "categoryCode") String categoryCode){
		return promotionManager.findByCategoryCode(categoryCode);

	}

	@RequestMapping(value = "/promotion/information/updateProInfo.json")
	@ResponseBody
	public BackWarnEntity createOrUpdatePromotionInformationPage(
			@RequestParam(value = "prmInfoId") Long prmInfoId,
			@RequestParam(value = "categoryCode") String categoryCode,
			@RequestParam(value = "content") String content){
		try{
			promotionManager.createOrUpdatePromotionInformationPage(prmInfoId, categoryCode, content);
		}catch (Exception e){
			new BackWarnEntity(false, null);
		}

		return new BackWarnEntity(true, null);
	}

}
