package com.baozun.nebula.web.controller.rule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.rule.CustomizeFilterClassCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.rule.CustomScopeType;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeConditionLoader;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeSettingLoader;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

@Controller
public class CsFilterClassRuleController extends BaseController {
	
	private final static Logger log = LoggerFactory.getLogger(CsFilterClassRuleController.class);

	private static final Integer			SCOPE_TYPE_MEMBER	= 2;

	@Autowired
	private ShopManager						shopManager;

	@Autowired
	private MemberManager					memberManager;

	@Autowired
	private ItemManager						itemManager;

	@Autowired
	private SdkCustomizeFilterClassManager	sdkCustomizeFilterClassManager;

	@RequestMapping(value = "/rule/csfilterclseslist.htm")
	public String toCsFilterClsPage(Model model) {

		return "/rule/custom-filter-rule-list";
	}

	/**
	 * 获取数据库自定义筛选信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/rule/customizeFilterList.json")
	@ResponseBody
	public Pagination<CustomizeFilterClassCommand> customizeFilterList(Model model, @QueryBeanParam QueryBean queryBean) {

		// Long shopId = shopManager.getShopId(getUserDetails());
		Sort[] sorts = queryBean.getSorts();

		if (Validator.isNullOrEmpty(sorts)) {
			Sort sort = new Sort("version", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		;
		Pagination<CustomizeFilterClassCommand> cfList = sdkCustomizeFilterClassManager
				.findCustomizeFilterClassListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
		return cfList;
	}

	@RequestMapping("/rule/validateCustomizeFilter.json")
	@ResponseBody
	public Object validateCustomizeFilter(@RequestParam(value = "id", required = false) Long id,
			HttpServletRequest request, HttpServletResponse response) {
		BackWarnEntity backWarnEntity = new BackWarnEntity();

		try {
			CustomizeFilterClass customizeFilterClass = sdkCustomizeFilterClassManager.findCustomizeFilterClassById(id);
			if(customizeFilterClass != null){
				Integer scopeType = customizeFilterClass.getScopeType();
				if(CustomScopeType.MEMBER.equals(scopeType) || CustomScopeType.ITEM.equals(scopeType)){
					SdkCustomizeFilterLoader.load(String.valueOf(id));
				}else if(CustomScopeType.CONDITION.equals(scopeType)){
					AtomicCondition condition = new AtomicCondition();					
					condition.setConditionValue(BigDecimal.valueOf(id));
					SdkCustomizeConditionLoader.load(String.valueOf(id), null, null, null);
				}else if(CustomScopeType.SETTING.equals(scopeType)){
					AtomicSetting set = new AtomicSetting();
					set.setSettingTag(scopeType.toString());
					set.setSettingValue(BigDecimal.valueOf(id));
					SdkCustomizeSettingLoader.load(set, null, null, null);
				}
			}
			backWarnEntity.setIsSuccess(true);
		} catch (BusinessException e) {
			backWarnEntity.setIsSuccess(false);
			backWarnEntity.setDescription(e.getMessage());
		}
		return backWarnEntity;
	}

	
	@ResponseBody 
	@RequestMapping("/rule/initiativeCustomFilter.json")
	public Object initiativeCustomFilter(@RequestParam(value="id", required=false)Long id){ 
		// 更新cacheverion
		sdkCustomizeFilterClassManager.updateCacheVersion(id);
		return SUCCESS;
	 }
	 

	/**
	 * 禁用或启用自定义筛选器
	 * 
	 * @param id
	 * @param state
	 * @return
	 */
	@RequestMapping("/rule/enableOrDisableCustomizeFilterByIds.json")
	@ResponseBody
	public Object enableOrDisableCustomizeFilterByIds(@RequestParam("ids") String ids,
			@RequestParam("state") Integer state) {

		if (StringUtils.isNotBlank(ids)) {
			List<Long> idList = new ArrayList<Long>();
			String[] idStrs = ids.split(",");
			for (String idStr : idStrs) {
				idList.add(Long.valueOf(idStr));
			}
			try {
				sdkCustomizeFilterClassManager.enableOrDisableCustomizeFilterClassByIds(idList, state);
			} catch (Exception e) {
				e.printStackTrace();
				return FAILTRUE;
			}
		} else {
			return FAILTRUE;
		}

		return SUCCESS;
	}

