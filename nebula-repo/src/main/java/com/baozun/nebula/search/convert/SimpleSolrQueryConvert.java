package com.baozun.nebula.search.convert;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.search.command.SearchCommand;

/**
 * 不需要分组的solrquery转换器
 * @author long.xia
 *
 */
public class SimpleSolrQueryConvert implements SolrQueryConvert{


	@Override
	public SolrQuery convert(SearchCommand searchCommand) {
		// TODO Auto-generated method stub
		return null;
	}

}
