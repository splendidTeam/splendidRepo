package com.baozun.nebula.solr.manager;

import java.util.List;

import loxia.dao.Pagination;
import net.sf.json.JSON;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SolrGroupData;


public interface SolrManager extends BaseManager{
	
	 /**
	  * 通过queryString查询分页的ItemCommand
	  * @param queryString
	  * @param sortString
	  * @param startNum
	  * @param rowsNum
	  * @return
	  */
	 public Pagination<ItemSolrCommand>  findAllItemCommandFormSolrByField(String queryString,String sortString,Integer startNum,Integer rowsNum,Integer currentPage,Integer size);  
	 
	 
	 /**
	  * 通过SolrQuery查询分页的ItemCommand
	  * 不分组
	  * @param queryString
	  * @param sortString
	  * @param startNum
	  * @param rowsNum
	  * @return
	  */
	 public SolrGroupData findItemCommandFormSolrBySolrQueryWithOutGroup(SolrQuery solrQuery);  
	 
	 /**
	  * 通过SolrQuery查询分页的ItemCommand
	  * 分组
	  * @param queryString
	  * @param sortString
	  * @param startNum
	  * @param rowsNum
	  * @return
	  */
	 public SolrGroupData findItemCommandFormSolrBySolrQueryWithGroup(SolrQuery solrQuery);  
	 
	 /**
	  * 返回JSON格式的ItemForSolrCommand
	  * @param channelId
	  * @param queryConditionCommand
	  * @param startNum
	  * @param rowsNum
	  * @param currentPage
	  * @param size
	  * @return
	  */
	 public JSON itemForSolrCommandToJSON(QueryConditionCommand queryConditionCommand,Integer startNum,Integer rowsNum,Integer currentPage,Integer size);
	 
	 /**
	  * 返回XML格式的ItemForSolrCommand
	  * @param channelId
	  * @param queryConditionCommand
	  * @param startNum
	  * @param rowsNum
	  * @param currentPage
	  * @param size
	  * @return
	  */
	 public String itemForSolrCommandToXML(QueryConditionCommand queryConditionCommand,Integer startNum,Integer rowsNum,Integer currentPage,Integer size);

	 /**
	  * 后台人员使用的批量创建Solr索引方法
	  * @param itemCommandList
	  */
	 public Boolean batchUpdateItemCommandToSolr(List<ItemForSolrCommand> itemList);
	 
	 /**
	  * 后台人员使用的小批量修改Solr信息
	  * @param itemCommandList
	  */
	 public Boolean updateItemCommandToSolr(List<ItemForSolrCommand> itemList);
	 
	 Boolean updateItemCommandToSolrI18n(List<ItemForSolrI18nCommand> itemList);
	 
	 /**
	  * 后台人员使用的删除索引方法
	  * @param ids
	  */
	 public Boolean deledteItemCommandToSolr(List<String> ids);
	 
	 
	 /**
	  * 后台人员使用的晴空所有Solr索引的方法
	  */
	 public Boolean cleanAll();
	 
	 /**
	  * 提供原始的查询方法
	  */
	 public QueryResponse query(SolrQuery solrQuery);

}
