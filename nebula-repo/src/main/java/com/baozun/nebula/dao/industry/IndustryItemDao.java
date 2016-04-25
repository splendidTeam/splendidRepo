package com.baozun.nebula.dao.industry;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.Item;

/**
 * 
 * @author shouqun.li
 * @version 2016年4月12日 下午2:23:30
 */
public interface IndustryItemDao extends GenericEntityDao<Item, Long>{
	/**
	 * 查找某个行业中的有某个属性的商品
	 * @param industryId
	 * @param propertyId
	 * @return
	 */
	@NativeQuery(model = Item.class)
	public List<Item> findItemsByIndustryIdAndPropertyId(@QueryParam("industryId") Long industryId, @QueryParam("propertyId") Long propertyId);

}
