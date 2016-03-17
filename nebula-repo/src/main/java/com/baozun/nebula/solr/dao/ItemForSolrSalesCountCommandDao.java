package com.baozun.nebula.solr.dao;

import java.util.List;

import com.baozun.nebula.solr.command.ItemSalesCountForSolrCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface ItemForSolrSalesCountCommandDao extends GenericEntityDao<ItemSalesCountForSolrCommand, Long> {

	@NativeQuery(model = ItemSalesCountForSolrCommand.class)
	List<ItemSalesCountForSolrCommand> findItemCountById(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemSalesCountForSolrCommand.class)
	List<ItemSalesCountForSolrCommand> findItemCount();
	
}
