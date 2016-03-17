/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.wormhole.scm.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.wormhole.constants.ProductPriceV5Constants;
import com.baozun.nebula.wormhole.mq.entity.ProductPriceV5;
import com.baozun.nebula.wormhole.mq.entity.sku.SkuInfoV5;
import com.baozun.nebula.wormhole.scm.handler.SyncItemHandler;

/**
 * 同步商品Manager的实现类
 * 
 * @author chenguang.zhou
 * @date 2014年5月16日 下午4:00:40
 */
@Service("scmItemManager")
@Transactional
public class ItemManagerImpl implements ItemManager {

	private static Logger	log				= LoggerFactory.getLogger(ItemManagerImpl.class);

	@Autowired
	private SdkItemManager	sdkItemManager;

	@Autowired
	private SdkSkuManager	sdkSkuManager;

	@Autowired
	private SdkMsgManager	sdkMsgManager;

	@Autowired(required = false)
	private SyncItemHandler	syncItemHandler;

	/****** 商城商品编码用的字段  ******/
	@Value("#{meta['item.code.field']}")
	private String			itemCodeField	= "";


	@Override
	public void syncBaseInfo(List<SkuInfoV5> skuInfoV5List, MsgReceiveContent msgReceive) {
		if (null != syncItemHandler && skuInfoV5List.size() > 0) {
			syncItemHandler.syncBaseInfo(skuInfoV5List);
			// 更新该MsgReceiveContent为已处理
			List<Long> ids = new ArrayList<Long>();
			ids.add(msgReceive.getId());
			sdkMsgManager.updateMsgRecContIsProByIds(ids);
			//msgReceive.setIsProccessed(true);
			//sdkMsgManager.saveMsgReceiveContent(msgReceive);
		}
	}
	
