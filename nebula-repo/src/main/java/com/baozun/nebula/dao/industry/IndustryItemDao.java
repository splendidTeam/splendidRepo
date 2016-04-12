package com.baozun.nebula.dao.industry;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.Item;

public interface IndustryItemDao extends GenericEntityDao<Item, Long>{
	
	@NativeQuery(model = Item.class)
	public List<Item> findItemsByIndustryIdAndPropertyId(@QueryParam("industryId") Long industryId, @QueryParam("propertyId") Long propertyId);

}
