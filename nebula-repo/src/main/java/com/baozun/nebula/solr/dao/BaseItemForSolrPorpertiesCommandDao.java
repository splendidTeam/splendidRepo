package com.baozun.nebula.solr.dao;

import java.util.List;

import com.baozun.nebula.command.BaseItemForSolrCommand;
import com.baozun.nebula.command.ItemForCategoryCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.model.product.ItemProperties;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface BaseItemForSolrPorpertiesCommandDao extends GenericEntityDao<ItemPropertiesCommand, Long> {

	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesByItemIdForSearch(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesForSearch();
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesByItemIdWithOutSearch(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesWithOutSearch();
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesCustomerForSearchById(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemPropertiesCommand.class)
	List<ItemPropertiesCommand> findItemPropertiesCustomerForSearch();
}
