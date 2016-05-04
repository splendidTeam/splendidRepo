
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.command.product.CommonPropertyCommand;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyLang;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.command.PropertyValueUploadCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 属性管理
 * 
 * @author lin.liu
 */

public interface PropertyManager extends BaseManager{

	/**
	 * 分页获取Property列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	Pagination<PropertyCommand> findPropertyListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

	/**
	 * 根据条件分页查询属性
	 * @return Pagination<PropertyCommand>
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @author 冯明雷
	 * @time 2016年4月8日下午4:55:41
	 */
	Pagination<PropertyCommand> findPropertyPaginationByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 通过ids批量启用或禁用Property 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	Integer enableOrDisablePropertyByIds(List<Long> ids,Integer state);

	/**
	 * 通过ids批量删除Property 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	Integer removePropertyByIds(List<Long> ids);

	/**
	 * 根据id查找商品属性
	 * 
	 * @param id
	 * @return
	 */
	Property findPropertyById(Long id);
	
	/**
	 * 根据id查找商品属性(返回数据中部分来源于t_pd_common_property表)
	 * 
	 * @param id
	 * @return
	 */
	Property findPropertiesById(Long id);

	/**
	 * 根据行业id和店铺id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 * @return
	 */
	List<Property> findPropertyListByIndustryId(Long industryId);

	/**
	 * 根据行业id和店铺id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 * @return
	 */
	List<com.baozun.nebula.command.product.PropertyCommand> findPropertyCommandListByIndustryId(Long industryId);

	/**
	 * 新增系统属性
	 * 
	 * @param Property
	 *            属性实体
	 * @return
	 */
	boolean createOrUpdateProperty(Property property);

	com.baozun.nebula.command.product.PropertyCommand createOrUpdateProperty(com.baozun.nebula.command.product.PropertyCommand property);
	
	
	/**
	 * 新的创建或更新属性的方法(属性为平台级的)
	 * @return com.baozun.nebula.command.product.PropertyCommand
	 * @param property
	 * @author 冯明雷
	 * @time 2016年4月8日下午3:08:57
	 */
	com.baozun.nebula.command.product.PropertyCommand nebulaCreateOrUpdateProperty(com.baozun.nebula.command.product.PropertyCommand property);

	/**
	 * 更新排序
	 * 
	 * @param id
	 *            属性ID
	 * @param sortNo
	 *            排序号
	 * @return
	 */
	boolean updatePropertyByParamList(String ids);

	/**
	 * 根据PropertyId查找属性值列表
	 * 
	 * @param propertyId
	 * @return
	 */
	List<PropertyValue> findPropertyValueList(Long propertyId);

	/**
	 * 根据PropertyId查找属性值列表
	 * 
	 * @param propertyId
	 * @return
	 */
	List<PropertyValueCommand> findPropertyValuecCommandList(Long propertyId);

	/**
	 * @param propertyValues
	 * @return
	 */
	void createOrUpdatePropertyValueByList(PropertyValue[] propertyValues,Long propertyId);

	void createOrUpdatePropertyValueByList(PropertyValueCommand[] propertyValues,Long propertyId);

	/**
	 * 查询列表根据分类和行业
	 * 
	 * @param categoryId
	 * @param industryId
	 * @return
	 */
	List<Property> selectPropertyByCategoryIdAndIndustryId(Long categoryId,Long industryId);

	List<Property> findAllPropertys();

	/**
	 * 根据行业ID查找通用属性表中没有被该行业属性关联过的且其通用属性名与该行业的属性名没有发生过重复的记录 @param industryId
	 * 
	 * @return
	 */
	List<CommonPropertyCommand> findAllCommonPropertyByindustryId(Long industryId);

	/**
	 * 根据propertyId查找属性扩展表中某个属性对应的所有属性值信息(与当前已使用过的属性值不重复的)
	 * 
	 * @param commonPropertyId
	 * @return
	 */
	List<PropertyValue> findPropertyValueListBycommonPropertyId(Long propertyId);

	/**
	 * 验证属性是否重复(属性名称不能重复,其关联的公共属性也不能重复)
	 * 
	 * @param industryId
	 *            行业Id
	 * @param commonPropertyId
	 *            公共属性Id
	 * @param propertyId
	 *            属性Id
	 * @param proname
	 *            属性名
	 * @return
	 */
	Map<String, Object> validatePropertyData(Long industryId,Long commonPropertyId,Long propertyId,String proname);

	/**
	 * 验证通用属性名是否存在(true:不存在且验证通过)
	 * 
	 * @param commonPropertyName
	 * @return
	 */
	boolean validatecommonPropertyName(String commonPropertyName);

	List<PropertyValueLang> findPropertyValueCommandById(Long id);
	
	/**
	 * 通过属性值分组ID找到相对的属性值列表
	 * @param proGroupId
	 * @return
	 */
	DynamicPropertyCommand  findByProGroupIdAndPropertyId(Long proGroupId,Long propertyId);

	
	/**
	 * 根据propertyId查询Property表(只查询Property表中数据并且生命周期不为2)
	 * @return Property
	 * @param propertyId
	 * @author 冯明雷
	 * @time 2016年4月8日下午5:42:21
	 */
	Property findPropertyByPropertyId(Long propertyId);
	
	
	/**
	 * 根据propertyId查询Property国际化数据
	 * @return List<PropertyLang>
	 * @param propertyId
	 * @author 冯明雷
	 * @time 2016年4月8日下午5:46:01
	 */
	List<PropertyLang> findPropertyLangByPropertyId(Long propertyId);

	/**
	 * 根据propertyId和语言查询propertyValue的国际化的值
	 * @return List<PropertyValueLang>
	 * @param propertyId
	 * @param langs
	 * @author 冯明雷
	 * @time 2016年4月14日上午11:20:06
	 */
	List<PropertyValueLang> findPropertyValueLangByPropertyId(Long propertyId);
	
	/**
	 * 上传创建或更新属性值
	 * @return void
	 * @param propertyValueUploadCommandList
	 * @param propertyId 
	 * @author 冯明雷
	 * @time 2016年4月14日下午4:28:14
	 */
	void createOrUpdatePropertyValueByUpload(List<PropertyValueUploadCommand> propertyValueUploadCommandList,Long propertyId);
	
	/***
	 * 查询全部有效,有属性值，可真检索的属性信息
	 */
	List<DynamicPropertyCommand> findAllDynamicPropertyCommand();	
	
}
