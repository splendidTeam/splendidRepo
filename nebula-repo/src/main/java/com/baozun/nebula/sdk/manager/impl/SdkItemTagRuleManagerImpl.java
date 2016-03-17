package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.baozun.nebula.command.product.ProductComboDetailsCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.command.rule.MiniItemAtomCommand;
import com.baozun.nebula.command.rule.MiniTagRuleCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.rule.CustomizeFilterClassDao;
import com.baozun.nebula.dao.rule.ItemTagRuleDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.model.rule.TagRule;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemTagRuleManager;
import com.baozun.nebula.sdk.utils.TagRuleUtils;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.JsonFormatUtil;

@Transactional
@Service("sdkCustomProductComboManager")
public class SdkItemTagRuleManagerImpl implements SdkItemTagRuleManager {

	private static final Logger log = LoggerFactory.getLogger(SdkItemTagRuleManagerImpl.class);

	/** 前台传来的表达式中，‘0’代表‘全场’ */
	private static final Long ALL_PRODUCT_ID = 0L;
	/** i18n-全场 */
	private static final String ALL_PRODUCT = "item.filter.all.item";
	/** 前台传来的表达式中，当分组类型时，以‘分号’分割后，数组长度必须为3 */
	private static final Integer LENGTH_AFTER_SPLIT = 3;
	@Autowired
	private ItemTagRuleDao itemTagRuleDao;
	@Autowired
	private ItemCategoryDao itemCategoryDao;
	@Autowired
	private ItemInfoDao itemInfoDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CustomizeFilterClassDao customizeFilterClassDao;

	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemTagRuleCommand> findCustomProductComboList(Page page, Sort[] sorts, Map<String, Object> paraMap, Long shopId) {
		return itemTagRuleDao.findCustomProductComboList(page, sorts, paraMap, shopId);
	}

	@Override
	public void saveItemTagRule(ItemTagRuleCommand command) {
		ItemTagRuleCommand dbModel = itemTagRuleDao.findTagRuleByName(command.getName());
		if (null != dbModel) // 名称重复
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_REPEATED_NAME);

