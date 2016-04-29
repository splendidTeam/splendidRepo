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

/**
 * facet结果显示使用的元数据
 * 
 * @author D.C
 */
public class FacetFilterMetaData implements Serializable{

	private static final long	serialVersionUID		= 1337274838133771487L;

	/** 分类的元数据，key是分类的id，value是分类的名称 */
	private Map<String, Object>	categoryMetaMap			= new LinkedHashMap<String, Object>();

	/** 属性的元数据，key是属性的id，value是属性的名称 */
	private Map<String, Object>	propertyMetaMap			= new LinkedHashMap<String, Object>();

	/** 属性值的元数据，key是属性值的id，value是属性值的名称 */
	private Map<String, Object>	propertyValueMetaMap	= new LinkedHashMap<String, Object>();

	/** 导航的元数据，key是导航的id，value是导航的名称 */
	private Map<String, Object>	navigationMetaMap		= new LinkedHashMap<String, Object>();

	/** 搜索条件的元数据，key是搜索条件的id，value是搜索条件的名称 */
	private Map<String, Object>	searchConditionMetaMap	= new LinkedHashMap<String, Object>();

	/**
	 * get categoryMetaMap
	 * 
	 * @return categoryMetaMap
	 */
	public Map<String, Object> getCategoryMetaMap(){
		return categoryMetaMap;
	}

	/**
	 * set categoryMetaMap
	 * 
	 * @param categoryMetaMap
	 */
	public void setCategoryMetaMap(Map<String, Object> categoryMetaMap){
		this.categoryMetaMap = categoryMetaMap;
	}

	/**
	 * get propertyMetaMap
	 * 
	 * @return propertyMetaMap
	 */
	public Map<String, Object> getPropertyMetaMap(){
		return propertyMetaMap;
	}

	/**
	 * set propertyMetaMap
	 * 
	 * @param propertyMetaMap
	 */
	public void setPropertyMetaMap(Map<String, Object> propertyMetaMap){
		this.propertyMetaMap = propertyMetaMap;
	}

	/**
	 * get propertyValueMetaMap
	 * 
	 * @return propertyValueMetaMap
	 */
	public Map<String, Object> getPropertyValueMetaMap(){
		return propertyValueMetaMap;
	}

	/**
	 * set propertyValueMetaMap
	 * 
	 * @param propertyValueMetaMap
	 */
	public void setPropertyValueMetaMap(Map<String, Object> propertyValueMetaMap){
		this.propertyValueMetaMap = propertyValueMetaMap;
	}

	/**
	 * get navigationMetaMap
	 * 
	 * @return navigationMetaMap
	 */
	public Map<String, Object> getNavigationMetaMap(){
		return navigationMetaMap;
	}

	/**
	 * set navigationMetaMap
	 * 
	 * @param navigationMetaMap
	 */
	public void setNavigationMetaMap(Map<String, Object> navigationMetaMap){
		this.navigationMetaMap = navigationMetaMap;
	}

	/**
	 * get searchConditionMetaMap
	 * 
	 * @return searchConditionMetaMap
	 */
	public Map<String, Object> getSearchConditionMetaMap(){
		return searchConditionMetaMap;
	}

	/**
	 * set searchConditionMetaMap
	 * 
	 * @param searchConditionMetaMap
	 */
	public void setSearchConditionMetaMap(Map<String, Object> searchConditionMetaMap){
		this.searchConditionMetaMap = searchConditionMetaMap;
	}

}
