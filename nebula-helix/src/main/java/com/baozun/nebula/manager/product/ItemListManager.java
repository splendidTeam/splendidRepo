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
package com.baozun.nebula.manager.product;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.DataFromSolrCommand;
import com.baozun.nebula.sdk.command.PropertyCommand;
import com.baozun.nebula.sdk.command.PropertyValueCommand;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionResultCommand;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.utils.SolrOrderSort;

/**
 * @author Tianlong.Zhang
 *
 */
public interface ItemListManager extends BaseManager{
	/**
	 * 显示商品列表，调用solr
	 * 推荐使用search 。 
	 */
	@Deprecated
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
	 * @return 该分类Id下的所有分类
	 */
	public List<CategoryCommand> findCategoryList(Long categoryId,Long rootCategoryId);
	
	/**
	 * 根据父cid查询所有有效的子分类
	 * @param parentCategoryId
	 * @return
	 */
	public List<CategoryCommand> findSubCategoryListByPid(Long parentCategoryId);
	
	public List<CategoryCommand> findCategorysByCid(List<Long> ids);
	
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
	
	CategoryCommand findCategoryByCode(String code);
	
	List<CategoryCommand> findCategoryByCodes(Collection<String> codes);
	
	List<CategoryCommand> findCategoryByParentCode(String parentCode);
	
	public PropertyCommand findPropertyById(Long id);
	
	public List<PropertyCommand> findPropertyByIds(List<Long> ids);
	
	List<PropertyValueCommand> findPropertyValueListById(Long propertyId);
	
	public void getFacetsByCid(Long categoryId, Set<String> facetFields,Map<Long,SearchConditionResultCommand> searchConditionResultMap,SearchConditionCommand priceSearchCodition ,QueryConditionCommand qCmd,Map<Long,PropertyCommand> propertyCommandMap);

	/**
	 * 根据条件搜索要查询的商品
	 * @param categoryCmdList  要搜索的分类
	 * @param searchKey	搜索的关键字
	 * @param searchConditionMap 搜索的过滤条件
	 * @param priceCdtItemId	搜索的价格条件
	 * @param salePriceArea		自定义的价格条件
	 * @param orderSort			排序条件
	 * @param pageSize			每页的数量
	 * @param curPage			当前页数
	 * @return
	 */
	DataFromSolrCommand search(List<CategoryCommand> categoryCmdList,
			String searchKey,Map<Long,List<Long>> searchConditionMap, Long priceCdtItemId,
			String salePriceArea, SolrOrderSort[] orderSort, Integer pageSize,
			Integer curPage);

	/**
	 * 根据属性名称查询对应的属性值列表
	 * @param propertyName
	 * @return
	 */
	public List<PropertyValueCommand> findPropertyValueListByPropertyName(String propertyName);
	
	/**
	 * 获取商品的促销活动
	 * @param itemIds
	 * @param userDetails
	 * @return
	 */
	public Map<Long,List<PromotionCommand>> getPromotionsForItemList(List<Long> itemIds, UserDetails userDetails);
	
	/**
	 * 获取商品的促销活动和促销价格
	 * @param itemIds
	 * @param UserDetails userDetails
	 * 
	 * @return Map<itemId, Map<"disCountAmount"/"promotionList", 单品优惠金额/促销活动列表 >>
	 * disCountAmount : 单品优惠金额
	 * promotionList : 促销活动列表
	 * 
	 * 通过map.get(itemId).get("disCountAmount")单品优惠金额
	 * 通过map.get(itemId).get("promotionList")获取促销活动列表
	 * 
	 */
	public Map<Long,Map<String,Object>> getPromotionsAndDisCountForItemList(List<Long> itemIds, UserDetails userDetails);
	/**
	 * 根据订单行（t_so_orderpromotion）上的Promotion List，从DB中取促销活动信息（可能过期不在内存中），
	 * 计算优惠设置，单品优惠金额。典型应用场景订单详情页。
	 */
	public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemListByOrderId(Long orderId);
}
