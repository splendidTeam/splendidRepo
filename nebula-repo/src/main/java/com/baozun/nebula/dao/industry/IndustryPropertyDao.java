package com.baozun.nebula.dao.industry;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.Property;

/**
 * 行业属性的Dao
 * @author shouqun.li
 * @version 2016年4月7日 下午7:28:09
 */
public interface IndustryPropertyDao extends GenericEntityDao<Property, Long>{
	
	@NativeQuery(model = Property.class)
	public List<Property> findIndustryPropertyListByIndustryId(@QueryParam("industryId") Long industryId);
	
	@NativeQuery(model = Property.class)
	public List<Property> findEnableSelectPropertyListByIndustryId(@QueryParam("industryId") Long industryId);
	
}
