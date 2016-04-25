
/**
 * Copyright (c) 2016 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.product.converter.InventoryViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.SkuViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.InventoryViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PriceViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;
import com.feilong.core.Validator;


/**
 * 商品详情controller的基类
 * <p>
 * 处理一些商品详情相关的公共逻辑，Pdp和bundle的Controller都继承自该类
 * </p>
 * 
 * @author yimin.qiao
 * @version 1.0
 */
public abstract class NebulaBasePdpController extends BaseController {

	/**
	 * log定义
	 */
	private static final Logger	LOG									= LoggerFactory.getLogger(NebulaBasePdpController.class);
	
	@Autowired
	private SdkItemManager sdkItemManager;
	
	@Autowired
	private ItemDetailManager itemDetailManager;
	
	@Autowired
	@Qualifier("skuViewCommandConverter")
	private SkuViewCommandConverter skuViewCommandConverter;
	
	@Autowired
	private InventoryViewCommandConverter inventoryViewCommandConverter;
	
	@Autowired
	protected ItemPropertyViewCommandResolver							itemPropertyViewCommandResolver;
	
	@Autowired
	ItemImageViewCommandConverter                                   itemImageViewCommandConverter;
	

	/**
	 * 构造商品基本信息
	 * @param itemCode 商品编码
	 * @return ItemBaseInfoViewCommand
	 */
	protected ItemBaseInfoViewCommand buildItemBaseInfoViewCommand(String itemCode) {
		
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = new ItemBaseInfoViewCommand();
		
		ItemBaseCommand itemBaseCommand = itemDetailManager.findItemBaseInfoByCode(itemCode);
		if(itemBaseCommand != null){
			BeanUtils.copyProperties(itemBaseCommand, itemBaseInfoViewCommand);
		}
		
		return itemBaseInfoViewCommand;
	}
	
	/**
	 * 构造商品基本信息
	 * 这个方法不用考虑pdp显示模式的问题，因为基本信息对于几种模式取法是一样的
	 * @param itemId
	 * @return
	 */
	@Deprecated
	protected ItemBaseInfoViewCommand buildItemBaseInfoViewCommand(Long itemId) {
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = new ItemBaseInfoViewCommand();
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoLang(itemId);
		if(itemBaseCommand!=null){
			BeanUtils.copyProperties(itemBaseCommand, itemBaseInfoViewCommand);
		}
		return itemBaseInfoViewCommand;
	}
	
	/**
	 * 构造sku信息
	 * @param itemId
	 * @return
	 */
	protected List<SkuViewCommand> buildSkuViewCommand(Long itemId) {
		assert itemId != null : "Please Check itemId!";
		List<SkuCommand> commands =itemDetailManager.findEffectiveSkuInvByItemId(itemId);
		if(Validator.isNotNullOrEmpty(commands)){
			List<SkuViewCommand> skuViewCommands =skuViewCommandConverter.convert(commands);
			for (SkuViewCommand skuViewCommand : skuViewCommands) {
				skuViewCommand.setItemId(itemId);
			}
			return skuViewCommands;
		}
		return null;
	}
	
	/**
	 * 构造库存信息
	 * @param itemId
	 * @return
	 */
	protected List<InventoryViewCommand> buildInventoryViewCommand(Long itemId) {
		List<SkuCommand> skuCommands = sdkItemManager.findEffectiveSkuInvByItemId(itemId);
		return inventoryViewCommandConverter.convert(skuCommands);
	}
	
	/**
	 * 构造价格信息
	 * @return
	 */
	protected PriceViewCommand buildPriceViewCommand(ItemBaseInfoViewCommand baseInfoViewCommand,
			List<SkuViewCommand> skuViewCommands) {
		assert baseInfoViewCommand != null && Validator.isNullOrEmpty(skuViewCommands)
				: "Please Check baseInfo and skuInfos!";
		PriceViewCommand priceViewCommand =new PriceViewCommand();
		priceViewCommand.setItemListPrice(baseInfoViewCommand.getListPrice());
		priceViewCommand.setItemSalesPrice(baseInfoViewCommand.getSalePrice());
		
		BigDecimal skuMinListPrice=BigDecimal.ZERO;
		BigDecimal skuMaxListPrice=BigDecimal.ZERO;
		BigDecimal skuMinSalesPrice=BigDecimal.ZERO;
		BigDecimal skuMaxSalesPrice=BigDecimal.ZERO;
		int i =0;
		for (SkuViewCommand skuViewCommand : skuViewCommands) {
			if(i == 0){
				skuMinListPrice =skuViewCommand.getListPrice();
				skuMaxListPrice =skuViewCommand.getListPrice();
				skuMinSalesPrice =skuViewCommand.getSalePrice();
				skuMaxSalesPrice =skuViewCommand.getSalePrice();
			}else{
				//>
				if(skuMinListPrice.compareTo(skuViewCommand.getListPrice()) == 1){
					skuMinListPrice =skuViewCommand.getListPrice();
				}
				//<
				if(skuMaxListPrice.compareTo(skuViewCommand.getListPrice()) == -1){
					skuMaxListPrice =skuViewCommand.getListPrice();
				}
				//>
				if(skuMinSalesPrice.compareTo(skuViewCommand.getSalePrice()) == 1){
					skuMinSalesPrice =skuViewCommand.getSalePrice();
				}
				//<
				if(skuMaxSalesPrice.compareTo(skuViewCommand.getSalePrice()) == -1){
					skuMaxSalesPrice =skuViewCommand.getSalePrice();
				}
			}
			i++;
		}
		priceViewCommand.setSkuMinListPrice(skuMinListPrice);
		priceViewCommand.setSkuMaxListPrice(skuMaxListPrice);
		priceViewCommand.setSkuMinSalesPrice(skuMinSalesPrice);
		priceViewCommand.setSkuMaxSalesPrice(skuMaxSalesPrice);
		return priceViewCommand;
	}
	
	/**
	 * <p>构造商品的属性信息，包括销售属性和非销售属性</p>
	 * 此方法在构造销售颜色属性信息时，将需要商品图片信息，
	 * 所以，之前需先获取商品图片(不一定有颜色属性，但无论如何必须先取图片)
	 * @param itemId
	 * @return
	 */
	protected ItemPropertyViewCommand buildItemPropertyViewCommand(ItemBaseInfoViewCommand baseInfoViewCommand, 
			List<ItemImageViewCommand> images) {
		return itemPropertyViewCommandResolver.resolve(baseInfoViewCommand, images);
	}
	
	/**
	 * 构造商品的图片
	 * @param itemId
	 * @return
	 */
	protected List<ItemImageViewCommand> buildItemImageViewCommand(Long itemId) {
		// 查询结果
		List<Long> itemIds = new ArrayList<Long>();
		itemIds.add(itemId);
		List<ItemImage> itemImageList = sdkItemManager.findItemImageByItemIds(itemIds, null);
		// 数据转换
		return itemImageViewCommandConverter.convert(itemImageList);
	}
}
