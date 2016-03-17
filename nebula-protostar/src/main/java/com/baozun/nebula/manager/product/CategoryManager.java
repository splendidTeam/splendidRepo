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

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import com.baozun.nebula.command.product.CategoryCommand;
import com.baozun.nebula.enumeration.TreeMoveType;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.CodeRepeatException;
import com.baozun.nebula.exception.NameRepeatException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Category;

/**
 * 商品分类manager
 * 
 * @author yi.huang
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
public interface CategoryManager extends BaseManager{

	// ***************************************操作****************************************************************

	/**
	 * 在父级分类.父节点下面添加新的分类
	 * 
	 * @param parentId
	 *            父级分类.父节点Id
	 * @param code
	 *            添加商品分类编码
	 * @param name
	 *            分类名称
	 * @throws CodeRepeatException
	 *             code 重复
	 * @throws NameRepeatException
	 *             名称重复
	 * @throws BusinessException
	 */
	void addLeafCategory(Long parentId,String code,String name) throws CodeRepeatException,NameRepeatException,BusinessException;

	void addLeafCategory(CategoryCommand command) throws CodeRepeatException,NameRepeatException,BusinessException;

	/**
	 * 在选中节点 上方 插入一个 新的节点
	 * <ul>
	 * <li>将当前父类下 ,sortNo >= 选择的分类 ++sortNo</li>
	 * <li>插入一条 新的 sortNo =原来 选中id 的sortNo</li>
	 * </ul>
	 * 
	 * @param selectCategoryId
	 *            选中id
	 * @param code
	 *            编码
	 * @param name
	 *            名称
	 * @throws CodeRepeatException
	 * @throws NameRepeatException
	 * @throws BusinessException
	 */
	void insertSiblingCategory(Long selectCategoryId,String code,String name) throws CodeRepeatException,NameRepeatException,
			BusinessException;

	void insertSiblingCategory(Long selectCategoryId,CategoryCommand command) throws CodeRepeatException,NameRepeatException,
			BusinessException;

	/**
	 * (逻辑上)移除一个分类 以及子分类
	 * 
	 * @param id
	 *            要删除分类的Id
	 * @throws BusinessException
	 */
	void removeCategoryById(Long id) throws BusinessException;

	/**
	 * 修改一个商品分类
	 * 
	 * @param id
	 *            要修改的分类的ID
	 * @param code
	 *            分类编码
	 * @param name
	 *            分类名称
	 * @throws CodeRepeatException
	 * @throws NameRepeatException
	 * @throws BusinessException
	 */
	void updateCategory(Long id,String code,String name) throws CodeRepeatException,NameRepeatException,BusinessException;

	void updateCategory(CategoryCommand categoryCommand) throws CodeRepeatException,NameRepeatException,BusinessException;

	/**
	 * 拖拽分类
	 * 
	 * @param selectCategoryId
	 *            选择的分类id
	 * @param targetCategoryId
	 *            目标分类id
	 * @param treeMoveType
	 *            指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	 * @throws NameRepeatException
	 * @throws BusinessException
	 */
	void dropCategory(Long selectCategoryId,Long targetCategoryId,TreeMoveType treeMoveType) throws NameRepeatException,BusinessException;

	// ***************************************查询****************************************************************

	/**
	 * 基于 category 名字 和 parentId 查询
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	Category findEnableCategoryByNameAndParentId(Long parentId,String name);

	/**
	 * 根据ID查询一个商品分类
	 * 
	 * @param id
	 *            分类的id
	 * @return Category
	 */
	Category findCategoryById(Long id);

	/**
	 * 基于 code 查询 商品分类
	 * 
	 * @param code
	 *            分类code
	 * @return
	 */
	Category findCategoryByCode(String code);

	/**
	 * 查询出所有可用的商品分类
	 * 
	 * @param sort
	 *            排序(loxia)
	 * @return
	 */
	List<Category> findEnableCategoryList(Sort[] sort);

	/**
	 * 查询出所有可用的商品分类
	 * 
	 * @param sort
	 *            排序(loxia)
	 * @return
	 */
	List<CategoryCommand> findEnableCategoryCommandList(Sort[] sort);

	/**
	 * 根据分类Id数组查询商品分类
	 * 
	 * @param categoryIds
	 *            分类id数组
	 * @return
	 */
	List<Category> findCategoryListByCategoryIds(Long[] categoryIds);

	/**
	 * 基于 parentId 查询 分类.
	 * 
	 * @param categoryId
	 * @return
	 */
	List<Category> findCategoryListByParentId(Long categoryId);

	CategoryCommand findCategoryLangByCategoryId(Long categoryId);

	/** 查询出所有可用的叶子节点的商品分类 */
	Map<String, Long> getLeafNodeCategoryMap();

	/**
	 * 根据code
	 * 
	 * @param categoryCode
	 * @return
	 */
	List<Category> findSubCategoryListByParentCode(String categoryCode);

}
