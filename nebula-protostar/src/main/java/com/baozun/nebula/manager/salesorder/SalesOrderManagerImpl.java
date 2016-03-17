package com.baozun.nebula.manager.salesorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.command.ItemPropertiesCommand;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLogDao;
import com.baozun.nebula.dao.salesorder.SdkOrderStatusLogDao;
import com.baozun.nebula.dao.salesorder.SdkPayNoDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.model.salesorder.OrderLog;
import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.command.DynamicPropertyCommand;
import com.baozun.nebula.sdk.command.EngineMemberCommand;
import com.baozun.nebula.sdk.command.ExCodeProp;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.ItemSkuCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.PayNoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.command.OrderCommand;
import com.baozun.nebula.web.command.PtsSalesOrderCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * @author qiang.yang
 * @createtime 2013-11-26 PM 14:26
 */

@Transactional
@Service
public class SalesOrderManagerImpl implements SalesOrderManager{

	@Autowired
	private OrderManager			sdkOrderService;

	@Autowired
	private LogisticsManager		logisticsManager;

	@Autowired
	private ChooseOptionDao			chooseOptionDao;

	@Autowired
	private SdkOrderStatusLogDao	sdkOrderStatusLogDao;

	@Autowired
	private MemberDao				memberDao;

	@Autowired
	private SdkPayNoDao				sdkPayNoDao;

	@Autowired
	private SdkOrderLogDao			sdkOrderLogDao;

	@Autowired
	private SkuDao					skuDao;

	@Autowired
	private ItemInfoDao				itemInfoDao;

	@Autowired
	private ItemDao					itemDao;

	@Autowired
	private SdkSkuInventoryDao		sdkSkuInventoryDao;

	@Autowired
	private SdkEngineManager		sdkEngineManager;

	@Autowired
	private SdkItemManager			sdkItemManager;

	private static final String		BACK_ITEM_LIST_CART_TRIGGER	= Constants.BACK_ITEM_LIST_CART_TRIGGER;

	private static final String		ACTIVITY_RES				= Constants.ACTIVITY_RES;

	private static final Integer	SUCCESS						= 1;

	/** 存物流状态 */
	private Map<Integer, String>	logisticsStatusMap			= new HashMap<Integer, String>();

	/** 存财务状态 */
	private Map<Integer, String>	financialStatusMap			= new HashMap<Integer, String>();

	/** 存订单来源 */
	private Map<Integer, String>	orderSourceMap				= new HashMap<Integer, String>();

	/** 存发票 */
	private Map<Integer, String>	receiptMap					= new HashMap<Integer, String>();

	/** 存支付类型 */
	private Map<Integer, String>	payTypeMap					= new HashMap<Integer, String>();

	/** 存行类型 */
	private Map<Integer, String>	orderLineTypeMap			= new HashMap<Integer, String>();

	/** 存评价 */
	private Map<Integer, String>	orderLineEvaluationMap		= new HashMap<Integer, String>();

	/** 存促销 */
	private Map<Integer, String>	promotionTypeMap			= new HashMap<Integer, String>();

	/** 定义获取物流状态信息 */
	private static List<String>		logisticgroupCodes			= new ArrayList<String>();

	private static final String		logisticCode				= "ORDER_STATUS_LOGISTICS";

	/** 定义获取财务状态信息 */
	private static List<String>		financialgroupCodes			= new ArrayList<String>();

	private static final String		financialCode				= "ORDER_STATUS_FINANCIAL";

	/** 定义获取订单来源信息 */
	private static List<String>		orderSourceCodes			= new ArrayList<String>();

	private static final String		orderSourceCode				= "ORDER_SOURCE";

	/** 定义获取发票信息 */
	private static List<String>		receiptCodes				= new ArrayList<String>();

	private static final String		receiptCode					= "RECEIPT_TYPE";

	/** 定义获取支付方式信息 */
	private static List<String>		payTypeCodes				= new ArrayList<String>();

	private static final String		payTypeCode					= "ORDER_PAYTYPE_TYPE";

	/** 定义行类型 */
	private static List<String>		orderLineTypeCodes			= new ArrayList<String>();

	private static final String		orderLineTypeCode			= "ORDER_LINE_TYPE";

	/** 定义评价信息 */
	private static List<String>		orderLineEvaluationCodes	= new ArrayList<String>();

	private static final String		orderLineEvaluationCode		= "ORDER_LINE_EVALUATION";

	/** 定义促销类型 */
	private static List<String>		orderpromotionTypeCodes		= new ArrayList<String>();

	private static final String		orderpromotionTypeCode		= "ORDER_PROMOTION_TYPE";

	private static final String		SHOPPINGCARTSUMMARY			= Constants.SHOPPINGCARTSUMMARY;

