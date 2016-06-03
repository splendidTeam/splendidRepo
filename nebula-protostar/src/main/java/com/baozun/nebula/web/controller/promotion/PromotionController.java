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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.ProductComboDetailsCommand;
import com.baozun.nebula.command.promotion.AudienceCommand;
import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.command.promotion.HeadCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.command.promotion.ScopeCommand;
import com.baozun.nebula.command.promotion.SettingNormalCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.command.rule.MiniItemAtomCommand;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.EngineWatchInvoke;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.promotion.PromotionManager;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.promotion.PromotionHead;
import com.baozun.nebula.model.rule.CustomScopeType;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.sdk.manager.SdkItemTagRuleManager;
import com.baozun.nebula.sdk.manager.SdkMemberTagRuleManager;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;
import com.baozun.nebula.solr.utils.DatePattern;
import com.baozun.nebula.utils.compare.ItemPriceComparator;
import com.baozun.nebula.utils.property.PropertyUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author 项硕
 */
@Controller
public class PromotionController extends BaseController {

	private final static Log log = LogFactory.getLog(PromotionController.class);
	
	@Autowired
	private PromotionManager promotionManager;
	@Autowired
	private ShopManager shopManager;
	@Autowired
	private SdkPromotionManager sdkPromotionManager;
	@Autowired
	private SdkPromotionCouponManager sdkPromotionCouponManager;
	@Autowired
	private SdkMemberTagRuleManager sdkMemberTagRuleManager;
	@Autowired
	private SdkItemTagRuleManager sdkItemTagRuleManager;
	@Autowired
	private ZkOperator zkOperator;
	@Autowired
	private SdkCustomizeFilterClassManager sdkCustomizeFilterClassManager;
	// 何波 注入商品管理接口
	@Autowired
	private  ItemManager itemManager;
	
	public  final String CALL= "call()";
	public  final String CID= "cid";
	public  final String NOT_CID= "!cid";
	public  final String PID= "pid";
	public  final String NOT_PID= "!pid";
	
	@Value("#{meta['frontend.url']}")
	private  String  frontend_url;
	@Value("#{meta['pdpPrefix']}")
	private  String  pdpPrefix;
	@Value("#{meta['pdp.param.type']}")
	private  String  pdp_param_type;
	
	private String getPdpInfoUrl(){
		return frontend_url.trim()+pdpPrefix.trim()+pdp_param_type.trim();
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				DatePattern.commonWithTime);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	/**
	 * 前往促销编辑列表页
	 */
	@RequestMapping(value = "/promotion/promotionEdit.htm", method = RequestMethod.GET)
	public String promotionEdit(Model  model) {
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/promotion/promotion-editorList";
	}

	/**
	 * 获取促销编辑列表页数据
	 * 
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/promotion/promotionEditList.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<PromotionQueryCommand> promotionEditList(Model model,
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts))
			sorts = Sort.parse("h.create_time desc"); // 默认排序

		queryBean.getParaMap().put("shopId",
				shopManager.getShopId(super.getUserDetails())); // 多店铺支持
		Pagination<PromotionQueryCommand> pagination = promotionManager
				.findInactivePromotionListConditionallyWithPage(
						queryBean.getPage(), sorts, queryBean.getParaMap());
		return pagination;
	}

	/**
	 * 前往促销启用列表页
	 */
	@RequestMapping(value = "/promotion/promotionList.htm", method = RequestMethod.GET)
	public String promotionList() {
		return "/promotion/promotion-list";
	}

