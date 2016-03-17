package com.baozun.nebula.solr.dao;

import java.util.List;

import com.baozun.nebula.command.ItemTagCommand;
import com.baozun.nebula.solr.command.ItemRateForSolrCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface ItemForSolrRateCommandDao extends GenericEntityDao<ItemRateForSolrCommand, Long> {

	@NativeQuery(model = ItemRateForSolrCommand.class)
	List<ItemRateForSolrCommand> findItemRateById(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemRateForSolrCommand.class)
	List<ItemRateForSolrCommand> findItemRate();
	
}
