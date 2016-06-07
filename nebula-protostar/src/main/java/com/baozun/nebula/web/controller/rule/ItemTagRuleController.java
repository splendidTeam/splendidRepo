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
package com.baozun.nebula.web.controller.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.ProductComboDetailsCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.curator.ZKWatchPath;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.EngineWatchInvoke;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.auth.OrganizationManager;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.rule.CustomScopeType;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.sdk.manager.SdkItemTagRuleManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.property.PropertyUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

@Controller
public class ItemTagRuleController extends BaseController {

	static final Logger						log			= LoggerFactory.getLogger(ItemTagRuleController.class);

	private static final String				RESULT_CODE	= "resultCode";

	/** 创建 */
	private static final String				RULE_CREATE	= "create";
	/** 修改 */
	private static final String				RULE_MODIFY	= "modify";

	@Autowired
	private SdkItemTagRuleManager			sdkItemTagRuleManager;
	@Autowired
	private CategoryManager					categoryManager;
	@Autowired
	private OrganizationManager				organizationManager;
	@Autowired
	private ShopManager						shopManager;
	@Autowired
	private ZkOperator				zkOperator;
	@Autowired
	private ZKWatchPath 			zkWatchPath;
	@Autowired
	private SdkPromotionManager				sdkPromotionManager;
	@Autowired
	private SdkCustomizeFilterClassManager	sdkCustomizerFilterClassManager;
	
	@Value("#{meta['frontend.url']}")
	private String							frontend_url;
	@Value("#{meta['pdpPrefix']}")
	private String							pdpPrefix;
	@Value("#{meta['pdp.param.type']}")
	private String							pdp_param_type;

	private String getPdpInfoUrl() {
		return frontend_url.trim() + pdpPrefix.trim() + pdp_param_type.trim();
	}

	/**
	 * 前往页面 商品筛选器查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/productcombolist.htm")
	public String memberCustomGroupList(Model model) {
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/rule/item-tag-rule-list";
	}

	/**
	 * 获取数据库商品选择器信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/customProductComboList.json")
	@ResponseBody
	public Pagination<ItemTagRuleCommand> promotionAudience(Model model, @QueryBeanParam QueryBean queryBean) {

		Long shopId = shopManager.getShopId(getUserDetails());
		Sort[] sorts = queryBean.getSorts();

		if (Validator.isNullOrEmpty(sorts)) {
			Sort sort = new Sort("r.create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		;
		Pagination<ItemTagRuleCommand> customProductComboCommand = sdkItemTagRuleManager.findCustomProductComboList(
				queryBean.getPage(), sorts, queryBean.getParaMap(), shopId);
		return customProductComboCommand;
	}

	/**
	 * 列表页面 功能 根据id和动作判断做操作
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/product/productcomboedit.htm")
	public String editorPage(Model model, @RequestParam("cmbno") Long cmbno, @RequestParam("type") String type,
			HttpServletRequest request, HttpServletResponse response) {
		Long userId = getUserDetails().getUserId();
		if (null != userId) {

			if (type == null)
				throw new BusinessException(ErrorCodes.PATH_NOT_MATCH);
			// model.addAttribute("act", act);
			if (RULE_CREATE.equals(type)) {
				return "redirect:/product/scope/comboScopeFilters.htm";
			} else if (RULE_MODIFY.equals(type)) {
				return "redirect:/product/combo/comboEdit.htm?cmbno=" + cmbno;
			}
			model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
			return "/rule/item-tag-rule-add";
		}
		throw new BusinessException(ErrorCodes.ACCESS_DENIED);

	}

	/**
	 * 保存商品筛选器
	 * 
	 * @return
	 */
	@RequestMapping("/product/scope/saveCategory.json")
	@ResponseBody
	public Object saveCategory(@ArrayCommand ItemTagRuleCommand cmd, HttpServletRequest request,
			HttpServletResponse response) {
		Long userId = getUserDetails().getUserId();
		Long shopId = shopManager.getShopId(getUserDetails());
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			cmd.setCreateTime(new Date());
			cmd.setCreateId(userId);
			cmd.setShopId(shopId);
			sdkItemTagRuleManager.saveItemTagRule(cmd);
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}

	/**
	 * 修改商品选择器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/scope/updateCombo.json", method = RequestMethod.POST)
	@ResponseBody
	public Object updateCombo(ItemTagRuleCommand combo) {
		Long userId = getUserDetails().getUserId();
		Map<String, Object> rs = new HashMap<String, Object>();
		if (null != userId) {
			combo.setCreateId(userId);
			combo.setCreateTime(new Date());
			sdkItemTagRuleManager.update(combo);
			zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(EngineWatchInvoke.class));
			rs.put("isSuccess", true);
		} else {
			rs.put(RESULT_CODE, "userNotLogin");
			rs.put("isSuccess", false);
		}
		return rs;
	}

	/**
	 * 启用禁用或启用商品组合
	 * 
	 * @param id
	 * @param state
	 * @return
	 */
	@RequestMapping("/product/enableOrDisableProductGroup.json")
	@ResponseBody
	public Object enableOrDisableMemberGroupByIds(@RequestParam("id") Long id,
			@RequestParam("lifecycle") Integer activeMark) {
		try {
			sdkItemTagRuleManager.enableOrDisableProductGroupById(id, activeMark);
			zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(EngineWatchInvoke.class));
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
		return SUCCESS;
	}