	/**
	 * 获取促销启用列表页数据
	 * 
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/promotion/promotionList.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<PromotionQueryCommand> promotionList(Model model,
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts))
			sorts = Sort.parse("h.create_time desc"); // 默认排序

		queryBean.getParaMap().put("shopId",
				shopManager.getShopId(super.getUserDetails())); // 多店铺支持
		Pagination<PromotionQueryCommand> pagination = promotionManager
				.findCompletePromotionListConditionallyWithPage(
						queryBean.getPage(), sorts, queryBean.getParaMap());
		return pagination;
	}

	/**
	 * 启用前检查
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/checkBeforeActivation.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity checkBeforeActivation(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		Long shopId = shopManager.getShopId(super.getUserDetails());
		try {
			List<PromotionQueryCommand> list = sdkPromotionManager.findConflictingPromotionListById(id, shopId);
			rs.setIsSuccess(true);
			rs.setDescription(list);
		} catch (BusinessException e) {
				rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}

	/**
	 * 
	* @author 何波
	* @Description: 检查不同活动之间礼品是否冲突
	* @param id
	* @return   
	* BackWarnEntity   
	* @throws
	 */
	@RequestMapping(value = "/promotion/checkConflictNotActvieGift.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity checkConflictNotActvieGift(Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkPromotionManager.checkConflictNotActvieGift(id,shopManager.getShopId(super.getUserDetails()));
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			log.error("检查不同活动之间礼品是否冲突", e);
			rs.setErrorCode(e.getErrorCode());
			rs.setDescription(e.getMessage());
		} catch (Exception e) {
			log.error("检查不同活动之间礼品是否冲突", e);
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	/**
	 * 启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/activate.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity activate(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkPromotionManager.activatePromotionById(id, getUserDetails()
					.getUserId());

			zkOperator.noticeZkServer(zkOperator.getPath(EngineWatchInvoke.PATH_KEY));
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
	 * 取消启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/inactivate.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity inactivate(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkPromotionManager.inactivatePromotionById(id, getUserDetails()
					.getUserId());

			zkOperator.noticeZkServer(zkOperator.getPath(EngineWatchInvoke.PATH_KEY));

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
	 * 前往创建促销页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/create.htm", method = RequestMethod.GET)
	public String goCreatePromotionPage(Model model) {
		List<PromotionCouponCommand> couponList = sdkPromotionCouponManager
				.findAllcouponList();
		model.addAttribute("couponList", couponList);
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/promotion/promotion-edit";
	}

	/**
	 * 检查促销名称是否重名
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/promotion/check-name.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity checkPromotionName(
			@RequestParam(required = false) Long id, @RequestParam String name) {
		if (!promotionManager.checkPromotionName(id, name)) {
			throw new BusinessException(ErrorCodes.PROMOTION_REPEATED_NAME,
					new Object[] { name });
		}
		return SUCCESS;
	}

	/**
	 * 创建促销头部
	 * 
	 * @param head
	 * @return
	 */
	@RequestMapping(value = "/promotion/step-one.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepOne(HeadCommand head) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			UserDetails user = getUserDetails();
			head.setShopId(shopManager.getShopId(user));
			Long id = promotionManager
					.savePromotionHead(head, user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 根据类型获取会员筛选器列表
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/promotion/member-filter-list.json", method = RequestMethod.POST)
	@ResponseBody
	public List<MemberTagRuleCommand> findMemberFilterList(
			@RequestParam Integer type) {
		return sdkMemberTagRuleManager.findCustomMemberGroupListByType(type);
	}

	/**
	 * 创建促销人群
	 * 
	 * @param audience
	 * @return
	 */
	@RequestMapping(value = "/promotion/step-two.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepTwo(AudienceCommand audience) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			MemberTagRuleCommand cmg = sdkMemberTagRuleManager
					.findMemberTagRuleCommandById(audience.getComboId());
			if (null == cmg) {
				throw new BusinessException(
						ErrorCodes.MEMBER_FILTER_INEXISTENCE);
			}

			audience.setComboExpression(cmg.getExpression());
			audience.setComboName(cmg.getName());
			audience.setComboType(cmg.getType());

			UserDetails user = getUserDetails();
			Long id = promotionManager.savePromotionAudience(audience,
					user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 根据类型获取商品筛选器列表
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/promotion/product-filter-list.json", method = RequestMethod.POST)
	@ResponseBody
	public List<ItemTagRuleCommand> findProductFilterList(
			@RequestParam Integer type) {
		Long shopId = shopManager.getShopId(super.getUserDetails());
		return sdkItemTagRuleManager.findCustomProductComboListByType(type, shopId);
	}

	/**
	 * 根据商品筛选器ID获取其信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/promotion/product-filter-info.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findProductFilterInfo(@RequestParam Long id) {
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("isSuccess", false);
		ItemTagRuleCommand combo = sdkItemTagRuleManager.findCustomProductComboById(id);
		rs.put("combo", combo);
		try {
			if (!ItemTagRule.TYPE_COMBO.equals(combo.getType())) { // 原子类型: 商品类型, 分类类型, 自定义类型
				ProductComboDetailsCommand details = sdkItemTagRuleManager.findDetailsById(id);
				List<MiniItemAtomCommand>  mtacs = details.getAtomList();
				mtacs = clearReptDatas(mtacs);
				details.setAtomList(mtacs);
				rs.put("details", details);
			} else { // 组合类型
				List<ProductComboDetailsCommand> detailsList = sdkItemTagRuleManager.findDetailsListById(id);
				for (int i = 0; i < detailsList.size(); i++) {
					ProductComboDetailsCommand details =detailsList.get(i);
					Integer type =details.getType();
					if(type==1){
						detailsList.get(i).setAtomList(clearReptDatas(details.getAtomList()));
					}
				}
				rs.put("detailsList", detailsList);
			}
			rs.put("isSuccess", true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.put("description", getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.put("description", e.getMessage());
		}
		return rs;
	}

	
	/**
	* @author 何波
	* @Description:去掉重复商品类型
	* @param mtacs
	* @return   
	* List<MiniItemAtomCommand>   
	* @throws
	 */
	private List<MiniItemAtomCommand> clearReptDatas(
			List<MiniItemAtomCommand> mtacs) {
		if (mtacs != null && mtacs.size() > 0) {
			for (int i = 0; i < mtacs.size(); i++) {
				MiniItemAtomCommand atomCommand = mtacs.get(i);
				if(atomCommand.getIsOut()==false){
					for (int j = mtacs.size() - 1; j > i; j--) {
						MiniItemAtomCommand returnAtomCommand = mtacs.get(j);
						if (atomCommand.getId().equals(returnAtomCommand.getId())) {
							mtacs.remove(j);
						}
					}
				}
				
			}
		}
		return mtacs;
	}

	/**
	 * 创建促销范围
	 * 
	 * @param scope
	 * @return
	 */
	@RequestMapping(value = "/promotion/step-three.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepThree(ScopeCommand scope) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			ItemTagRuleCommand cpc = sdkItemTagRuleManager
					.findCustomProductComboById(scope.getComboId());
			if (null == cpc) {
				throw new BusinessException(
						ErrorCodes.PRODUCT_FILTER_INEXISTENCE);
			}

			scope.setComboExpression(cpc.getExpression());
			scope.setComboName(cpc.getName());
			scope.setComboType(cpc.getType());

			UserDetails user = getUserDetails();
			Long id = promotionManager.savePromotionScope(scope,
					user.getUserId());
			rs.setDescription(id);
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 创建促销条件
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/promotion/step-four.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepFour(ConditionNormalCommand condition) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			Long id = promotionManager.savePromotionCondition(condition,
					getUserDetails().getUserId());
			rs.setDescription(id);
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 创建促销优惠设置
	 * 
	 * @param setting
	 * @return
	 */
	@RequestMapping(value = "/promotion/step-five.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepFive(SettingNormalCommand setting) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			Long id = promotionManager.savePromotionSetting(setting,
					getUserDetails().getUserId());
			rs.setDescription(id);
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 前往促销编辑页-编辑
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/update.htm", method = RequestMethod.GET)
	public String goUpdatePromotionPage(@RequestParam Long id, Model model) {
		try {
			Long userId = getUserDetails().getUserId();
//			Long shopId = shopManager.getShopId(getUserDetails());
			PromotionCommand promotion = promotionManager.findPromotionById(id);
			promotion.setLifecycle(sdkPromotionManager.calculateLifecycle(
					promotion.getLifecycle(), promotion.getStartTime(),
					promotion.getEndTime())); // 设置lifecycle
			if (PromotionHead.LIFECYCLE_ACTIVATED.equals(promotion
					.getLifecycle())) { // 如果是在启用期，则回到待启用
				sdkPromotionManager.inactivatePromotionById(id, userId);
			}
			if (PromotionHead.LIFECYCLE_EFFECTIVE.equals(promotion
					.getLifecycle())) { // 如果是在生效期，则返回副本
				Long copyId = promotionManager.copyPromotion(id, userId, true);
				promotion = promotionManager.findPromotionById(copyId);
			}
			List<PromotionCouponCommand> couponList = sdkPromotionCouponManager.findAllcouponList();
			boolean isConditionFrozen = false; // 是否冻结条件类型下拉框
			if (null != promotion.getConditionNormal()) {
				isConditionFrozen = true;
			}
			boolean isEffective = null != promotion.getCopyFrom(); // 是否是生效期
			model.addAttribute("couponList", couponList);
			model.addAttribute("promotion", promotion);
			model.addAttribute("isConditionFrozen", isConditionFrozen);
			model.addAttribute("isEffective", isEffective);
			model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/promotion/promotion-edit";
	}

	/**
	 * 删除步骤4和步骤5
	 * 
	 * @param id
	 *            促销id
	 * @return
	 */
	@RequestMapping(value = "/promotion/delete-step.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity deleteStep(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			promotionManager.deleteConditionAndSettingByPromotionId(id);
			rs.setIsSuccess(true);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(1));
		}
		return rs;
	}

	/**
	 * 前往促销编辑页-复制草稿
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/copy.htm", method = RequestMethod.GET)
	public String copy(@RequestParam Long id, Model model) {
		try {
			Long copyId = promotionManager.copyPromotion(id, getUserDetails()
					.getUserId(), false);
			PromotionCommand promotion = promotionManager
					.findPromotionById(copyId);
			List<PromotionCouponCommand> couponList = sdkPromotionCouponManager
					.findAllcouponList();
			boolean isConditionFrozen = false; // 是否冻结条件类型下拉框
			if (null != promotion.getConditionNormal()) {
				isConditionFrozen = true;
			}
			model.addAttribute("couponList", couponList);
			model.addAttribute("promotion", promotion);
			model.addAttribute("isConditionFrozen", isConditionFrozen);
			model.addAttribute("pdp_base_url",PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/promotion/promotion-edit";
	}

	/**
	 * 前往促销查看页
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/view.htm", method = RequestMethod.GET)
	public String view(@RequestParam Long id, Model model) {
		try {
			PromotionCommand promotion = promotionManager.findPromotionById(id);
			List<PromotionCouponCommand> couponList = sdkPromotionCouponManager
					.findAllcouponList();
			model.addAttribute("isView", true);
			model.addAttribute("couponList", couponList);
			model.addAttribute("promotion", promotion);
			model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/promotion/promotion-edit";
	}

	/**
	 * @author 何波
	 * @Description: 通过分类id获取商品并计算出最低价格
	 * @param cateIds
	 *            , type 商品 分类 组合
	 * @return BackWarnEntity
	 * @throws
	 */
	@RequestMapping(value = "/promotion/getItemsByCateIds.json")
	@ResponseBody
	public  BigDecimal getItemsByCateIds(String[] inProItems,int proType) {
		Map<String ,ItemInfo>  prices = new HashMap<String ,ItemInfo>();
		//包含的全部商品
		List<ItemInfo>   inAllitems =   new ArrayList<ItemInfo>();
		//排除的全部商品
		List<ItemInfo>   exAllitems =   new ArrayList<ItemInfo>();
		String  id =  inProItems[0];
		String ruleId = id.substring(id.indexOf(":") + 1, id.length());
		ItemTagRuleCommand itemTagRuleCommand = sdkItemTagRuleManager.findCustomProductComboById(Long.parseLong(ruleId));
		String exp = itemTagRuleCommand.getExpression();
		Integer type =itemTagRuleCommand.getType();
		if(type==2){
			//分类id
			dealExp(inAllitems, exAllitems, exp);
		}else if(type==1){
			//商品id
			Long[]  itemids = splitRuleId(exp);
			inAllitems.addAll(itemManager.findItemInfosByItemIds(Arrays.asList(itemids)));
		}else if(type==3){
			//自定义商品筛选器
			Long[]  ids = splitRuleId(exp);
			List<Long>  itemids = null;
			if (null!=ids)
			{
				for (Long cstid : ids) {
					itemids = SdkCustomizeFilterLoader.load(cstid.toString());
					inAllitems.addAll(itemManager.findItemInfosByItemIds(itemids));
				}
			}
		}else if(type==4){
			String[] rules = exp.split("&");
			for (int i = 0; i < rules.length; i++) {
				String rule = rules[i];
				Long[] cmbruleIds = splitRuleId(rule);
				List<ItemTagRuleCommand> itemTagRuleCommands =sdkItemTagRuleManager.findEffectItemTagRuleListByIdList(Arrays.asList(cmbruleIds));
				for (ItemTagRuleCommand itemTagRuleCommand1 : itemTagRuleCommands) {
					String exp1 = itemTagRuleCommand1.getExpression();
					dealExp(inAllitems, exAllitems, exp1);
				}
			}
		}
		//将包含商品加入map 中 将 key 商品id value 商品
		if(inAllitems != null && inAllitems.size() > 0){
			for (int i = 0; i <inAllitems.size(); i++) {
				prices.put((inAllitems.get(i).getItemId()+"").trim(), inAllitems.get(i));
			}
		}
	
		if(exAllitems != null && exAllitems.size() > 0){
			for (int i = 0; i <exAllitems.size(); i++) {
				prices.remove((exAllitems.get(i).getId()+"").trim());
			}
		}
		List<ItemInfo> itemIds = new ArrayList<ItemInfo>();
		for (String key : prices.keySet()) {
			ItemInfo  value = prices.get(key);
			if (value.getSalePrice() != null) {
				itemIds.add(value);
			}
		}
		if(itemIds != null && itemIds.size() > 0){
			//将对象价格进行排序
			Collections.sort(itemIds, new ItemPriceComparator());
			return itemIds.get(0).getSalePrice();
		}
		return new BigDecimal(0);
	}
	
	/**
	 * @author 何波
	 * @Description: 通过分类id获取商品并计算出最低价格
	 * @param cateIds
	 *            , type 商品 分类 组合
	 * @return BackWarnEntity
	 * @throws
	 */
	@RequestMapping(value = "/promotion/validateChooseItem.json")
	@ResponseBody
	public  BackWarnEntity validateChooseItem(Long comboId,Long[] itemIds) {
		//包含的全部商品
		List<ItemInfo>   inAllitems =   new ArrayList<ItemInfo>();
		//排除的全部商品
		List<ItemInfo>   exAllitems =   new ArrayList<ItemInfo>();
		String ruleId =String.valueOf(comboId) ;
		ItemTagRuleCommand itemTagRuleCommand = sdkItemTagRuleManager.findCustomProductComboById(Long.parseLong(ruleId));
		String exp = itemTagRuleCommand.getExpression();
		Integer type =itemTagRuleCommand.getType();
		List<Long> reitemIds = new ArrayList<Long>();
		if(type==2){
			//分类id
			dealExp(inAllitems, exAllitems, exp);
		}else if(type==1){
			//商品id
			Long[]  itemids = splitRuleId(exp);
			inAllitems.addAll(itemManager.findItemInfosByItemIds(Arrays.asList(itemids)));
		}else if(type==4){
			String[] rules = exp.split("&");
			for (int i = 0; i < rules.length; i++) {
				String rule = rules[i];
				Long[] cmbruleIds = splitRuleId(rule);
				List<ItemTagRuleCommand> itemTagRuleCommands =sdkItemTagRuleManager.findEffectItemTagRuleListByIdList(Arrays.asList(cmbruleIds));
				for (ItemTagRuleCommand itemTagRuleCommand1 : itemTagRuleCommands) {
					String exp1 = itemTagRuleCommand1.getExpression();
					dealExp(inAllitems, exAllitems, exp1);
				}
			}
		}
		if(inAllitems.size()>0){
			for (ItemInfo itemInfo : inAllitems) {
				Long id = itemInfo.getItemId();
				for (Long itemId : itemIds) {
					if(id.equals(itemId)){
					if(!reitemIds.contains(itemId)){
						reitemIds.add(itemId);
					}
					}
				}
			}
		}
		if(reitemIds.size()==0){
			return null;
		}
		List<ItemInfo> infos = new ArrayList<ItemInfo>();
		for (Long itemId : reitemIds) {
			infos.add(itemManager.findItemInfoByItemId(itemId));
		}
		String result = "";
		for (ItemInfo itemInfo : infos) {
			result=result+itemInfo.getTitle()+" ";
		}
		BackWarnEntity back = new BackWarnEntity();
		back.setDescription(result);
		return back;
	}
	
	private void dealExp(List<ItemInfo> initems, List<ItemInfo> exitems,
			String exp) {
		if(exp.indexOf(CALL)>-1){
			String[] rules = exp.split("&");
			List<Long> cateIds  = new ArrayList<Long>();
			List<Long> itemIds= new ArrayList<Long>();
			for (String rule : rules) {
				
				if(rule.startsWith(NOT_CID)){
					String[]  ids = rule.substring(rule.indexOf("(") + 1, rule.lastIndexOf(")")).split(",");
					for (String id : ids) {
						cateIds.add(Long.parseLong(id));
					}
				}
				if(rule.startsWith(NOT_PID)){
					String[]  ids = rule.substring(rule.indexOf("(") + 1, rule.lastIndexOf(")")).split(",");
					for (String id : ids) {
						itemIds.add(Long.parseLong(id));
					}
				}
			}
			initems.addAll(itemManager.findCallItemInfos(cateIds, itemIds));
		}else{
			String[] rules = exp.split("&");
			for (int i = 0; i < rules.length; i++) {
				String rule = rules[i];
				Long[] cmbruleIds = splitRuleId(rule);
				// 包含的商品
				if (!rule.startsWith("!")) {
					initems.addAll(getProTypeInItem(rule, cmbruleIds));
				} else {
					// 排除的商品
					exitems.addAll(getProTypeExItem(rule, cmbruleIds));
				}
			}
		}
		
	}
	
	private  List<ItemInfo> getProTypeInItem(String rule,Long[] ruleIds) {
		List<ItemInfo>  inItemInfos = new ArrayList<ItemInfo>();
		// 包含的分类
		if (rule.startsWith(CID)) {
			// 通过分类获取 商品最低价格
			inItemInfos.addAll(itemManager.findItemInfosByCateIds(Arrays.asList(ruleIds)));
		}
		// 包含的商品
		if (rule.startsWith(PID)) {
			inItemInfos.addAll(itemManager.findItemInfosByItemIds(Arrays.asList(ruleIds)));
		}
		return inItemInfos;
	}
	
	private List<ItemInfo>  getProTypeExItem(String rule,Long[] ruleIds) {
		
		List<ItemInfo>  exItemInfos = new ArrayList<ItemInfo>();
		// 排除的分类
		if (rule.startsWith(NOT_CID)) {
			exItemInfos.addAll(itemManager.findItemInfosByCateIds(Arrays.asList(ruleIds)));
		}
		// 排除的商品
		if (rule.startsWith(NOT_PID)) {
			exItemInfos.addAll(itemManager.findItemInfosByItemIds(Arrays.asList(ruleIds)));
		}
		return exItemInfos;
	}
	
	private  Long[] splitRuleId(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		String[] arr = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).split(",");
		Long[] larr = new Long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			larr[i] = Long.parseLong(arr[i]);
		}
		return larr;
	}
	
	/**
	 * 查询自定义条件
	 * @return
	 */
	@RequestMapping("/promotion/findCustomizeFilterClass.json")
	@ResponseBody
	public Object findCustomizeFilterClass(){
		Long shopId = shopManager.getShopId(getUserDetails());
		// 自定义条件
		List<CustomizeFilterClass> customizeFilterClassList = sdkCustomizeFilterClassManager.findEffectCustomizeFilterClassListByTypeAndShopId(CustomScopeType.CONDITION, shopId);
		return customizeFilterClassList;
	}
	/**
	 * 查询自定义优惠设置
	 * @return
	 */
	@RequestMapping("/promotion/findCustomizeSettingClass.json")
	@ResponseBody
	public Object findCustomizeSettingClass(){
		Long shopId = shopManager.getShopId(getUserDetails());
		// 自定义优惠设置
		List<CustomizeFilterClass> customizeFilterClassList = sdkCustomizeFilterClassManager.findEffectCustomizeFilterClassListByTypeAndShopId(CustomScopeType.SETTING, shopId);
		return customizeFilterClassList;
	}
}
