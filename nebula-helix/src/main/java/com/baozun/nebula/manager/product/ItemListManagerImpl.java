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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemListResultCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.DataFromSolrCommand;
import com.baozun.nebula.sdk.command.PropertyCommand;
import com.baozun.nebula.sdk.command.PropertyValueCommand;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.command.SearchConditionResultCommand;
import com.baozun.nebula.sdk.command.SearchItemResultCommand;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.sdk.manager.SdkPromotionGuideManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.convert.CommandConvert;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.FilterUtil;
import com.baozun.nebula.solr.utils.PaginationForSolr;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.constants.CommonUrlConstants;

/**
 * @author Tianlong.Zhang
 *
 */
@Transactional
@Service("itemListManager")
public class ItemListManagerImpl implements ItemListManager{
	
	private static final Logger log = LoggerFactory
	.getLogger(ItemListManager.class);
	
	private static final String COLON_CONNECTOR = ":";
	
	private static final String TYPE_ID = "id";
	
	private static final String SEARCH_URL = CommonUrlConstants.SEARCH_URL;
	
	private static final String CATEGORY_URL = CommonUrlConstants.CATEGORY_URL;

	@Autowired
	private SearchConditionManager searchConditionManager;
	
//	@Autowired
//	private SdkItemListManager sdkItemListManager;
	
	@Autowired
	private CategoryDao	categoryDao;
	
	@Autowired
	private ItemSolrManager itemSolrManager;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private SdkNavigationManager sdkNavigationManager;
	
	@Autowired
	private NavigationDao navigationDao;
	
	@Autowired
	private PropertyValueDao propertyValueDao;
	
	@Autowired
	private PropertyDao propertyDao;
	
	@Autowired
	private SdkPromotionGuideManager sdkPromotionGuideManager;
	
