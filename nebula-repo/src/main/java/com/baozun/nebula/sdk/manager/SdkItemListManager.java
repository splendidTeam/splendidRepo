/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
 *
 */
package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.PropertyCommand;
import com.baozun.nebula.sdk.command.PropertyValueCommand;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.utils.SolrOrderSort;

/**
 * 商品列表Manager
 * @author Tianlong.Zhang
 *
 */
public interface SdkItemListManager extends BaseManager{
	
	
	/**
	 * 显示商品列表，调用solr
	 */
	public DataFromSolr findItemList(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, String groupField,Integer currentPage);
	
	/**
	 * 获取某分类的筛选条件列表
	 * @param categoryId
	 * @return
	 */
	public List<SearchConditionCommand> findSearchCoditionList(Long categoryId);
	
	/**
	 * 获取面包屑
	 * @param categoryId
	 * @return
	 */
	public List<CurmbCommand> findCurmbList(Long categoryId);

	/**
	 * 获取分类树
	 * @param categoryId
	 * @return 该分类Id 对应的一级分类，所有的二级分类，以及二级分类下的小分类
	 */
	public List<CategoryCommand> findCategoryList(Long categoryId,Long rootCategoryId);
	
	/**
	 * 获取关键字联想提示
	 * @return 联想的词组List
	 */
	public SuggestCommand findKeyAssociateList(String key);
	
	/**
	 * 对比商品
	 * @param categoryId
	 * @param itemIds
	 */
	public void contrastItem(Long categoryId,List<Long> itemIds);
	
	CategoryCommand findCategoryById(Long id);
	
	public PropertyCommand findPropertyById(Long id);
	
	List<PropertyValueCommand> findPropertyValueListById(Long propertyId);
	
	List<PropertyCommand> findPropertyByIds(List<Long> ids);
}