	@Override
	public OrderCommand findOrderByCode(String orderCode){
		init();
		OrderCommand orderCommand = null;
		SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderByCode(orderCode, 1);
		if (salesOrderCommand != null){
			orderCommand = new OrderCommand();
			orderCommand.setSalesOrderCommand(salesOrderCommand);
			orderCommand.setLogisticsInfo(logisticsStatusMap.get(salesOrderCommand.getLogisticsStatus()));
			orderCommand.setFinancialStatusInfo(financialStatusMap.get(salesOrderCommand.getFinancialStatus()));
			orderCommand.setOrderSource(orderSourceMap.get(salesOrderCommand.getSource()));
			orderCommand.setReceiptType(receiptMap.get(salesOrderCommand.getReceiptType()));
			orderCommand.setPayTypeMap(payTypeMap);
			// 获取地址
			// orderCommand.getSalesOrderCommand().setCountry(salesOrderCommand.getCountry()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getCountryId()).getName());
			// orderCommand.getSalesOrderCommand().setProvince(salesOrderCommand.getProvince()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getProvinceId()).getName());
			// orderCommand.getSalesOrderCommand().setCity(salesOrderCommand.getCity()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getCityId()).getName());
			// orderCommand.getSalesOrderCommand().setArea(salesOrderCommand.getArea()==null ? "":
			// AddressUtil.getAddressById(salesOrderCommand.getAreaId()).getName());
			List<OrderLog> orderLogs = null;
			try{
				orderLogs = sdkOrderLogDao.findOrderLogByOrderId(salesOrderCommand.getId());
			}catch (Exception e){
				e.printStackTrace();
				orderLogs = new ArrayList<OrderLog>();
			}
			orderCommand.setOrderLogs(orderLogs);
			List<OrderStatusLog> orderStatusLogs = null;
			try{
				orderStatusLogs = sdkOrderStatusLogDao.findOrderStatusLogByOrderId(salesOrderCommand.getId());
			}catch (Exception e){
				e.printStackTrace();
				orderStatusLogs = new ArrayList<OrderStatusLog>();
			}
			orderCommand.setOrderStatusLogs(orderStatusLogs);
			orderCommand.setStatusMap(logisticsStatusMap);
			orderCommand.setOrderLineEvaluationMap(orderLineEvaluationMap);
			orderCommand.setOrderLineTypeMap(orderLineTypeMap);
			orderCommand.setPromotionTypeMap(promotionTypeMap);
			if (StringUtils.isNotBlank(salesOrderCommand.getAppointType())){
				orderCommand.setAppointReceive(salesOrderCommand.getAppointType());
			}else if (StringUtils.isNotBlank(salesOrderCommand.getAppointTime())){
				orderCommand.setAppointReceive(salesOrderCommand.getAppointTime());
			}else{
				orderCommand.setAppointReceive(salesOrderCommand.getAppointTimeQuantum());
			}

			Long memberId = salesOrderCommand.getMemberId();
			if (memberId != null){
				List<Long> ids = new ArrayList<Long>();
				ids.add(memberId);
				List<Member> members = memberDao.findMemberListByIds(ids);
				if (members != null && members.size() > 0){
					orderCommand.setMemberName(members.get(0).getLoginName());
				}
			}
		}

		return orderCommand;
	}

