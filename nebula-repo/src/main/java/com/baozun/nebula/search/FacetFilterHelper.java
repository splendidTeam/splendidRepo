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

import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;

/**
 * 实例设置为single， metaData数据需要定期更新
 * @author D.C
 *
 */
public interface FacetFilterHelper{
	
	/**
	 * 加载元数据，从数据库中获取所有有效的分类、属性、属性值、导航的id和国际化的名称(有24H的缓存)
	 * @return FacetFilterMetaData
	 * @param storeId
	 * @author 冯明雷
	 * @time 2016年4月28日下午2:35:45
	 */
	FacetFilterMetaData loadFacetFilterMetaData();
	
	/**
	 * 将facet查询结果封装成页面显示的数据，这个结果是有顺序的，
	 * 和pts中设置t_pd_search_con 的顺序一致
	 * @param page
	 * @return
	 */
	List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrI18nCommand> pagination);
}
