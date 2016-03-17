package com.baozun.nebula.solr.dao;

import java.util.List;

import com.baozun.nebula.command.ItemTagCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface BaseItemForSolrTagCommandDao extends GenericEntityDao<ItemTagCommand, Long> {

	@NativeQuery(model = ItemTagCommand.class)
	List<ItemTagCommand> findItemTagByItemId(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemTagCommand.class)
	List<ItemTagCommand> findItemTag();
	
}
