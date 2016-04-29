package com.baozun.nebula.search.convert;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.solr.factory.NebulaSolrQueryFactory;
import com.baozun.nebula.solr.factory.SortTypeEnum;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.feilong.core.Validator;

/**
 * 不需要分组的solrquery转换器
 * @author long.xia
 *
 */
public class SimpleSolrQueryConvert implements SolrQueryConvert{


	@Override
	public SolrQuery convert(SearchCommand searchCommand) {
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery=NebulaSolrQueryFactory.createSolrQuery(searchCommand,solrQuery);
		
		//设置排序
		SortTypeEnum sortTypeEnum=SortTypeEnum.getInstance(searchCommand.getSortStr());
		if(Validator.isNotNullOrEmpty(sortTypeEnum)){
			SolrOrderSort[] order=sortTypeEnum.getSolrOrderSort();
			NebulaSolrQueryFactory.setSort(solrQuery, order);
		}
		return solrQuery;
	}

}
