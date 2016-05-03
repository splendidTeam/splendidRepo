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
package com.baozun.nebula.search;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.baozun.nebula.search.command.MetaDataCommand;

/**
 * facet结果显示使用的元数据
 * 
 * @author D.C
 */
public class FacetFilterMetaData implements Serializable{

	private static final long			serialVersionUID			= 1337274838133771487L;

	/** 分类的元数据，key是分类的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	categoryMetaMap				= new LinkedHashMap<Long, MetaDataCommand>();

	/** 属性的元数据，key是属性的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	propertyMetaMap				= new LinkedHashMap<Long, MetaDataCommand>();

	/** 属性值的元数据，key是属性值的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	propertyValueMetaMap		= new LinkedHashMap<Long, MetaDataCommand>();

	/** 导航的元数据，key是导航的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	navigationMetaMap			= new LinkedHashMap<Long, MetaDataCommand>();

	/** 搜索条件的元数据，key是搜索条件的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	searchConditionMetaMap		= new LinkedHashMap<Long, MetaDataCommand>();

	/** 搜索条件的选项 ，key是搜索条件选项的id，value是具体的数据 */
	private Map<Long, MetaDataCommand>	searchConditionItemMetaMap	= new LinkedHashMap<Long, MetaDataCommand>();

	/**
	 * get categoryMetaMap
	 * 
	 * @return categoryMetaMap
	 */
	public Map<Long, MetaDataCommand> getCategoryMetaMap(){
		return categoryMetaMap;
	}

	/**
	 * set categoryMetaMap
	 * 
	 * @param categoryMetaMap
	 */
	public void setCategoryMetaMap(Map<Long, MetaDataCommand> categoryMetaMap){
		this.categoryMetaMap = categoryMetaMap;
	}

	/**
	 * get propertyMetaMap
	 * 
	 * @return propertyMetaMap
	 */
	public Map<Long, MetaDataCommand> getPropertyMetaMap(){
		return propertyMetaMap;
	}

	/**
	 * set propertyMetaMap
	 * 
	 * @param propertyMetaMap
	 */
	public void setPropertyMetaMap(Map<Long, MetaDataCommand> propertyMetaMap){
		this.propertyMetaMap = propertyMetaMap;
	}

	/**
	 * get propertyValueMetaMap
	 * 
	 * @return propertyValueMetaMap
	 */
	public Map<Long, MetaDataCommand> getPropertyValueMetaMap(){
		return propertyValueMetaMap;
	}

	/**
	 * set propertyValueMetaMap
	 * 
	 * @param propertyValueMetaMap
	 */
	public void setPropertyValueMetaMap(Map<Long, MetaDataCommand> propertyValueMetaMap){
		this.propertyValueMetaMap = propertyValueMetaMap;
	}

	/**
	 * get navigationMetaMap
	 * 
	 * @return navigationMetaMap
	 */
	public Map<Long, MetaDataCommand> getNavigationMetaMap(){
		return navigationMetaMap;
	}

	/**
	 * set navigationMetaMap
	 * 
	 * @param navigationMetaMap
	 */
	public void setNavigationMetaMap(Map<Long, MetaDataCommand> navigationMetaMap){
		this.navigationMetaMap = navigationMetaMap;
	}

	/**
	 * get searchConditionMetaMap
	 * 
	 * @return searchConditionMetaMap
	 */
	public Map<Long, MetaDataCommand> getSearchConditionMetaMap(){
		return searchConditionMetaMap;
	}

	/**
	 * set searchConditionMetaMap
	 * 
	 * @param searchConditionMetaMap
	 */
	public void setSearchConditionMetaMap(Map<Long, MetaDataCommand> searchConditionMetaMap){
		this.searchConditionMetaMap = searchConditionMetaMap;
	}

	/**
	 * get searchConditionItemMetaMap
	 * 
	 * @return searchConditionItemMetaMap
	 */
	public Map<Long, MetaDataCommand> getSearchConditionItemMetaMap(){
		return searchConditionItemMetaMap;
	}

	/**
	 * set searchConditionItemMetaMap
	 * 
	 * @param searchConditionItemMetaMap
	 */
	public void setSearchConditionItemMetaMap(Map<Long, MetaDataCommand> searchConditionItemMetaMap){
		this.searchConditionItemMetaMap = searchConditionItemMetaMap;
	}

}