	@RequestMapping("/rule/createCustomFilter.htm")
	public String toCreateCustomizeFilterPage(Model model) {

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);

		Sort[] paraSorts = Sort.parse("s.create_time desc");

		List<ShopCommand> shopCommandList = shopManager.findShopListByQueryMap(paraMap, paraSorts);

		model.addAttribute("shopCommandList", shopCommandList);

		return "/rule/custom-filter-add";
	}

	@RequestMapping("/rule/updateCustomFilter.htm")
	public String toUpdateCustomizeFilterPage(Model model, @RequestParam() Long id) {

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);

		Sort[] paraSorts = Sort.parse("s.create_time desc");

		List<ShopCommand> shopCommandList = shopManager.findShopListByQueryMap(paraMap, paraSorts);

		model.addAttribute("shopCommandList", shopCommandList);

		// mode
		CustomizeFilterClass customizeFilterClass = sdkCustomizeFilterClassManager.findCustomizeFilterClassById(id);

		model.addAttribute("customizeFilterClass", customizeFilterClass);

		return "/rule/custom-filter-update";
	}

	/**
	 * 保存自定义筛选器
	 * @param customizeFilterClass
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/rule/saveCustomFilter.json")
	@ResponseBody
	public Object saveCustomFilter(@ModelAttribute() CustomizeFilterClass customizeFilterClass,
			HttpServletRequest request, HttpServletResponse response) {

		// 会员是没有店铺(SHOPID)之分, 只有商品有店铺(SHOPID)之分
		if (CustomScopeType.MEMBER.equals(customizeFilterClass.getScopeType())) {
			customizeFilterClass.setShopId(null);
		}
		
		try {
			sdkCustomizeFilterClassManager.createOrUpdate(customizeFilterClass);
			return SUCCESS;
		} catch (Exception e) {
			return FAILTRUE;
		}
	}

	@RequestMapping("/rule/findCustomFilterCls.json")
	@ResponseBody
	public Object findCustomFilterCls(@RequestParam() Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		CustomizeFilterClass customizeFilterClass = sdkCustomizeFilterClassManager.findCustomizeFilterClassById(id);
		return customizeFilterClass;
	}

	@RequestMapping("/rule/resultForTestCustomFilterOnMember.json")
	@ResponseBody
	public Pagination<MemberCommand> resultForTestCustomFilterOnMember(@RequestParam() Long id, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		List<Long> listIds = null;
		try {
			listIds = SdkCustomizeFilterLoader.load(id.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		Long[] memberIds = null;
		if(null == listIds || listIds.isEmpty()){
			memberIds = new Long[0];
		}else{
			memberIds = listIds.toArray(new Long[listIds.size()]);
		}

		List<Member> memberList = memberManager.findMemberListByMemberIds(memberIds);

		List<MemberCommand> memberCommandList = new ArrayList<MemberCommand>();
		MemberCommand command = null;
		MemberPersonalDataCommand memberPersonalDataCommand = null;

		for (Member member : memberList) {
			command = new MemberCommand();
			command.setId(member.getId());
			memberPersonalDataCommand = memberManager.findMemberById(member.getId());
			command.setNickName(memberPersonalDataCommand.getNickname());
			command.setLifecycle(member.getLifecycle());
			command.setSex(memberPersonalDataCommand.getSex());
			command.setLoginName(member.getLoginName());
			memberCommandList.add(command);
		}

		Pagination<MemberCommand> page = new Pagination<MemberCommand>(memberCommandList, memberCommandList.size(), 1, 1, 0, Integer.MAX_VALUE);

		return page;
	}

	@RequestMapping("/rule/resultForTestCustomFilterOnItem.json")
	@ResponseBody
	public Pagination<ItemCommand> resultForTestCustomFilterOnItem(@RequestParam() Long id, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		List<Long> listIds = null;
		try {
			listIds = SdkCustomizeFilterLoader.load(id.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		List<ItemCommand> itemCommandList = itemManager.findItemCommandListByIds(listIds);
		Pagination<ItemCommand> page = new Pagination<ItemCommand>(itemCommandList, itemCommandList.size(), 1, 1, 0, Integer.MAX_VALUE);
		return page;
	}

}
