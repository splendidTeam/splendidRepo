package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.sdk.command.logistics.AreaCommand;
import com.baozun.nebula.sdk.command.logistics.CityCommand;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsTypeCommand;
import com.baozun.nebula.sdk.command.logistics.ProvinceCommand;
import com.baozun.nebula.sdk.command.logistics.ShippingProviderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.utilities.library.address.Address;

public interface LogisticsManager extends BaseManager{
	/**
	 * 查询省级列表
	 * @return
	 */
	List<Address> findProvinceList();
	
	/**
	 * 查询市级列表
	 * @param provinceId
	 * @return
	 */
	List<Address> findCities(Long provinceId);
	
	/**
	 * 查询区域(县/区)
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	List<Address> findCounties(Long cityId);
	
	/**
	 * 查询乡镇（街道）
	 * @param areaId
	 * @return
	 */
	List<Address> findTowns(Long areaId);
	
	/**
	 * (new)
	 * 通过收货地址获取支持的物流方式 ,
     *       	参数：省、市、区、镇
     *      	返回：物流方式列表
	 * @return
	 */
	List<DistributionMode> findDistributeMode(Long shopId,Long provienceId,Long cityId,Long countyId,Long townId);
	
	/**
	 * 查询到达某个地方支持的物流方式   
	 * 已废弃（请用 findDistributeMode 代替）
	 * @param areaId
	 * @return
	 */
	@Deprecated
	List<ShippingProviderCommand> findLogisticsTypes(Long itemId,String areaId);
	
	/**
	 * 计算运费
	 * @param itemList  货物信息
	 * @param distributionModeId 物流方式Id
	 * @param shopId 店铺Id 
	 * @param provienceId 省id
	 * @param cityId 市id
	 * @param countyId 县id
	 * @param townId 乡id
	 * @return
	 */
	BigDecimal findFreight(List<ItemFreightInfoCommand> itemList,Long distributionModeId,Long shopId,Long provienceId,Long cityId,Long countyId,Long townId);
	
	/**
	 * 根据订单id查询物流跟踪信息
	 */
	LogisticsCommand findLogisticsByOrderId(Long orderId);
	
	/**
	 * 商品绑定运费模板
	 * @param itemId  商品Id 
	 * @param temeplateId 运费模板Id
	 * @return
	 */
	@Deprecated
	boolean applyItemShippingTemeplate(Long itemId,Long temeplateId);
	
	/**
	 * 解除该商品运费模板的绑定
	 * (当删除一个商品时候，删除对应的冗余运费信息)
	 * @param itemId
	 * @return
	 */
	@Deprecated
	boolean removeItemShippingTemeplate(Long itemId);
	
	public Pagination<ShippingTemeplate> findShippingTemeplateList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap,
			Long shopId);

	//********************* 运费模板
	
	/**
	 * 新增运费模板
	 * @param shippingTemeplate
	 * @return
	 */
	ShippingTemeplateCommand saveShippingTemeplate(ShippingTemeplateCommand shippingTemeplate); 
	
	/**
	 * 根据运费模板Id 查询模板数据
	 * @param temeplateId
	 * @return
	 */
	ShippingTemeplateCommand findShippingTemeplateCommandById(Long temeplateId);
	
	/**
	 * 删除运费模板
	 * @param temeplate
	 * @return  true  或者BusinessException  如果该模板Id 还和其他商品有关联的话
	 */
	boolean removeShippingTemeplate(Long temeplate);
	
	/**
	 * 更新运费模板
	 * @param shippingTemeplateCmd
	 * @return
	 */
	boolean updateShippingTemeplate(ShippingTemeplateCommand shippingTemeplateCmd);
	
	/** 设置默认模板*/
	Integer updateShippingTemeplateIsDefault(Long shopId,Long id,boolean isDefault);
	
	//************************************* 支持的区域
	
	/**
	 * 增加物流方式支持的区域
	 * @param supportedAreaCommandList
	 * @return
	 */
	List<SupportedAreaCommand> saveSupportedAreas(List<SupportedAreaCommand> supportedAreaCommandList);
	
	// 不提供 单个 支持的物流区域的方法。   修改一个支持的物流区域 没有意义。  因为只能 修改 areaId 或者代称。这样在物理意义上其实对应了删除和新增。
//	boolean updateSupportedArea(SupportedAreaCommand supportedAreaCommand);
	
	/**
	 * 删除物流方式支持的区域
	 * @param ids
	 * @return
	 */
	boolean deleteSupportedAreas(List<Long> ids);
	
	//****************************** 物流方式
	
	/**
	 * 增加物流方式
	 * @param cmd
	 * @return
	 */
	DistributionModeCommand saveDistributionMode(DistributionModeCommand cmd);
	
	/**
	 * 删除物流方式
	 * @param distributionModeId
	 * @return
	 */
	boolean deleteDistributionMode(Long distributionModeId);
	
	/**
	 * 修改物流方式
	 * @param cmd
	 * @return
	 */
	boolean updateDistributionMode(DistributionModeCommand cmd);
	
	/**
	 * 保存物流信息
	 * @param logistics
	 * @return
	 */
	Logistics saveLogistics(Logistics logistics);
	
	/**
	 * 根据queryMap查询列表
	 * @param queryMap
	 * @return
	 */
	List<LogisticsCommand> findLogisticsListByQueryMap(Map<String,Object> queryMap);
	/**
	 * 该地区是否有物流方式支持
	 * @param calcFreightCommand
	 * @param shopId
	 * @return
	 */
	public Boolean hasDistributionMode(CalcFreightCommand calcFreightCommand, Long shopId);
}
