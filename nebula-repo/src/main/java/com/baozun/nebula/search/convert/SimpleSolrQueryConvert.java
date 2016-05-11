package com.baozun.nebula.search.convert;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.solr.factory.NebulaSolrQueryFactory;
import com.baozun.nebula.solr.factory.SortTypeEnum;
import com.baozun.nebula.solr.manager.SortTypeManager;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.feilong.core.Validator;

/**
 * 不需要分组的solrquery转换器
 * 
 * @author long.xia
 */
public class SimpleSolrQueryConvert implements SolrQueryConvert{

	@Autowired(required = false)
	private SortTypeManager sortTypeManager;

	@Override
	public SolrQuery convert(SearchCommand searchCommand){
		SolrQuery solrQuery = new SolrQuery();

		solrQuery = NebulaSolrQueryFactory.createSolrQuery(searchCommand, solrQuery);

		// 设置排序
		SolrOrderSort[] orders = null;
		String sortStr = searchCommand.getSortStr();
		// 如果传过来的排序字段不为空执行下边代码，如果为空会根据boost去排序
		if (Validator.isNotNullOrEmpty(sortStr)) {
			if (sortTypeManager == null) {
				SortTypeEnum sortTypeEnum = SortTypeEnum.getInstance(sortStr);
				if (Validator.isNotNullOrEmpty(sortTypeEnum)) {
					orders = sortTypeEnum.getSolrOrderSort();
				}
			}else{
				orders = sortTypeManager.getSolrOrder(sortStr);
			}
			NebulaSolrQueryFactory.setSort(solrQuery, orders);
		}

		return solrQuery;
	}

}