	@Override
	public OrderCommand findOrderById(Long orderId){
		init();
		OrderCommand orderCommand = null;
		SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderById(orderId, 1);
		if (salesOrderCommand != null){
			orderCommand = new OrderCommand();
			orderCommand.setSalesOrderCommand(salesOrderCommand);
			orderCommand.setLogisticsInfo(logisticsStatusMap.get(salesOrderCommand.getLogisticsStatus()));
			orderCommand.setFinancialStatusInfo(financialStatusMap.get(salesOrderCommand.getFinancialStatus()));
			orderCommand.setOrderSource(orderSourceMap.get(salesOrderCommand.getSource()));
			orderCommand.setReceiptType(receiptMap.get(salesOrderCommand.getReceiptType()));
			orderCommand.setPayTypeMap(payTypeMap);
			// 获取地址
			// orderCommand.getSalesOrderCommand().setCountry(salesOrderCommand.getCountry()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getCountryId()).getName());
			// orderCommand.getSalesOrderCommand().setProvince(salesOrderCommand.getProvince()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getProvinceId()).getName());
			// orderCommand.getSalesOrderCommand().setCity(salesOrderCommand.getCity()==null ? "" :
			// AddressUtil.getAddressById(salesOrderCommand.getCityId()).getName());
			// orderCommand.getSalesOrderCommand().setArea(salesOrderCommand.getArea()==null ? "":
			// AddressUtil.getAddressById(salesOrderCommand.getAreaId()).getName());
			List<OrderLog> orderLogs = null;
			try{
				orderLogs = sdkOrderLogDao.findOrderLogByOrderId(salesOrderCommand.getId());
			}catch (Exception e){
				e.printStackTrace();
				orderLogs = new ArrayList<OrderLog>();
			}
			orderCommand.setOrderLogs(orderLogs);
			List<OrderStatusLog> orderStatusLogs = null;
			try{
				orderStatusLogs = sdkOrderStatusLogDao.findOrderStatusLogByOrderId(salesOrderCommand.getId());
			}catch (Exception e){
				e.printStackTrace();
				orderStatusLogs = new ArrayList<OrderStatusLog>();
			}
			orderCommand.setOrderStatusLogs(orderStatusLogs);
			orderCommand.setStatusMap(logisticsStatusMap);
			orderCommand.setOrderLineEvaluationMap(orderLineEvaluationMap);
			orderCommand.setOrderLineTypeMap(orderLineTypeMap);
			orderCommand.setPromotionTypeMap(promotionTypeMap);
			if (StringUtils.isNotBlank(salesOrderCommand.getAppointType())){
				orderCommand.setAppointReceive(salesOrderCommand.getAppointType());
			}else if (StringUtils.isNotBlank(salesOrderCommand.getAppointTime())){
				orderCommand.setAppointReceive(salesOrderCommand.getAppointTime());
			}else{
				orderCommand.setAppointReceive(salesOrderCommand.getAppointTimeQuantum());
			}

			Long memberId = salesOrderCommand.getMemberId();
			if (memberId != null){
				List<Long> ids = new ArrayList<Long>();
				ids.add(memberId);
				List<Member> members = memberDao.findMemberListByIds(ids);
				if (members != null && members.size() > 0){
					orderCommand.setMemberName(members.get(0).getLoginName());
				}
			}
		}

		return orderCommand;
	}

	/**
	 * 初始化状态Map
	 */
	private void init(){
		initMap(logisticgroupCodes, logisticCode, logisticsStatusMap);
		initMap(financialgroupCodes, financialCode, financialStatusMap);
		initMap(orderSourceCodes, orderSourceCode, orderSourceMap);
		initMap(receiptCodes, receiptCode, receiptMap);
		initMap(payTypeCodes, payTypeCode, payTypeMap);
		initMap(orderLineTypeCodes, orderLineTypeCode, orderLineTypeMap);
		initMap(orderLineEvaluationCodes, orderLineEvaluationCode, orderLineEvaluationMap);
		initMap(orderpromotionTypeCodes, orderpromotionTypeCode, promotionTypeMap);
	}

	/**
	 * 添加值
	 * 
	 * @param Codes
	 * @param code
	 * @param map
	 */
	private void initMap(List<String> Codes,String code,Map<Integer, String> map){
		Codes.add(code);
		List<ChooseOption> optionList = chooseOptionDao.findChooseOptionValue(Codes);
		for (ChooseOption chooseOption : optionList){
			String value = chooseOption.getOptionLabel();
			if (code.equals(payTypeCode) && value.contains("&&")){
				value = value.split("&&")[0];
			}
			map.put(Integer.parseInt(chooseOption.getOptionValue()), value);
		}
	}

	@Override
	public List<PayNoCommand> findPayNoList(Long payInfoId){
		List<PayNoCommand> payNoCommands = null;
		try{
			payNoCommands = sdkPayNoDao.findPayNosByPayInfoId(payInfoId);
		}catch (Exception e){
			e.printStackTrace();
			payNoCommands = new ArrayList<PayNoCommand>();
		}
		return payNoCommands;
	}

