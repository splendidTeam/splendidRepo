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
package com.baozun.nebula.dao.product;

import java.util.Collection;
import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

import com.baozun.nebula.command.rule.MiniItemAtomCommand;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.CategoryLang;

/**
 * 商品分类处理Dao.
 * 
 * @author yi.huang
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 */
public interface CategoryDao extends GenericEntityDao<Category, Long>{

	/**
	 * 在父级分类.父节点下面添加新的分类
	 * 
	 * @param parentId
	 *            父级分类.父节点Id
	 * @param code
	 *            商品分类编码
	 * @param name
	 *            分类名称
	 * @return the integer
	 */
	@NativeUpdate
	Integer insertCategory(@QueryParam("parentId") Long parentId,@QueryParam("code") String code,@QueryParam("name") String name);

	/**
	 * 修改一个商品分类.
	 * 
	 * @param id
	 *            要修改的分类的ID
	 * @param code
	 *            分类编码
	 * @param name
	 *            分类名称
	 * @return the integer
	 */
	@NativeUpdate
	Integer updateCategoryById(@QueryParam("id") Long id,@QueryParam("code") String code,@QueryParam("name") String name);

	// *************************************tree 操作*************************************************************************************

	/**
	 * 在父级分类.父节点下面添加新的分类(带sortNo)
	 * 
	 * @param parentId
	 *            父级分类.父节点Id
	 * @param code
	 *            商品分类编码
	 * @param name
	 *            分类名称
	 * @param sortNo
	 *            排序值
	 * @return the integer
	 */
	@NativeUpdate
	Integer treeInsertNodeWithSortNo(
			@QueryParam("parentId") Long parentId,
			@QueryParam("code") String code,
			@QueryParam("name") String name,
			@QueryParam("sortNo") Integer sortNo);

	/**
	 * 修改Category ParentId(sortNo修改成 parentId 子 sortNo+1).
	 * 
	 * @param categoryId
	 *            要修改的分类的ID
	 * @param parentId
	 *            parentId 修改parentId
	 * @return the integer
	 */
	@NativeUpdate
	Integer treeUpdateNodeParentId(@QueryParam("categoryId") Long categoryId,@QueryParam("parentId") Long parentId);

	/**
	 * 修改 category 父类id和 sortNo.
	 * 
	 * @param categoryId
	 *            要修改的分类的ID
	 * @param parentId
	 *            parentId 修改parentId
	 * @param sortNo
	 *            the sort no
	 * @return the integer
	 */
	@NativeUpdate
	Integer treeUpdateNodeParentIdAndSortNo(
			@QueryParam("categoryId") Long categoryId,
			@QueryParam("parentId") Long parentId,
			@QueryParam("sortNo") Integer sortNo);

	/**
	 * 将当前父类下 ,sortNo >= 目标分类 sortNo +1.
	 * 
	 * @param targetNodeId
	 *            the target node id
	 * @return the integer
	 */
	@NativeUpdate
	Integer treeIncrSortNoGtAndEqTargetNodeSortNo(@QueryParam("targetNodeId") Long targetNodeId);

	/**
	 * 将当前父类下 ,sortNo<=目标分类 的 sortNo -1 .
	 * 
	 * @param targetNodeId
	 *            目标节点
	 * @return the integer
	 */
	@NativeUpdate
	Integer treeDecrSortNoLtAndEqTargetNodeSortNo(@QueryParam("targetNodeId") Long targetNodeId);

	// *********************************删除***************************************************************************************
	/**
	 * (逻辑上)移除一个分类.
	 * 
	 * @param id
	 *            分类的ID
	 * @return the integer
	 */
	@NativeUpdate
	Integer removeCategoryById(@QueryParam("id") Long id);

	/**
	 * (逻辑上)移除多个分类.
	 * 
	 * @param categoryIds
	 *            分类的ID
	 * @return the integer
	 */
	@NativeUpdate
	Integer removeCategoryByIds(@QueryParam("categoryIds") List<Long> categoryIds);

	// *********************************查询***************************************************************************************
	/**
	 * 根据ID查询.
	 * 
	 * @param id
	 *            the id
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	Category findCategoryById(@QueryParam("id") Long id);
	
	@NativeQuery(model = Category.class)
	Category findCategoryByIdI18n(@QueryParam("id") Long id,@QueryParam("lang")String lang);

	/**
	 * 根据code查询.
	 * 
	 * @param code
	 *            商品分类
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	Category findCategoryByCode(@QueryParam("code") String code);
	
	@NativeQuery(model = Category.class)
	Category findCategoryByCodeI18n(@QueryParam("code") String code, @QueryParam("lang")String lang);

	/**
	 * 根据名称 和 parentid查询 可用的.
	 * 
	 * @param parentId
	 *            parentId
	 * @param name
	 *            名称
	 * @return
	 */
	@NativeQuery(model = Category.class)
	Category findEnableCategoryByNameAndParentId(@QueryParam("parentId") Long parentId,@QueryParam("name") String name);

