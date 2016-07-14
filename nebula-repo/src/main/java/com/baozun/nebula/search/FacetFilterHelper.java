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

import java.util.List;
import java.util.Map;

import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.search.command.MetaDataCommand;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrCommand;

/**
 * 实例设置为single， metaData数据需要定期更新
 * 
 * @author D.C
 */
public interface FacetFilterHelper{

	/**
	 * 加载元数据，从数据库中获取所有有效的分类、属性、属性值、导航的id和国际化的名称(有24H的缓存)
	 * 
	 * @return FacetFilterMetaData
	 * @param storeId
	 * @author 冯明雷
	 * @time 2016年4月28日下午2:35:45
	 */
	FacetFilterMetaData loadFacetFilterMetaData();
	
	/**
	 * 加载导航的元数据，从loadFacetFilterMetaData 方法中分离出来，方便其他地方调用
	 * @return
	 */
	Map<Long, MetaDataCommand> loadNavigationMetaData();
	
	/**
	 * 加载分类的元数据，从loadFacetFilterMetaData 方法中分离出来，方便其他地方调用
	 * @return
	 */
	Map<Long, MetaDataCommand> loadCategoryMetaData();
	
	/**
	 * 加载属性的元数据，从loadFacetFilterMetaData 方法中分离出来，方便其他地方调用
	 * @return
	 */
	Map<Long, MetaDataCommand> loadPropertyMetaData();
	
	/**
	 * 加载属性值的源数据，从loadFacetFilterMetaData 方法中分离出来，方便其他地方调用
	 * @return
	 */
	Map<Long, MetaDataCommand> loadPropertyValueMetaData();
	
	/**
	 * 加载筛选项的源数据，从loadFacetFilterMetaData 方法中分离出来，方便其他地方调用
	 * @return
	 */
	Map<Long, SearchConditionCommand> loadSearchConditionMetaData();

	/**
	 * 
	 * @return List<FacetGroup>
	 * @param pagination solr查询出来的数据
	 * @param searchCommand	搜索条件，用来设置facet的selected顺序宁
	 * @author 冯明雷
	 * @time 2016年5月3日下午3:28:21
	 */
	List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrCommand> pagination,SearchCommand searchCommand);
}
