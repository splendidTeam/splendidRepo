/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.breadcrumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.FilterNavigationCommand;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.exception.IllegalItemStateException.IllegalItemState;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.manager.navigation.NavigationHelperManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.manager.SdkItemCollectionManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.search.AutoCollection;
import com.baozun.nebula.search.FacetFilterHelper;
import com.baozun.nebula.search.ItemCollectionContext;
import com.baozun.nebula.search.command.MetaDataCommand;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * 面包屑
 * <h3>获取页面的面包(分为plp、pdp两种场景)</h3>
 * <p>
 * 		1. 导航navId不为空,
 * 			<li>itemId不为空时，可构造[导航树结构+商品名称]作为面包屑</li>
 * 			<li>itemId为空时，可构造[导航树结构]作为面包屑</li>
 * </p>
 * <p>
 * 		2. 导航navId为空时,
 * 			<li>itemId不为空时,</br>
 * 				<p style="padding-left:10px;">
 * 					a.获取所有的itemCollection集合,构造该itemId对应的ItemCollectionContext
 * 				</p>
 * 				<p style="padding-left:10px;">
 * 					b.遍历itemCollection,利用AutoCollection的apply方法获取最长、最早的itemCollection,
 * 				</p>
 * 				<p style="padding-left:10px;">
 * 					c.根据此itemCollection找出对应的Navication，可构造[导航树结构+商品名称]作为面包屑
 * 				</p>
 * 			</li>
 * 			<li>itemId为空时，抛异常</li>
 * </p>
 * @Description 
 * @author dongliang ma
 * @date 2016年5月16日 下午5:29:12 
 * @version   
 */
@Service
public class BreadcrumbManagerImpl implements BreadcrumbManager {
	
	private static final Logger	LOG									= LoggerFactory.getLogger(BreadcrumbManagerImpl.class);
	
	private static final String	HEADER_REFERER						="referer";
	
	private final static String	NAV_ITEMCOLLECTION_CACHEKEY			= "navItemCollectionCacheKey_";
	
	@Autowired
	private NavigationHelperManager									navigationHelperManager;
	
	@Autowired
	private SdkNavigationManager									sdkNavigationManager;

	@Autowired
	private CacheManager											cacheManager;
	
	@Autowired
	private SdkItemManager											sdkItemManager;
	
	@Autowired
	private SdkItemCollectionManager								sdkItemCollectionManager;
	
	@Autowired
	@Qualifier("facetFilterHelper")
	private FacetFilterHelper										facetFilterHelper;
	
	@Autowired
	private CategoryDao												categoryDao;
	
	
	/* 
	 * @see com.baozun.nebula.manager.breadcrumb.BreadcrumbManager#loadNavItemCollectionMap()
	 */
	@Override
	public Map<Long, ItemCollection> loadNavItemCollectionMap() {
		Map<Long, ItemCollection> resultMap = null;
		String lang = LangUtil.getCurrentLang();
		try {
			resultMap = cacheManager.getObject(NAV_ITEMCOLLECTION_CACHEKEY + lang);
			// 如果导航的元数据为空
			if (resultMap == null) {
				//查询所有ItemCollection
				List<ItemCollection> collections =sdkItemCollectionManager.findAll();
				if(Validator.isNullOrEmpty(collections)){
					return Collections.emptyMap();
				}
				Map<Long, ItemCollection> collectionsMap =new HashMap<Long, ItemCollection>();
				for (ItemCollection itemCollection : collections) {
					collectionsMap.put(itemCollection.getId(), itemCollection);
				}
				//查询所有导航
				List<Navigation> navigations = sdkNavigationManager.findNavigationList(Sort.parse("parent_id asc,sort asc"));
				if(Validator.isNullOrEmpty(navigations)){
					return Collections.emptyMap();
				}
				resultMap =new HashMap<Long, ItemCollection>();
				for (Navigation navigation : navigations) {
					if(Validator.isNotNullOrEmpty(collectionsMap.get(navigation.getCollectionId()))){
						resultMap.put(navigation.getId(),
								collectionsMap.get(navigation.getCollectionId()));
					}
				}
				cacheManager.setObject(NAV_ITEMCOLLECTION_CACHEKEY + lang, resultMap, TimeInterval.SECONDS_PER_DAY);
			}
		} catch (Exception e) {
			LOG.error("[LOAD_NAVIGATION_META] cacheManager error. time:{}", new Date());
		}
		
		return resultMap;
	};
	