	/**
	 * 查询出所有可用的叶子节点的商品分类.
	 * 
	 * @param sorts
	 *            排序数组
	 * @return the list
	 */
	@NativeQuery(model = Category.class)
	List<Category> findEnableLeafNodeCategoryList(Sort[] sorts);
	
	
	/**
	 * 查询出所有可用的商品分类.
	 * 
	 * @param sorts
	 *            排序数组
	 * @return the list
	 */
	@NativeQuery(model = Category.class)
	List<Category> findEnableCategoryList(Sort[] sorts);
	
	

	/**
	 * 根据分类Id数组查询商品分类.
	 * 
	 * @param categoryIds
	 *            分类id数组
	 * @return the list
	 */
	@NativeQuery(model = Category.class)
	List<Category> findCategoryListByCategoryIds(@QueryParam("categoryIds") Long[] categoryIds);

	
	/**
	 * 查询所有可用分类
	 * @return
	 */
	@NativeQuery(model = Category.class)
	List<Category> findEnableAllCategory();
	
	/**
	 * 基于 parentId 查询 分类.
	 * 
	 * @param parentId
	 *            parentId
	 * @return the list
	 */
	@NativeQuery(model = Category.class)
	List<Category> findCategoryListByParentId(@QueryParam("parentId") Long parentId);
	
	/**
	 * 基于 parentId 查询可用 分类.
	 * 
	 * @param parentId
	 *            parentId
	 * @return the list
	 */
	@NativeQuery(model = Category.class)
	List<Category> findEnableCategoryListByParentId(@QueryParam("parentId") Long parentId);
	
	/**
	 * 根据ID查询可用分类.
	 * 
	 * @param id
	 *            the id
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	Category findEnableCategoryById(@QueryParam("id") Long id);

	/**
	 * 根据ID集合查询可用分类集合.
	 * 
	 * @param id
	 *            the id
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	List<Category> findCategoryListByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 根据Code集合查询可用分类集合.
	 * 
	 * @param id
	 *            the id
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	List<Category> findCategoryListByCodes(@QueryParam("codes") Collection<String> codes);
	
	@NativeQuery(model = Category.class)
	List<Category> findCategoryListByCodesI18n(@QueryParam("codes") Collection<String> codes,@QueryParam("lang")String lang);
	/**
	 * 根据Code集合查询可用分类集合.
	 * 
	 * @param id
	 *            the id
	 * @return the category
	 */
	@NativeQuery(model = Category.class)
	List<Category> findSubCategoryListByParentCode(@QueryParam("code") String code);
	
	@NativeQuery(model = Category.class)
	List<Category> findSubCategoryListByParentCodeI18n(@QueryParam("code") String code,@QueryParam("lang")String lang);

	/**
	 * 根据id列表查询简要的分类信息
	 * @param idList
	 * @return
	 */
	@NativeQuery(model = MiniItemAtomCommand.class)
	List<MiniItemAtomCommand> findMiniTagRuleCommandByIdList(@QueryParam("idList") List<Long> idList);
	
	@NativeQuery(clazzes = Integer.class,alias={"seq"})
	int findCategorySeqByParentId(@QueryParam("parentId") Long parentId);
	
	@NativeQuery(clazzes = Integer.class,alias={"count"})
	int findCategoryLangByNameAndParentId(@QueryParam("name") String name,@QueryParam("lang") String lang,@QueryParam("cIds")List<Long> cIds);
	
	@NativeUpdate
	int updateCategoryLangByCategoryIdAndLang(@QueryParam("name") String name,@QueryParam("lang") String lang,@QueryParam("cId")Long cId);

	@NativeQuery(model = CategoryLang.class)
	List<CategoryLang> findCategoryLangList(@QueryParam("categoryIds")List<Long> categoryIds,@QueryParam("langs") List<String> langs);
	
	
	@NativeQuery(model = CategoryLang.class)
	CategoryLang findCategoryLang(@QueryParam("id")Long id,@QueryParam("lang") String lang);
}
