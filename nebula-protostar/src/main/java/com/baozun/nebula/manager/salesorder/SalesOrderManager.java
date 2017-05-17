package com.baozun.nebula.manager.salesorder;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.ExCodeProp;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.PayNoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.command.OrderCommand;
import com.baozun.nebula.web.command.PtsSalesOrderCommand;

/**
 * 订单相关操作
 * 
 * @author qiang.yang
 * @createtime 2013-11-26 PM 14:26
 */
public interface SalesOrderManager extends BaseManager{

	/**
	 * 根据订单号查询订单明细
	 * 
	 * @param orderCode
	 * @return
	 */
	public OrderCommand findOrderByCode(String orderCode);

	/**
	 * 根据订单id查询订单明细
	 * 
	 * @param orderCode
	 * @return
	 */
	public OrderCommand findOrderById(Long orderId);

	/**
	 * 根据支付ID 查询支付流水列表
	 */
	public List<PayNoCommand> findPayNoList(Long payInfoId);

	/**
	 * 获取带分页订单列表
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */

	public Pagination<PtsSalesOrderCommand> findOrderListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 获取物流信息
	 */

	public LogisticsCommand findLogisticsByOrderId(Long orderId);

	/**
	 * 后台下单
	 * 
	 * @param String
	 * @return
	 */
	public String createBackOrder(SalesOrderCommand salesOrderCommand,HttpServletRequest request);

	/**
	 * 客服下单->添加商品 查询商品及sku信息
	 * 
	 * @param page
	 * @param sorts
	 * @return
	 */
	public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);

	/**
	 * 清单
	 * 
	 * @param itemIds
	 * @return
	 */

	public List<OrderLineCommand> getOrderLineList(List<Long> itemIds,String extentionCode);

	/**
	 * 查询itemId所在的code和prop
	 * 
	 * @param itemId
	 * @return
	 */

	public List<ExCodeProp> querySalePropAndExCode(Long itemId);

	/**
	 * 后台下单获取商品清单
	 * 
	 * @param shoppingCartCommand
	 * @return
	 */
	//nebula项目结构合并，20160308 johnnyxia 注释掉 
//	public ShoppingCartCommand processShoppingCartCommand(List<ShoppingCartLineCommand> shoppingCartLineCommands,HttpServletRequest request);

	//nebula项目结构合并，20160308 johnnyxia 注释掉 
//	public Integer doCheckSku(Long skuId,Integer quantity,HttpServletRequest request);

	public ItemBaseCommand findItemBaseInfo(Long itemId);

	public Map<String, Object> findDynamicProperty(Long itemId);

	public List<SkuCommand> findInventoryByItemId(Long itemId);

	/**
	 * 更新订单财务状态
	 * @param id
	 * @return
	 */
    public boolean updateOrderFinancialStatus(String id);
}
