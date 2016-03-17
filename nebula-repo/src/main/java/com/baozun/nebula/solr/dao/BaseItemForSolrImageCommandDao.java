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

public interface BaseItemForSolrImageCommandDao extends GenericEntityDao<ItemImageCommand, Long> {

	@NativeQuery(model = ItemImageCommand.class)
	List<ItemImageCommand> findItemImageByInfo(@QueryParam("itemIds") List<Long> itemId,@QueryParam("pid") Long pid,@QueryParam("pvid") Long pvid,@QueryParam("imageType")String imageType);
	
	
	@NativeQuery(model = ItemImageCommand.class)
	List<ItemImageCommand> findItemImage(@QueryParam("imageType")String imageType);
	
	@NativeQuery(model = ItemImageCommand.class)
	List<ItemImageCommand> findItemImageByItemId(@QueryParam("itemIds") List<Long> itemId,@QueryParam("type") String imgType);
	
}
