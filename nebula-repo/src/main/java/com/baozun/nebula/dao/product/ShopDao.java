package com.baozun.nebula.dao.product;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;

/**
 * 店铺Dao
 * 
 * @author yi.huang
 * @date 2013-7-2 上午10:50:40
 */
public interface ShopDao extends GenericEntityDao<Shop, Long> {

	/**
	 * 在新增店铺属性的时候给出默认的排序
	 * 
	 * @param shopId
	 *            店铺id
	 * @param industryId
	 *            行业id
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "RANKS")
	Integer findCreatePropertyDefaultSortNo(@QueryParam("shopId") Long shopId, @QueryParam("industryId") Long industryId);

	/**
	 * 通过ids批量删除Property 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removePropertyByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过ids批量启用或禁用Property 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisablePropertyByIds(@QueryParam("ids") List<Long> ids, @QueryParam("state") Integer state);

	/**
	 * 根据行业id和店铺id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 * @param shopId
	 *            店铺id
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByIndustryIdAndShopId(@QueryParam("industryId") Long industryId, @QueryParam("shopId") Long shopId, Sort[] sorts);
	
	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByIndustryIdAndShopIdI18n(@QueryParam("industryId") Long industryId, 
			@QueryParam("shopId") Long shopId, Sort[] sorts,@QueryParam("lang")String lang);

	/**
	 * 启用或禁用店铺 ：‘1’表示启用；‘0’表示禁用。
	 * 
	 * @param shopIds
	 *            店铺ID
	 * @param type
	 *            启用或禁用
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisableShopByIds(@QueryParam("shopIds") Long[] shopIds, @QueryParam("type") Integer type);

	/**
	 * 逻辑删除店铺 ：‘2’表示删除
	 * 
	 * @param shopIds
	 *            店铺ID数组
	 * @return
	 */
	@NativeUpdate
	Integer removeShopByIds(@QueryParam("shopIds") Long[] shopIds);

	/**
	 * 查询店铺的相关行业
	 * 
	 * @param sorts
	 * @return caihong.wu
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryList(Sort[] sorts);

	/**
	 * 根据行业Id查询相关的属性
	 * 
	 * @param industryId
	 * @return caihong.wu
	 */
	@NativeQuery(model = Property.class)
	List<Property> findSysPropertyListByIndustryId(@QueryParam("industryId") Long industryId);

	/**
	 * 根据行业Id和店铺Id查询相关的属性及根据行业Id查询系统属性
	 * 
	 * @param industryId
	 * @param shopId
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findIsSysByIidAndIsShopBySidAndIid(@QueryParam("industryId") Long industryId, @QueryParam("shopId") Long shopId);

	/**
	 * 在shopProperty表中插入相关纪录
	 * 
	 * @param industryId
	 * @param shopId
	 * @param propertyId
	 * @return caihong.wu
	 */
	@NativeUpdate
	Integer createShopproperty(@QueryParam("industryId") Long industryId, @QueryParam("shopId") Long shopId, @QueryParam("propertyId") Long propertyId);

	/**
	 * 修改店铺的Organization信息
	 * 
	 * @param name
	 * @param code
	 * @param description
	 * @return caihong.wu
	 */
	@NativeUpdate
	Integer updateOrganization(@QueryParam("id") Long orgId, @QueryParam("name") String name, @QueryParam("code") String code, @QueryParam("description") String description, @QueryParam("lifecycle") Integer lifecycle);

	/**
	 * 修改店铺属性信息
	 * 
	 * @param industryId
	 * @param shopId
	 * @param propertyId
	 * @return caihong.wu
	 */
	@NativeUpdate
	Integer updateShopproperty(@QueryParam("industryId") Integer industryId, @QueryParam("shopId") Integer shopId, @QueryParam("propertyId") Long propertyId);

	/**
	 * 修改店铺属性时应该删除原有的关于店铺的属性，然后重新赋值
	 * 
	 * @param shopId
	 * @return
	 */
	@NativeUpdate
	Integer removeShopPropertyByshopId(@QueryParam("shopId") Long shopId);

	Integer removeByShopIds(@QueryParam("shopIds") List<Long> shopIds);

	/**
	 * 根据店铺id查询店铺
	 * 
	 * @param id
	 *            店铺ID
	 * @return
	 */
	@NativeQuery(model = ShopCommand.class)
	ShopCommand findShopById(@QueryParam("id") Long id);

	/**
	 * 根据店铺id查询店铺
	 * 
	 * @param id
	 *            店铺ID
	 * @return
	 */
	@NativeQuery(model = ShopCommand.class)
	ShopCommand findShopByOrgId(@QueryParam("id") Long id);

	/**
	 * 根据属性名和行业id查询相同的属性的数量
	 * 
	 * @param propertyName
	 *            属性名
	 * @param industryId
	 *            行业id
	 * @return
	 */
	@NativeQuery(clazzes = Integer.class, alias = "COUNT")
	Integer countRepeatNameNo(@QueryParam("propertyName") String propertyName, @QueryParam("industryId") Integer industryId);

	/**
	 * 通过店铺名称, 查询店铺信息
	 * 
	 * @param orgName
	 * @return
	 */
	@NativeQuery(model = ShopCommand.class)
	ShopCommand findShopCommandByOrgName(@QueryParam("orgName") String orgName);
	
	
	@NativeQuery(clazzes = Integer.class, alias = "COUNT")
	Integer countRepeatNameNoi18n(@QueryParam("name") String name, @QueryParam("propertyId") Integer propertyId,@QueryParam("lang")String lang);
	
	/**
	 * 根据店铺ID查询商品所在的行业(可用行业)
	 * @param shopId
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findAllEnabledIndustryByShopId(@QueryParam("shopId") Long shopId);
	
	
	/**
	 * 根据属性名查询属性的数量,并排除当前属性id
	 * @return Integer
	 * @param propertyName
	 * @author 冯明雷
	 * @time 2016年4月8日下午4:11:23
	 */
	@NativeQuery(clazzes = Integer.class, alias = "COUNT")
	Integer findCountByPropertyName(@QueryParam("propertyName") String propertyName,@QueryParam("propertyId") Long propertyId);

	/**
	 * 根据属性名和语言查询属性的数量,并排除当前属性id
	 * @return Integer
	 * @param propertyName
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年4月8日下午4:11:48
	 */
	@NativeQuery(clazzes = Integer.class, alias = "COUNT")
	Integer findCountByPropertyNameAndLang(@QueryParam("propertyName") String propertyName,@QueryParam("lang")String lang,@QueryParam("propertyId") Long propertyId);
	
	
	
}
