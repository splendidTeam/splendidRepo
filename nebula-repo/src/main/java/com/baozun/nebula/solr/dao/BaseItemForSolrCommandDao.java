package com.baozun.nebula.solr.dao;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.BaseItemForSolrCommand;

public interface BaseItemForSolrCommandDao extends GenericEntityDao<BaseItemForSolrCommand, Long> {

	@NativeQuery(model = BaseItemForSolrCommand.class)
	List<BaseItemForSolrCommand> findItemCommandByItemId(@QueryParam("type") Integer[] types, @QueryParam("itemIds") List<Long> itemId);
	
	@NativeQuery(model = BaseItemForSolrCommand.class)
	List<BaseItemForSolrCommand> findItemCommand(@QueryParam("type") Integer[] types);
	
}
