/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.manager.product;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPresalseInfoCommand;
import com.baozun.nebula.command.ItemPresalseSkuInfoCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemPresaleInfoDao;
import com.baozun.nebula.dao.product.ItemPresellItemPriceInfoDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.PresellItem;
import com.baozun.nebula.model.product.PresellItemPriceInfo;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.utils.Validator;

/**
 * @author jinbao.ji
 * @date 2016年2月3日 上午9:59:03
 */
@Transactional
@Service("itemPresaleInfoManager")
public class ItemPresaleInfoManagerImpl implements ItemPresaleInfoManager{

	private static final Logger			log					= LoggerFactory.getLogger(ItemPresaleInfoManagerImpl.class);

	public static final String			FULLMONEY_SUFFIX	= "FULLMONEY_SUFFIX";

	public static final String			EARNEST_SUFFIX		= "EARNEST_SUFFIX";

	public static final String			BALANCE_SUFFIX		= "BALANCE_SUFFIX";

	public static final String			SORT_SUFFIX			= "SORT_SUFFIX";

	@Autowired
	private ItemDao						itemDao;

	@Autowired
	private SkuDao						skuDao;

	@Autowired
	private ItemPresaleInfoDao			itemPresaleInfoDao;

	@Autowired
	private ItemPresellItemPriceInfoDao	itemPresellItemPriceInfoDao;

	@Autowired
	SdkSkuInventoryDao					sdkSkuInventoryDao;

	@Override
	public ItemPresalseInfoCommand getItemPresalseInfoCommand(Long itemId){
		ItemPresalseInfoCommand itemPresalseInfoCommand = new ItemPresalseInfoCommand();
		ItemCommand itemCommand = itemDao.findItemCommandById(itemId);
		List<Map<String, String>> propertvaluenameMapList = itemPresaleInfoDao.findpropertvaluenameByitempropertyIds(itemId);
		PresellItem presellItem = itemPresaleInfoDao.findPresaleInfoByitemId(itemId);
		List<PresellItemPriceInfo> presellItemPriceInfoList = itemPresellItemPriceInfoDao.findItemPresalseSkuInfoByItemId(itemId);
		List<SkuCommand> skuCommandList = skuDao.findEffectiveSkuInvByItemId(itemId);
		Map<String, BigDecimal> presellItemPriceInfoMap = findpresellItemPriceInfoMap(presellItemPriceInfoList);
		Map<String, String> propertnameMap = findpropertnameMap(propertvaluenameMapList);
		List<ItemPresalseSkuInfoCommand> itemPresalseSkuInfoCommandList = new ArrayList<ItemPresalseSkuInfoCommand>();
		for (SkuCommand skuCommand : skuCommandList){
			ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand = mergeProperty(
					skuCommand,
					presellItemPriceInfoMap,
					propertnameMap,
					itemId);
			itemPresalseSkuInfoCommandList.add(itemPresalseSkuInfoCommand);
		}
		itemPresalseInfoCommand.setItemId(itemId);
		copyToItemPresalseInfoCommand(itemPresalseInfoCommand, itemCommand, presellItem);
		itemPresalseInfoCommand.setItemPresalseSkuInfoCommandList(itemPresalseSkuInfoCommandList);
		return itemPresalseInfoCommand;
	}