	@RequestMapping("/product/scope/comboScopeFilters.htm")
	public String scopeEdit(Model model) {
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);

		List<ItemTagRuleCommand> customProductComboCommand = sdkItemTagRuleManager
				.findAllAvailableCustomProductComboList();
		model.addAttribute("comboList", customProductComboCommand);
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/product/scope/product-scope-selector";
	}

	@RequestMapping("/product/combo/comboEdit.htm")
	public String comboModify(Model model, @RequestParam("cmbno") Long cmbno) {
		ItemTagRuleCommand combo = sdkItemTagRuleManager.findCustomProductComboById(cmbno);
		if (combo != null) {
			if (!ItemTagRule.TYPE_COMBO.equals(combo.getType())) { // 原子类型: 商品类型, 分类类型, 自定义类型
				ProductComboDetailsCommand details = sdkItemTagRuleManager.findItemComboListByExpression(combo
						.getExpression());
				model.addAttribute("details", details);
			} else { // 组合类型
				List<ProductComboDetailsCommand> detailsList = sdkItemTagRuleManager.findDetailsListById(cmbno);
				model.addAttribute("detailsList", detailsList);
			}
		} else {
			combo = new ItemTagRuleCommand();
		}
		model.addAttribute("customCombo", combo);
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);

		List<ItemTagRuleCommand> customProductComboCommand = sdkItemTagRuleManager
				.findAllAvailableCustomProductComboList();
		model.addAttribute("comboList", customProductComboCommand);
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/rule/item-tag-rule-update";
	}

	/**
	 * 查看组合
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/product/combo/view.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> viewCombo(@RequestParam Long id) {
		Map<String, Object> rs = new HashMap<String, Object>();

		ItemTagRuleCommand combo = sdkItemTagRuleManager.findCustomProductComboById(id);
		if (null == combo) {
			rs.put("isSuccess", false);
			rs.put("errorCode", Constants.MEMBER_CUSTOM_GROUP_INEXISTED);
			return rs;
		}
		rs.put("combo", combo);
		if (!ItemTagRule.TYPE_COMBO.equals(combo.getType())) { // 原子类型: 商品类型, 分类类型, 自定义类型
			ProductComboDetailsCommand details = sdkItemTagRuleManager.findItemComboListByExpression(combo.getExpression());
			rs.put("details", details);
		} else { // 组合类型
			List<ProductComboDetailsCommand> detailsList = sdkItemTagRuleManager.findDetailsListById(id);
			rs.put("detailsList", detailsList);
		}

		rs.put("isSuccess", true);

		return rs;
	}

	/**
	 * 验证分组是否包含会员
	 * 
	 * @param members
	 * @param groups
	 * @return
	 */
	@RequestMapping(value = "/product/combo/check.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity checkCategory(@RequestParam String items, @RequestParam String categorys) {
		try {
			if (sdkItemTagRuleManager.checkCategoryWithItem(items, categorys)) {
				return SUCCESS;
			} else {
				return FAILTRUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return FAILTRUE;
		}
	}

	/**
	 * 获取所需筛选器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/combo/custom-list.json", method = RequestMethod.POST)
	@ResponseBody
	public List<ItemTagRuleCommand> findComboList() {
		Long shopId = shopManager.getShopId(getUserDetails());
		List<ItemTagRuleCommand> comboList = sdkItemTagRuleManager.findAllAvailableCustomProductComboListByShopId(shopId);
		List<ItemTagRuleCommand> rs = new ArrayList<ItemTagRuleCommand>();
		for (ItemTagRuleCommand cmg : comboList) {
			if (!ItemTagRule.TYPE_COMBO.equals(cmg.getType())) {
				rs.add(cmg);
			}
		}
		return rs;
	}

	/**
	 * 
	 * @author 何波
	 * @Description: 验证修改的商品筛选器是否参加了有效的促销活动
	 * @param comboId
	 * @return Boolean
	 * @throws
	 */
	@RequestMapping("/product/combo/validatItemTagRule.json")
	@ResponseBody
	public Boolean validatItemTagRule(Long comboId) {
		List<PromotionCommand> pcs = sdkPromotionManager.getEffectPromotion(new Date());
		for (PromotionCommand pc : pcs) {
			if (pc.getProductComboId().equals(comboId)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 查询自定义筛选器
	 * 
	 * @return
	 */
	@RequestMapping("/product/combo/userDefinedList.json")
	@ResponseBody
	public Object findUserDefinedList() {
		Long shopId = shopManager.getShopId(getUserDetails());
		List<CustomizeFilterClass> customFilterClassList = sdkCustomizerFilterClassManager.findEffectCustomizeFilterClassListByTypeAndShopId(CustomScopeType.ITEM, shopId);
		return customFilterClassList;
	}
}
