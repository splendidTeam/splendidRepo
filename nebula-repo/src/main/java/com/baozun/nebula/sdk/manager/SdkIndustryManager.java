package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.IndustryPropertyRelation;

/**
 * 
 * 商品行业sdkManager
 * 
 * @author chenguang.zhou
 * @date 2014年6月13日 下午2:49:36
 */
public interface SdkIndustryManager extends BaseManager{
	
	/**
	 * 根据行业名称去查询行业信息
	 * @param industryNames
	 * @return
	 */
	public List<Industry> findIndustryListByNames(List<String> industryNames);
	
	/**
	 * 根据行业id查询行业属性关系 
	 * @param industryId
	 * @author dongliang.ma
	 * @return
	 */
	public List<IndustryPropertyRelation> findIndustryPropertyRelationListByIndustryId(Long industryId);
}
