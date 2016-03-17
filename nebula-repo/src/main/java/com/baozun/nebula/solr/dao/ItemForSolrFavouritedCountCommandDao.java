package com.baozun.nebula.solr.dao;

import java.util.List;

import com.baozun.nebula.solr.command.ItemFavouriteCountForSolrCommand;
import com.baozun.nebula.solr.command.ItemSalesCountForSolrCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface ItemForSolrFavouritedCountCommandDao extends GenericEntityDao<ItemFavouriteCountForSolrCommand, Long> {

	@NativeQuery(model = ItemFavouriteCountForSolrCommand.class)
	List<ItemFavouriteCountForSolrCommand> findItemCountById(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemFavouriteCountForSolrCommand.class)
	List<ItemFavouriteCountForSolrCommand> findItemCount();
	
}