	@Value("#{meta['crumbUrlType']}")
	private String CRUMB_TYPE;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findItemList()
	 */
	@Override
	public DataFromSolr findItemList(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, String groupField,Integer currentPage) {
		return itemSolrManager.queryItemForAll(rows, queryConditionCommand, facetFields, order, groupField,currentPage);
		//		return sdkItemListManager.findItemList(rows, queryConditionCommand, facetFields, order, groupField, currentPage);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findSearchCoditionList(java.lang.Long)
	 */
	@Override
	public List<SearchConditionCommand> findSearchCoditionList(Long categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCurmbList(java.lang.Long)
	 */
	@Override
	public List<CurmbCommand> findCurmbList(Long categoryId) {
// rootCategoryId 为0 ，但是这个记录并不在真正的存在。
		
		List<CurmbCommand> cmdList = new LinkedList<CurmbCommand>();

		Long pid = categoryId;
		do{
			Category category = null;
			
			boolean i18n = LangProperty.getI18nOnOff();
			if(i18n){
				category = categoryDao.findCategoryByIdI18n(pid, LangUtil.getCurrentLang());
			}else{
				category = categoryDao.findCategoryById(pid);
			}
			
			if(null != category){
				pid = category.getParentId();
				CategoryCommand cmd = new CategoryCommand();
				cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, category);
				CurmbCommand curmbCommand  = categoryCommand2CurmbCommand(cmd);
				curmbCommand = getUrl(curmbCommand);
				cmdList.add(0, curmbCommand);
			}
			
		}while(pid!=null&&pid>0);
		
		return cmdList;
//		return sdkItemListManager.findCurmbList(categoryId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategoryList(java.lang.Long)
	 */
	@Override
	public List<CategoryCommand> findCategoryList(Long categoryId,Long rootCategoryId) {
		List<CategoryCommand> cmdList =new ArrayList<CategoryCommand>();
		
		if(null!=categoryId&&categoryId>0){ //如果是正确的分类，则查询出该Id 对应的 一级或者二级分类
			
			List<CurmbCommand> curmbList = findCurmbList(categoryId);
			//如果curmbList 不为空的话，第一个是一级分类 信息， (rootCategoryId 为0 ，但是这个记录并不在真正的存在。)
			CategoryCommand firstLvlCmd= new CategoryCommand();
			if(curmbList.size()>=1){
				Category c = categoryDao.findCategoryById(curmbList.get(0).getId()); 
				firstLvlCmd = (CategoryCommand) ConvertUtils.convertFromTarget(firstLvlCmd, c);
			}
			
			cmdList.add(firstLvlCmd);
			
			//该分类Id 对应的一级分类，所有的二级分类，以及二级分类下的小分类
			
			//二级分类
			List<Category> secondCtgList = categoryDao.findEnableCategoryListByParentId(firstLvlCmd.getId());
			for(Category c : secondCtgList){
				CategoryCommand cmd = new CategoryCommand();
				cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, c);
				cmdList.add(cmd);
				
				//三级分类
				List<Category> thirdCtgList = categoryDao.findEnableCategoryListByParentId(c.getId());
				for(Category thirdCtg : thirdCtgList){
					CategoryCommand trdCmd = new CategoryCommand();
					trdCmd = (CategoryCommand) ConvertUtils.convertFromTarget(trdCmd, thirdCtg);
					cmdList.add(trdCmd);
				}
			}
		}else{//如果没有分类，对应了搜索进入，此时返回所有的分类。然后根据搜索到的
			cmdList = getChildrenCategoryListRecursion(rootCategoryId,cmdList);
			
		}
		
		return cmdList;
		
//		return sdkItemListManager.findCategoryList(categoryId,rootCategoryId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findKeyAssociateList(java.lang.String)
	 */
	@Override
	public SuggestCommand findKeyAssociateList(String key) {
		return itemSolrManager.keywordSpellSpeculation(key);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#contrastItem(java.lang.Long, java.util.List)
	 */
	@Override
	public void contrastItem(Long categoryId, List<Long> itemIds) {
		
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategoryById(java.lang.Long)
	 */
	@Override
	public CategoryCommand findCategoryById(Long id) {
		boolean i18n = LangProperty.getI18nOnOff();
		Category category = null;
		if(i18n){
			category = categoryDao.findCategoryByIdI18n(id, LangUtil.getCurrentLang());
		}else{
			category = categoryDao.getByPrimaryKey(id);
		}
		if(category==null) return null;
		
		CategoryCommand cmd = new CategoryCommand();
		cmd.setCode(category.getCode());
		cmd.setCreateTime(category.getCreateTime());
		cmd.setId(category.getId());
		cmd.setLifecycle(category.getLifecycle());
		cmd.setModifyTime(category.getModifyTime());
		cmd.setName(category.getName());
		cmd.setParentId(category.getParentId());
		cmd.setSortNo(category.getSortNo());

		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findPropertyById(java.lang.Long)
	 */
	@Override
	public PropertyCommand findPropertyById(Long id) {
		Property p = propertyDao.findPropertyById(id);
		PropertyCommand cmd = new PropertyCommand();
		cmd = (PropertyCommand) ConvertUtils.convertFromTarget(cmd, p);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findPropertyValueListById(java.lang.Long)
	 */
	@Override
	public List<PropertyValueCommand> findPropertyValueListById(Long propertyId) {
		List<PropertyValue> valueList =  propertyValueDao.findPropertyValueListById(propertyId);
		List<PropertyValueCommand> cmdList = new ArrayList<PropertyValueCommand>();
		if(null!=valueList){
			for(PropertyValue pv:valueList){
				PropertyValueCommand cmd = new PropertyValueCommand();
				cmd = (PropertyValueCommand)ConvertUtils.convertFromTarget(cmd, pv);
				cmdList.add(cmd);
			}
		}
		
		return cmdList;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findPropertyById(java.util.List)
	 */
	@Override
	public List<PropertyCommand> findPropertyByIds(List<Long> ids) {
		List<PropertyCommand> cmdList = new ArrayList<PropertyCommand>();
		
		List<Property> propertyList = propertyDao.findPropertyListByIds(ids, null);
		
		if(propertyList!=null){
			for(Property p:propertyList){
				PropertyCommand cmd = new PropertyCommand();
				cmd = (PropertyCommand)ConvertUtils.convertFromTarget(cmd, p);
				cmdList.add(cmd);
			}
		}
		
		return cmdList;
//		return sdkItemListManager.findPropertyByIds(ids);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#getFacetsByCid(java.lang.Long, java.util.Set, java.util.Map, com.baozun.nebula.sdk.command.SearchConditionCommand, com.baozun.nebula.solr.command.QueryConditionCommand, java.util.Map)
	 */
	@Override
	@Deprecated
	public void getFacetsByCid(Long categoryId, Set<String> facetFields,
			Map<Long, SearchConditionResultCommand> searchConditionResultMap,
			SearchConditionCommand priceSearchCodition,
			QueryConditionCommand qCmd,
			Map<Long, PropertyCommand> propertyCommandMap) {
//		if(categoryId!=null &&categoryId < 1){
//			throw new BusinessException(ErrorCodes.PARAMS_ERROR);
//		}

		//根据CategoryId查询该分类下的所有搜索选项条件（同时返回所有CategoryId为null 的记录） 
		/**
		 * 	根据传入的cid ，在condition表中拿到所有cid对应的 condition_item，
		 * 然后在condition_item中根据刚才的sid 查出 c_items ，
		 * 看每个c_items 的propertyValueId 是否为空，如果为空，
		 * 那么就用searchConditionItemId 
		 */
		List<SearchConditionCommand> cmdList = searchConditionManager
				.findConditionByCategoryId(categoryId);
		
		if (null == cmdList || cmdList.size() < 1) {
			return ;
		}

		List<String> propertyList = new ArrayList<String>();
		
		if(cmdList!=null&&cmdList.size()>0){
			
			List<PropertyCommand> pList = getPropertyListBySearchConditionCommandList(cmdList);
			
			if(pList!=null){
				for(PropertyCommand propertyCmd : pList){
					propertyCommandMap.put(propertyCmd.getId(), propertyCmd);
				}
			}
		}
		
		for (SearchConditionCommand cmd : cmdList) {
			
			Long propertyId = cmd.getPropertyId();
			
			PropertyCommand propertyCmd = null;
			
			if(null!= propertyId){
				propertyCmd = findPropertyById(propertyId);
				propertyCommandMap.put(propertyId, propertyCmd);
//				if(null!= propertyCmd&&propertyCmd.getIsColorProp()){
//					qCmd.setIsSpread(true);
//				}
				Integer type = cmd.getType();
				
				List<SearchConditionItemCommand> scItemList = null;
				
				//如果类型为常规属性,则通过propertyId来找对应的searchConditionItem
				if(cmd.getType().equals(SearchCondition.NORMAL_TYPE)){
					scItemList = searchConditionManager.findItemByPropertyId(propertyId);
				}
				//否则还是通过ConditionId来查找SearchConditionItem列表
				else{
					scItemList = searchConditionManager.findItemBySId(cmd.getId());
				}
				
				List<SearchItemResultCommand> sirCmdList = new ArrayList<SearchItemResultCommand>();
				if(SearchCondition.SALE_PRICE_TYPE.equals(type)){//如果是价格区间,  价格区间的显示值不从propertyValue中来
					
					cmd.setItemList(scItemList);
					
					//这个时候 propertyId 是空的
					priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
		
					for(SearchConditionItemCommand scItemCmd : scItemList){
						if(null!= scItemCmd){
							Integer min = scItemCmd.getAreaMin();
							Integer max = scItemCmd.getAreaMax();
							if(null!=min&&null!=max&&min<=max){
								String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
								StringBuilder sb = new StringBuilder();
								sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(areaStr);
								propertyList.add(sb.toString());
							}
						}
					}
				}else{// 一般类型和区间类型 都是 放在 动态属性里边。 此时 propertyCmd 肯定不为null,因为  searchCondition 中的propertyId 不为null
					if(propertyCmd!=null){
						propertyList.add(SkuItemParam.dynamicCondition+propertyId.toString());
					}
					
//					List<SearchConditionItemCommand> scItemList = searchConditionManager.findItemBySId(cmd.getId());
					
					for(SearchConditionItemCommand scItemCmd : scItemList){
						SearchItemResultCommand sirc = new SearchItemResultCommand();
						if(scItemCmd.getPropertyValueId()==null||scItemCmd.getPropertyValueId().equals(0L)){  //propertyValueId 是否为空，如果为空， 那么就用searchConditionItemId 
							sirc.setItemKey(scItemCmd.getId());
						}else{
							sirc.setItemKey(scItemCmd.getPropertyValueId());
						}
						sirc.setItemName(scItemCmd.getName());
						sirc.setSort(scItemCmd.getSort());
						
						sirCmdList.add(sirc);
					}
					
					SearchConditionResultCommand scrCmd = new SearchConditionResultCommand();
					
					scrCmd.setsId(cmd.getPropertyId());// Id 为 propertyId
					scrCmd.setName(cmd.getName()); // 显示的名称为 searchCondition 中的Name
					scrCmd.setSort(cmd.getSort()); // 排序也为 searchCondition 的sort
					scrCmd.setItems(sirCmdList);  // 具体搜索项要展示的的信息
					
					searchConditionResultMap.put(scrCmd.getsId(), scrCmd);
					
				}
			}else{
				Integer type = cmd.getType();
//				priceSearchCodition = new SearchConditionCommand();
				priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
				List<SearchConditionItemCommand> scItemList = searchConditionManager.findItemBySId(cmd.getId());
				
				List<SearchItemResultCommand> sirCmdList = new ArrayList<SearchItemResultCommand>();
				
				cmd.setItemList(scItemList);
				
				//这个时候 propertyId 是空的
				priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
				
				for(SearchConditionItemCommand scItemCmd : scItemList){
					if(null!= scItemCmd){
						Integer min = scItemCmd.getAreaMin();
						Integer max = scItemCmd.getAreaMax();
						if(null!=min&&null!=max&&min<=max){
							String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
							StringBuilder sb = new StringBuilder();
							sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(areaStr);
							propertyList.add(sb.toString());
						}
					}
				}
				
				for(SearchConditionItemCommand scItemCmd : scItemList){
					SearchItemResultCommand sirc = new SearchItemResultCommand();
					if(scItemCmd.getPropertyValueId()==null||scItemCmd.getPropertyValueId().equals(0L)){  //propertyValueId 是否为空，如果为空， 那么就用searchConditionItemId 
						sirc.setItemKey(scItemCmd.getId());
					}else{
						sirc.setItemKey(scItemCmd.getPropertyValueId());
					}
					sirc.setItemName(scItemCmd.getName());
					sirc.setSort(scItemCmd.getSort());
					
					sirCmdList.add(sirc);
				}
				
				priceSearchCodition.setItemList(scItemList);
			}
			
		}

		facetFields.addAll(propertyList);

		return ;
		
	}
	
	private void getFacetsByCids(List<Long> categoryIds, Set<String> facetFields,
			Map<Long, SearchConditionResultCommand> searchConditionResultMap,
			SearchConditionCommand priceSearchCodition,
			QueryConditionCommand qCmd,
			Map<Long, PropertyCommand> propertyCommandMap) {
//		if(categoryId!=null &&categoryId < 1){
//			throw new BusinessException(ErrorCodes.PARAMS_ERROR);
//		}

		//根据CategoryId查询该分类下的所有搜索选项条件（同时返回所有CategoryId为null 的记录） 
		/**
		 * 	根据传入的cid ，在condition表中拿到所有cid对应的 condition_item，
		 * 然后在condition_item中根据刚才的sid 查出 c_items ，
		 * 看每个c_items 的propertyValueId 是否为空，如果为空，
		 * 那么就用searchConditionItemId 
		 */
		List<SearchConditionCommand> cmdList = searchConditionManager
				.findConditionByCategoryIds(categoryIds);
		
		if (null == cmdList || cmdList.size() < 1) {
			return ;
		}

		List<String> propertyList = new ArrayList<String>();
		
		if(cmdList!=null&&cmdList.size()>0){
			
			List<PropertyCommand> pList = getPropertyListBySearchConditionCommandList(cmdList);
			
			if(pList!=null){
				for(PropertyCommand propertyCmd : pList){
					propertyCommandMap.put(propertyCmd.getId(), propertyCmd);
				}
			}
		}
		
		for (SearchConditionCommand cmd : cmdList) {
			
			Long propertyId = cmd.getPropertyId();
			
			PropertyCommand propertyCmd = null;
			
			if(null!= propertyId){
				propertyCmd = findPropertyById(propertyId);
				propertyCommandMap.put(propertyId, propertyCmd);
//				if(null!= propertyCmd&&propertyCmd.getIsColorProp()){
//					qCmd.setIsSpread(true);
//				}
				Integer type = cmd.getType();
				
				List<SearchConditionItemCommand> scItemList = null;
				
				//如果类型为常规属性,则通过propertyId来找对应的searchConditionItem
				if(cmd.getType().equals(SearchCondition.NORMAL_TYPE)){
					scItemList = searchConditionManager.findItemByPropertyId(propertyId);
				}
				//否则还是通过ConditionId来查找SearchConditionItem列表
				else{
					scItemList = searchConditionManager.findItemBySId(cmd.getId());
				}
				
				List<SearchItemResultCommand> sirCmdList = new ArrayList<SearchItemResultCommand>();
				if(SearchCondition.SALE_PRICE_TYPE.equals(type)){//如果是价格区间,  价格区间的显示值不从propertyValue中来
					
					cmd.setItemList(scItemList);
					
					//这个时候 propertyId 是空的
					priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
		
					for(SearchConditionItemCommand scItemCmd : scItemList){
						if(null!= scItemCmd){
							Integer min = scItemCmd.getAreaMin();
							Integer max = scItemCmd.getAreaMax();
							if(null!=min&&null!=max&&min<=max){
								String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
								StringBuilder sb = new StringBuilder();
								sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(areaStr);
								propertyList.add(sb.toString());
							}
						}
					}
				}else{// 一般类型和区间类型 都是 放在 动态属性里边。 此时 propertyCmd 肯定不为null,因为  searchCondition 中的propertyId 不为null
					if(propertyCmd!=null){
						propertyList.add(SkuItemParam.dynamicCondition+propertyId.toString());
					}
					
//					List<SearchConditionItemCommand> scItemList = searchConditionManager.findItemBySId(cmd.getId());
					
					for(SearchConditionItemCommand scItemCmd : scItemList){
						SearchItemResultCommand sirc = new SearchItemResultCommand();
						if(scItemCmd.getPropertyValueId()==null||scItemCmd.getPropertyValueId().equals(0L)){  //propertyValueId 是否为空，如果为空， 那么就用searchConditionItemId 
							sirc.setItemKey(scItemCmd.getId());
						}else{
							sirc.setItemKey(scItemCmd.getPropertyValueId());
						}
						sirc.setItemName(scItemCmd.getName());
						sirc.setSort(scItemCmd.getSort());
						
						sirCmdList.add(sirc);
					}
					
					SearchConditionResultCommand scrCmd = new SearchConditionResultCommand();
					
					scrCmd.setsId(cmd.getPropertyId());// Id 为 propertyId
					scrCmd.setName(cmd.getName()); // 显示的名称为 searchCondition 中的Name
					scrCmd.setSort(cmd.getSort()); // 排序也为 searchCondition 的sort
					scrCmd.setItems(sirCmdList);  // 具体搜索项要展示的的信息
					
					searchConditionResultMap.put(scrCmd.getsId(), scrCmd);
					
				}
			}else{
				Integer type = cmd.getType();
//				priceSearchCodition = new SearchConditionCommand();
				priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
				List<SearchConditionItemCommand> scItemList = searchConditionManager.findItemBySId(cmd.getId());
				
				List<SearchItemResultCommand> sirCmdList = new ArrayList<SearchItemResultCommand>();
				
				cmd.setItemList(scItemList);
				
				//这个时候 propertyId 是空的
				priceSearchCodition = (SearchConditionCommand) ConvertUtils.convertFromTarget(priceSearchCodition, cmd);
				
				for(SearchConditionItemCommand scItemCmd : scItemList){
					if(null!= scItemCmd){
						Integer min = scItemCmd.getAreaMin();
						Integer max = scItemCmd.getAreaMax();
						if(null!=min&&null!=max&&min<=max){
							String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
							StringBuilder sb = new StringBuilder();
							sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(areaStr);
							propertyList.add(sb.toString());
						}
					}
				}
				
				for(SearchConditionItemCommand scItemCmd : scItemList){
					SearchItemResultCommand sirc = new SearchItemResultCommand();
					if(scItemCmd.getPropertyValueId()==null||scItemCmd.getPropertyValueId().equals(0L)){  //propertyValueId 是否为空，如果为空， 那么就用searchConditionItemId 
						sirc.setItemKey(scItemCmd.getId());
					}else{
						sirc.setItemKey(scItemCmd.getPropertyValueId());
					}
					sirc.setItemName(scItemCmd.getName());
					sirc.setSort(scItemCmd.getSort());
					
					sirCmdList.add(sirc);
				}
				
				priceSearchCodition.setItemList(scItemList);
			}
			
		}

		facetFields.addAll(propertyList);

		return ;
		
	}

	private List<PropertyCommand> getPropertyListBySearchConditionCommandList(List<SearchConditionCommand> cmdList){
		if(cmdList == null){
			return null;
		}
		//findPropertyByIds
		List<Long> propertyIdList = new ArrayList<Long>();
		for(SearchConditionCommand scCmd:cmdList){
			if(scCmd.getPropertyId()!=null){
				propertyIdList.add(scCmd.getPropertyId());
			}
		}
		
		return findPropertyByIds(propertyIdList);
	} 
	
	private CurmbCommand categoryCommand2CurmbCommand(CategoryCommand cCmd){
		CurmbCommand c = new CurmbCommand();
		c = (CurmbCommand) ConvertUtils.convertFromTarget(c, cCmd);
		return c;
	}
	
	private CurmbCommand getUrl(CurmbCommand crumb){
		Navigation navigation = navigationDao.findNavigationByCategoryId(crumb.getId());
		
		if(navigation!=null){
			crumb.setType(navigation.getType());
			crumb.setUrl(navigation.getUrl());
		}else{
			crumb.setUrl(null);
			crumb.setType(null);
		}
		//根据规则获得面包屑的url
		Integer type = crumb.getType();
		Long id = crumb.getId();
		String code = crumb.getCode();
		
		String str = null;
		if(CRUMB_TYPE.equals(TYPE_ID)){
			str = id.toString();
		}else{
			str = code ;
		}
		String url = "";
		if(type == null){
			url = SEARCH_URL + str;
		}else if(type == 1){
			url = crumb.getUrl();
		}else if(type == 2){
			url = CATEGORY_URL + str;
		}
		crumb.setUrl(url);
		return crumb;
	}
	
	private List<CategoryCommand> getChildrenCategoryListRecursion(Long categoryId,List<CategoryCommand> cmdList){
		
		if (Validator.isNullOrEmpty(cmdList)){
			cmdList = new ArrayList<CategoryCommand>();
		}
		List<Category> childrenCategoryList = categoryDao.findCategoryListByParentId(categoryId);

		if (Validator.isNotNullOrEmpty(childrenCategoryList)){

			for (Category category : childrenCategoryList){
				Integer lifeCycle = category.getLifecycle();
				if(null!=lifeCycle&&lifeCycle.equals(1)){
					CategoryCommand cmd = new CategoryCommand();
					cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, category);
					
					cmdList.add(cmd);

					cmdList = getChildrenCategoryListRecursion(category.getId(), cmdList);
				}
				
			}
		}

		return cmdList;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategoryByCode(java.lang.String)
	 */
	@Override
	public CategoryCommand findCategoryByCode(String code) {
		Category c = categoryDao.findCategoryByCode(code); 
		if(null!=c){
			CategoryCommand cmd = new CategoryCommand();
			cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, c);
			return cmd;
		}else{
			return null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findSubCategoryListByPid(java.lang.Long)
	 */
	@Override
	public List<CategoryCommand> findSubCategoryListByPid(Long parentCategoryId) {
		List<Category> categoryList = categoryDao.findCategoryListByParentId(parentCategoryId);
		List<CategoryCommand> cateCmdList = new ArrayList<CategoryCommand>();
		if(Validator.isNotNullOrEmpty(categoryList)){
			for(Category c :categoryList){
				if(null!=c.getLifecycle()&&c.getLifecycle().equals(1)){
					CategoryCommand cmd = new CategoryCommand();
					cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, c);
					cateCmdList.add(cmd);
				}
				
			}
		}
		return cateCmdList;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategorysByCid()
	 */
	@Override
	public List<CategoryCommand> findCategorysByCid(List<Long> ids) {
		List<Category> categoryList = categoryDao.findCategoryListByIds(ids);
		return convertCategoryList2CategoryCmdList(categoryList);
	}
	
	@Override
	public DataFromSolrCommand search(List<CategoryCommand> categoryCmdList , String searchKey,Map<Long,List<Long>> searchConditionMap,Long priceCdtItemId,String salePriceArea,SolrOrderSort[] orderSort,Integer pageSize,Integer curPage){
		// facet 来源： 动态查询条件propertyId（单值条件，区间条件（吊牌价和实际销售价除外）。） 、吊牌价和实际销售价、 分类对应的 propertyId 
		Set<String> facetFields = new HashSet<String>();

		// 用于展示搜索选项 信息的Map
		Map<Long,SearchConditionResultCommand> searchConditionResultMap = new HashMap<Long,SearchConditionResultCommand>();
		
		// PropertyCommand Map
		Map<Long,PropertyCommand> propertyCommandMap = new HashMap<Long,PropertyCommand>();
		
		// 价格区间对应的搜索选项信息
		SearchConditionCommand priceSearchCodition = new SearchConditionCommand();

		//传给solr的查询条件
		QueryConditionCommand qCmd = new QueryConditionCommand();
		
		//先将是否有颜色属性设置为false ,在下边的 getFacetsByCid 中有根据 颜色属性进行相应的判断
		qCmd.setIsSpread(false);

		// 按照关键字搜索商品
		qCmd.setKeyword(searchKey);

		List<Long> cids = new ArrayList<Long>();
		
		// 按照 分类 搜索商品
		if (Validator.isNotNullOrEmpty(categoryCmdList)) {

			// 按照 分类搜索商品 如果有多个 分类， categoryMap 传入多个 cid 和 cateName 即可 。目前仅支持单个
			Map<String, String> categoryMap = new HashMap<String, String>();

			for(CategoryCommand curCateCmd : categoryCmdList){
				if (null != curCateCmd) {// 根据 产品分类 查询商品
					String cateName = curCateCmd.getName();
					if (null != cateName) {
						categoryMap.put(curCateCmd.getId().toString(), cateName);
					}
				}
			}
			
			qCmd.setCategory_name(categoryMap);
			
		}
		
		getFacetsByCids(cids,facetFields,searchConditionResultMap,priceSearchCodition, qCmd, propertyCommandMap);

		// 将 用户的查询条件作为参数传入solr
		if ((null!=priceCdtItemId&&priceCdtItemId>0)||(salePriceArea!=null)) {
			Map<Long, List<Long>> dynamicMap = searchConditionMap;
				
			fillPriceCondition(qCmd,priceCdtItemId , salePriceArea);

			qCmd.setDynamicConditionMap(dynamicMap);

			for (Long key : dynamicMap.keySet()) { // 动态查询条件的 key 作为facetFields
				
				facetFields.add(SkuItemParam.dynamicCondition+key.toString());
			}
			
			// 价格选项的facet sale_price:[XX TO XX]
			if(null!=qCmd.getSale_price()){
				StringBuilder sb = new StringBuilder();
				sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(qCmd.getSale_price());
				facetFields.add(sb.toString());
			}

		}
		
		DataFromSolr data = findItemList(pageSize, qCmd,
				getFacetFields(facetFields), orderSort, null,
				curPage);
		
		Long numsFound = Long.parseLong(data.getNumber().toString());
		data.getItems().setCurrentPage(curPage);
		Integer totalPage = 1;
		if(numsFound%pageSize==0){
			totalPage = (int) (numsFound/pageSize);
		}else{
			totalPage = (int) (numsFound/pageSize + 1);
		}
		data.getItems().setTotalPages(totalPage);
		data.getItems().setItems(data.getItems().getCurrentPageItem());
		
		//根据返回过来的值，组合数据 反查Id  
		DataFromSolrCommand dataCmd = convertFacetCodeToName(data, searchConditionResultMap,facetFields,numsFound,priceSearchCodition );
		dataCmd.setNumber(numsFound);
		
		PaginationForSolr<ItemListResultCommand> itemPageToJson = CommandConvert.convert(data.getItems());
		dataCmd.setItems(itemPageToJson);
		return dataCmd;
	}
	
	private String[] getFacetFields(Set<String> facetFields) {
		String[] facets =  facetFields.toArray(new String[facetFields.size()]);
		
		return facets;
	}
	
	/**将 facetCode 转化为 搜索选项的名称
	 * @param data solr返回的搜索结果
	 * @param propertyMap propertyMap
	 * @param facetFields facet 列
	 * @param numsFound 
	 * @param priceSearchCodition
	 * @return
	 */
	private DataFromSolrCommand convertFacetCodeToName(DataFromSolr data,Map<Long,SearchConditionResultCommand> searchConditionResultMap,Set<String> facetFields ,Long numsFound,SearchConditionCommand priceSearchCodition ) {
		
		DataFromSolrCommand dataFromSolrCommand = new DataFromSolrCommand(); // 搜索结果的cmd类
		
		List<SearchConditionResultCommand> facetResultList = new ArrayList<SearchConditionResultCommand>();
		
		List<SearchConditionResultCommand> salePriceList  = new ArrayList<SearchConditionResultCommand>(); 
		
		Map<String, Map<String, Long>> facetMap = data.getFacetMap(); //单值的 facetResultMap
		
		Map<String, Integer> areaFacetMap = data.getFacetQueryMap(); // 区间的 facetResultMap
		
		SearchConditionResultCommand salePriceResultCmd = null; // 销售价条件选项的结果
		List<SearchItemResultCommand> salePriceItemList = new ArrayList<SearchItemResultCommand>();
		
		List<SearchConditionItemCommand> itemList = null;
		
		if(null!=priceSearchCodition){
			itemList = priceSearchCodition.getItemList();
			salePriceResultCmd=new SearchConditionResultCommand();
			
			//初始化  销售价 条件选项结果
			if(itemList!=null){
				for(SearchConditionItemCommand itemCmd : itemList){

					Long numFound = 0L;
					
					SearchItemResultCommand resultCmd = new SearchItemResultCommand();
					
					resultCmd.setItemKey(itemCmd.getId());
					
					resultCmd.setNumberFound(numFound);
					resultCmd.setSort(itemCmd.getSort());
					resultCmd.setItemName(itemCmd.getName());
					
					salePriceItemList.add(resultCmd);
				}
			}
			
			
			salePriceResultCmd.setItems(salePriceItemList);
		}
		
		for(String facetFiled :facetFields){// 遍历传入solr的 facet列表, 丰富返回的 搜索选项的 内容，从Id 转化为Name
			Long propertyId = null;
			try{ // try catch 捕捉的是 单次循环中的异常
				if(facetFiled.contains(SkuItemParam.dynamicCondition)){ // 动态属性
					propertyId = Long.parseLong(facetFiled.substring(SkuItemParam.dynamicCondition.length(),facetFiled.length()));
					
					SearchConditionResultCommand scrCmd = searchConditionResultMap.get(propertyId);
					
					SearchConditionResultCommand crCmd = new SearchConditionResultCommand();
					
					crCmd.setsId(scrCmd.getsId());// 前台页面 的 sid  为propertyId
					crCmd.setSort(scrCmd.getSort());
					crCmd.setName(scrCmd.getName());
					
					Map<String, Long> valueMap = facetMap.get(facetFiled);
					
					List<SearchItemResultCommand> itemResultList = new ArrayList<SearchItemResultCommand>();
					
					for(SearchItemResultCommand sirCmd: scrCmd.getItems()){
						Long valueId = sirCmd.getItemKey();
						Long numFound =null;
						if(null!=valueMap){
							numFound = valueMap.get(valueId.toString());
						}
						
						if(numFound==null){
							numFound = 0L;
						}
						SearchItemResultCommand resultCmd = new SearchItemResultCommand();
						resultCmd.setItemKey(valueId);
						resultCmd.setItemName(sirCmd.getItemName());
						resultCmd.setNumberFound(numFound);
						resultCmd.setSort(sirCmd.getSort());
						
						itemResultList.add(resultCmd);
					}
					
					crCmd.setItems(itemResultList);
					facetResultList.add(crCmd);
				}else if(facetFiled.contains(SkuItemParam.sale_price)){//价格区间

					if(null!=priceSearchCodition){
						salePriceResultCmd.setsId(priceSearchCodition.getPropertyId());//木有property 怎么办？  价格选项单独放出来
						salePriceResultCmd.setSort(priceSearchCodition.getSort());
						salePriceResultCmd.setName(priceSearchCodition.getName());
						
						// 拿到本次 facetFiled 的值
						Integer value = areaFacetMap.get(facetFiled);
						
						if(itemList!=null){
							for(SearchConditionItemCommand itemCmd : itemList){
								
								Integer start = itemCmd.getAreaMin();
								Integer end = itemCmd.getAreaMax();
								String areaStr = getSalePriceArea(start,end);
								
								Long numFound = 0L;
								if(areaStr.equals(facetFiled)){// 找到 conditionItemList 中 和 该 facetFiled 对应的项。  根据 conditionItem 的id 和 salePriceItemList 中的item Id 进行匹配
									numFound = Long.parseLong(value.toString());
									
									for(SearchItemResultCommand sirCmd: salePriceItemList){
										if(sirCmd.getItemKey().equals(itemCmd.getId())){
											sirCmd.setNumberFound(numFound);
										}
									}
								}
								
							}
						}
						
					}
					
				}
				
				
			}catch(Exception e){
				log.error("parse facetFiled wrong, facetFiled is "+facetFiled);
				log.error(e.getMessage());
				
				//抛出搜索结果异常
//				throw new BusinessException(ErrorCodes.RESULT_ERROR);
			}
			
		}
		if(null!=salePriceResultCmd){
			salePriceList.add(salePriceResultCmd);
		}
		
		// 填充 item 中的小图 中图 img url地址   现在不用了 
//		List<ItemSolrCommand> itemsFromSolr = data.getItems().getItems();
//		
//		if(itemsFromSolr!=null){
//			for(ItemSolrCommand isCmd :itemsFromSolr){
//				String picUrl ="";
//				Integer index = 0;
//				Integer pos = null;
//				List<ItemImageCommand> imgList = isCmd.getImageList();
//				if(null!=imgList){
//					
//					// 排序 按照 sort_no 从小到大排序
//					Collections.sort(imgList);
//					
//					for(int i=0;i<imgList.size();i++){
//						ItemImageCommand iiCmd = imgList.get(i);
//						iiCmd=fillImgWithPicUrl(iiCmd);
//						
//						if(i==0){
//							pos= iiCmd.getPosition();
//						}else{
//							if(pos.compareTo(iiCmd.getPosition())>0){
//								index = i;
//							}
//						}
//					}
//					
//					if(imgList.size()>0){
//						picUrl=imgList.get(index).getMiddlePicUrl();
//					}
////					else{
////						picUrl = defaultNonItemImgUrl;
////					}
//					
//					
//					isCmd.setImgUrl(picUrl);
//					isCmd.setRankavg(getRankAvg(isCmd.getRankavg()));
//				}
//			}
//		}
//		

		dataFromSolrCommand.setCategoryMap(data.getCategoryMap());
		dataFromSolrCommand.setItems(CommandConvert.convert(data.getItems()));
		dataFromSolrCommand.setFacetMap(facetResultList);
		dataFromSolrCommand.setSalePriceMap(salePriceList);
		dataFromSolrCommand.setFacetQueryMap(data.getFacetQueryMap());
		

		return dataFromSolrCommand;
	}
	
	private void fillPriceCondition(QueryConditionCommand qCmd,Long priceCdtItemId,String salePriceArea){
		
		//设置价格搜索选项
		if(priceCdtItemId!=null&&priceCdtItemId>0){// 如果有价格搜索的选项，就以价格选项的为准
			SearchConditionItemCommand salePriceItemCmd = searchConditionManager.findItemById(priceCdtItemId);
			
			if(null!= salePriceItemCmd){
				Integer min = salePriceItemCmd.getAreaMin();
				Integer max = salePriceItemCmd.getAreaMax();
				if(null!=min&&null!=max&&min<=max){
					qCmd.setSale_price(FilterUtil.paramConverToArea(min.toString(), max.toString()))  ;
				}
			}
		}else{// 没有价格选项，就用用户输入的值
			qCmd.setSale_price(salePriceArea)  ;
		}

	}
	
//	/**
//	 * 解析用户输入的 搜索条件 输入的数值key 已经是 propertyId 和 propertyValueId(或者是searchConditionItemId) 将用户选中的搜索选项化 转为
//	 * 动态搜索选项map 1e2o3 a 3e4 key 为 1和3 value对应为2 3 ,4
//	 * 
//	 * condition中 不包含 价格搜索条件
//	 * 价格条件是在 priceCdtItemId 中的
//	 * 
//	 * @param condition
//	 *            搜索条件
//	 * @return Map<Long, List<Long>> key propertyId, value
//	 *         为用户选中的该条件propertyId的对应的 propertyValueId(或者是searchConditionItemId)
//	 */
//	private Map<Long, List<Long>> parseDynamicCondition(Map<Long, List<Long>> searchConditionMap,SearchConditionCommand priceSearchCodition,QueryConditionCommand qCmd,Long priceCdtItemId,String salePriceArea,Map<Long,PropertyCommand> propertyCommandMap) {
//		Map<Long, List<Long>> resultMap = new HashMap<Long, List<Long>>();
//		
//		//设置价格搜索选项
//		if(priceCdtItemId!=null&&priceCdtItemId>0){// 如果有价格搜索的选项，就以价格选项的为准
//			SearchConditionItemCommand salePriceItemCmd = searchConditionManager.findItemById(priceCdtItemId);
//			
//			if(null!= salePriceItemCmd){
//				Integer min = salePriceItemCmd.getAreaMin();
//				Integer max = salePriceItemCmd.getAreaMax();
//				if(null!=min&&null!=max&&min<=max){
//					qCmd.setSale_price(FilterUtil.paramConverToArea(min.toString(), max.toString()))  ;
//				}
//			}
//		}else{// 没有价格选项，就用用户输入的值
//			qCmd.setSale_price(salePriceArea)  ;
//		}
//
//		return resultMap;
//	}
	
	private String getSalePriceArea(Integer min,Integer max){
		String result = null;

		if(null!=min&&null!=max&&min<=max){
			String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
			StringBuilder sb = new StringBuilder();
			sb.append(SkuItemParam.sale_price).append(COLON_CONNECTOR).append(areaStr);
			
			result = sb.toString();
		}
		
		return result;
	}
	
	private List<CategoryCommand> convertCategoryList2CategoryCmdList(List<Category> categoryList){
		List<CategoryCommand> cateCmdList = new ArrayList<CategoryCommand>();
		if(Validator.isNotNullOrEmpty(categoryList)){
			for(Category c :categoryList){
				CategoryCommand cmd = new CategoryCommand();
				cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, c);
				cateCmdList.add(cmd);
			}
		}
		
		return cateCmdList;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategoryByCodes(java.util.Collection)
	 */
	@Override
	public List<CategoryCommand> findCategoryByCodes(Collection<String> codes) {
		boolean i18n = LangProperty.getI18nOnOff();
		List<Category> categoryList = null;
		if(i18n){
			categoryList = categoryDao.findCategoryListByCodesI18n(codes, LangUtil.getCurrentLang());
		}else{
			categoryList = categoryDao.findCategoryListByCodes(codes);
		}
		return convertCategoryList2CategoryCmdList(categoryList);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ItemListManager#findCategoryByParentCode(java.lang.String)
	 */
	@Override
	public List<CategoryCommand> findCategoryByParentCode(String parentCode) {
		boolean i18n = LangProperty.getI18nOnOff();
		List<Category> categoryList = null;
		if(i18n){
			categoryList = categoryDao.findSubCategoryListByParentCodeI18n(parentCode,LangUtil.getCurrentLang());
		}else{
			 categoryList = categoryDao.findSubCategoryListByParentCode(parentCode);
		}
		return convertCategoryList2CategoryCmdList(categoryList);
	}

	@Override
	public List<PropertyValueCommand> findPropertyValueListByPropertyName(
			String propertyName) {
		List<PropertyValue> valueList =  propertyValueDao.findPropertyValueListByPropertyName(propertyName);
		List<PropertyValueCommand> cmdList = new ArrayList<PropertyValueCommand>();
		if(null!=valueList){
			for(PropertyValue pv:valueList){
				PropertyValueCommand cmd = new PropertyValueCommand();
				cmd = (PropertyValueCommand)ConvertUtils.convertFromTarget(cmd, pv);
				cmdList.add(cmd);
			}
		}
		
		return cmdList;
	}

	@Override
	public Map<Long, List<PromotionCommand>> getPromotionsForItemList(List<Long> itemIds, UserDetails userDetails) {
		Map<Long, List<PromotionCommand>> itemPromotionMap = sdkPromotionGuideManager.getPromotionsForItemList(itemIds, userDetails);
		return itemPromotionMap;
	}
	/**
	 * 根据ItemId列表，获取有促销活动的活动列表和单件优惠金额。典型应用场景列表页。
	 * Map<itemId, Map<"disCountAmount"/"promotionList", disCountAmount/促销活动列表 >>
	 * 备注：只是估算并不准确，没走引擎，优先级折上折等逻辑，只能用作导购的时候提示
	 */
	@Override
	public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemList(List<Long> itemIds, UserDetails userDetails) {
		if(null == itemIds || itemIds.size() <= 0){
			return null;
		}
		return sdkPromotionGuideManager.getPromotionsAndDisCountForItemList(itemIds, userDetails);
	}
	/**
	 * 根据订单行（t_so_orderpromotion）上的Promotion List，从DB中取促销活动信息（可能过期不在内存中），
	 * 计算优惠设置，单品优惠金额。典型应用场景订单详情页。
	 * 备注：只是估算并不准确，没走引擎，优先级折上折等逻辑，只能用作导购的时候提示
	 */
	@Override
	public Map<Long, Map<String, Object>> getPromotionsAndDisCountForItemListByOrderId(Long orderId){
		return sdkPromotionGuideManager.getPromotionsAndDisCountForItemListByOrderId(orderId);
	}
}