	/**
	 * 查询订单列表
	 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination<PtsSalesOrderCommand> findOrderListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam){

		searchParam.put("sdkQueryType", "1");

		Pagination<SalesOrderCommand> paginationOrder = sdkOrderService.findOrders(page, sorts, searchParam);

		Pagination<PtsSalesOrderCommand> result = new Pagination<PtsSalesOrderCommand>();

		result = (Pagination<PtsSalesOrderCommand>) ConvertUtils.convertTwoObject(result, paginationOrder);

		List<SalesOrderCommand> items = paginationOrder.getItems();

		List<PtsSalesOrderCommand> qItems = result.getItems();

		List<String> groupCodes = new ArrayList<String>();
		groupCodes.add(logisticCode);
		groupCodes.add(financialCode);
		groupCodes.add(payTypeCode);
		groupCodes.add(orderSourceCode);
		List<ChooseOption> optionList = chooseOptionDao.findChooseOptionValue(groupCodes);

		Map<String, String> optionMap = new HashMap<String, String>();
		for (ChooseOption co : optionList){
			optionMap.put(co.getGroupCode() + "-" + co.getOptionValue(), co.getOptionLabel());
		}

		for (int i = 0; i < qItems.size(); i++){

			PtsSalesOrderCommand temp = new PtsSalesOrderCommand();

			temp = (PtsSalesOrderCommand) ConvertUtils.convertTwoObject(temp, paginationOrder.getItems().get(i));

			temp.setLogisticsLabel(optionMap.get(logisticCode + "-" + items.get(i).getLogisticsStatus()));
			temp.setFinancialLabel(optionMap.get(financialCode + "-" + items.get(i).getFinancialStatus()));
			temp.setPaymentLabel(optionMap.get(payTypeCode + "-" + items.get(i).getPayment()));
			temp.setSourceLabel(optionMap.get(orderSourceCode + "-" + items.get(i).getSource()));
			qItems.set(i, temp);

		}

		return result;
	}

	@Override
	public LogisticsCommand findLogisticsByOrderId(Long orderId){
		LogisticsCommand logisticsCommand = null;
		try{
			logisticsCommand = logisticsManager.findLogisticsByOrderId(orderId);
		}catch (Exception e){
			e.printStackTrace();
			logisticsCommand = new LogisticsCommand();
		}
		return logisticsCommand;
	}

	/**
	 * 后台下订单
	 */
	@Override
	public String createBackOrder(SalesOrderCommand salesOrderCommand,HttpServletRequest request){
		// String orderCode =orderManager.saveOrder(salesOrderCommand,request);
		return null;
	}

