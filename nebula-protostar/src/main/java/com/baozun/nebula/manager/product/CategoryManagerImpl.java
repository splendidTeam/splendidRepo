/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.CategoryCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.CategoryLangDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.enumeration.TreeMoveType;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.CodeRepeatException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.exception.NameRepeatException;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.CategoryLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.Validator;

/**
 * 商品分类manager
 * 
 * @author yi.huang
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
@Transactional
@Service("categoryManager")
public class CategoryManagerImpl implements CategoryManager{

	private static final Logger	log	= LoggerFactory.getLogger(CategoryManagerImpl.class);

	@Autowired
	private CategoryDao			categoryDao;

	@Autowired
	private ItemCategoryDao		itemCategoryDao;

	@Autowired
	private CategoryLangDao		categoryLangDao;

	@Autowired
	private SdkI18nLangManager	sdkI18nLangManager;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#addLeafCategory(java .lang.Long, java.lang.String, java.lang.String)
	 */
	public void addLeafCategory(Long parentId,String code,String name) throws CodeRepeatException,NameRepeatException,BusinessException{
		// 检查code是否重复
		boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
		if (isExistSameCodeCategory){
			throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
		}

		// 查找 目标父类下 是否存在同名字的分类
		Category sameNameCategory = findEnableCategoryByNameAndParentId(parentId, name);

		if (null != sameNameCategory){
			Category parentCategory = findCategoryById(parentId);
			Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
			throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
		}

		Integer actual = categoryDao.insertCategory(parentId, code, name);
		ifNotExpectedCountThrowException(1, actual);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#insertSiblingCategory (java.lang.Long, java.lang.String, java.lang.String)
	 */
	public void insertSiblingCategory(Long selectCategoryId,String code,String name) throws CodeRepeatException,NameRepeatException,
			BusinessException{
		// 检查code是否重复
		boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
		if (isExistSameCodeCategory){
			throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
		}

		// 检查父类 下面 是否有同名的 分类
		Category selectCategory = findCategoryById(selectCategoryId);
		Long selectCategoryParentId = selectCategory.getParentId();

		// 查找 目标父类下 是否存在同名字的分类
		Category sameNameCategory = findEnableCategoryByNameAndParentId(selectCategoryParentId, name);

		if (null != sameNameCategory){
			Category parentCategory = findCategoryById(selectCategoryParentId);
			Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
			throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
		}

		// 选中的 category
		Integer selectCategorySortNo = selectCategory.getSortNo();

		// 将当前父类下 ,sortNo >= 目标分类 sortNo +1.
		Integer result = categoryDao.treeIncrSortNoGtAndEqTargetNodeSortNo(selectCategoryId);

		log.info("treeIncrSortNoGtAndEqTargetNodeSortNo:{}", result);

		if (result >= 1){
			// 插入一条 新的 sortNo =原来 选中id 的sortNo
			Integer actual = categoryDao.treeInsertNodeWithSortNo(selectCategoryParentId, code, name, selectCategorySortNo);
			ifNotExpectedCountThrowException(1, actual);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#updateCategory(java .lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateCategory(Long id,String code,String name) throws CodeRepeatException,NameRepeatException,BusinessException{

		Category category = findCategoryById(id);
		String oldCode = category.getCode();

		// 如果新的code和之前的code不相同 ，则检查新更新的code是否重复
		if (!oldCode.equals(code)){
			boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
			if (isExistSameCodeCategory){
				throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
			}
		}

		// 检查父类 下面 是否有同名的 分类
		Long parentId = category.getParentId();
		// 查找 目标父类下 是否存在同名字的分类
		Category sameNameCategory = findEnableCategoryByNameAndParentId(parentId, name);

		if (null != sameNameCategory){
			// 不是 选择的id,表示存在同名字的 code
			boolean isNotSelectCategory = !sameNameCategory.getId().equals(id);
			if (isNotSelectCategory){
				Category parentCategory = findCategoryById(parentId);
				Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
				throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
			}
		}

		Integer actual = categoryDao.updateCategoryById(id, code, name);
		ifNotExpectedCountThrowException(1, actual);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#removeCategoryById( java.lang.Long)
	 */
	@Override
	public void removeCategoryById(Long id) throws BusinessException{
		List<Long> idList = new ArrayList<Long>();
		idList.add(id);
		idList = getChildrenCategoryIdListRecursion(id, idList);

		// 删除分类与商品之间的关联关系
		itemCategoryDao.deleteItemCategoryByCategoryIds(idList);

		Integer actual = categoryDao.removeCategoryByIds(idList);
		Integer expected = idList.size();

		ifNotExpectedCountThrowException(expected, actual);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			categoryLangDao.deleteCategoryLangByCategoryIds(idList);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#dropCategory(java.lang .Long, java.lang.Long,
	 * com.baozun.nebula.enumeration.TreeMoveType)
	 */
	public void dropCategory(Long selectCategoryId,Long targetCategoryId,TreeMoveType treeMoveType) throws NameRepeatException,
			BusinessException{

		Category selectCategory = findCategoryById(selectCategoryId);
		Category targetCategory = findCategoryById(targetCategoryId);

		// 父类 ,用来 校验 下面 是否有同名 分类
		Long parentCategoryId = null;

		switch (treeMoveType) {
			case NEXT:
				parentCategoryId = targetCategory.getParentId();
				break;
			case PREV:
				parentCategoryId = targetCategory.getParentId();
				break;
			case INNER:
				parentCategoryId = targetCategoryId;
				break;
			default:
				break;
		}

		String selectCategoryName = selectCategory.getName();

		// 查找 目标父类下 是否存在同名字的分类
		Category sameNameCategory = findEnableCategoryByNameAndParentId(parentCategoryId, selectCategoryName);

		if (null != sameNameCategory){
			// 不是 选择的id,表示存在同名字的 code
			boolean isNotSelectCategory = !sameNameCategory.getId().equals(selectCategoryId);
			if (isNotSelectCategory){
				Category parentCategory = findCategoryById(targetCategoryId);
				Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
				throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
			}
		}

		// *************************************************************************************************
		// targetCategory 原来的 sortNo
		Integer targetCategorySortNo = targetCategory.getSortNo();

		// Long selectCategoryParentId = selectCategory.getParentId();
		Long targetCategoryParentId = targetCategory.getParentId();

		Integer actual = 0;

		// 是否是同级移动
		// boolean isSibling =
		// selectCategoryParentId.equals(targetCategoryParentId);

		switch (treeMoveType) {
			case NEXT:// 成为同级后一个节点

				// 将当前父类下 ,sortNo介于选择分类之间的分类sortNo -1
				actual = categoryDao.treeDecrSortNoLtAndEqTargetNodeSortNo(targetCategoryId);
				if (actual > 0){
					// 再将 移动的 节点 sortNo 改成 目标 节点 sortNo
					actual = categoryDao.treeUpdateNodeParentIdAndSortNo(selectCategoryId, targetCategoryParentId, targetCategorySortNo);
					ifNotExpectedCountThrowException(1, actual);
				}
				break;

			case PREV:// 成为同级前一个节点
				// 将当前父类下 ,sortNo >= 目标分类 sortNo +1
				actual = categoryDao.treeIncrSortNoGtAndEqTargetNodeSortNo(targetCategoryId);
				if (actual > 0){
					// 再将 移动的 节点 sortNo 改成 目标 节点 sortNo
					actual = categoryDao.treeUpdateNodeParentIdAndSortNo(selectCategoryId, targetCategoryParentId, targetCategorySortNo);
					ifNotExpectedCountThrowException(1, actual);
				}
				break;

			case INNER:// 成为子节点
				// 修改 parentId 且 sortNo 改成 targetCategory 子sortNo+1
				actual = categoryDao.treeUpdateNodeParentId(selectCategoryId, targetCategoryId);
				ifNotExpectedCountThrowException(1, actual);
				break;

			default:
				break;
		}
	}

	/**
	 * 如果不是 期望操作行 ,抛出异常
	 * 
	 * @param expected
	 * @param actual
	 */
	private void ifNotExpectedCountThrowException(Integer expected,Integer actual){
		if (expected != actual){
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected, actual });
			// throw new EffectRangeUnexpectedException(expected, actual);
		}
	}

	/**
	 * 是否存在 code 相同的 分类
	 * 
	 * @param code
	 *            要查询的分类code
	 * @return true:有相同的code存在；false：没有相同的code存在
	 */
	@Transactional(readOnly = true)
	private boolean isExistSameCodeCategory(String code){
		Category category = findCategoryByCode(code);
		return null != category;
	}

	// ******************************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#findCategoryById(java .lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Category findCategoryById(Long id){
		return categoryDao.getByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#findCategoryByCode( java.lang.String)
	 */
	@Transactional(readOnly = true)
	public Category findCategoryByCode(String code){
		return categoryDao.findCategoryByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager#findAllCategoryList (loxia.dao.Sort[])
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> findEnableCategoryList(Sort[] sort){
		return categoryDao.findEnableCategoryList(sort);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager# findCategoryListByCategoryIds(java.lang.Long[])
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Category> findCategoryListByCategoryIds(Long[] categoryIds){
		return categoryDao.findCategoryListByCategoryIds(categoryIds);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.CategoryManager# findEnableCategoryByNameAndParentId(java.lang.Long, java.lang.String)
	 */
	@Transactional(readOnly = true)
	public Category findEnableCategoryByNameAndParentId(Long parentId,String name){
		Category category = categoryDao.findEnableCategoryByNameAndParentId(parentId, name);
		return category;
	}

	// *********************************************************************************************
	/**
	 * 根据 categoryId 递归查找 子节点(不包括自己)
	 * 
	 * @param parentId
	 *            父id
	 * @param categories
	 * @return
	 */
	private List<Long> getChildrenCategoryIdListRecursion(Long categoryId,List<Long> idList){
		if (Validator.isNullOrEmpty(idList)){
			idList = new ArrayList<Long>();
		}
		List<Category> childrenCategoryList = categoryDao.findCategoryListByParentId(categoryId);

		if (Validator.isNotNullOrEmpty(childrenCategoryList)){

			for (Category category : childrenCategoryList){
				Long id = category.getId();
				idList.add(id);

				idList = getChildrenCategoryIdListRecursion(id, idList);

			}
		}

		return idList;
	}

	@Override
	public List<Category> findCategoryListByParentId(Long categoryId){
		return categoryDao.findCategoryListByParentId(categoryId);
	}

	@Override
	public void addLeafCategory(CategoryCommand command) throws CodeRepeatException,NameRepeatException,BusinessException{
		Long parentId = command.getParentId();
		String code = command.getCode();
		// 检查code是否重复
		boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
		if (isExistSameCodeCategory){
			throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
		}
		Category category = new Category();
		category.setCode(code);
		category.setParentId(command.getParentId());
		category.setLifecycle(1);
		Date date = new Date();
		category.setCreateTime(date);
		category.setModifyTime(date);
		// 查询序号
		int seq = categoryDao.findCategorySeqByParentId(command.getParentId());
		if (seq > 0){
			seq = seq + 1;
		}
		category.setSortNo(seq);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang mutlLang = (MutlLang) command.getName();
			String[] values = mutlLang.getValues();
			String[] langs = mutlLang.getLangs();

			// 查找 目标父类下 是否存在同名字的分类
			String defaultName = mutlLang.getDefaultValue();
			Category sameNameCategory = findEnableCategoryByNameAndParentId(parentId, defaultName);
			if (null != sameNameCategory){
				Category parentCategory = findCategoryById(parentId);
				Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
				throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
			}
			category.setName(defaultName);
			category = categoryDao.save(category);
			if (category == null || category.getId() == null){
				ifNotExpectedCountThrowException(1, 0);
			}
			Long id = category.getId();

			// 多语言校验同级名称是否重复
			List<Category> categories = categoryDao.findCategoryListByParentId(parentId);
			List<Long> cIds = new ArrayList<Long>();
			if (categories != null && categories.size() > 0){
				for (Category c : categories){
					cIds.add(c.getId());
				}
				for (int i = 0; i < values.length; i++){
					String val = values[i];
					String lang = langs[i];
					/*
					 * //设置默认国际化数据 if(LangUtil.ZH_CN.equals(lang)){ category.setName(val); }
					 */
					int count = categoryDao.findCategoryLangByNameAndParentId(val, lang, cIds);
					if (count > 0){
						Category parentCategory = findCategoryById(parentId);
						Object[] args = { parentCategory.getName(), val };
						throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
					}
				}
			}
			for (int i = 0; i < values.length; i++){
				String val = values[i];
				String lang = langs[i];
				CategoryLang categoryLang = new CategoryLang();
				categoryLang.setCategoryid(id);
				categoryLang.setName(val);
				categoryLang.setLang(lang);
				categoryLangDao.save(categoryLang);
			}
		}else{
			SingleLang singleLang = (SingleLang) command.getName();
			String name = singleLang.getValue();
			// 查找 目标父类下 是否存在同名字的分类
			Category sameNameCategory = findEnableCategoryByNameAndParentId(parentId, name);
			if (null != sameNameCategory){
				Category parentCategory = findCategoryById(parentId);
				Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
				throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
			}
			category.setName(name);
			category = categoryDao.save(category);
		}
		if (category == null || category.getId() == null){
			ifNotExpectedCountThrowException(1, 0);
		}

	}

	private String getDefaultlang(List<I18nLang> i18nLangCache){
		if (Validator.isNotNullOrEmpty(i18nLangCache)){
			for (I18nLang i18nLang : i18nLangCache){
				Integer defaultlang = i18nLang.getDefaultlang();
				if (defaultlang != null && defaultlang == 1){
					return i18nLang.getKey();
				}
			}
		}
		return null;
	}

	@Override
	public void updateCategory(CategoryCommand categoryCommand) throws CodeRepeatException,NameRepeatException,BusinessException{
		Long id = categoryCommand.getId();
		String code = categoryCommand.getCode();
		Category category = findCategoryById(id);
		String oldCode = category.getCode();
		// 如果新的code和之前的code不相同 ，则检查新更新的code是否重复
		if (!oldCode.equals(code)){
			boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
			if (isExistSameCodeCategory){
				throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
			}
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang mutlLang = (MutlLang) categoryCommand.getName();
			category = categoryDao.save(category);
			if (category == null || category.getId() == null){
				ifNotExpectedCountThrowException(1, 0);
			}
			String[] values = mutlLang.getValues();
			String[] langs = mutlLang.getLangs();
			// 多语言校验同级名称是否重复
			Long parentId = category.getParentId();
			List<Category> categories = categoryDao.findCategoryListByParentId(parentId);
			List<Long> cIds = new ArrayList<Long>();
			String name = mutlLang.getDefaultValue();
			if (categories != null && categories.size() > 0){
				for (Category c : categories){
					if (id.equals(c.getId())){
						continue;
					}
					cIds.add(c.getId());
				}
				for (int i = 0; i < values.length; i++){
					String val = values[i];
					String lang = langs[i];
					int count = categoryDao.findCategoryLangByNameAndParentId(val, lang, cIds);
					if (count > 0){
						Category parentCategory = findCategoryById(parentId);
						Object[] args = { parentCategory.getName(), val };
						throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
					}
				}
			}
			for (int i = 0; i < values.length; i++){
				String val = values[i];
				String lang = langs[i];
				// hibernate方式 1、根据id和lang查询出数据 2、再根据数据id get 出来再 save
				// sql方式修改
				CategoryLang categoryLang1 = categoryDao.findCategoryLang(id, lang);
				int result = 0;
				if (categoryLang1 != null){
					result = categoryDao.updateCategoryLangByCategoryIdAndLang(val, lang, id);
				}else{
					CategoryLang categoryLang = new CategoryLang();
					categoryLang.setCategoryid(id);
					categoryLang.setName(val);
					categoryLang.setLang(lang);
					categoryLang = categoryLangDao.save(categoryLang);
					if (categoryLang.getId() != null){
						result = 1;
					}
				}
				ifNotExpectedCountThrowException(1, result);
			}
			int result = categoryDao.updateCategoryById(id, code, name);
			ifNotExpectedCountThrowException(1, result);
		}else{
			SingleLang singleLang = (SingleLang) categoryCommand.getName();
			String name = singleLang.getValue();
			// 检查父类 下面 是否有同名的 分类
			Long parentId = category.getParentId();
			// 查找 目标父类下 是否存在同名字的分类
			Category sameNameCategory = findEnableCategoryByNameAndParentId(parentId, name);
			if (null != sameNameCategory){
				// 不是 选择的id,表示存在同名字的 code
				boolean isNotSelectCategory = !sameNameCategory.getId().equals(id);
				if (isNotSelectCategory){
					Category parentCategory = findCategoryById(parentId);
					Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
					throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
				}
			}
			Integer actual = categoryDao.updateCategoryById(id, code, name);
			ifNotExpectedCountThrowException(1, actual);
		}
	}

	@Override
	public void insertSiblingCategory(Long selectCategoryId,CategoryCommand command) throws CodeRepeatException,NameRepeatException,
			BusinessException{
		// 检查code是否重复
		String code = command.getCode();
		// 检查code是否重复
		boolean isExistSameCodeCategory = isExistSameCodeCategory(code);
		if (isExistSameCodeCategory){
			throw new CodeRepeatException(ErrorCodes.PRODUCT_CATEGORY_CODE_REPEAT);
		}
		// 检查父类 下面 是否有同名的 分类
		Category selectCategory = findCategoryById(selectCategoryId);
		Long selectCategoryParentId = selectCategory.getParentId();

		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang mutlLang = (MutlLang) command.getName();
			Category category = new Category();
			LangProperty.I18nPropertyCopy(command, category);
			category.setParentId(selectCategoryParentId);
			// 查询序号
			int seq = categoryDao.findCategorySeqByParentId(command.getParentId());
			if (seq > 0){
				seq = seq + 1;
			}
			category.setSortNo(seq);
			String name = mutlLang.getDefaultValue();
			category.setName(name);
			category.setLifecycle(1);
			category = categoryDao.save(category);
			if (category == null || category.getId() == null){
				ifNotExpectedCountThrowException(1, 0);
			}
			Long id = category.getId();
			String[] values = mutlLang.getValues();
			String[] langs = mutlLang.getLangs();
			// 多语言校验同级名称是否重复
			List<Category> categories = categoryDao.findCategoryListByParentId(selectCategoryId);
			List<Long> cIds = new ArrayList<Long>();
			if (categories != null && categories.size() > 0){
				for (Category c : categories){
					cIds.add(c.getId());
				}
				for (int i = 0; i < values.length; i++){
					String val = values[i];
					String lang = langs[i];
					// 设置默认国际化数据
					if (LangUtil.ZH_CN.equals(lang)){
						category.setName(val);
					}
					int count = categoryDao.findCategoryLangByNameAndParentId(val, lang, cIds);
					if (count > 0){
						Category parentCategory = findCategoryById(selectCategoryId);
						Object[] args = { parentCategory.getName(), val };
						throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
					}
				}
			}
			for (int i = 0; i < values.length; i++){
				String val = values[i];
				String lang = langs[i];
				CategoryLang categoryLang = new CategoryLang();
				categoryLang.setCategoryid(id);
				categoryLang.setName(val);
				categoryLang.setLang(lang);
				categoryLangDao.save(categoryLang);
			}
		}else{
			SingleLang singleLang = (SingleLang) command.getName();
			String name = singleLang.getValue();
			// 查找 目标父类下 是否存在同名字的分类
			Category sameNameCategory = findEnableCategoryByNameAndParentId(selectCategoryParentId, name);
			if (null != sameNameCategory){
				Category parentCategory = findCategoryById(selectCategoryParentId);
				Object[] args = { parentCategory.getName(), sameNameCategory.getName() };
				throw new NameRepeatException(ErrorCodes.PRODUCT_CATEGORY_PARENT_EXIST_NAMECATEGORY, args);
			}
			// 选中的 category
			Integer selectCategorySortNo = selectCategory.getSortNo();
			// 将当前父类下 ,sortNo >= 目标分类 sortNo +1.
			Integer result = categoryDao.treeIncrSortNoGtAndEqTargetNodeSortNo(selectCategoryId);

			log.info("treeIncrSortNoGtAndEqTargetNodeSortNo:{}", result);
			if (result >= 1){
				// 插入一条 新的 sortNo =原来 选中id 的sortNo
				Integer actual = categoryDao.treeInsertNodeWithSortNo(selectCategoryParentId, code, name, selectCategorySortNo);
				ifNotExpectedCountThrowException(1, actual);
			}
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryCommand> findEnableCategoryCommandList(Sort[] sort){
		List<CategoryCommand> list = new ArrayList<CategoryCommand>();
		List<Category> categories = categoryDao.findEnableCategoryList(sort);
		if (Validator.isNotNullOrEmpty(categories)){
			List<Long> categoryIds = new ArrayList<Long>();
			Map<Long, Category> map = new HashMap<Long, Category>();
			for (Category category : categories){
				Long cid = category.getId();
				categoryIds.add(cid);
				map.put(cid, category);
			}
			List<CategoryLang> categoryLangs = categoryDao.findCategoryLangList(categoryIds, MutlLang.i18nLangs());
			if (Validator.isNotNullOrEmpty(categoryLangs)){
				for (CategoryLang categoryLang : categoryLangs){
					Long cid = categoryLang.getCategoryid();
					Category c = map.get(cid);

				}
			}
		}

		return list;
	}

	@Override
	public CategoryCommand findCategoryLangByCategoryId(Long categoryId){
		Category category = categoryDao.getByPrimaryKey(categoryId);
		if (category == null){
			return null;
		}
		CategoryCommand categoryCommand = new CategoryCommand();
		LangProperty.I18nPropertyCopyToSource(category, categoryCommand);
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			List<Long> categoryIds = new ArrayList<Long>();
			categoryIds.add(categoryId);
			List<CategoryLang> categoryLangs = categoryDao.findCategoryLangList(categoryIds, MutlLang.i18nLangs());
			if (Validator.isNotNullOrEmpty(categoryLangs)){
				String[] values = new String[MutlLang.i18nSize()];
				String[] langs = new String[MutlLang.i18nSize()];
				for (int i = 0; i < categoryLangs.size(); i++){
					CategoryLang CategoryLang = categoryLangs.get(i);
					values[i] = CategoryLang.getName();
					langs[i] = CategoryLang.getLang();
				}
				MutlLang mutlLang = new MutlLang();
				mutlLang.setValues(values);
				mutlLang.setLangs(langs);
				categoryCommand.setName(mutlLang);
			}
		}else{
			SingleLang singleLang = new SingleLang();
			singleLang.setValue(category.getName());
			categoryCommand.setName(singleLang);
		}

		return categoryCommand;
	}

	/** 所有可用的叶子节点的商品分类 */
	public Map<String, Long> getLeafNodeCategoryMap(){
		Map<String, Long> categoryMap = new HashMap<String, Long>();
		List<Category> categoryList = categoryDao.findEnableLeafNodeCategoryList(null);
		if (Validator.isNotNullOrEmpty(categoryList)){
			for (Category category : categoryList){
				categoryMap.put(category.getCode(), category.getId());
			}
		}
		return categoryMap;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> findSubCategoryListByParentCode(String categoryCode){
		return categoryDao.findSubCategoryListByParentCode(categoryCode);

	}
}
