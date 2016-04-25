package com.baozun.nebula.dao.industry;


import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.IndustryPropertyRelation;

/**
 * @author shouqun.li
 * @version 2016年4月8日 下午8:43:57
 */
public interface IndustryPropertyRelationDao extends GenericEntityDao<IndustryPropertyRelation, Long>{

	@NativeUpdate
	public void deleteIndustryPropertyRelation(@QueryParam("industryId") Long industryId, @QueryParam("propertyId") Long propertyId);
	
	@NativeUpdate
	public Integer updateIndustryPropertySort(@QueryParam("industryId") Long industryId, @QueryParam("propertyId") Long propertyId, @QueryParam("sortNo") Integer sortNo);
	
	@NativeQuery(alias = "max",clazzes = Integer.class)
	public Integer findMaxIndustryPropertySortId(@QueryParam("industryId") Long industryId);
	
}
