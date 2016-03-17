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
package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.sdk.command.CategoryCommand;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.PropertyCommand;
import com.baozun.nebula.sdk.command.PropertyValueCommand;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.manager.SdkItemListManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.baozun.nebula.utils.Validator;

/**
 * 
 * @author Tianlong.Zhang
 *
 */
@Transactional
@Service("sdkItemListManager")
public class SdkItemListManagerImpl implements SdkItemListManager{
	
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
	
	private static final String SEARCH_URL = "/product/itemList.htm?cid=";
	
	private static final String ASSIGN_URL = "/category/";

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findItemList()
	 */
	@Override
	public  DataFromSolr findItemList(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, String groupField,Integer currentPage) {
		return itemSolrManager.queryItemForAll(rows, queryConditionCommand, facetFields, order, groupField,currentPage);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findSearchCoditionList(java.lang.Long)
	 */
	@Override
	public List<SearchConditionCommand> findSearchCoditionList(Long categoryId) {
		//TODO  根据categoryId 查询SearchCondition  然后根据SearchConditionId 查出来对应的searchItem ？？
		return null;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findCurmbList(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CurmbCommand> findCurmbList(Long categoryId) {
		// rootCategoryId 为0 ，但是这个记录并不在真正的存在。
		
		List<CurmbCommand> cmdList = new LinkedList<CurmbCommand>();

		Long pid = categoryId;
		do{
			Category category=categoryDao.findCategoryById(pid);
			if(null!=category){
				pid = category.getParentId();
				CategoryCommand cmd = new CategoryCommand();
				cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, category);
				CurmbCommand curmbCommand  = categoryCommand2CurmbCommand(cmd);
				curmbCommand = getUrl(curmbCommand);
				cmdList.add(0, curmbCommand);
			}
			
		}while(pid!=null&&pid>0);
		
		return cmdList;
	}
	@Transactional(readOnly=true)
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
		String url = "";
		if(type == null){
			url = SEARCH_URL + crumb.getId();
		}else if(type == 1){
			url = crumb.getUrl();
		}else if(type == 2){
			url = ASSIGN_URL + crumb.getId();
		}
		crumb.setUrl(url);
		return crumb;
	}
	
	private CurmbCommand categoryCommand2CurmbCommand(CategoryCommand cCmd){
		CurmbCommand c = new CurmbCommand();
		c = (CurmbCommand) ConvertUtils.convertFromTarget(c, cCmd);
		return c;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findCategoryList(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
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
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findKeyAssociateList(java.lang.String)
	 */
	@Override
	public SuggestCommand findKeyAssociateList(String key) {
		return itemSolrManager.keywordSpellSpeculation(key);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#contrastItem(java.lang.Long, java.util.List)
	 */
	@Override
	public void contrastItem(Long categoryId, List<Long> itemIds) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly=true)
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
	
//	private SolrGroup getTestSolrGroup(){
//		SolrGroup group = new SolrGroup();
//		
//		Pagination<List<ProdcutItemCommand>> beans = new Pagination<List<ProdcutItemCommand>>();
//		List<Long> itemIds = new ArrayList<Long>();
//		itemIds.add(1931L);
//		itemIds.add(1943L);
//		itemIds.add(1962L);
//		itemIds.add(1969L);
//		itemIds.add(2700L);
//		itemIds.add(2788L);
//		itemIds.add(2946L);
//		itemIds.add(2986L);
//		
//		List<Item> itemList=itemDao.findItemListByIds(itemIds);
//		List<ProdcutItemCommand> cmdList = new ArrayList<ProdcutItemCommand>(itemList.size());
//		for(Item item:itemList){
//			ProdcutItemCommand itmCmd = new ProdcutItemCommand();
//			itmCmd = (ProdcutItemCommand) ConvertUtils.convertModelToApi(itmCmd, item);
//			cmdList.add(itmCmd);
//		}
//		
//		List<List<ProdcutItemCommand>> list = new ArrayList<List<ProdcutItemCommand>>();
//		
//		beans.setCount(itemList.size());
//		beans.setCurrentPage(1);
//		
//		list.add(cmdList);
//		beans.setItems(list);
//		beans.setSize(5);
//		beans.setStart(0);
//		beans.setTotalPages(2);
//		
//		group.setNumFound(100L);
////		group.setBeans(beans);
//		return group;
//	}
//	
	@Transactional(readOnly=true)
	public CategoryCommand findCategoryById(Long id){
		Category category = categoryDao.getByPrimaryKey(id);
		CategoryCommand cmd = new CategoryCommand();
		cmd = (CategoryCommand) ConvertUtils.convertFromTarget(cmd, category);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findPropertyById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public PropertyCommand findPropertyById(Long id) {
		Property p = propertyDao.findPropertyById(id);
		PropertyCommand cmd = new PropertyCommand();
		cmd = (PropertyCommand) ConvertUtils.convertFromTarget(cmd, p);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findPropertyValueListById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
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
	 * @see com.baozun.nebula.sdk.manager.SdkItemListManager#findPropertyById(java.util.List)
	 */
	@Override
	@Transactional(readOnly=true)
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
	}
}
