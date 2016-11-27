package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

public interface LogisticsManager extends BaseManager{

    /**
     * 计算运费
     * 
     * @param itemList
     *            货物信息
     * @param distributionModeId
     *            物流方式Id
     * @param shopId
     *            店铺Id
     * @param provienceId
     *            省id
     * @param cityId
     *            市id
     * @param countyId
     *            县id
     * @param townId
     *            乡id
     * @return
     */
    BigDecimal findFreight(List<ItemFreightInfoCommand> itemList,Long distributionModeId,Long shopId,Long provienceId,Long cityId,Long countyId,Long townId);

    /**
     * 根据订单id查询物流跟踪信息
     */
    LogisticsCommand findLogisticsByOrderId(Long orderId);

    Pagination<ShippingTemeplate> findShippingTemeplateList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap,Long shopId);

    //********************* 运费模板

    /**
     * 新增运费模板
     * 
     * @param shippingTemeplate
     * @return
     */
    ShippingTemeplateCommand saveShippingTemeplate(ShippingTemeplateCommand shippingTemeplate);

    /**
     * 根据运费模板Id 查询模板数据
     * 
     * @param temeplateId
     * @return
     */
    ShippingTemeplateCommand findShippingTemeplateCommandById(Long temeplateId);

    /**
     * 删除运费模板
     * 
     * @param temeplate
     * @return true 或者BusinessException 如果该模板Id 还和其他商品有关联的话
     */
    boolean removeShippingTemeplate(Long temeplate);

    /**
     * 更新运费模板
     * 
     * @param shippingTemeplateCmd
     * @return
     */
    boolean updateShippingTemeplate(ShippingTemeplateCommand shippingTemeplateCmd);

    /** 设置默认模板 */
    Integer updateShippingTemeplateIsDefault(Long shopId,Long id,boolean isDefault);

    //************************************* 支持的区域

    /**
     * 增加物流方式支持的区域
     * 
     * @param supportedAreaCommandList
     * @return
     */
    List<SupportedAreaCommand> saveSupportedAreas(List<SupportedAreaCommand> supportedAreaCommandList);

    /**
     * 删除物流方式支持的区域
     * 
     * @param ids
     * @return
     */
    boolean deleteSupportedAreas(List<Long> ids);

    //****************************** 物流方式

    /**
     * 增加物流方式
     * 
     * @param cmd
     * @return
     */
    DistributionModeCommand saveDistributionMode(DistributionModeCommand cmd);

    /**
     * 删除物流方式
     * 
     * @param distributionModeId
     * @return
     */
    boolean deleteDistributionMode(Long distributionModeId);

    /**
     * 修改物流方式
     * 
     * @param cmd
     * @return
     */
    boolean updateDistributionMode(DistributionModeCommand cmd);

    /**
     * 保存物流信息
     * 
     * @param logistics
     * @return
     */
    Logistics saveLogistics(Logistics logistics);

    /**
     * 根据queryMap查询列表
     * 
     * @param queryMap
     * @return
     */
    List<LogisticsCommand> findLogisticsListByQueryMap(Map<String, Object> queryMap);

    /**
     * 该地区是否有物流方式支持
     * 
     * @param calcFreightCommand
     * @param shopId
     * @return
     */
    Boolean hasDistributionMode(CalcFreightCommand calcFreightCommand,Long shopId);
}
