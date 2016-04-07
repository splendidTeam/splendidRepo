package com.baozun.nebula.sdk.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.condition.AtomicAudience;
import com.baozun.nebula.calculateEngine.condition.AtomicCondition;
import com.baozun.nebula.calculateEngine.condition.AtomicScope;
import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.promotion.ConditionComplexCommand;
import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.command.promotion.SettingComplexCommand;
import com.baozun.nebula.command.promotion.SettingNormalCommand;
import com.baozun.nebula.command.promotion.SimpleExpressionCommand;
import com.baozun.nebula.command.queue.QsMemberCacheCommand;
import com.baozun.nebula.command.queue.QsOrderCacheCommand;
import com.baozun.nebula.command.queue.QsOrderTempCommand;
import com.baozun.nebula.command.queue.QsSalesOrderCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.promotion.PromotionAudiences;
import com.baozun.nebula.model.promotion.PromotionScope;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.UserDetails;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.QsQueueSaleOrderManager;
import com.baozun.nebula.security.crypto.PIIEncryptionModule;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.feilong.tools.jsonlib.JsonUtil;

import net.sf.json.JSONObject;

/***
 * 
 * @Title: QsSaleOrderManagerImpl.java
 * @Package com.baozun.store.manager.saleorder
 * @Description: 用来处理QS流程相关业务
 * @author zlh
 * @date 2016-1-18 下午7:48:07
 * @version V1.0
 */