	@Override
	public Pagination<ItemSkuCommand> findItemSkuListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){

		return sdkOrderService.findItemSkuListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public List<OrderLineCommand> getOrderLineList(List<Long> itemIds,String extentionCode){

		List<OrderLineCommand> result = new ArrayList<OrderLineCommand>();

		for (int i = 0; i < itemIds.size(); i++){
			SkuInventory inventory = null;
			OrderLineCommand lineCommand = new OrderLineCommand();

			Sku sku = new Sku();
			ItemInfo itemInfo = itemInfoDao.findItemInfoByItemId(itemIds.get(i));

			Item item = itemDao.findItemById(itemIds.get(i));
			lineCommand.setItemName(itemInfo.getTitle());
			lineCommand.setItemPic(item.getPicUrl());
			lineCommand.setItemId(itemIds.get(i));

			if (StringUtils.isNotBlank(extentionCode)){
				// 修改销售属性的时候重绘某一条清单
				inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(extentionCode);
				// 库存
				lineCommand.setCount(inventory.getAvailableQty());
				// 价格
				sku = skuDao.findSkuByExtentionCode(extentionCode);

			}else{
				// 添加
				List<Sku> skuList = skuDao.findSkuByItemId(itemIds.get(i));
				if (skuList != null && skuList.size() > 0){

					// 默认第一个
					sku = skuList.get(0);
					inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
					lineCommand.setCount(inventory.getAvailableQty());

				}
			}

			lineCommand.setExtentionCode(sku.getOutid());
			lineCommand.setSalePrice(sku.getSalePrice());
			lineCommand.setSaleProperty(convertSaleProp(sku.getPropertiesName()));
			result.add(lineCommand);

		}
		return result;
	}

	// 绘制已选择的销售属性
	public String convertSaleProp(String propertiesName){
		String result = "";
		Gson gson = new Gson();
		List<SkuProperty> pList = gson.fromJson(propertiesName, new TypeToken<List<SkuProperty>>(){}.getType());
		for (SkuProperty skuProperty : pList){

			result += "<span class='salePropNameStyle'>";
			result += skuProperty.getpName() + "</span>:";
			result += "&nbsp;&nbsp;";
			result += skuProperty.getValue() + "<br/>";
			result += "<br/>";
		}
		return result;
	}

	@Override
	public List<ExCodeProp> querySalePropAndExCode(Long itemId){

		List<ExCodeProp> resultList = new ArrayList<ExCodeProp>();

		List<Sku> skuList = skuDao.findSkuByItemId(itemId);

		Sku sku = new Sku();
		ExCodeProp ecp = null;

		List<SkuProperty> pList = null;

		Gson gson = new Gson();

		for (int i = 0; i < skuList.size(); i++){
			sku = skuList.get(i);

			ecp = new ExCodeProp();
			ecp.setExtentionCode(sku.getOutid());
			pList = new ArrayList<SkuProperty>();
			pList = gson.fromJson(sku.getPropertiesName(), new TypeToken<List<SkuProperty>>(){}.getType());
			ecp.setPropertyList(pList);
			resultList.add(ecp);
		}

		return resultList;
	}

	/*
	@Override
	public ShoppingCartCommand processShoppingCartCommand(List<ShoppingCartLineCommand> shoppingCartLineCommands,HttpServletRequest request){

		ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
		ShoppingCartSummary cartSummary = null;
		BigDecimal totalPrice = new BigDecimal(0);
		BigDecimal totalDiscount = new BigDecimal(0);

		// 得到引擎对象 。 获取购物车列表时候要经过 有效性引擎和促销引擎。 不走限购检查引擎
		EngineManager engine = EngineManager.getInstance();
		ChannelEngine channelEngine = engine.getChannelEngine();// 渠道引擎
		EffectEngine effectEngine = engine.getEffectEngine();// 有效性检查引擎
		ActivityEngine activityEngine = engine.getActivityEngine();// 促销引擎

		List<ShoppingCartLineCommand> validedLines = new ArrayList<ShoppingCartLineCommand>();
		EngineContext engineContext = getEngineContext(new EngineMemberAdapter(new EngineMemberCommand()), channelEngine, request);
		for (ShoppingCartLineCommand shoppingCartLine : shoppingCartLineCommands){
			// sku信息先从数据库读取，之后可能改成从缓存里读取
			Sku sku = skuDao.findSkuById(shoppingCartLine.getSkuId());
			// 有效性检查
			boolean singleResult = doSkuEffectValidate(sku, effectEngine, shoppingCartLine, engineContext);

			if (singleResult){
				shoppingCartLine.setExtentionCode(sku.getOutid());
				validedLines.add(shoppingCartLine);
			}
			shoppingCartLine.setValid(singleResult);
		}
		if (validedLines != null && validedLines.size() > 0){
			// 购物车活动引擎只计算 有效的购物行
			// cartSummary = new
			// ShoppingCartSummaryAdapter(sdkEngineManager.mappingEngineShoppingCartSummaryData(validedLines,null,request,null));
			// 促销引擎 计算促销
			// engineContext.getParameters().put(Constants.SHOPPINGCARTSUMMARY,cartSummary);
			activityEngine.doActivity(engineContext, BACK_ITEM_LIST_CART_TRIGGER);
			// 设置购物车里边的价格（从cartSummary 中取得）
			Map<String, Object> resultMap = engineContext.getParameters();
			String activityResult = (String) resultMap.get(ACTIVITY_RES);
			boolean activityFlag = new Boolean(activityResult);
			cartSummary = (ShoppingCartSummary) resultMap.get(SHOPPINGCARTSUMMARY);

			// shoppingCart.setActivityResult(activityFlag);
			if (activityFlag){
				Map<Long, SalesActivityInfoBean> infoBeans = cartSummary.getActivityBean().getInfoBeans();
				for (Entry<Long, SalesActivityInfoBean> entry : infoBeans.entrySet()){
					SalesActivityInfoBean infoBean = entry.getValue();
					totalDiscount = totalDiscount.add(infoBean.getFinalDiscountAmount());
				}
				totalDiscount = totalDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
				List<ShoppingCartSku> cartSkus = (List<ShoppingCartSku>) cartSummary.getSkuList();
				for (ShoppingCartSku cartSku : cartSkus){
					totalPrice = totalPrice.add(cartSku.getActivityUnitPrice().multiply(new BigDecimal(cartSku.getQuantity())));
				}
				totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		// shoppingCart.setTotalDiscount(totalDiscount);
		// shoppingCart.setTotalPrice(totalPrice);
		shoppingCart.setShoppingCartLineCommands(shoppingCartLineCommands);
		return shoppingCart;
	}
*/
	// 检查商品的有效性
/*	private boolean doSkuEffectValidate(Sku sku,EffectEngine effectEngine,ShoppingCartLineCommand shoppingLineCommand,EngineContext context){
		// ShoppingCartLineCommand cartLineCommand = sdkEngineManager.mappingEngineCartLineSkuData(sku.getItemId());
		// EngineSku engineSku = new EngineCartLineAdapter(cartLineCommand);
		// engineSku.setState(sku.getState());
		// context.getParameters().put(Constants.ENGINESKU,engineSku);
		// boolean flag = effectEngine.doEffectEngineValidate(context,BACK_ITEM_LIST_CART_TRIGGER);
		// if(!flag){//如果购物车中的商品无效，那么不抛出异常，只是设置 改行的有效性状态
		// shoppingLineCommand.setValid(false);
		// }else{
		// shoppingLineCommand.setValid(true);
		// }
		// shoppingLineCommand.setItemName(cartLineCommand.getItemName());
		// shoppingLineCommand.setItemPic(cartLineCommand.getItemPic());
		// shoppingLineCommand.setSalePrice(sku.getSalePrice());
		// shoppingLineCommand.setSaleProperty(sku.getPropertiesName());
		return true;
	}*/

/*	private EngineContext getEngineContext(EngineMember engineMember,ChannelEngine channelEngine,HttpServletRequest request){
		EngineContext memberContext = new EngineContext();
		Map<String, Object> memberMap = new HashMap<String, Object>();
		memberMap.put(Constants.ENGINEMEMBER, engineMember);
		memberContext.setParameters(memberMap);

		List<String> channelIdList = channelEngine.doChannel(memberContext, BACK_ITEM_LIST_CART_TRIGGER, request);
		String channelId = getChannelIdByList(channelIdList);
		engineMember.setChannelNo(channelId);

		memberMap.put(Constants.ENGINEMEMBER, engineMember);
		memberContext.setParameters(memberMap);

		return memberContext;
	}*/

	/**
	 * 将 渠道引擎返回的 List 转化为 字符串
	 * 
	 * @param channelIdList
	 * @return
	 */
	private String getChannelIdByList(List<String> channelIdList){
		if (null == channelIdList || 0 == channelIdList.size()){
			return null;
		}

		StringBuilder channelIdStr = new StringBuilder();
		for (String id : channelIdList){
			channelIdStr.append(id).append(",");
		}

		// 去掉最后一个逗号
		String result = null;
		if (channelIdStr.length() > 0){
			result = channelIdStr.substring(0, channelIdStr.length() - 1);
		}

		return result;
	}

	/*@Override
	public Integer doCheckSku(Long skuId,Integer quantity,HttpServletRequest request){
		boolean flag = true;
		EngineManager engine = EngineManager.getInstance();
		ChannelEngine channelEngine = engine.getChannelEngine();
		EffectEngine effectEngine = engine.getEffectEngine();
		LimitaryEngine limitaryEngine = engine.getLimitaryEngine();

		EngineMember engineMember = new EngineMemberAdapter(new EngineMemberCommand());
		EngineContext engineContext = new EngineContext();
		engineContext = getEngineContext(engineMember, channelEngine, request);

		Map<String, Object> engineMap = engineContext.getParameters();
		getParamMap(engineMap, skuId, quantity, request);
		engineContext.setParameters(engineMap);

		flag = effectEngine.doEffectEngineValidate(engineContext, BACK_ITEM_LIST_CART_TRIGGER);
		if (!flag){
			return Constants.CHECK_VALID_FAILURE;
		}
		flag = limitaryEngine.doEffectEngineValidate(engineContext, BACK_ITEM_LIST_CART_TRIGGER);
		if (!flag){
			return Constants.CHECK_LIMIT_FAILURE;
		}
		return SUCCESS;
	}*/

/*	private Map<String, Object> getParamMap(Map<String, Object> engineMap,Long skuId,Integer quantity,HttpServletRequest request){
		// Sku sku = skuDao.findSkuById(skuId);
		// EngineSku engineSku = new EngineCartLineAdapter(sdkEngineManager.mappingEngineCartLineSkuData(sku.getItemId()));
		// engineSku.setQuantity(quantity);
		// engineSku.setState(sku.getState());
		// engineMap.put(Constants.ENGINESKU,engineSku);
		// List<ShoppingCartLineCommand> cartLineCommands = new ArrayList<ShoppingCartLineCommand>();
		// ShoppingCartLineCommand shoppingCartLine = new ShoppingCartLineCommand();
		// shoppingCartLine.setQuantity(quantity);
		// shoppingCartLine.setSkuId(skuId);
		// shoppingCartLine.setExtentionCode(sku.getOutid());
		// shoppingCartLine.setCreateTime(new Date());
		// cartLineCommands.add(shoppingCartLine);
		// ShoppingCartSummaryCommand cartSummary =
		// sdkEngineManager.mappingEngineShoppingCartSummaryData(cartLineCommands,null,request,null);
		// engineMap.put(Constants.SHOPPINGCARTSUMMARY,new ShoppingCartSummaryAdapter(cartSummary));
		return engineMap;
	}*/

	@Override
	public ItemBaseCommand findItemBaseInfo(Long itemId){
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfo(itemId);
		if (itemBaseCommand == null){
			throw new BusinessException(ErrorCodes.ITEM_NOT_EXIST);
		}
		return itemBaseCommand;
	}

	@Override
	public Map<String, Object> findDynamicProperty(Long itemId){
		List<ItemProperties> dbItemPropertiesList = sdkItemManager.findItemPropertiesByItemId(itemId);
		// 商品的动态属性Map
		Map<String, Object> responseMap = getDynamicPropertyMap(dbItemPropertiesList, itemId);
		return responseMap;
	}

	@Override
	public List<SkuCommand> findInventoryByItemId(Long itemId){
		return sdkItemManager.findInventoryByItemId(itemId);
	}

	/**
	 * 获得商品的动态属性Map
	 * 
	 * @param dbItemPropertiesList
	 *            :数据库存中的商品属性集合
	 * @return Map<String, Object>中存放的是: 分组名称集合(groupNameList), 销售属性集合(salePropCommandList), 一般属性集合(generalPropCommandList)
	 */
	private Map<String, Object> getDynamicPropertyMap(List<ItemProperties> dbItemPropertiesList,Long itemId){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DynamicPropertyCommand> salePropCommandList = new ArrayList<DynamicPropertyCommand>();
		List<DynamicPropertyCommand> generalPropCommandList = new ArrayList<DynamicPropertyCommand>();
		DynamicPropertyCommand salePropCommand = null;
		DynamicPropertyCommand generalPropCommand = null;
		List<ItemPropertiesCommand> itemPropertiesList = null;
		// 属性Id集合
		Set<Long> propertyIdSet = new HashSet<Long>();
		List<Long> propertyIds = new ArrayList<Long>();
		// 属性值Id集合
		Set<Long> propertyValueIdSet = new HashSet<Long>();
		List<Long> propertyValueIds = new ArrayList<Long>();
		// 分组名称集合
		Map<String, String> groupNameMap = new HashMap<String, String>();
		List<String> groupNameList = new ArrayList<String>();
		// 获得"属性Id集合"和"属性值Id集合"
		for (ItemProperties itemProperties : dbItemPropertiesList){
			Long propertyId = itemProperties.getPropertyId();
			propertyIdSet.add(propertyId);
			Long propertyValueId = itemProperties.getPropertyValueId();
			if (propertyValueId != null){
				propertyValueIdSet.add(propertyValueId);
			}
		}
		propertyIds.addAll(propertyIdSet);
		propertyValueIds.addAll(propertyValueIdSet);
		// 通过"属性id的集合"获得"属性集合"
		Sort[] sorts = Sort.parse("sort_no asc");
		List<Property> propertyList = sdkItemManager.findPropertyListByIds(propertyIds, sorts);
		// 通过"属性值id的集合"获得"属性值集合"
		List<PropertyValue> propertyValueList = sdkItemManager.findPropertyValueListByIds(propertyValueIds);

		Map<Long, String> propertyValueMap = new HashMap<Long, String>();
		for (PropertyValue propertyValue : propertyValueList){
			propertyValueMap.put(propertyValue.getId(), propertyValue.getValue());
		}

		for (Property property : propertyList){
			Boolean isSaleProp = property.getIsSaleProp();
			// 分离销售属性与一般属性
			if (isSaleProp){
				// 销售属性
				salePropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList){
					if (itemProperties.getPropertyId().equals(property.getId())){
						if (itemProperties.getPropertyValueId() == null){
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}else{
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1){
					salePropCommand.setItemProperties(itemPropertiesList.get(0));
				}else{
					salePropCommand.setItemPropertiesList(itemPropertiesList);
				}
				salePropCommand.setProperty(property);
				salePropCommandList.add(salePropCommand);
			}else{

				String groupName = property.getGroupName();
				// 当groupName在groupNameMap中不存在时, 将groupName增加到 groupNameList和groupNameMap中;
				if (StringUtils.isNotBlank(groupName)){
					if (StringUtils.isBlank(groupNameMap.get(groupName))){
						groupNameMap.put(groupName, groupName);
						groupNameList.add(groupName);
					}
				}

				// 一般属性
				generalPropCommand = new DynamicPropertyCommand();
				itemPropertiesList = new ArrayList<ItemPropertiesCommand>();
				for (ItemProperties itemProperties : dbItemPropertiesList){
					if (itemProperties.getPropertyId().equals(property.getId())){
						if (itemProperties.getPropertyValueId() == null){
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}else{
							itemProperties.setPropertyValue(propertyValueMap.get(itemProperties.getPropertyValueId()));
							itemPropertiesList.add(itemPropertiesToCommand(itemProperties));
						}
					}
				}
				// 当商品属性只有一个属性值时, 就将其加到DynamicPropertyCommand对象中的itemProperties字段中
				if (itemPropertiesList != null && itemPropertiesList.size() == 1){
					generalPropCommand.setItemProperties(itemPropertiesList.get(0));
				}else{
					generalPropCommand.setItemPropertiesList(itemPropertiesList);
				}
				generalPropCommand.setProperty(property);
				generalPropCommandList.add(generalPropCommand);
			}
		}
		List<DynamicPropertyCommand> salePropList = salePropHandle(salePropCommandList, itemId);
		responseMap.put("groupNameList", groupNameList);
		responseMap.put("salePropCommandList", salePropList);
		responseMap.put("generalPropCommandList", generalPropCommandList);
		return responseMap;
	}

	/**
	 * 处理销售属性
	 * 
	 * @param salePropCommandList
	 * @return
	 */
	private List<DynamicPropertyCommand> salePropHandle(List<DynamicPropertyCommand> salePropCommandList,Long itemId){
		List<DynamicPropertyCommand> salePropList = new ArrayList<DynamicPropertyCommand>();
		List<ItemPropertiesCommand> itemPropCommandList = null;
		// key:extentionCode, value:库存数
		Map<String, Integer> extentionCodeMap = new HashMap<String, Integer>();
		// 通过ItemId获得sku集合
		List<Sku> skuList = sdkItemManager.findSkuByItemId(itemId);
		// 获取outId集合
		List<String> outIdList = new ArrayList<String>();
		for (Sku sku : skuList){
			String outId = sku.getOutid();
			if (StringUtils.isNotBlank(outId)){
				outIdList.add(outId);
			}
		}
		// 通过extentionCode集合获得商品的库存信息
		List<SkuInventory> skuInventoryList = sdkItemManager.findSkuInventoryByExtentionCodes(outIdList);
		for (SkuInventory skuInventory : skuInventoryList){
			extentionCodeMap.put(skuInventory.getExtentionCode(), skuInventory.getAvailableQty());
		}

		for (DynamicPropertyCommand saleProp : salePropCommandList){
			ItemPropertiesCommand itemProperties = saleProp.getItemProperties();
			// 单个属性值
			if (null != itemProperties){
				sku: for (Sku sku : skuList){
					String properties = sku.getProperties();
					String extentionCode = sku.getOutid();
					if (StringUtils.isNotBlank(properties)){
						// 条件:
						// 1, itemProperties.Id是否在sku.properties中
						// 2, extentionCode不为空, 且库存大于0
						properties = properties.substring(1, properties.length() - 1);
						if (properties.indexOf(",") != -1){
							String[] props = properties.split(",");
							for (String str : props){
								if (str.equals(String.valueOf(itemProperties.getItem_properties_id()))
										&& isExistSkuInventory(extentionCode, extentionCodeMap)){
									itemProperties.setIsEnabled(true);
									break sku;
								}
							}
						}else{
							if (properties.equals(String.valueOf(itemProperties.getItem_properties_id()))
									&& isExistSkuInventory(extentionCode, extentionCodeMap)){
								itemProperties.setIsEnabled(true);
								break sku;
							}
						}
					}
				}
				saleProp.setItemProperties(itemProperties);
			}else{
				// 多个属性值
				itemPropCommandList = new ArrayList<ItemPropertiesCommand>();
				for (ItemPropertiesCommand itemProp : saleProp.getItemPropertiesList()){
					sku: for (Sku sku : skuList){
						String properties = sku.getProperties();
						String extentionCode = sku.getOutid();
						if (StringUtils.isNotBlank(properties)){
							// itemProperties.Id是否在sku.properties中
							properties = properties.substring(1, properties.length() - 1);
							if (properties.indexOf(",") != -1){
								String[] props = properties.split(",");
								for (String str : props){
									if (str.equals(String.valueOf(itemProp.getItem_properties_id()))
											&& isExistSkuInventory(extentionCode, extentionCodeMap)){
										itemProp.setIsEnabled(true);
										break sku;
									}
								}
							}else{
								if (properties.equals(String.valueOf(itemProp.getItem_properties_id()))
										&& isExistSkuInventory(extentionCode, extentionCodeMap)){
									itemProp.setIsEnabled(true);
									break sku;
								}
							}
						}
					}
					itemPropCommandList.add(itemProp);
				}
				saleProp.setItemPropertiesList(itemPropCommandList);
			}
			salePropList.add(saleProp);
		}
		return salePropList;
	}

	/**
	 * @param itemProperties
	 * @return
	 */
	private ItemPropertiesCommand itemPropertiesToCommand(ItemProperties itemProperties){
		ItemPropertiesCommand itemPropertiesCommand = new ItemPropertiesCommand();
		itemPropertiesCommand.setIsEnabled(false);
		itemPropertiesCommand.setPropertyId(itemProperties.getPropertyId());
		itemPropertiesCommand.setPropertyValue(itemProperties.getPropertyValue());
		itemPropertiesCommand.setItem_properties_id(itemProperties.getId());
		itemPropertiesCommand.setItemId(itemProperties.getItemId());
		return itemPropertiesCommand;
	}

	/**
	 * 是否存在库存 :extentionCode不为空, 且库存大于0
	 * 
	 * @param extentionCode
	 * @param extentionCodeMap
	 * @return
	 */
	private Boolean isExistSkuInventory(String extentionCode,Map<String, Integer> extentionCodeMap){
		if (StringUtils.isNotBlank(extentionCode) && extentionCodeMap != null && extentionCodeMap.get(extentionCode) != null
				&& extentionCodeMap.get(extentionCode) > 0){
			return true;
		}
		return false;
	}
}