	@Override
	public void syncItemPrice(List<ProductPriceV5> pdpriceV5List, MsgReceiveContent msgReceive) {
		
		/****** 扩展点 ******/
		if (null != syncItemHandler) {
			pdpriceV5List = syncItemHandler.syncItemPrice(pdpriceV5List);
		}
		
		List<String> itemCodeList = new ArrayList<String>();
		for (ProductPriceV5 pdPrice : pdpriceV5List) {
			if ("supplierSkuCode".equalsIgnoreCase(itemCodeField)) {
				itemCodeList.add(pdPrice.getSupplierSkuCode());
			} else if ("jmCode".equalsIgnoreCase(itemCodeField)) {
				itemCodeList.add(pdPrice.getJmCode());
			} else {
				log.error("item Code Field not exist, item code field is {}.", itemCodeField);
			}
		}
		// 商品信息
		List<ItemCommand> itemCommandList = sdkItemManager.findItemCommandByCodes(itemCodeList);

		Map<String, ItemCommand> itemCommandMap = new HashMap<String, ItemCommand>();
		for (ItemCommand itemCommand : itemCommandList) {
			itemCommandMap.put(itemCommand.getCode(), itemCommand);
		}

		for (ProductPriceV5 pdPrice : pdpriceV5List) {
			Integer priceLevel = pdPrice.getPriceLevel();
			// 商品编码
			String itemCode = "";
			// 在不同的商城中商品编码的字段不一样.
			if ("supplierSkuCode".equalsIgnoreCase(itemCodeField)) {
				itemCode = pdPrice.getSupplierSkuCode();
			} else if ("jmCode".equalsIgnoreCase(itemCodeField)) {
				itemCode = pdPrice.getJmCode();
			} else {
				log.error("item Code Field not exist, item code field is {}.", itemCodeField);
				break;
			}
			
			BigDecimal salesPrice = pdPrice.getSalesPrice();
			// 保留两位小数
			if(Validator.isNotNullOrEmpty(salesPrice)){
				salesPrice = salesPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal listPrice = pdPrice.getListPrice();
			// 保留两位小数
			if(Validator.isNotNullOrEmpty(listPrice)){
				listPrice = listPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			if (ProductPriceV5Constants.PRICE_LEVEL_ITEM.equals(priceLevel)) {
				ItemCommand itemCommand = itemCommandMap.get(itemCode);
				if(null == itemCommand){
					log.error("item code {} not exist.", itemCode);
					continue;
				}
				/****** 同步item级别的价格 ******/
				syncItemPrice(salesPrice, listPrice, itemCode);
			} else if (ProductPriceV5Constants.PRICE_LEVEL_SKU.equals(priceLevel)) {
				String extentionCode = pdPrice.getExtentionCode();
				/****** 同步sku级别的价格 ******/
				syncSkuPrice(salesPrice, listPrice, extentionCode, itemCommandMap);
			} else {
				/****** 不存在同步价格的级别 ******/
				log.error("synchronization product price level not have {}", priceLevel);
			}
		}
		/****** 更新该MsgReceiveContent为已处理 ******/
		List<Long> ids = new ArrayList<Long>();
		ids.add(msgReceive.getId());
		sdkMsgManager.updateMsgRecContIsProByIds(ids);
//		msgReceive.setIsProccessed(true);
//		sdkMsgManager.saveMsgReceiveContent(msgReceive);
	}

	/**
	 * 同步商品价格(item级别)
	 * @param salesPrice
	 * @param listPrice
	 * @param itemCode
	 */
	private void syncItemPrice(BigDecimal salesPrice, BigDecimal listPrice, String itemCode) {
		sdkItemManager.syncItemPriceByCode(salesPrice, listPrice, itemCode);
		// 修改item对应的sku价格
		sdkSkuManager.updateSkuPriceByItemCode(salesPrice, listPrice, itemCode);
	}

	/**
	 * 同步商品价格(sku级别)
	 * @param salesPrice
	 * @param listPrice
	 * @param extentionCode
	 */
	private void syncSkuPrice(BigDecimal salesPrice, BigDecimal listPrice/*, String itemCode*/, String extentionCode,
			Map<String, ItemCommand> itemCommandMap) {
		
		/****** 判断是否要修改item的销售价格和吊牌价 ******/
		/****** 当item的价格(销售价格和吊牌价), 在对应的sku的价格之间, 则不用修改, 否则修改成item对应的sku中价格的最大的价格 ******/
		Sku syncSku = sdkSkuManager.findSkuByExtentionCode(extentionCode);
		if(null == syncSku){
			throw new BusinessException("extention code is "+extentionCode+" not exists.");
		}
		
		/****** 同步商品价格(sku) ******/
		sdkSkuManager.syncSkuPriceByExtentionCode(salesPrice, listPrice, extentionCode);
		
		ItemCommand itemCommand = sdkItemManager.findItemCommandById(syncSku.getItemId());
		if(null == itemCommand){
			throw new BusinessException("extention code is "+extentionCode+" not exists item info.");
		}
		String itemCode = itemCommand.getCode();
		//ItemCommand itemCommand = itemCommandMap.get(itemCode);
		Long itemId = itemCommand.getId();
		BigDecimal itemSalesPrice = itemCommand.getSalePrice();
		BigDecimal itemListPrice = itemCommand.getListPrice();
		List<Sku> skuList = sdkItemManager.findSkuByItemId(itemId);

		/****** sku价格的最大与最小 ******/
		// 销售价格的最大值
		BigDecimal skuMaxSalesPrice = salesPrice;
		// 销售价格的最小值
		BigDecimal skuMinSalesPrice = salesPrice;
		// 吊牌价的最大值
		BigDecimal skuMaxListPrice = listPrice;
		// 吊牌价的最小值
		BigDecimal skuMinListPrice = listPrice;
		for (Sku sku : skuList) {
			BigDecimal skuSalesPrice = sku.getSalePrice();
			BigDecimal skuListPrice = sku.getListPrice();
			// 销售价格的最大值
			if (Validator.isNotNullOrEmpty(skuMaxSalesPrice)) {
				if (skuMaxSalesPrice.compareTo(skuSalesPrice) < 0) {
					skuMaxSalesPrice = skuSalesPrice;
				}
			} else {
				skuMaxSalesPrice = skuSalesPrice;
			}
			// 销售价格的最小值
			if (Validator.isNotNullOrEmpty(skuMinSalesPrice)) {
				if (skuMinSalesPrice.compareTo(skuSalesPrice) > 0) {
					skuMinSalesPrice = skuSalesPrice;
				}
			} else {
				skuMinSalesPrice = skuSalesPrice;
			}

			/****** 当吊牌价为null时,说明官网维护吊牌价 ******/
			if (Validator.isNotNullOrEmpty(itemListPrice)) {
				// 吊牌价的最大值
				if (Validator.isNotNullOrEmpty(skuMaxListPrice)) {
					if (skuMaxListPrice.compareTo(skuListPrice) < 0) {
						skuMaxListPrice = skuListPrice;
					}
				} else {
					skuMaxListPrice = skuListPrice;
				}
				// 吊牌价的最小值
				if (Validator.isNotNullOrEmpty(skuMinListPrice)) {
					if (skuMinListPrice.compareTo(skuListPrice) > 0) {
						skuMinListPrice = skuListPrice;
					}
				} else {
					skuMinListPrice = skuListPrice;
				}
			}
		}

		/****** 官网是否维护吊牌价(list_price) ******/
		if (Validator.isNotNullOrEmpty(itemListPrice)) {
			if (itemSalesPrice.compareTo(skuMaxSalesPrice) > 0 || itemSalesPrice.compareTo(skuMinSalesPrice) < 0
					|| itemListPrice.compareTo(skuMaxListPrice) > 0 || itemListPrice.compareTo(skuMinListPrice) < 0) {
				if (itemSalesPrice.compareTo(skuMaxSalesPrice) > 0 || itemSalesPrice.compareTo(skuMinSalesPrice) < 0) {
					itemSalesPrice = skuMaxSalesPrice;
				}

				if (itemListPrice.compareTo(skuMaxListPrice) > 0 || itemListPrice.compareTo(skuMinListPrice) < 0) {
					itemListPrice = skuMaxListPrice;
				}
				sdkItemManager.syncItemPriceByCode(itemSalesPrice, itemListPrice, itemCode);
			}

		} else {
			if (itemSalesPrice.compareTo(skuMaxSalesPrice) > 0 || itemSalesPrice.compareTo(skuMinSalesPrice) < 0) {
				sdkItemManager.syncItemPriceByCode(skuMaxSalesPrice, null, itemCode);
			}
		}

	}
}