	/* 
	 * @see com.baozun.nebula.manager.breadcrumb.BreadcrumbManager#createCurmbCommandsByNavId(java.lang.Long)
	 */
	@Override
	public List<CurmbCommand> createCurmbCommandsByNavId(Long navId) {
		//获取所有导航
		Map<Long, MetaDataCommand> navigationMetaMap =facetFilterHelper.loadNavigationMetaData();
		//获取相关导航树结构
		if(Validator.isNullOrEmpty(navigationMetaMap)){
			return Collections.emptyList();
		}
		List<CurmbCommand> results =new LinkedList<CurmbCommand>();
		Long tempId =navId;
		while(tempId != null && tempId >= 0){
			MetaDataCommand current =navigationMetaMap.get(tempId);
			if(Validator.isNullOrEmpty(current)){
				break;
			}
			results.add(0, convertFromNavigationMeta(current));
			tempId =current.getParentId();
		}
		return results;
	}

	/* 
	 * @see com.baozun.nebula.manager.breadcrumb.BreadcrumbManager#findCurmbCommands(java.lang.Long, java.lang.Long, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public List<CurmbCommand> findCurmbCommands(Long navId, Long itemId,
			HttpServletRequest request) throws IllegalItemStateException {
		List<CurmbCommand> results =null;
		//校验参数
		if(Validator.isNullOrEmpty(navId)&&Validator.isNullOrEmpty(itemId)){
			LOG.error("[BUILD_BREADCRUMB] param navigationId and itemId should not be all empty.");
			throw new IllegalItemStateException(IllegalItemState.INVALIDE_PARAM);
		}
		if(Validator.isNotNullOrEmpty(navId)){
			results =buildCurmbCommand(navId, itemId);
		}else{
			//PDP
			//获取当前导航id
			String refer =request.getHeader(HEADER_REFERER);
			if(LOG.isDebugEnabled()){
				LOG.debug("[BUILD_BREADCRUMB] referer:{}", refer);
			}
			refer = refer.endsWith("/") ? refer.substring(0, refer.length()-1) : refer;
			FilterNavigationCommand filterNavigation = navigationHelperManager.matchNavigationByUrl(refer, "");
			if(Validator.isNotNullOrEmpty(filterNavigation)&&
					Validator.isNotNullOrEmpty(filterNavigation.getNavId())){
				results =buildCurmbCommand(filterNavigation.getNavId(), itemId);
			}else{
				//从refer中获取不到导航id，应该是链接直接打开，此时需构造ItemCollectionContext，根据ItemCollectionContext
				//从所有的ItemCollection集合中利用AutoCollection找出符合条件的ItemCollection,有了ItemCollection就可以很快的定位到导航id
				//1.查询所有的ItemCollection
				Map<Long, ItemCollection> navItemCMap =loadNavItemCollectionMap();
				if(Validator.isNullOrEmpty(navItemCMap)){
					//如果为空，就不必要向下面进行了，找不到导航id的
					LOG.warn("[BUILD_BREADCRUMB] navigation hasn't bulid relations with itemCollection yet!");
					return Collections.emptyList();
				}
				//2.构造ItemCollectionContext
				ItemCollectionContext itemCollectionContext =constructItemCollectionContextByItemId(itemId);
				if(Validator.isNotNullOrEmpty(itemCollectionContext)){
					if(LOG.isDebugEnabled()){
						LOG.debug("[BUILD_BREADCRUMB] itemCollectionContext:{}", JsonUtil.format(itemCollectionContext));
					}
					//定位最佳导航id (取最大长度，长度相等时取最远时间)
					Long navigationId =null;
					int idx =0;
					int tempTreeSize =0;
					ItemCollection tempCollection =null;
					List<CurmbCommand> resultCurmbCommands =null;
					List<CurmbCommand> currentCurmbCommands =null;
					for (Map.Entry<Long, ItemCollection> entry : navItemCMap.entrySet()) {
						if(AutoCollection.apply(entry.getValue(), itemCollectionContext)){
							navigationId =entry.getKey();
							if(idx == 0){
								tempCollection=entry.getValue();
								resultCurmbCommands =buildCurmbCommand(navigationId, itemId);
								tempTreeSize =Validator.isNotNullOrEmpty(resultCurmbCommands)?
										resultCurmbCommands.size(): 0;
							}else{
								currentCurmbCommands =buildCurmbCommand(navigationId, itemId);
								int currentTreeSize =Validator.isNotNullOrEmpty(currentCurmbCommands)?
										currentCurmbCommands.size(): 0;
								if(tempTreeSize < currentTreeSize){
									//最大长度
									resultCurmbCommands =currentCurmbCommands;
								}else if(tempTreeSize == currentTreeSize){
									//距离现在最久远时间
									if(entry.getValue().getCreateTime().before(tempCollection.getCreateTime())){
										resultCurmbCommands =currentCurmbCommands;
									}
								}
							}
							idx++;
						}
					}
					results =resultCurmbCommands;
				}
			}
		}   
		return results;
	}

	/**
	 * 根据已知的navigationId、itemId
	 * @param navId
	 * @param itemId
	 * @throws IllegalItemStateException
	 */
	private List<CurmbCommand> buildCurmbCommand(Long navId, Long itemId)
			throws IllegalItemStateException {
		//根据navId找出导航树，即为面包屑
		List<CurmbCommand> results =createCurmbCommandsByNavId(navId);
		if(Validator.isNullOrEmpty(results)){
			results =new ArrayList<CurmbCommand>();
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("[BUILD_BREADCRUMB]get curmbs by navigations. curmbs:{}", JsonUtil.format(results));
		}
		if(Validator.isNotNullOrEmpty(itemId)){
			//PDP
			ItemBaseCommand baseCommand = sdkItemManager.findItemBaseInfo(itemId);
			if(Validator.isNotNullOrEmpty(baseCommand)){
				CurmbCommand childCommand =new CurmbCommand();
				childCommand.setName(baseCommand.getTitle());
				childCommand.setUrl("");
				//后添一个商品名称
				results.add(childCommand);
			}else{
				LOG.error("[BUILD_BREADCRUMB] item not exists.");
				throw new IllegalItemStateException(IllegalItemState.ITEM_NOT_EXISTS);
			}
		}
		//itemId为空时 将导航树作为面包屑(场景PLP)
		return results;
	}

