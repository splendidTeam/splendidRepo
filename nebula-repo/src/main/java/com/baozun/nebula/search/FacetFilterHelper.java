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
import com.baozun.nebula.solr.command.ItemForSolrCommand;

/**
 * 实例设置为single， metaData数据需要定期更新
 * @author D.C
 *
 */
public interface FacetFilterHelper{
	/**
	 * 加载元数据，此处需要考虑缓存
	 * @return
	 */
	public abstract FacetFilterMetaData loadFacetFilterMetaData(Long storeId);
	
	/**
	 * 将facet查询结果封装成页面显示的数据，这个结果是有顺序的，
	 * 和pts中设置t_pd_search_con 的顺序一致
	 * @param page
	 * @return
	 */
	public abstract  List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrCommand> pagination);
}
