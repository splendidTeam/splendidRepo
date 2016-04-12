package com.baozun.nebula.manager.industry;

import java.util.List;

import com.baozun.nebula.command.product.PropertyCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.IndustryPropertyRelation;
import com.baozun.nebula.model.product.Item;

/**
 * 行业属性管理Manager
 * @author shouqun.li
 * @version 2016年4月7日 下午6:52:26
 */
public interface IndustryPropertyManager extends BaseManager{
	/**
	 * 根据industryid查找与该行业关联的平台属性
	 * @param industryId
	 * @return
	 */
	public List<PropertyCommand> findIndustryPropertyListByIndustryId(long industryId);
	
	/**
	 * 找出与某个平台不相关的属性
	 * @param industryId
	 * @return
	 */
	public List<PropertyCommand> findEnableSelectPropertyListByIndustryId(long industryId);
	
	/**
	 * 将某个属性与某个行业相关联
	 * @param industryId
	 * @param propertyId
	 */
	public void relationIndustryProperty(IndustryPropertyRelation industryPropertyRelation);
	
	/**
	 * 删除某个属性与某个行业的关联关系
	 * @param industryId
	 * @param propertyId
	 */
	public void deleteIndustryPropertyRelation(long industryId, long propertyId);
	
	/**
	 * 找出属于某个行业且包含某个属性的商品
	 * @param industryId
	 * @param propertyId
	 * @return
	 */
	public List<Item> findItemsByIndustryIdAndPropertyId(long industryId, long propertyId);
	
	/**
	 * 更新行业中属性的排序
	 * @param industryId
	 * @param str
	 */
	public boolean updateIndustryPropertySort(long industryId, String str);
}