	/**
	 * 将导航转换为面包屑 
	 * @param metaDataCommand
	 * @return
	 */
	private CurmbCommand convertFromNavigationMeta(MetaDataCommand metaDataCommand){
		CurmbCommand command =new CurmbCommand();
		ConvertUtils.convertTwoObject(command, metaDataCommand);
		return command;
	}

	/* 
	 * itemId
	 * categories
	 * tags
	 * properties
	 * @see com.baozun.nebula.manager.breadcrumb.BreadcrumbManager#constructItemCollectionContextBy(java.lang.Long)
	 */
	@Override
	public ItemCollectionContext constructItemCollectionContextByItemId(Long itemId) 
			throws IllegalItemStateException{
		ItemCollectionContext collectionContext =new ItemCollectionContext();
		ItemBaseCommand baseCommand = sdkItemManager.findItemBaseInfo(itemId);
		if(Validator.isNotNullOrEmpty(baseCommand)){
			collectionContext.setItemId(itemId);
			collectionContext.setCategories(getCategories(itemId));
			collectionContext.setTags(getTags(itemId));
			collectionContext.setProperties(getProperties(itemId));
		}else{
			LOG.error("[BUILD_BREADCRUMB] item not exists.");
			throw new IllegalItemStateException(IllegalItemState.ITEM_NOT_EXISTS);
		}
		
		return collectionContext;
	}

