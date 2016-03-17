package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.member.SimpleMemberCombo;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.command.rule.MiniTagRuleCommand;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.member.MemberGroupDao;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.dao.rule.CustomizeFilterClassDao;
import com.baozun.nebula.dao.rule.MemberTagRuleDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkMemberTagRuleManager;
import com.baozun.nebula.sdk.utils.TagRuleUtils;

/**
 * 
 * @author 项硕
 */
@Transactional
@Service("sdkMemberTagRuleManager")
public class SdkMemberTagRuleManagerImpl implements SdkMemberTagRuleManager {

	private static final Logger log = LoggerFactory.getLogger(SdkMemberTagRuleManagerImpl.class);

	/** 前台传来的表达式中，包含列表与排除列表的分隔符 */
	private static final String EXCLUDE_DELIMITER = ";";
	/** 前台传来的表达式中，‘0’代表‘全体’ */
	private static final Long ALL_MEMBER_ID = 0L;
	/** 前台传来的表达式中，id之间的分隔符 */
	private static final String ID_DELIMITER = ",";
	/** 前台传来的表达式中，当分组类型时，以‘分号’分割后，数组长度必须为3 */
	private static final Integer LENGTH_AFTER_SPLIT = 3;
	/** i18n-全体 */
	private static final String ALL_STAFF = "member.filter.allStaff";

	@Autowired
	private MemberTagRuleDao memberTagRuleDao;
	@Autowired
	private MemberGroupRelationDao memberGroupRelationDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberGroupDao memberGroupDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CustomizeFilterClassDao customizeFilterClassDao;

