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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.EngineWatchInvoke;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.member.MemberGroupManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.rule.CustomScopeType;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.sdk.manager.SdkMemberTagRuleManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;
import com.baozun.nebula.solr.utils.DatePattern;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author 项硕
 */
@Controller
public class MemberTagRuleController extends BaseController {

	@Autowired
	private SdkMemberTagRuleManager			sdkMemberTagRuleManager;
	@Autowired
	private MemberManager					memberManager;
	@Autowired
	private MemberGroupManager				memberGroupManager;
	@Autowired
	private ZkOperator				zkOperator;
	@Autowired
	private SdkPromotionManager				sdkPromotionManager;
	@Autowired
	private SdkCustomizeFilterClassManager	sdkCustomizerFilterClassManager;

	/** 会员筛选器列表页-默认排序（创建时间降序） */
	private static final Sort[]				LIST_PAGE_DEFAULT_SORTS	= Sort.parse("mr.create_time desc");

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.commonWithTime);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 前往会员标签规则列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/member/membercombolist.htm", method = RequestMethod.GET)
	public String goMemberTagRulePage() {
		return "/rule/member-tag-rule-list";
	}

	/**
	 * 会员标签规则列表页数据
	 * 
	 * @param queryBean
	 * @return
	 */
	@RequestMapping(value = "/member/memberCustomgroupList.json", method = RequestMethod.GET)
	@ResponseBody
	public Pagination<MemberTagRuleCommand> findMemberTagRuleList(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = ArrayUtils.isEmpty(queryBean.getSorts()) ? LIST_PAGE_DEFAULT_SORTS : queryBean.getSorts();
		Pagination<MemberTagRuleCommand> pagination = sdkMemberTagRuleManager
				.findMemberTagRuleListConditionallyWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
		return pagination;
	}

	/**
	 * 启用会员标签规则
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/member/activateMemberTagRule.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity activateMemberTagRule(@RequestParam("id") Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkMemberTagRuleManager.activateMemberTagRule(id);
			rs.setIsSuccess(true);
			zkOperator.noticeZkServer(zkOperator.getPath(EngineWatchInvoke.PATH_KEY));
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
	 * 禁用会员标签规则
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/member/inactivateMemberTagRule.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity inactivateMemberTagRule(@RequestParam("id") Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkMemberTagRuleManager.inactivateMemberTagRule(id);
			rs.setIsSuccess(true);
			zkOperator.noticeZkServer(zkOperator.getPath(EngineWatchInvoke.PATH_KEY));
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
	 * 前往会员组合创建页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/combo/add.htm", method = RequestMethod.GET)
	public String goAddPage(Model model) {
		List<MemberGroup> groupList = memberGroupManager.findMemberGroupListByQueryMap(null, Sort.parse("mgr.id asc"));
		model.addAttribute("groupList", groupList);
		return "rule/member-tag-rule-edit";
	}

	/**
	 * 创建筛选器
	 * 
	 * @param customMemberGroup
	 * @return
	 */
	@RequestMapping(value = "/member/combo/add.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity add(MemberTagRuleCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			cmd.setCreateId(getUserDetails().getUserId());
			cmd.setCreateTime(new Date());
			sdkMemberTagRuleManager.save(cmd);
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
	 * 验证分组是否包含会员
	 * 
	 * @param members
	 * @param groups
	 * @return
	 */
	@RequestMapping(value = "/member/combo/check.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity checkGroup(@RequestParam String members, @RequestParam String groups) {
		try {
			if (sdkMemberTagRuleManager.checkGroupWithMember(members, groups)) {
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
	 * 查看组合
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/member/combo/view.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> view(@RequestParam Long id) {
		Map<String, Object> rs = new HashMap<String, Object>();
		MemberTagRuleCommand cmg = sdkMemberTagRuleManager.findMemberTagRuleCommandById(id);
		if (null == cmg) {
			rs.put("isSuccess", false);
			rs.put("errorCode", Constants.MEMBER_CUSTOM_GROUP_INEXISTED);
			return rs;
		}
		rs.put("combo", cmg);

		String exp = cmg.getExpression();
		List<SimpleMemberCombo> list = sdkMemberTagRuleManager.findSimpleMemberComboListByExpression(exp);
		List<SimpleMemberCombo> incList = new ArrayList<SimpleMemberCombo>(); // 包含列表
		List<SimpleMemberCombo> excList = new ArrayList<SimpleMemberCombo>(); // 排除列表

		for (SimpleMemberCombo smc : list) {
			if (smc.isExcluded()) {
				excList.add(smc);
			} else {
				incList.add(smc);
			}
		}
		rs.put("isSuccess", true);
		rs.put("incList", incList);
		rs.put("excList", excList);

		return rs;
	}

	/**
	 * 前往会员组合更新页
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/combo/update.htm", method = RequestMethod.GET)
	public String goUpdatePage(@RequestParam Long id, Model model) {
		MemberTagRuleCommand cmg = sdkMemberTagRuleManager.findMemberTagRuleCommandById(id);
		List<MemberGroup> groupList = memberGroupManager.findMemberGroupListByQueryMap(null, Sort.parse("mgr.id asc"));
		List<MemberTagRuleCommand> customList = sdkMemberTagRuleManager.findAllAvailableMemberTagRuleCommandList();

		String exp = cmg.getExpression();
		List<SimpleMemberCombo> list = sdkMemberTagRuleManager.findSimpleMemberComboListByExpression(exp);
		List<SimpleMemberCombo> incList = new ArrayList<SimpleMemberCombo>(); // 包含列表
		List<SimpleMemberCombo> excList = new ArrayList<SimpleMemberCombo>(); // 排除列表

		for (SimpleMemberCombo smc : list) {
			if (smc.isExcluded()) {
				excList.add(smc);
			} else {
				incList.add(smc);
			}
		}
		
		model.addAttribute("combo", cmg);
		model.addAttribute("groupList", groupList);
		model.addAttribute("customList", customList);
		model.addAttribute("incList", incList);
		model.addAttribute("excList", excList);
		model.addAttribute("isUpdate", true);
		return "rule/member-tag-rule-edit";
	}

	/**
	 * 修改组合
	 * 
	 * @param cmd
	 * @return
	 */
	@RequestMapping(value = "/member/combo/update.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity update(MemberTagRuleCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkMemberTagRuleManager.update(cmd);
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
	 * 获取会员和分组筛选器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/member/combo/custom-list.json", method = RequestMethod.POST)
	@ResponseBody
	public List<MemberTagRuleCommand> findComboList() {
		return sdkMemberTagRuleManager.findAvailableAtomicMemberTagRuleList();
	}
	
	/**
	 * 
	 * @author 何波
	 * @Description: 验证修改的会员筛选器是否参加了有效的促销活动
	 * @param comboId
	 * @return Boolean
	 * @throws
	 */
	@RequestMapping("/member/combo/validatMemTagRule.json")
	@ResponseBody
	public Boolean validatItemTagRule(Long comboId) {
		List<PromotionCommand> pcs = sdkPromotionManager.getEffectPromotion(new Date());
		for (PromotionCommand pc : pcs) {
			if (pc.getMemComboId().equals(comboId)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询自定义筛选器
	 * @return
	 */
	@RequestMapping("/member/combo/userDefinedList.json")
	@ResponseBody
	public Object findUserDefinedList(){
		return sdkCustomizerFilterClassManager.findEffectCustomizeFilterClassListByTypeAndShopId(CustomScopeType.MEMBER, null);
	}
}