	/**
	 * CategoryId
	 * 构造ItemCollectionContext之获取商品分类
	 * @param itemId
	 */
	private List<String> getCategories(Long itemId) {
		List<String> results =new ArrayList<String>();
		ItemCategory itemCategory = sdkItemManager.findDefaultCateoryByItemId(itemId);
		List<Category> categories = new LinkedList<Category>();

		Long pid = itemCategory.getCategoryId();
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
				categories.add(0, category);
			}
		}while(pid!=null&&pid>0);
		//构造["1"]或者["1-10"]这样的数据
		if(Validator.isNotNullOrEmpty(categories)){
			if(categories.size() == 1){
				results.add(categories.get(0).getId().toString());
			}else{
				String res ="";
				int i =0;
				for (Category category : categories) {
					if(i == 0){
						res=category.getId().toString();
					}else
						res +="-"+category.getId().toString();
					i++;
				}
				results.add(res);
			}
		}
		return results;
	}
	
	/**
	 * 构造ItemCollectionContext之获取商品标签
	 * @param itemId
	 */
	private List<String> getTags(Long itemId) {
		//TODO
		return Collections.emptyList();
	}
	
	/**
	 * 构造ItemCollectionContext之获取商品属性
	 * propertyValueId
	 * @param itemId
	 */
	private List<String> getProperties(Long itemId) {
		//商品有多个属性，每一个属性下有多个值，这些值得id集合作为一个元素
		//例如["1","2","3"] ["10","11","13"],如果属性是自定义多选或者单行输入的情况?TODO
		List<String> results =new ArrayList<String>();
		List<ItemProperties> dbItemPropertiesList = sdkItemManager.findItemPropertiesByItemId(itemId);
		if(Validator.isNullOrEmpty(dbItemPropertiesList)){
			return results;
		}
		//构造Map<Long, List<ItemProperties>>，key为propertyId
		Map<Long, List<ItemProperties>> itemPropertiesMap =new HashMap<Long, List<ItemProperties>>();
		List<ItemProperties> itemPropertiesList =null;
		for (ItemProperties itemProperties : dbItemPropertiesList) {
			Long pId =itemProperties.getPropertyId();
			if(Validator.isNotNullOrEmpty(itemPropertiesMap.get(pId))){
				itemPropertiesList =itemPropertiesMap.get(pId);
			}else{
				itemPropertiesList =new ArrayList<ItemProperties>();
			}
			itemPropertiesList.add(itemProperties);
			itemPropertiesMap.put(itemProperties.getPropertyId(), itemPropertiesList);
		}
		//每一个属性下的itemProperties (用String类型为了以后可能会加自定义多选)
		List<String> pvIds =null;
		for(Map.Entry<Long, List<ItemProperties>> entry :itemPropertiesMap.entrySet()){
			pvIds =null;
			if(Validator.isNotNullOrEmpty(entry.getValue())){
				pvIds =new ArrayList<String>();
				for (ItemProperties itemProperties : entry.getValue()) {
					if(Validator.isNotNullOrEmpty(itemProperties.getPropertyValueId())){
						pvIds.add(itemProperties.getPropertyValueId().toString());
					}else{
						//自定义多选
						//TODO
					}
				}
				if(Validator.isNotNullOrEmpty(pvIds)){
					Collections.sort(pvIds);
					results.add(JSON.toJSONString(pvIds));
				}
			}
		}
		return results;
	}
	
	
}