		if (StringUtils.isBlank(command.getExpression())) {// 表达式错误
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION);
		}

		String exp = command.getExpression();
		command.setExpression(generateExpression(exp, command.getType()));
		ItemTagRule rule = new ItemTagRule();
		BeanUtils.copyProperties(command, rule);
		itemTagRuleDao.save(rule);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemTagRuleCommand findCustomProductCombo(String comboName) {
		ItemTagRuleCommand combo = null;
		if (StringUtils.isNotBlank(comboName)) {
			combo = itemTagRuleDao.findTagRuleByName(comboName);
		}
		return combo;
	}

	@Override
	public void enableOrDisableProductGroupById(Long id, Integer lifecycle) {
		ItemTagRule cc = itemTagRuleDao.getByPrimaryKey(id);
		if (Validator.isNotNullOrEmpty(cc)) {
			cc.setLifecycle(lifecycle);
		} else {
			log.info("enableOrDisableProductGroupById method getByPrimaryKey isnull");
			throw new BusinessException(Constants.PRODUCT_FILTER_INEXISTED);
		}

	}

	@Override
	@Transactional(readOnly=true)
	public ItemTagRuleCommand findCustomProductComboById(Long id) {
		return itemTagRuleDao.findCustomProductComboById(id);
	}

	@Override
	public void update(ItemTagRuleCommand combo) {
		ItemTagRuleCommand dbModel = itemTagRuleDao.findTagRuleByName(combo.getName());
		if (null != dbModel && (!dbModel.getId().equals(combo.getId()))) // 名称重复
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_REPEATED_NAME);

		if (StringUtils.isBlank(combo.getExpression())) {// 表达式错误
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION);
		}
		String exp = combo.getExpression();
		combo.setExpression(generateExpression(exp, combo.getType()));

		ItemTagRule rule = itemTagRuleDao.getByPrimaryKey(combo.getId());
		rule.setExpression(combo.getExpression());
		rule.setText(combo.getText());
		rule.setName(combo.getName());
	}

	@Override
	@Transactional(readOnly=true)
	public boolean checkCategoryWithItem(String items, String categorys) {
		if (Validator.isNullOrEmpty(items) || items.trim().length() <= 0) {
			return true;
		}
		String[] itemArr = items.split(TagRule.REGEX_DELIMITER);
		String[] cateArr = categorys.split(TagRule.REGEX_DELIMITER);
		for (int i = 0; i < itemArr.length; i++) {
			List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByItemId(new Long(itemArr[i]));
			if (Validator.isNullOrEmpty(itemCategoryList))
				return false; // 该商品没有所属分组
			boolean has = false; // 是否包含
			for (ItemCategory r : itemCategoryList) {
				for (int j = 0; j < cateArr.length; j++) {
					if (r.getCategoryId().equals(new Long(cateArr[j]))) {
						has = true;
					}
				}
			}
			if (!has)
				return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemTagRuleCommand> findCustomProductComboListByType(Integer type, Long shopId) {
		return itemTagRuleDao.findCustomProductComboListByType(type, shopId);
	}

	@Override
	@Transactional(readOnly=true)
	public ProductComboDetailsCommand findDetailsById(Long id) {
		ProductComboDetailsCommand cmd = new ProductComboDetailsCommand();

		ItemTagRuleCommand combo = itemTagRuleDao.findCustomProductComboById(id);
		if (null == combo) {
			throw new BusinessException(Constants.PRODUCT_FILTER_ERROR);
		}
		cmd.setId(combo.getId());
		cmd.setName(combo.getName());
		cmd.setType(combo.getType());

		String exp = combo.getExpression();

		Map<String, Set<MiniTagRuleCommand>> map = TagRuleUtils.product(exp);
		Set<MiniTagRuleCommand> inSet = map.get(MemberTagRule.ANALYSIS_KEY_IN);
		Set<MiniTagRuleCommand> outSet = map.get(MemberTagRule.ANALYSIS_KEY_OUT);

		List<MiniItemAtomCommand> atomList = new ArrayList<MiniItemAtomCommand>();

		List<Long> idList = new ArrayList<Long>(); // 包含的id列表
		for (MiniTagRuleCommand c : inSet) {
			idList.add(c.getId());
		}

		if (ItemTagRule.TYPE_PRODUCT.equals(combo.getType())) { // 商品类型
			atomList = itemInfoDao.findMiniTagRuleCommandByIdList(idList);
			for (MiniItemAtomCommand c : atomList) {
				c.setType(ItemTagRule.TYPE_PRODUCT);
				c.setIsOut(false);
			}
		} else if (ItemTagRule.TYPE_CATEGORY.equals(combo.getType())) { // 分类类型
			if (exp.startsWith(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) { // 全场
				MiniItemAtomCommand atom = new MiniItemAtomCommand();
				atom.setId(ALL_PRODUCT_ID);
				atom.setName(messageSource.getMessage(ALL_PRODUCT, null, LocaleContextHolder.getLocale()));
				atom.setType(ItemTagRule.TYPE_CATEGORY);
				atom.setIsOut(false);
				atomList.add(atom);
			} else { // 非全场
				atomList = categoryDao.findMiniTagRuleCommandByIdList(idList);
				for (MiniItemAtomCommand c : atomList) {
					c.setType(ItemTagRule.TYPE_CATEGORY);
					c.setIsOut(false);
				}
			}

			/* 排除 */
			List<Long> itemIdList = new ArrayList<Long>();
			List<Long> cateIdList = new ArrayList<Long>();
			for (MiniTagRuleCommand c : outSet) {
				if (ItemTagRule.TYPE_PRODUCT.equals(c.getType())) {
					itemIdList.add(c.getId());
				} else if (ItemTagRule.TYPE_CATEGORY.equals(c.getType())) {
					cateIdList.add(c.getId());
				}
			}
			List<MiniItemAtomCommand> itemList = itemInfoDao.findMiniTagRuleCommandByIdList(itemIdList);
			List<MiniItemAtomCommand> cateList = categoryDao.findMiniTagRuleCommandByIdList(cateIdList);
			for (MiniItemAtomCommand c : itemList) {
				c.setType(ItemTagRule.TYPE_PRODUCT);
				c.setIsOut(true);
				atomList.add(c);
			}
			for (MiniItemAtomCommand c : cateList) {
				c.setType(ItemTagRule.TYPE_CATEGORY);
				c.setIsOut(true);
				atomList.add(c);
			}
		}else if (ItemTagRule.TYPE_CUSTOM.equals(combo.getType())){
			List<CustomizeFilterClass> customizeFilterClassList = customizeFilterClassDao.findCustomizeFilterClassListByIds(idList);
			List<Long> itemIds = new ArrayList<Long>();
			for(CustomizeFilterClass customizeFilterClass : customizeFilterClassList){
				itemIds.addAll(SdkCustomizeFilterLoader.load(String.valueOf(customizeFilterClass.getId())));
			}
			List<MiniItemAtomCommand> itemList = itemInfoDao.findMiniTagRuleCommandByIdList(itemIds);
			for (MiniItemAtomCommand c : itemList) {
				c.setType(ItemTagRule.TYPE_CUSTOM);
				c.setIsOut(false);
				atomList.add(c);
			}
		}

		cmd.setAtomList(atomList);
		log.info(JsonFormatUtil.format(cmd));
		return cmd;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ProductComboDetailsCommand> findDetailsListById(Long id) {
		List<ProductComboDetailsCommand> list = new ArrayList<ProductComboDetailsCommand>();
		ItemTagRuleCommand combo = itemTagRuleDao.findCustomProductComboById(id);

		Map<String, Set<MiniTagRuleCommand>> map = TagRuleUtils.product(combo.getExpression());
		Set<MiniTagRuleCommand> inSet = map.get(MemberTagRule.ANALYSIS_KEY_IN);

		for (MiniTagRuleCommand cmd : inSet) {
			ProductComboDetailsCommand pcdc = findDetailsById(cmd.getId());
			list.add(pcdc);
		}

		return list;
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

		if (ItemTagRule.TYPE_PRODUCT.equals(type)) { // 商品
			String template = ItemTagRule.EXP_PRODUCT;
			result.append(template.replace(TagRule.REGEX_PLACEHOLDER, exp));
		} else if (ItemTagRule.TYPE_CATEGORY.equals(type)) { // 分类
			String allTemplate = ItemTagRule.EXP_ALLPRODUCT;
			String incTemplate = ItemTagRule.EXP_CATEGORY;
			String excCategory = TagRule.REGEX_EXCLUDE_CONNECT + ItemTagRule.EXP_CATEGORY;
			String excTemplate = TagRule.REGEX_EXCLUDE_CONNECT + ItemTagRule.EXP_PRODUCT;

			String[] arr = exp.split(TagRule.REGEX_SEMICOLON);
			if (arr.length != LENGTH_AFTER_SPLIT) {
				throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION);
			}
			String incStr = arr[0];
			String excCategoryStr = arr[1].trim(); // 去掉空格
			String excItemStr = arr[2].trim(); // 去掉空格
			if (ItemTagRule.ALL_ITEM_ID.equals(incStr)) { // 全场
				result.append(allTemplate);
			} else {
				result.append(incTemplate.replace(TagRule.REGEX_PLACEHOLDER, incStr));
			}

			if (StringUtils.isNotBlank(excCategoryStr)) {
				result.append(excCategory.replace(MemberTagRule.REGEX_PLACEHOLDER, excCategoryStr));
			}
			if (StringUtils.isNotBlank(excItemStr)) {
				result.append(excTemplate.replace(MemberTagRule.REGEX_PLACEHOLDER, excItemStr));
			}
		} else if(ItemTagRule.TYPE_CUSTOM.equals(type)){ // 自定义筛选器
			result.append(ItemTagRule.EXP_CUSTOM.replace(TagRule.REGEX_PLACEHOLDER, exp));
		}else if (ItemTagRule.TYPE_COMBO.equals(type)) { // 组合筛选器
			String template = ItemTagRule.EXP_COMBO;
			result.append(template.replace(TagRule.REGEX_PLACEHOLDER, exp));
		}

		return result.toString();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboList() {
		return itemTagRuleDao.findAllAvailableCustomProductComboList();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboListByShopId(Long shopId) {
		return itemTagRuleDao.findAllAvailableCustomProductComboListByShopId(shopId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.sdk.manager.SdkItemTagRuleManager#
	 * findSimpleMemberComboListByExpression(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public ProductComboDetailsCommand findItemComboListByExpression(String exp) {
		// TODO Auto-generated method stub
		ProductComboDetailsCommand cmd = new ProductComboDetailsCommand();
		Pattern pattern = Pattern.compile(ItemTagRule.REGEX_IN_BRACKETS);
		String[] expArr = exp.split(ItemTagRule.REGEX_AND);
		for (int i = 0; i < expArr.length; i++) {
			String s = expArr[i];
			if (ItemTagRule.EXP_ALLPRODUCT.equals(s)) {
				Category category = new Category();
				category.setId(0L);
				cmd.getCategoryList().add(category);
				continue; // 全场
			}

			boolean isOut = s.startsWith(ItemTagRule.REGEX_NOT); // 是否排除
			int type = TagRuleUtils.getItemType(isOut ? s.substring(1) : s); // 去除感叹号
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				String ids = matcher.group(1);
				if (type == ItemTagRule.TYPE_PRODUCT) {
					List<Long> itemIds = new ArrayList<Long>();
					for (String id : Arrays.asList(ids.split(ItemTagRule.REGEX_DELIMITER))) {
						itemIds.add(new Long(id));
					}
					cmd.getItemList().addAll(itemInfoDao.findItemCommandListByItemIds(itemIds));
				} else if (type == ItemTagRule.TYPE_CATEGORY && !isOut) {
					for (String id : Arrays.asList(ids.split(ItemTagRule.REGEX_DELIMITER))) {
						Long cid = new Long(id);
						Category category = categoryDao.findCategoryById(cid);
						cmd.getCategoryList().add(category);
					}
				} else if (type == ItemTagRule.TYPE_CATEGORY && isOut) {
					for (String id : Arrays.asList(ids.split(ItemTagRule.REGEX_DELIMITER))) {
						Long cid = new Long(id);
						Category category = categoryDao.findCategoryById(cid);
						cmd.getExcCategoryList().add(category);
					}
				} else if (type == ItemTagRule.TYPE_CUSTOM){
					List<Long> filterIds = new ArrayList<Long>();
					for (String filterId : Arrays.asList(ids.split(ItemTagRule.REGEX_DELIMITER))) {
						filterIds.add(new Long(filterId));
					}
					cmd.setCustomizeFilterClassList(customizeFilterClassDao.findCustomizeFilterClassListByIds(filterIds));
				}
			}
		}
		return cmd;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemTagRuleCommand> findEffectItemTagRuleListByIdList(
			List<Long> idList) {
		if( idList==null || idList.size()==0){
			return null;
		}
		return itemTagRuleDao.findEffectItemTagRuleListByIdList(idList);
		
	}
}
