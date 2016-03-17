/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Baozun. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Baozun.
 * 
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.baozun.nebula.web.controller.promotion;

import java.util.Arrays;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.promotion.PromotionCouponQueryCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.promotion.PromotionCouponManager;
import com.baozun.nebula.model.promotion.PromotionCoupon;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * @author 何波
 */
@Controller
@RequestMapping(value = "/coupon")
public class PromotionCouponController extends BaseController {

	@Autowired
	private PromotionCouponManager promotionCouponManager;

	/**
	 * 前往优惠劵类型管理界面
	 */
	@RequestMapping(value = "/coupon.htm", method = RequestMethod.GET)
	public String promotioncouponlist() {
		return "/promotion/pcoupon-list";
	}

	@RequestMapping(value = "/create.htm", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("optype", "new");
		return "/promotion/coupon-edit";
	}

	@RequestMapping(value = "/view.htm", method = RequestMethod.GET)
	public String view(Model model, Long id) {
		model.addAttribute("coupon", promotionCouponManager.queryByid(id));
		model.addAttribute("optype", "view");
		return "/promotion/coupon-edit";
	}
	
	@RequestMapping(value = "/edit.htm", method = RequestMethod.GET)
	public String edit(Model model, Long id) {
		model.addAttribute("coupon", promotionCouponManager.queryByid(id));
		model.addAttribute("optype", "edit");
		return "/promotion/coupon-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<PromotionCouponQueryCommand> promotionEditList(
			Model model, @QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts)) {
			sorts = Sort.parse("create_time desc");
		}
		Pagination<PromotionCouponQueryCommand> pagination = promotionCouponManager
				.queryCouponListByConditionallyWithPage(queryBean.getPage(),
						sorts, queryBean.getParaMap());
		return pagination;
	}

	@RequestMapping("/createOrupdate.json")
	@ResponseBody
	public BackWarnEntity createOrupdate(PromotionCoupon model) {
		BackWarnEntity failure = new BackWarnEntity(false, "");
		try {
			UserDetails user = getUserDetails();
			model.setCreateId(user.getUserId());
			promotionCouponManager.createOrupdate(model);
			return SUCCESS;
		} catch (BusinessException e) {
			failure.setDescription(getMessage(e.getErrorCode()));
			return failure;
		} catch (Exception e) {
			failure.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
			return failure;
		}

	}

	@RequestMapping("/delCouponsByIds.json")
	@ResponseBody
	public BackWarnEntity delCouponsByIds(Long[] ids) {
		BackWarnEntity failure = new BackWarnEntity(false, "");
		try {
			promotionCouponManager.delCouponsByIds(Arrays.asList(ids));
			return SUCCESS;
		} catch (BusinessException e) {
			failure.setDescription(getMessage(e.getErrorCode()));
			return failure;
		} catch (Exception e) {
			failure.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
			return failure;
		}

	}

	@RequestMapping("/updateActive.json")
	@ResponseBody
	public BackWarnEntity updateActive(Long id, int active) {
		BackWarnEntity failure = new BackWarnEntity(false, "");
		try {
			promotionCouponManager.updateActive(id, active);
			return SUCCESS;
		} catch (BusinessException e) {
			failure.setDescription(getMessage(e.getErrorCode()));
			return failure;
		} catch (Exception e) {
			failure.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
			return failure;
		}

	}

}
