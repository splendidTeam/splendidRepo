package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.command.rule.MiniTagRuleCommand;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.rule.ItemTagRuleDao;
import com.baozun.nebula.dao.rule.MemberTagRuleDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkFilterManager;
import com.baozun.nebula.sdk.utils.TagRuleUtils;

/**
 * 
 * @author 项硕
 */
@Transactional
@Service("sdkFilterManager")
public class SdkFilterManagerImpl implements SdkFilterManager {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SdkFilterManagerImpl.class);

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ItemCategoryDao itemCategoryDao;
	@Autowired
	private MemberTagRuleDao memberTagRuleDao;
	@Autowired
	private ItemTagRuleDao itemTagRuleDao;

	@Override
	@Transactional(readOnly=true)
	public Set<Long> analyzeMemberExpression(String exp) {
		Set<Long> result = new HashSet<Long>();

		Map<String, Set<MiniTagRuleCommand>> ruleMap = TagRuleUtils.member(exp);
		Set<MiniTagRuleCommand> inSet = ruleMap.get(MemberTagRule.ANALYSIS_KEY_IN);
		Set<MiniTagRuleCommand> outSet = ruleMap.get(MemberTagRule.ANALYSIS_KEY_OUT);

		int type = TagRuleUtils.getMemberType(exp);
		if (MemberTagRule.TYPE_MEMBER.equals(type)) { // 会员
			for (MiniTagRuleCommand cmd : inSet) {
				result.add(cmd.getId());
			}
		} else if (MemberTagRule.TYPE_GROUP.equals(type)) { // 分组
			/* 包含 */
			if (exp.startsWith(MemberTagRule.EXP_PREFIX_ALLMEMBER)) { // 全体
				result.addAll(memberDao.findAllEffectMemberIdList());
			} else {
				List<Long> groupList = new ArrayList<Long>();
				for (MiniTagRuleCommand cmd : inSet) {
					groupList.add(cmd.getId());
				}
				result.addAll(memberDao.findEffectMemberIdListByGroupIdList(groupList));
			}

			/* 排除 */
			List<Long> groupList = new ArrayList<Long>();
			for (MiniTagRuleCommand cmd : outSet) {
				if (MemberTagRule.TYPE_MEMBER.equals(cmd.getType())) { // 排除会员
					result.remove(cmd.getId());
				} else if (MemberTagRule.TYPE_GROUP.equals(cmd.getType())) { // 排除分组
					groupList.add(cmd.getId());
				}
			}
			result.removeAll(memberDao.findEffectMemberIdListByGroupIdList(groupList));
		} else if(MemberTagRule.TYPE_CUSTOM.equals(type)){ // 自定义
			for (MiniTagRuleCommand cmd : inSet) {
				List<Long> listIds = SdkCustomizeFilterLoader.load(cmd.getId().toString());
				if (null!=listIds)
				{
					result.addAll(listIds);
				}
			}
		}else if (MemberTagRule.TYPE_COMBO.equals(type)) { // 组合
			List<Long> idList = new ArrayList<Long>();
			for (MiniTagRuleCommand cmd : inSet) {
				idList.add(cmd.getId());
			}
			List<MemberTagRuleCommand> ruleList = memberTagRuleDao.findEffectMemberTagRuleListByIdList(idList);
			for (MemberTagRuleCommand cmd : ruleList) {
				result.addAll(analyzeMemberExpression(cmd.getExpression())); // 递归
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly=true)
	public Set<Long> analyzeProductExpression(String exp, Long shopId) {
		Set<Long> result = new HashSet<Long>();

		Map<String, Set<MiniTagRuleCommand>> ruleMap = TagRuleUtils.product(exp);
		Set<MiniTagRuleCommand> inSet = ruleMap.get(MemberTagRule.ANALYSIS_KEY_IN);
		Set<MiniTagRuleCommand> outSet = ruleMap.get(MemberTagRule.ANALYSIS_KEY_OUT);

		int type = TagRuleUtils.getItemType(exp);
		if (ItemTagRule.TYPE_PRODUCT.equals(type)) { // 商品
			for (MiniTagRuleCommand cmd : inSet) {
				result.add(cmd.getId());
			}
		} else if (ItemTagRule.TYPE_CATEGORY.equals(type)) { // 分类
			/* 包含 */
			if (exp.startsWith(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) { // 全场
				result.addAll(itemCategoryDao.findAllEffectProductIdListByShopId(shopId));
			} else {
				List<Long> cateList = new ArrayList<Long>();
				for (MiniTagRuleCommand cmd : inSet) {
					cateList.add(cmd.getId());
				}
				result.addAll(itemCategoryDao.findEffectItemIdListByCategoryIdList(cateList, shopId));
			}

			/* 排除 */
			List<Long> cateList = new ArrayList<Long>();
			for (MiniTagRuleCommand cmd : outSet) {
				if (ItemTagRule.TYPE_PRODUCT.equals(cmd.getType())) { // 排除商品
					result.remove(cmd.getId());
				} else if (ItemTagRule.TYPE_CATEGORY.equals(cmd.getType())) { // 排除分类
					cateList.add(cmd.getId());
				}
			}
			result.removeAll(itemCategoryDao.findEffectItemIdListByCategoryIdList(cateList, shopId));
		}else if (ItemTagRule.TYPE_CUSTOM.equals(type)) { // 自定义
			for (MiniTagRuleCommand cmd : inSet) {
				List<Long> listIds = SdkCustomizeFilterLoader.load(cmd.getId().toString());
				if (null!=listIds)
				{
					result.addAll(listIds);
				}
			}
		}else if (ItemTagRule.TYPE_COMBO.equals(type)) { // 组合
			List<Long> idList = new ArrayList<Long>();
			for (MiniTagRuleCommand cmd : inSet) {
				idList.add(cmd.getId());
			}
			List<ItemTagRuleCommand> ruleList = itemTagRuleDao.findEffectItemTagRuleListByIdList(idList);
			for (ItemTagRuleCommand cmd : ruleList) {
				result.addAll(analyzeProductExpression(cmd.getExpression(), shopId)); // 递归
			}
		}
		return result;
	}

	@Override
	public boolean isMemberConfilct(String exp1, String exp2) {
		if (exp1.equals(exp2))
			return true; // 1.表达式相同
		if (MemberTagRule.EXP_ALLMEMBER.equals(exp1) || MemberTagRule.EXP_ALLMEMBER.equals(exp2))
			return true; // 2.其中一个为‘全体’
		Set<Long> memberIdList1 = analyzeMemberExpression(exp1);
		Set<Long> memberIdList2 = analyzeMemberExpression(exp2);
		return CollectionUtils.containsAny(memberIdList1, memberIdList2); // 有重叠项
	}

	@Override
	public boolean isProductConfilct(String exp1, String exp2, Long shopId) {
		if (exp1.equals(exp2))
			return true; // 1.表达式相同
		if (ItemTagRule.EXP_ALLPRODUCT.equals(exp1) || ItemTagRule.EXP_ALLPRODUCT.equals(exp2))
			return true; // 2.其中一个为‘全体’
		Set<Long> productIdList1 = analyzeProductExpression(exp1, shopId);
		Set<Long> productIdList2 = analyzeProductExpression(exp2, shopId);
		return CollectionUtils.containsAny(productIdList1, productIdList2); // 有重叠项
	}

	@Override
	public Map<String, Map<String, Set<Long>>> analyzeProductExpression(Long comboId) {
		Map<String, Map<String, Set<Long>>> rs = new HashMap<String, Map<String, Set<Long>>>();
		Map<String, Set<Long>> in = new HashMap<String, Set<Long>>();
		Map<String, Set<Long>> out = new HashMap<String, Set<Long>>();
		rs.put(ItemTagRule.ANALYSIS_KEY_IN, in);
		rs.put(ItemTagRule.ANALYSIS_KEY_OUT, out);
		Set<Long> all = null;
		Set<Long> category = new HashSet<Long>();
		Set<Long> item = new HashSet<Long>();
		Set<Long> outItem = new HashSet<Long>(); // 排除列表
		Set<Long> outCategory = new HashSet<Long>(); // 排除列表

		ItemTagRuleCommand combo = null;
		List<ItemTagRuleCommand> list = EngineManager.getInstance().getItemScopeList();
		for (ItemTagRuleCommand cmd : list) {
			if (cmd.getId().equals(comboId)) {
				combo = cmd;
				break;
			}
		}
		if (null == combo)
			throw new BusinessException(Constants.PRODUCT_FILTER_INEXISTED);

		int type = combo.getType();
		String exp = combo.getExpression();
		Map<String, Set<MiniTagRuleCommand>> map = TagRuleUtils.product(exp); // 获取表达式中的数值
		Set<MiniTagRuleCommand> inList = map.get(ItemTagRule.ANALYSIS_KEY_IN);
		Set<MiniTagRuleCommand> outList = map.get(ItemTagRule.ANALYSIS_KEY_OUT);
		if (ItemTagRule.TYPE_PRODUCT.equals(type)) { // 商品
			for (MiniTagRuleCommand cmd : inList) {
				item.add(cmd.getId());
			}
		} else if (ItemTagRule.TYPE_CATEGORY.equals(type)) { // 分类
			if (exp.startsWith(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) { // 全场
				all = new HashSet<Long>(); // 当‘全场’时，只需返回非空即可
			} else {
				for (MiniTagRuleCommand cmd : inList) {
					category.add(cmd.getId());
				}
			}

			/* 排除 */
			for (MiniTagRuleCommand cmd : outList) {
				if (ItemTagRule.TYPE_PRODUCT.equals(cmd.getType())) {
					outItem.add(cmd.getId());
				} else if (ItemTagRule.TYPE_CATEGORY.equals(cmd.getType())) {
					outCategory.add(cmd.getId());
				}
			}
		} else if (ItemTagRule.TYPE_COMBO.equals(type)) { // 组合
			for (MiniTagRuleCommand cmd : inList) {
				Map<String, Map<String, Set<Long>>> recursionMap = analyzeProductExpression(cmd.getId()); // 递归
				Map<String, Set<Long>> recursionIn = recursionMap.get(ItemTagRule.ANALYSIS_KEY_IN);
				Map<String, Set<Long>> recursionOut = recursionMap.get(ItemTagRule.ANALYSIS_KEY_OUT);
				all = recursionIn.get(ItemTagRule.EXP_ALLPRODUCT);
				category.addAll(recursionIn.get(ItemTagRule.EXP_CATEGORY));
				item.addAll(recursionIn.get(ItemTagRule.EXP_PRODUCT));
				outItem.addAll(recursionOut.get(ItemTagRule.EXP_PRODUCT));
				outCategory.addAll(recursionOut.get(ItemTagRule.EXP_CATEGORY));
			}
		}

		in.put(ItemTagRule.EXP_ALLPRODUCT, all);
		in.put(ItemTagRule.EXP_CATEGORY, category);
		in.put(ItemTagRule.EXP_PRODUCT, item);
		out.put(ItemTagRule.EXP_PRODUCT, outItem);
		out.put(ItemTagRule.EXP_CATEGORY, outCategory);
		return rs;
	}
}