	@Override
	@Transactional(readOnly=true)
	public Pagination<MemberTagRuleCommand> findMemberTagRuleListConditionallyWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		log.debug("Query: paraMap={}", paraMap);
		return memberTagRuleDao.findMemberTagRuleListConditionallyWithPage(page, sorts, paraMap);
	}

	@Override
	public int activateMemberTagRule(Long id) {
		int rs = memberTagRuleDao.updateLifecycleById(id, MemberTagRule.LIFECYCLE_ENABLE);
		if (rs == 0)
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_INEXISTED);
		return rs;
	}

	@Override
	public int inactivateMemberTagRule(Long id) {
		int rs = memberTagRuleDao.updateLifecycleById(id, MemberTagRule.LIFECYCLE_DISABLE);
		if (rs == 0)
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_INEXISTED);
		return rs;
	}

	/*
	 * ----------------------------------------------- X
	 * -----------------------------------------------
	 */

	@Override
	@Transactional(readOnly=true)
	public List<MemberTagRuleCommand> findAllAvailableMemberTagRuleCommandList() {
		return memberTagRuleDao.findAllAvailableMemberTagRuleCommandList();
	}

	@Override
	public void save(MemberTagRuleCommand cmd) {
		MemberTagRuleCommand dbModel = memberTagRuleDao.findByGroupName(cmd.getName());
		if (null != dbModel) // 名称重复
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_REPEATED_NAME);
		String exp = cmd.getExpression();
		cmd.setExpression(generateExpression(exp, cmd.getType()));
		MemberTagRule rule = new MemberTagRule();
		BeanUtils.copyProperties(cmd, rule);
		memberTagRuleDao.save(rule);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean checkGroupWithMember(String members, String groups) {
		List<String> memList = Arrays.asList(members.split(ID_DELIMITER));
		List<String> grpList = Arrays.asList(groups.split(ID_DELIMITER));
		for (String memId : memList) {
			List<MemberGroupRelation> relationList = memberGroupRelationDao.findMemberGroupRelationListByMemberId(new Long(memId));
			if (null == relationList || relationList.size() == 0)
				return false; // 该会员没有所属分组
			boolean has = false; // 是否包含
			for (MemberGroupRelation mgr : relationList) {
				if (grpList.contains(mgr.getGroupId().toString())) {
					has = true;
				}
			}
			if (!has)
				return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public MemberTagRuleCommand findMemberTagRuleCommandById(Long id) {
		return memberTagRuleDao.findMemberTagRuleById(id);
	}

	@Override
	public void update(MemberTagRuleCommand cmd) {
		MemberTagRuleCommand dbModel = memberTagRuleDao.findByGroupName(cmd.getName());
		if (null != dbModel && (!dbModel.getId().equals(cmd.getId()))) // 名称重复
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_REPEATED_NAME);
		String exp = cmd.getExpression();

		cmd.setExpression(generateExpression(exp, cmd.getType()));

		MemberTagRule rule = memberTagRuleDao.getByPrimaryKey(cmd.getId());
		rule.setExpression(cmd.getExpression());
		rule.setText(cmd.getText());
		rule.setName(cmd.getName());
	}

	/**
	 * 根据前端原始数据生成表达式
	 * 
	 * @param exp
	 * @param type
	 *            筛选器类型
	 * @return
	 */
	private String generateExpression(String exp, int type) {
		StringBuffer result = new StringBuffer();

		if (MemberTagRule.TYPE_MEMBER.equals(type)) { // 会员类型
			String template = MemberTagRule.EXP_MEMBER;
			result.append(template.replace(MemberTagRule.REGEX_PLACEHOLDER, exp));
		} else if (MemberTagRule.TYPE_GROUP.equals(type)) { // 分组类型
			String allTemplate = MemberTagRule.EXP_ALLMEMBER;
			String incTemplate = MemberTagRule.EXP_GROUP;
			String excGroup = MemberTagRule.REGEX_EXCLUDE_CONNECT + MemberTagRule.EXP_GROUP;
			String excMember = MemberTagRule.REGEX_EXCLUDE_CONNECT + MemberTagRule.EXP_MEMBER;
			// String excTemplate = MemberTagRule.REGEX_EXCLUDE_CONNECT +
			// MemberTagRule.EXP_MEMBER;

			String[] arr = exp.split(EXCLUDE_DELIMITER);
			if (arr.length != LENGTH_AFTER_SPLIT) {
				throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION);
			}
			String incStr = arr[0];
			String excGroupStr = arr[1].trim(); // 去掉空格
			String excMemberStr = arr[2].trim(); // 去掉空格
			if (ALL_MEMBER_ID.toString().equals(incStr)) { // 全体
				result.append(allTemplate);
			} else {
				result.append(incTemplate.replace(MemberTagRule.REGEX_PLACEHOLDER, incStr));
			}
			if (StringUtils.isNotBlank(excGroupStr)) {
				result.append(excGroup.replace(MemberTagRule.REGEX_PLACEHOLDER, excGroupStr));
			}
			if (StringUtils.isNotBlank(excMemberStr)) {
				result.append(excMember.replace(MemberTagRule.REGEX_PLACEHOLDER, excMemberStr));
			}
		} else if (MemberTagRule.TYPE_COMBO.equals(type)) { // 组合类型
			String template = MemberTagRule.EXP_COMBO;
			result.append(template.replace(MemberTagRule.REGEX_PLACEHOLDER, exp));
		} else if (MemberTagRule.TYPE_CUSTOM.equals(type)){ //自定义类型
			result.append(MemberTagRule.EXP_CUSTOM.replace(MemberTagRule.REGEX_PLACEHOLDER, exp));
		}

		return result.toString();
	}

	@Override
	@Transactional(readOnly=true)
	public List<MemberTagRuleCommand> findCustomMemberGroupListByType(Integer type) {
		return memberTagRuleDao.findCustomMemberGroupListByType(type);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SimpleMemberCombo> findSimpleMemberComboListByExpression(String exp) {
		List<SimpleMemberCombo> list = new ArrayList<SimpleMemberCombo>();
		Map<String, Set<MiniTagRuleCommand>> map = TagRuleUtils.member(exp);
		Set<MiniTagRuleCommand> inSet = map.get(MemberTagRule.ANALYSIS_KEY_IN);
		Set<MiniTagRuleCommand> outSet = map.get(MemberTagRule.ANALYSIS_KEY_OUT);

		List<Long> inIdList = new ArrayList<Long>();
		for (MiniTagRuleCommand cmd : inSet) {
			inIdList.add(cmd.getId());
		}
		int type = TagRuleUtils.getMemberType(exp);
		if (MemberTagRule.TYPE_MEMBER.equals(type)) { // 会员
			List<SimpleMemberCombo> memberList = memberDao.findSimpleMemberComboListByIdList(inIdList);
			for (SimpleMemberCombo cmd : memberList) {
				cmd.setExcluded(false);
			}
			list.addAll(memberList);
		} else if (MemberTagRule.TYPE_GROUP.equals(type)) { // 分组
			/* 包含 */
			if (exp.startsWith(MemberTagRule.EXP_PREFIX_ALLMEMBER)) { // 全体
				SimpleMemberCombo smc = new SimpleMemberCombo();
				smc.setId(ALL_MEMBER_ID);
				smc.setName(messageSource.getMessage(ALL_STAFF, null, LocaleContextHolder.getLocale()));
				list.add(smc);
			} else {
				List<SimpleMemberCombo> groupList = memberGroupDao.findSimpleMemberComboListByIdList(inIdList);
				for (SimpleMemberCombo cmd : groupList) {
					cmd.setExcluded(false);
				}
				list.addAll(groupList);
			}

			/* 排除 */
			List<Long> outMemberIdList = new ArrayList<Long>();
			List<Long> outGroupIdList = new ArrayList<Long>();
			for (MiniTagRuleCommand cmd : outSet) {
				if (MemberTagRule.TYPE_MEMBER.equals(cmd.getType())) { // 排除会员
					outMemberIdList.add(cmd.getId());
				} else if (MemberTagRule.TYPE_GROUP.equals(cmd.getType())) { // 排除分组
					outGroupIdList.add(cmd.getId());
				}
			}
			List<SimpleMemberCombo> memberList = memberDao.findSimpleMemberComboListByIdList(outMemberIdList);
			for (SimpleMemberCombo cmd : memberList) {
				cmd.setExcluded(true);
			}
			List<SimpleMemberCombo> groupList = memberGroupDao.findSimpleMemberComboListByIdList(outGroupIdList);
			for (SimpleMemberCombo cmd : groupList) {
				cmd.setExcluded(true);
			}
			list.addAll(memberList);
			list.addAll(groupList);
		} else if (MemberTagRule.TYPE_COMBO.equals(type)) { // 组合
			List<SimpleMemberCombo> comboList = memberTagRuleDao.findSimpleMemberComboListByIdList(inIdList);
			for (SimpleMemberCombo cmd : comboList) {
				cmd.setExcluded(false);
			}
			list.addAll(comboList);
		} else if (MemberTagRule.TYPE_CUSTOM.equals(type)){	// 自定义
			List<SimpleMemberCombo> simpleComboList = customizeFilterClassDao.findCustomizerFilterListByIds(inIdList);
			list.addAll(simpleComboList);
		}

		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<MemberTagRuleCommand> findAvailableAtomicMemberTagRuleList() {
		return memberTagRuleDao.findAvailableAtomicMemberTagRuleList();
	}

}
