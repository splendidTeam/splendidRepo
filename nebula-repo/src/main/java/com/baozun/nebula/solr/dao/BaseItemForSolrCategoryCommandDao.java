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

public interface BaseItemForSolrCategoryCommandDao extends GenericEntityDao<ItemForCategoryCommand, Long> {

	@NativeQuery(model = ItemForCategoryCommand.class)
	List<ItemForCategoryCommand> findItemCategoryByItemId(@QueryParam("itemIds") List<Long> itemId);
	
	
	@NativeQuery(model = ItemForCategoryCommand.class)
	List<ItemForCategoryCommand> findItemCategory();
	
	@NativeQuery(model = ItemForCategoryCommand.class)
	ItemForCategoryCommand findParentCategoryById(@QueryParam("categoryId") Long categoryId);
	
}