@Transactional
public abstract class QsQueueSaleOrderManagerImpl implements QsQueueSaleOrderManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(QsQueueSaleOrderManagerImpl.class);

	@Autowired
	private  CacheManager cacheManager;
	
	/***
	 * 抽象方法
	 *        用来商城自己进行下单逻辑
	 *            QsSalesOrderCommand由商城自己转换为自己需要的command然后调用公用下单逻辑
	 * @param qsCommand
	 * @return
	 * @throws Exception
	 */
	protected abstract Map<String, Object> saveQsQueueOrder(QsSalesOrderCommand qsCommand) throws Exception;
	


	/***
	 * 由于QsSalesOrderCommand不一定能够满足业务需要
	 *        如果不满足需求  需要集成QsSalesOrderCommand   并注册复杂对象   用以反序列化
	 * @return
	 */
	protected abstract Map<String,Class<?>> setCustomerOrderAttribute();
	
	
	
	/***
	 * 由商城自己反序列下单对象
	 * @param classMap
	 * @param meesage
	 */
	protected abstract QsSalesOrderCommand reversalOrderCommand(Map<String,Class<?>> classMap,String message);
	
	
	/***
	 * QS流程 最核心功能是将下单数据序列化下单数据，由下单业务反序列化完成下单 随着业务的需要以及后续反黄牛或抵御BOT 需要进行一系列的操作
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> pushQsOrderToQueue(QsSalesOrderCommand salesOrderCommand)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			if (salesOrderCommand == null
					|| salesOrderCommand.getShoppingCartCommand() == null
					|| Validator.isNullOrEmpty(salesOrderCommand.getShoppingCartCommand()
							.getShoppingCartLineCommands())) {
				throw new BusinessException(Constants.SHOPCART_IS_NULL);
			}

			returnMap.put("isQS", true);
			ShoppingCartCommand shoppingCartCommand = salesOrderCommand.getShoppingCartCommand();
			/****
			 * 1)检查是否已在排队中
			 */
			Long memberId = salesOrderCommand.getMemberId();
			String extentionCode = shoppingCartCommand
					.getShoppingCartLineCommands().get(0).getExtentionCode();
			Boolean isInQueue = this.getCurrentQueueState(memberId,
					extentionCode);
			String memberKey = this.getUserQsQid(memberId, extentionCode);
			if (!isInQueue) {
				/**特殊情况由于shoppingCartCommand ShoppingCartLineCommand String 类型saleProperty 数据格式为  [223,444,2499]  影响反序列化  在不影响
				 * nebula 基础类情况下特殊处理
				 * **/
				List<ShoppingCartLineCommand> shoppingCartLineCommands = shoppingCartCommand.getShoppingCartLineCommands();
				for(ShoppingCartLineCommand lineCommand : shoppingCartLineCommands) {
					lineCommand.setSaleProperty(Validator.isNotNullOrEmpty(lineCommand.getSaleProperty())?
							lineCommand.getSaleProperty().replace("[","").replace("]",""):"");
				}
				/**设置当前状态为正在排队**/
				this.addShoppingQueueIdToUser(memberId,extentionCode);
				/** 加入队列 **/
				this.cacheManager.pushToListFooter(
						Constants.QS_ORDER_QUEUE,
						JsonUtil.format(salesOrderCommand));
				returnMap.put("Qid", memberKey);
			} else {
				returnMap.put("Qid", memberKey);
				/** 已在排队中 请等待 **/
			}
		}catch (Exception e) {
			LOGGER.error("QsSaleOrderManagerImpl pushQsOrderToQueue error!",e);
			throw e;
		}
		return returnMap;
	}

	/***
	 * qs订单
	 * 
	 * @param qsCommand
	 */
	public void saveQsOrder(QsSalesOrderCommand qsCommand,String[] tempData) {
		QsOrderCacheCommand queueResult = new QsOrderCacheCommand();
		Map<String, Object> returnMap = null;
		String description = "";
		/**用户Id**/
		Long memberId = null;
		/**用户购买的UPC**/
		String extentionCode = "";
		try {
			/****
			 * 调用公用下单逻辑
			 */
			memberId = qsCommand.getMemberId();
			extentionCode = qsCommand.getShoppingCartCommand().getShoppingCartLineCommands().get(0).getExtentionCode();
//			ShoppingCartCommand shoppingCartCommand = qsCommand.getShoppingCartCommand();
//			returnMap = saleOrderManager.saveOrder(shoppingCartCommand,
//					qsCommand.getSamSalesOrderCommand(),
//					qsCommand.getMemCombos()); 
			returnMap = this.saveQsQueueOrder(qsCommand);
			/**上次所需要的特殊返回值    由商城自己设置map实现**/
//			returnMap.put("memberId", memberId);
//			returnMap.put("paymentBank", qsCommand.getSalesOrderCommand().getPaymentStr());
			queueResult.setIsSuccess(true);
		} catch (BusinessException be) {
			LOGGER.error("save qs queue order business error!", be);
			description = String.valueOf(be.getErrorCode());
			if (Validator.isNotNullOrEmpty(be.getArgs())){
				queueResult.setArgs(be.getArgs());
			}
			description = String.valueOf(be.getErrorCode());
			queueResult.setIsSuccess(false);
			queueResult.setErrorMessage(description);
		} catch (Exception e) {
			LOGGER.error("save qs queue order error!", e);
			queueResult.setIsSuccess(false);
			queueResult.setErrorMessage(String.valueOf(Constants.CREATE_ORDER_FAILURE));
		} finally {
			//1,清楚 临时消息缓存  消息消费成功
			this.cacheManager.removeFromSet(Constants.QS_ORDER_TEMP_POOL, tempData);
			String key = this.getUserQsRid(memberId, extentionCode);
			//2 清除 rid 缓存
			if(Validator.isNotNullOrEmpty(key)) {
				this.cacheManager.remove(key);
			}
			if(Validator.isNullOrEmpty(memberId) || Validator.isNullOrEmpty(extentionCode)) {
				/**无用户及购物信息**/
				LOGGER.warn("Abnormal data!");
				queueResult.setIsSuccess(false);
				queueResult.setErrorMessage(String.valueOf(Constants.CREATE_ORDER_FAILURE));
				return;
			}
			queueResult.setData(returnMap);
			/***
			 * 清除排队缓存 设置排队结果
			 */
			//1,清除用户rid对象  使用户可以进行新一轮的尝试购买
			//2,处理缓存结果
			this.dealCacheOrderCommand(queueResult,memberId,extentionCode);
		}
	}

	
	
	/**
	 * 设置rid
	 * 
	 * @param memberId
	 * @param upc
	 * @return
	 */
	public String getUserQsRid(Long memberId, String upc) {
		String prefix = memberId + upc;
		String rid = Constants.QS_SALE_ORDER_RID;
		try {
			return EncryptUtil.getInstance().getEncryptor("AES").encrypt(rid + prefix);
		} catch (EncryptionException e1) {
			LOGGER.warn("[DECRYPTION_ERROR] [{}]", e1);
		}
		return null;
	}

	/**
	 * 设置qid
	 * 
	 * @param memberId
	 * @param upc
	 * @return
	 */
	public String getUserQsQid(Long memberId, String upc) {
		String prefix = memberId + upc;
		String qid = Constants.QS_SALE_ORDER_QID;
		
		try {
			return EncryptUtil.getInstance().getEncryptor("AES").encrypt(qid + prefix);
		} catch (EncryptionException e1) {
			LOGGER.warn("[DECRYPTION_ERROR] [{}]", e1);
		}
		return null;
	}
	
	/***
	 * 设置当前排队状态为正在排队
	 * @throws Exception
	 */
	private void addShoppingQueueIdToUser(Long memberId,String upc) throws Exception{
		try{
			String userRid = this.getUserQsRid(memberId,upc);
			//获取用户缓存对象
			String shoppingObj = this.cacheManager.getValue(userRid);
			if(Validator.isNotNullOrEmpty(shoppingObj)) {
				//用户再次点击   判断状态
				QsMemberCacheCommand cacheCommand = JsonUtil.toBean(shoppingObj, QsMemberCacheCommand.class);
				if(Validator.isNotNullOrEmpty(cacheCommand)) {
					cacheCommand.setStatus(QsMemberCacheCommand.IN_QUEUE_STATUS);
					this.cacheManager.setValue(userRid,JsonUtil.format(cacheCommand),
							Constants.EXPIRE_TIME);
				}
			}else {
				/**排队中**/
				QsMemberCacheCommand cacheCommand = new QsMemberCacheCommand();
				cacheCommand.setrId(userRid);
				cacheCommand.setMemberId(String.valueOf(memberId));
				cacheCommand.setUpc(upc);
				//初始化状态
				cacheCommand.setStatus(QsMemberCacheCommand.IN_QUEUE_STATUS);
				this.cacheManager.setValue(userRid,JsonUtil.format(cacheCommand)
						,Constants.EXPIRE_TIME);
			}
		}catch(Exception e) {
			LOGGER.error("addShoppingQueueIdToUser error!",e);
			throw e;
		}
	}

	/****
	 * 判断用户是否在排队状态 防止用户重复排队
	 * 
	 * @param memberId
	 * @param upc
	 * @return
	 */
	public Boolean getCurrentQueueState(Long memberId, String upc) {
		Boolean inQueue = false;
		String userRid = this.getUserQsRid(memberId, upc);
		String shoppingObj = this.cacheManager.getValue(userRid);
		if (Validator.isNotNullOrEmpty(shoppingObj)) {
			QsMemberCacheCommand cacheCommand = JsonUtil.toBean(shoppingObj,
					QsMemberCacheCommand.class);
			if (Validator.isNotNullOrEmpty(cacheCommand)
					&& QsMemberCacheCommand.IN_QUEUE_STATUS.equals(cacheCommand
							.getStatus())) {
				// 已经在队列中
				inQueue = true;
			}
		}
		return inQueue;
	}
	
	
	/***
	 * 
	 * @param queueResult
	 * @param returnMap
	 * @param memberId
	 * @param extentionCode
	 */
	private void dealCacheOrderCommand(QsOrderCacheCommand queueResult,Long memberId,
			String extentionCode) {
		if(Validator.isNotNullOrEmpty(extentionCode)) {
			//设置排队结果
			String qId = this.getUserQsQid(memberId, extentionCode);
			/**多线程处理 存在并发问题  先判断是否该用户同一upc排到了2次   如果排到两次 判断之前是否排队成功  如果成功  则该条记录为不能重复购买  暂时还提示排队成功   防止成功结果对象被失败覆盖**/
//			Object qIdObj = this.cacheManager.getValue(qId);
//			if(Validator.isNotNullOrEmpty(qIdObj)) {
//				/**判断没有清除的结果是否是  已经排队成功**/
//				QsOrderCacheCommand cacheCommand = JsonUtil.toBean(qIdObj, QsOrderCacheCommand.class);
//				if(Validator.isNotNullOrEmpty(cacheCommand)) {
//					/**是否产生订单*/
//					Boolean isSuccess = cacheCommand.getIsSuccess();
//					if(isSuccess) {
//						/**不在覆盖成功结果    保证用户获取到成功结果  虽然多次进入队列   基本是bot**/
//						return;
//					}
//				}
//			}
			/**最新排队结果**/
			queueResult.setQueueId(qId);
			this.cacheManager.setValue(qId,JsonUtil.format(queueResult) ,Constants.EXPIRE_TIME);	
		}
	}
	
	/***
	 * 获取用户是否30分钟排队未响应
	 * @param qId
	 * @param memberId
	 * @return
	 */
	public Boolean dealQueueIsTimeOut(String qId,Long memberId) {
		try{
			if(Validator.isNotNullOrEmpty(memberId)&&Validator.isNotNullOrEmpty(qId)) {
				String cacheKey = EncryptUtil.getInstance().getEncryptor("AES").decrypt(qId);
				String prefix = Constants.QS_SALE_ORDER_QID + memberId;
				String upc = cacheKey.replace(prefix, "");
				/**重新获取upc  判断rid对象是否失效**/
				if(Validator.isNotNullOrEmpty(upc)) {
					String rid = this.getUserQsRid(memberId, upc);
					Object ridObj = this.cacheManager.getValue(rid);
					if(Validator.isNotNullOrEmpty(ridObj)) {
						return false;
					}else {
						/**超时**/
						return true;
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("dealQueueIsTimeOut error!",e);
		    return true;
		}
		return true;
	}

	
	/***
	 * 
	 * 注册nebula 中基础的订单command用来进行反序列化
	 *        并提供抽象方法由商城自己注册新增的属性
	 * @return
	 */
	private Map<String,Class<?>> registerOrderClass() {
		Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();
		classMap = this.setCustomerOrderAttribute();
		classMap.put("shoppingCartCommand",ShoppingCartCommand.class);
		classMap.put("salesOrderCommand", SalesOrderCommand.class);
		classMap.put("logisticsList", LogisticsCommand.class);
		classMap.put("shoppingCartLineCommands",ShoppingCartLineCommand.class);
		classMap.put("couponCodeCommands",PromotionCouponCodeCommand.class);
		classMap.put("userDetails",UserDetails.class);
		classMap.put("cartPromotionBriefList", PromotionBrief.class);
		classMap.put("couponCodeOnLine", PromotionCouponCodeCommand.class);
		classMap.put("PromotionBrief", PromotionBrief.class);
		classMap.put("summaryShopCartList", ShopCartCommandByShop.class);
		classMap.put("conditionNormal", ConditionNormalCommand.class);
		classMap.put("settingNormal", SettingNormalCommand.class);
		classMap.put("conditionComplexList", ConditionComplexCommand.class);
		classMap.put("settingComplexList", SettingComplexCommand.class);
		classMap.put("audiences", PromotionAudiences.class);
		classMap.put("scope", PromotionScope.class);
		classMap.put("skuPropertys", SkuProperty.class);
		classMap.put("itemProperties", ItemProperties.class);
		classMap.put("details", PromotionSettingDetail.class);
		classMap.put("affectSKUDiscountAMTList", PromotionSKUDiscAMTBySetting.class);
		classMap.put("promotionList",PromotionCommand.class);
		classMap.put("atomicAudienceList", AtomicAudience.class);
		classMap.put("atomicScopeList",AtomicScope.class);
		classMap.put("atomicConditionList",AtomicCondition.class);
		classMap.put("atomicComplexConditionList",AtomicCondition.class);
		classMap.put("atomicSettingList",AtomicSetting.class);
		classMap.put("atomicComplexSettingList",AtomicSetting.class);
		classMap.put("expressionList", SimpleExpressionCommand.class);
		return classMap;
	}

	/****
	 * 反序列化订单对象
	 * @param message
	 * @return
	 */
	public Map<String,Class<?>> analyisOrderCommand(String message){
		Map<String,Class<?>> classMap = this.registerOrderClass();
		return classMap;
	}
	
	
	/***
	 * 反序列化对象
	 * @param classMap
	 * @param message
	 * @return
	 */
	public QsSalesOrderCommand reversalMessageToOrderCommand(Map<String,Class<?>> classMap,String message) {
		QsSalesOrderCommand orderData = this.reversalOrderCommand(classMap, message);
		return orderData;
	}
	
	/***
	 * 反序列化  下单对象
	 *         由于nebula中orderCommand复杂对象较多  并且包含复杂map因此要进行两次
	 *         转换
	 * @param message
	 * @return
	 */
	public QsSalesOrderCommand reSerializbleOrderCommand(String message) {
		QsSalesOrderCommand orderData = null;
		Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();
		classMap = this.analyisOrderCommand(message);
		orderData = this.reversalMessageToOrderCommand(classMap, message);
		
		
		
		/***
		 * 对map对象单独解析
		 */
		Map<Long, ShoppingCartCommand>  map = orderData.getShoppingCartCommand().getShoppingCartByShopIdMap();
		Map<String,Class<?>> classMap2 = new HashMap<String,Class<?>>();
		classMap.put(".*",ShoppingCartCommand.class);
		Map<Long, ShoppingCartCommand> map2 = new HashMap<Long, ShoppingCartCommand>();
		map = (Map<Long, ShoppingCartCommand>) JSONObject.toBean(JSONObject.fromObject(map), HashMap.class, classMap);
		orderData.getShoppingCartCommand().setShoppingCartByShopIdMap(map);
		if(Validator.isNotNullOrEmpty(orderData.getShoppingCartCommand())) {
			/**特殊情况由于shoppingCartCommand ShoppingCartLineCommand String 类型saleProperty 数据格式为  [223,444,2499]  影响反序列化  在不影响
			 * nebula 基础类情况下特殊处理
			 * **/
			List<ShoppingCartLineCommand> shoppingCartLineCommands = orderData.getShoppingCartCommand().getShoppingCartLineCommands();
			for(ShoppingCartLineCommand lineCommand : shoppingCartLineCommands) {
				lineCommand.setSaleProperty(Validator.isNotNullOrEmpty(lineCommand.getSaleProperty())?
						"["+lineCommand.getSaleProperty()+"]":"");
			}
		}
		Iterator it = map.keySet().iterator();
		map = orderData.getShoppingCartCommand().getShoppingCartByShopIdMap();
		while(it.hasNext()) {
			String next = (String) it.next();
			ShoppingCartCommand commond = map.get(next);
			/**特殊情况由于shoppingCartCommand ShoppingCartLineCommand String 类型saleProperty 数据格式为  [223,444,2499]  影响反序列化  在不影响
			 * nebula 基础类情况下特殊处理
			 * **/
			List<ShoppingCartLineCommand> shoppingCartLineCommands = commond.getShoppingCartLineCommands();
			for(ShoppingCartLineCommand lineCommand : shoppingCartLineCommands) {
				lineCommand.setSaleProperty(Validator.isNotNullOrEmpty(lineCommand.getSaleProperty())?
						"["+lineCommand.getSaleProperty()+"]":"");
			}
			map2.put(Long.parseLong(next),commond);
		}
		orderData.getShoppingCartCommand().setShoppingCartByShopIdMap(map2);
		return orderData;
	}
	
	/***
	 * 补偿机制   尽量防止消息丢失
	 * @param message 信息
	 */
	public String[] putOrderDataToTempPool(String message) {
		QsOrderTempCommand tempCommand = new QsOrderTempCommand();
		tempCommand.setTime(new Date());
		tempCommand.setMessage(message);
		String tempDatas[] = {JsonUtil.format(tempCommand)};
		this.cacheManager.addSet(Constants.QS_ORDER_TEMP_POOL,tempDatas);
		return tempDatas;
	}
}