	/**
	 * 将Map集合转换成一个map
	 * 
	 * @param propertvaluenameMapList
	 *            其中元素(map(key:itempropertyId,value:propertyvalueName))
	 * @return
	 */
	private Map<String, String> findpropertnameMap(List<Map<String, String>> propertvaluenameMapList){
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < propertvaluenameMapList.size(); i++){
			map.put(propertvaluenameMapList.get(i).get("itempropertyId") + "", propertvaluenameMapList.get(i).get("propertyvalueName") + "");
			map.put(propertvaluenameMapList.get(i).get("itempropertyId") + SORT_SUFFIX, i + "");
		}
		return map;
	}

	/**
	 * 将集合转换成一个map(key:ExtentionCode+后缀名,value:具体金额)
	 * 
	 * @param presellItemPriceInfoList
	 * @return
	 */
	private Map<String, BigDecimal> findpresellItemPriceInfoMap(List<PresellItemPriceInfo> presellItemPriceInfoList){
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		for (PresellItemPriceInfo presellItemPriceInfo : presellItemPriceInfoList){
			map.put(presellItemPriceInfo.getExtentionCode() + FULLMONEY_SUFFIX, presellItemPriceInfo.getFullmoney());
			map.put(presellItemPriceInfo.getExtentionCode() + EARNEST_SUFFIX, presellItemPriceInfo.getEarnest());
			map.put(presellItemPriceInfo.getExtentionCode() + BALANCE_SUFFIX, presellItemPriceInfo.getBalance());
		}
		return map;
	}

	/**
	 * 复制信息到ItemPresalseInfoCommand
	 * 
	 * @param itemPresalseInfoCommand
	 * @param itemCommand
	 * @param presellItem
	 */
	private void copyToItemPresalseInfoCommand(
			ItemPresalseInfoCommand itemPresalseInfoCommand,
			ItemCommand itemCommand,
			PresellItem presellItem){
		itemPresalseInfoCommand.setItemCode(itemCommand.getCode());
		itemPresalseInfoCommand.setItemName(itemCommand.getTitle());
		if (presellItem != null){
			itemPresalseInfoCommand.setId(presellItem.getId());
			itemPresalseInfoCommand.setActivityName(presellItem.getActivityName());
			itemPresalseInfoCommand.setPaymentMethod(presellItem.getPaymentMethod());
			itemPresalseInfoCommand.setDeliveryTime(presellItem.getDeliveryTime());
			itemPresalseInfoCommand.setActivityStartTime(presellItem.getActivityStartTime());
			itemPresalseInfoCommand.setActivityEndTime(presellItem.getActivityEndTime());
			itemPresalseInfoCommand.setEndtime(presellItem.getEndtime());
			itemPresalseInfoCommand.setLifecycle(presellItem.getLifecycle());
		}

	}

	/**
	 * 将sku信息与属性名称信息和预售价格信息合并到ItemPresalseSkuInfoCommand
	 * 
	 * @param skuCommand
	 * @param presellItemPriceInfoMap
	 * @param propertnameMap
	 * @param itemId
	 * @return
	 */
	private ItemPresalseSkuInfoCommand mergeProperty(
			SkuCommand skuCommand,
			Map<String, BigDecimal> presellItemPriceInfoMap,
			Map<String, String> propertnameMap,
			Long itemId){
		ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand = new ItemPresalseSkuInfoCommand();
		itemPresalseSkuInfoCommand.setItemId(itemId);
		itemPresalseSkuInfoCommand.setExtentionCode(skuCommand.getExtentionCode());
		itemPresalseSkuInfoCommand.setSaleProperty(findfindpropertnameByProperties(propertnameMap, skuCommand));
		itemPresalseSkuInfoCommand.setListPrice(skuCommand.getListPrice());
		itemPresalseSkuInfoCommand.setSalePrice(skuCommand.getSalePrice());
		itemPresalseSkuInfoCommand.setCurrentInventory(skuCommand.getAvailableQty());
		if (!presellItemPriceInfoMap.isEmpty()){
			itemPresalseSkuInfoCommand.setFullMoney(presellItemPriceInfoMap.get(skuCommand.getExtentionCode() + FULLMONEY_SUFFIX));
			itemPresalseSkuInfoCommand.setEarnest(presellItemPriceInfoMap.get(skuCommand.getExtentionCode() + EARNEST_SUFFIX));
			itemPresalseSkuInfoCommand.setBalance(presellItemPriceInfoMap.get(skuCommand.getExtentionCode() + BALANCE_SUFFIX));
		}

		return itemPresalseSkuInfoCommand;

	}

	/**
	 * 获取销售属性名称
	 * 
	 * @param propertnameMap
	 * @param skuCommand
	 * @return
	 */
	private String findfindpropertnameByProperties(Map<String, String> propertnameMap,SkuCommand skuCommand){
		String itempropertyIds = skuCommand.getProperties();
		List<Long> itempropertyIdList = JSON.parseArray(itempropertyIds, Long.class);
		String[] propertnameArray = new String[propertnameMap.size()];
		for (Long itempropertyId : itempropertyIdList){
			String name = propertnameMap.get(itempropertyId + "");
			int index = Integer.parseInt(propertnameMap.get(itempropertyId + SORT_SUFFIX) + "");
			propertnameArray[index] = name;
		}
		if (propertnameArray.length == 0){
			return "";
		}
		StringBuffer sbf = new StringBuffer();
		for (String propertname : propertnameArray){
			if (propertname == null || propertname.equals("null")){
				continue;
			}
			sbf.append(propertname).append("+");
		}
		return sbf.substring(0, sbf.length() - 1);

	}

	@Override
	public void updateOrSaveItemPresalseInfo(ItemPresalseInfoCommand itemPresalseInfoCommand){
		itemPresaleInfoDao.save(copyPropertytopresellItem(itemPresalseInfoCommand));// 保存或更新
		List<ItemPresalseSkuInfoCommand> ItemPresalseSkuInfoCommandList = itemPresalseInfoCommand.getItemPresalseSkuInfoCommandList();
		for (ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand : ItemPresalseSkuInfoCommandList){
			itemPresellItemPriceInfoDao.save(copyPropertytoPresellItemPriceInfo(itemPresalseSkuInfoCommand, itemPresalseInfoCommand));// 保存或更新
			updateInventory(itemPresalseSkuInfoCommand);// 更新商品库存
		}

	}

	/**
	 * 属性复制
	 * 
	 * @param presellItem
	 * @param itemPresalseInfoCommand
	 * @return
	 */
	private PresellItem copyPropertytopresellItem(ItemPresalseInfoCommand itemPresalseInfoCommand){
		PresellItem presellItem = null;
		Long presellItemId = itemPresalseInfoCommand.getId();
		if (presellItemId != null){
			presellItem = itemPresaleInfoDao.getByPrimaryKey(presellItemId);
		}
		if (Validator.isNullOrEmpty(presellItem)){
			presellItem = new PresellItem();
			presellItem.setCreateTime(new Date());
			presellItem.setItemCode(itemPresalseInfoCommand.getItemCode());
			presellItem.setItemId(itemPresalseInfoCommand.getItemId());
		}
		if (itemPresalseInfoCommand.PAYMENT_FULLMONEY.equals(itemPresalseInfoCommand.getPaymentMethod())){// 全款
			presellItem.setEndtime(null);
		}else{
			presellItem.setEndtime(formatetimestr(itemPresalseInfoCommand.getEndtimeStr()));
		}
		presellItem.setActivityEndTime(formatetimestr(itemPresalseInfoCommand.getActivityEndTimeStr()));
		presellItem.setActivityName(itemPresalseInfoCommand.getActivityName());
		presellItem.setModifyTime(new Date());
		presellItem.setDeliveryTime(formatetimestr(itemPresalseInfoCommand.getDeliveryTimeStr()));
		presellItem.setPaymentMethod(itemPresalseInfoCommand.getPaymentMethod());
		presellItem.setActivityStartTime(formatetimestr(itemPresalseInfoCommand.getActivityStartTimeStr()));
		presellItem.setActivityEndTime(formatetimestr(itemPresalseInfoCommand.getActivityEndTimeStr()));
		presellItem.setLifecycle(itemPresalseInfoCommand.getLifecycle());
		return presellItem;
	}

	/**
	 * 属性复制
	 * 
	 * @param itemPresalseSkuInfoCommand
	 * @param itemPresalseInfoCommand
	 * @return
	 */
	private PresellItemPriceInfo copyPropertytoPresellItemPriceInfo(
			ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand,
			ItemPresalseInfoCommand itemPresalseInfoCommand){
		PresellItemPriceInfo presellItemPriceInfo = itemPresellItemPriceInfoDao.finditemPresellItemPriceInfoByitemIdAndExtentionCode(
				itemPresalseInfoCommand.getItemId(),
				itemPresalseSkuInfoCommand.getExtentionCode());
		if (Validator.isNullOrEmpty(presellItemPriceInfo)){
			presellItemPriceInfo = new PresellItemPriceInfo();
			presellItemPriceInfo.setCreateTime(new Date());
			presellItemPriceInfo.setItemCode(itemPresalseInfoCommand.getItemCode());
			presellItemPriceInfo.setItemId(itemPresalseInfoCommand.getItemId());
			presellItemPriceInfo.setExtentionCode(itemPresalseSkuInfoCommand.getExtentionCode());
		}
		if (PresellItem.PAYMENTMETHOD_FULL.equals(itemPresalseInfoCommand.getPaymentMethod())){
			presellItemPriceInfo.setEarnest(null);
			presellItemPriceInfo.setBalance(null);
			presellItemPriceInfo.setFullmoney(itemPresalseSkuInfoCommand.getFullMoney());

		}else{
			presellItemPriceInfo.setEarnest(itemPresalseSkuInfoCommand.getEarnest());
			presellItemPriceInfo.setBalance(itemPresalseSkuInfoCommand.getBalance());
			presellItemPriceInfo.setFullmoney(null);
		}
		presellItemPriceInfo.setModifyTime(new Date());
		return presellItemPriceInfo;
	}

	private Date formatetimestr(String timestr){
		Date date = null;
		try{
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timestr);
		}catch (ParseException e){
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 更新商品库存
	 * 
	 * @param itemPresalseSkuInfoCommand
	 * @param itemPresalseInfoCommand
	 */
	private Integer updateInventory(ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand){
		Integer inventoryIncrement = itemPresalseSkuInfoCommand.getInventoryIncrement();
		Integer updatestatus = null;
		if (itemPresalseSkuInfoCommand.getInventoryIncrement() != null){
			if (inventoryIncrement > 0){
				updatestatus = sdkSkuInventoryDao.addSkuInventory(itemPresalseSkuInfoCommand.getExtentionCode(), inventoryIncrement);
			}else if (inventoryIncrement < 0){
				updatestatus = sdkSkuInventoryDao.liquidateSkuInventory(
						itemPresalseSkuInfoCommand.getExtentionCode(),
						0 - inventoryIncrement);
			}
		}
		return updatestatus;
	}

	@Override
	public boolean validateitemPresalseInfoCommand(ItemPresalseInfoCommand itemPresalseInfoCommand){
		Long itemId = itemPresalseInfoCommand.getItemId();
		Integer paymentMethod = itemPresalseInfoCommand.getPaymentMethod();
		Integer lifecycle = itemPresalseInfoCommand.getLifecycle();
		String itemCode = itemPresalseInfoCommand.getItemCode();
		String deliveryTimeStr = itemPresalseInfoCommand.getDeliveryTimeStr();
		String activityStartTimeStr = itemPresalseInfoCommand.getActivityStartTimeStr();
		String activityEndTimeStr = itemPresalseInfoCommand.getActivityEndTimeStr();
		String endtimeStr = itemPresalseInfoCommand.getEndtimeStr();
		String activityName = itemPresalseInfoCommand.getActivityName();
		Item item = itemDao.getByPrimaryKey(itemId);
		if (item == null || !itemCode.equals(item.getCode())){
			return false;
		}
		boolean boo1 = validatenull(
				itemId,
				paymentMethod,
				lifecycle,
				endtimeStr,
				itemCode,
				deliveryTimeStr,
				activityStartTimeStr,
				activityEndTimeStr,
				activityName);
		if (boo1 == false){
			return false;
		}else{
			boolean boo2 = validateeffect(
					itemId,
					paymentMethod,
					lifecycle,
					itemCode,
					deliveryTimeStr,
					activityStartTimeStr,
					activityEndTimeStr,
					endtimeStr,
					activityName);
			if (boo2 == false){
				return false;
			}

		}
		boolean boo3 = validateItemPresalseSkuInfoCommandList(
				itemPresalseInfoCommand.getItemPresalseSkuInfoCommandList(),
				itemId,
				itemPresalseInfoCommand.getPaymentMethod());
		if (boo3 == false){
			return false;
		}
		return true;
	}

	/**
	 * 验证预售商品SKU价格库存信息有效性
	 * 
	 * @param itemPresalseSkuInfoCommandList
	 * @param itemId
	 * @param paymentMethod
	 * @return
	 */
	private boolean validateItemPresalseSkuInfoCommandList(List<ItemPresalseSkuInfoCommand> itemPresalseSkuInfoCommandList, Long itemId, Integer paymentMethod){
		List<SkuCommand> skuCommandList = skuDao.findEffectiveSkuInvByItemId(itemId);
		Map<String,BigDecimal> map=new HashMap<String, BigDecimal>();
		for (SkuCommand skuCommand : skuCommandList){
			map.put(skuCommand.getExtentionCode(), skuCommand.getSalePrice());
		}
		for (ItemPresalseSkuInfoCommand itemPresalseSkuInfoCommand : itemPresalseSkuInfoCommandList){
			if(Validator.isNullOrEmpty(itemPresalseSkuInfoCommand.getExtentionCode())){
				return false;
			}
			BigDecimal salePrice = map.get(itemPresalseSkuInfoCommand.getExtentionCode());
			if(salePrice==null){
				return false;
			}else{
				if(PresellItem.PAYMENTMETHOD_FULL==paymentMethod){//全款
					BigDecimal fullMoney = itemPresalseSkuInfoCommand.getFullMoney();
					if(fullMoney.compareTo(salePrice)!=0){
						return false;
					}
				}else{
					BigDecimal earnest=itemPresalseSkuInfoCommand.getEarnest();
					BigDecimal balance=itemPresalseSkuInfoCommand.getBalance();
					if((earnest.add(balance)).compareTo(salePrice)!=0){
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 验证数据逻辑的有效性
	 * 
	 * @param itemId
	 * @param paymentMethod
	 * @param lifecycle
	 * @param itemCode
	 * @param deliveryTimeStr
	 * @param activityStartTimeStr
	 * @param activityEndTimeStr
	 * @param endtimeStr
	 * @param activityName
	 * @return
	 */
	private boolean validateeffect(
			Long itemId,
			Integer paymentMethod,
			Integer lifecycle,
			String itemCode,
			String deliveryTimeStr,
			String activityStartTimeStr,
			String activityEndTimeStr,
			String endtimeStr,
			String activityName){
		if (!paymentMethod.equals(ItemPresalseInfoCommand.PAYMENT_FULLMONEY)
				&& !paymentMethod.equals(ItemPresalseInfoCommand.EARNEST_AND_BALANCE)){
			return false;
		}
		if (!lifecycle.equals(PresellItem.STATUS_ENABLE) && !lifecycle.equals(PresellItem.LIFECYCLE_UNACTIVE)){
			return false;
		}
		if (validatetimestr(deliveryTimeStr, activityStartTimeStr, activityEndTimeStr, endtimeStr,paymentMethod)==false){
			return false;
		}

		return true;
	}

	/**
	 * 验证非空
	 * 
	 * @param itemId
	 * @param paymentMethod
	 * @param lifecycle
	 * @param itemCode
	 * @param deliveryTimeStr
	 * @param activityStartTimeStr
	 * @param activityEndTimeStr
	 * @param endtimeStr
	 * @param activityName
	 * @return true:验证通过
	 */
	private boolean validatenull(Long itemId,Integer paymentMethod,Integer lifecycle,String endtimeStr,String...attrs){
		if (Validator.isNullOrEmpty(itemId) || Validator.isNullOrEmpty(paymentMethod) || Validator.isNullOrEmpty(lifecycle)){
			return false;
		}
		if (paymentMethod.equals(PresellItem.PAYMENTMETHOD_EARNEST_BALANCE) && Validator.isNullOrEmpty(endtimeStr)){// 定金加尾款的方式则尾款截止日期不能为空
			return false;
		}
		for (String attr : attrs){
			if (attr == null || attr.trim().equals("")){
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证时间的合法性
	 * 
	 * @param endtimeStr
	 * @param activityEndTimeStr
	 * @param activityStartTimeStr
	 * @param deliveryTimeStr
	 * @param paymentMethod 
	 * @param timestr
	 * @return
	 */
	private boolean validatetimestr(String deliveryTimeStr,String activityStartTimeStr,String activityEndTimeStr,String endtimeStr, Integer paymentMethod){
		try{
			Date activityStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(activityStartTimeStr);// 活动开始时间
			Date activityEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(activityEndTimeStr);// 活动结束时间
			Date deliveryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(deliveryTimeStr);// 预计发货时间
			Date endtime=null;
			boolean boo=true;
			if(paymentMethod.equals(ItemPresalseInfoCommand.EARNEST_AND_BALANCE)){//定金和尾款
				endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endtimeStr);// 尾款截止时间
				boo=endtime.getTime() > activityStartTime.getTime();
			}
			if (!(activityEndTime.getTime() > activityStartTime.getTime() && boo && deliveryTime
					.getTime() > activityEndTime.getTime())){
				return false;
			}
		}catch (ParseException e){
			return false;
		}
		return true;
	}
}
