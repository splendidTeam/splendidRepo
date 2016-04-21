package com.baozun.nebula.search.convert;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.search.command.SearchCommand;

/**
 * solrQuery转换器
 * @author long.xia
 *
 */
public interface SolrQueryConvert {
	
	
	/**
	 * 将前台传输的search参数，转成solrQuery
	 * @param searchCommand
	 * 			FacetParameter 生成 fq。
	 * @return
	 */
	SolrQuery convert(SearchCommand searchCommand);
	

	
}
