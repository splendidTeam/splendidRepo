
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

import com.baozun.nebula.manager.CacheManager;
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
import com.feilong.tools.jsonlib.JsonUtil;


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
	private static final Logger	LOG = LoggerFactory.getLogger(NebulaBasePdpController.class);
	
	@Autowired
	protected SdkItemManager sdkItemManager;
	
	@Autowired
	protected ItemDetailManager itemDetailManager;
	
	@Autowired
	@Qualifier("skuViewCommandConverter")
	private SkuViewCommandConverter skuViewCommandConverter;
	
	@Autowired
	@Qualifier("inventoryViewCommandConverter")
	private InventoryViewCommandConverter inventoryViewCommandConverter;
	
	@Autowired
	protected ItemPropertyViewCommandResolver itemPropertyViewCommandResolver;
	
	@Autowired
	@Qualifier("itemImageViewCommandConverter")
	protected ItemImageViewCommandConverter itemImageViewCommandConverter;
	
	@Autowired
	protected CacheManager cacheManager;
	

	/**
	 * 构造商品基本信息
	 * @param itemCode 商品编码
	 * @return {@link ItemBaseInfoViewCommand}
	 */
	protected ItemBaseInfoViewCommand buildItemBaseInfoViewCommand(String itemCode) {
		
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = new ItemBaseInfoViewCommand();
		
		ItemBaseCommand itemBaseCommand = itemDetailManager.findItemBaseInfoByCode(itemCode);
		if(itemBaseCommand != null){
			BeanUtils.copyProperties(itemBaseCommand, itemBaseInfoViewCommand);
			itemBaseInfoViewCommand.setId(itemBaseCommand.getItemId());
		}
		
		return itemBaseInfoViewCommand;
	}
	
	/**
	 * 构造商品基本信息
	 * @param itemId 商品Id
	 * @return {@link ItemBaseInfoViewCommand}
	 */
	protected ItemBaseInfoViewCommand buildItemBaseInfoViewCommand(Long itemId) {
		
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = new ItemBaseInfoViewCommand();
		
		ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoLang(itemId);
		if(itemBaseCommand!=null){
			BeanUtils.copyProperties(itemBaseCommand, itemBaseInfoViewCommand);
			itemBaseInfoViewCommand.setId(itemBaseCommand.getItemId());
		}
		
		return itemBaseInfoViewCommand;
	}
	
	/**
	 * 构造sku信息
	 * @param itemId
	 * @return List {@link SkuViewCommand}
	 */
	protected List<SkuViewCommand> buildSkuViewCommand(Long itemId) {
		assert itemId != null : "Please Check itemId!";
		
		//获取所有有效的sku信息
		List<SkuCommand> commands = itemDetailManager.findEffectiveSkuInvByItemId(itemId);
		
		if(Validator.isNotNullOrEmpty(commands)){
			//转换
			List<SkuViewCommand> skuViewCommands = skuViewCommandConverter.convert(commands);
			
			return skuViewCommands;
		}
		
		return null;
	}
	
	/**
	 * 构造库存信息
	 * @param itemId
	 * @return List {@link InventoryViewCommand}
	 * 
	 */
	protected List<InventoryViewCommand> buildInventoryViewCommand(Long itemId) {
		
		List<SkuCommand> skuCommands = sdkItemManager.findEffectiveSkuInvByItemId(itemId);
		
		return inventoryViewCommandConverter.convert(skuCommands);
	}
	
	/**
	 * 构造价格信息
	 * @return {@link PriceViewCommand}
	 */
	protected PriceViewCommand buildPriceViewCommand(ItemBaseInfoViewCommand baseInfoViewCommand,
			List<SkuViewCommand> skuViewCommands) {
		
		assert baseInfoViewCommand != null && Validator.isNotNullOrEmpty(skuViewCommands)
				: "Please Check baseInfo and skuInfos!";
		
		PriceViewCommand priceViewCommand = new PriceViewCommand();
		
		//商品上定义的吊牌价
		priceViewCommand.setItemListPrice(baseInfoViewCommand.getListPrice());
		
		//商品上定义的销售价
		priceViewCommand.setItemSalesPrice(baseInfoViewCommand.getSalePrice());
		
		//该商品所有sku吊牌价的最小值
		BigDecimal skuMinListPrice = BigDecimal.ZERO;
		
		//该商品所有sku吊牌价的最大值
		BigDecimal skuMaxListPrice = BigDecimal.ZERO;
		
		//该商品所有sku销售价的最小值
		BigDecimal skuMinSalesPrice = BigDecimal.ZERO;
		
		//该商品所有sku销售价的最大值
		BigDecimal skuMaxSalesPrice = BigDecimal.ZERO;
		
		int i =0;
		for (SkuViewCommand skuViewCommand : skuViewCommands) {
			if(i == 0){
				skuMinListPrice = Validator.isNotNullOrEmpty(skuViewCommand.getListPrice())?
						skuViewCommand.getListPrice() : BigDecimal.ZERO;
				skuMaxListPrice =skuMinListPrice;
				
				skuMinSalesPrice = Validator.isNotNullOrEmpty(skuViewCommand.getSalePrice())?
						skuViewCommand.getListPrice() : BigDecimal.ZERO;
				skuMaxSalesPrice = skuMinSalesPrice;
			}else{
				//>
				if(Validator.isNotNullOrEmpty(skuViewCommand.getListPrice())&&
						skuMinListPrice.compareTo(skuViewCommand.getListPrice()) == 1){
					skuMinListPrice = skuViewCommand.getListPrice();
				}
				//<
				if(Validator.isNotNullOrEmpty(skuViewCommand.getListPrice())&&
						skuMaxListPrice.compareTo(skuViewCommand.getListPrice()) == -1){
					skuMaxListPrice = skuViewCommand.getListPrice();
				}
				//>
				if(Validator.isNotNullOrEmpty(skuViewCommand.getSalePrice())&&
						skuMinSalesPrice.compareTo(skuViewCommand.getSalePrice()) == 1){
					skuMinSalesPrice = skuViewCommand.getSalePrice();
				}
				//<
				if(Validator.isNotNullOrEmpty(skuViewCommand.getSalePrice())&&
						skuMaxSalesPrice.compareTo(skuViewCommand.getSalePrice()) == -1){
					skuMaxSalesPrice = skuViewCommand.getSalePrice();
				}
			}
			i++;
		}
		priceViewCommand.setSkuMinListPrice(skuMinListPrice);
		priceViewCommand.setSkuMaxListPrice(skuMaxListPrice);
		priceViewCommand.setSkuMinSalesPrice(skuMinSalesPrice);
		priceViewCommand.setSkuMaxSalesPrice(skuMaxSalesPrice);
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("[PDP_BUILD_PRICE_VIEW_COMMAND] price:{}", JsonUtil.format(priceViewCommand));
		}
		
		return priceViewCommand;
	}
	
	/**
	 * 构造商品的属性信息，包括销售属性和非销售属性
	 * 
	 * <p>此方法在构造销售颜色属性信息时，将需要商品图片信息，
	 * 所以，之前需先获取商品图片(不一定有颜色属性，但无论如何必须先取图片)
	 * </p>
	 * 
	 * @param itemId
	 * @return {@link ItemPropertyViewCommand}
	 */
	protected ItemPropertyViewCommand buildItemPropertyViewCommand(ItemBaseInfoViewCommand baseInfoViewCommand, 
			List<ItemImageViewCommand> images) {
		
		return itemPropertyViewCommandResolver.resolve(baseInfoViewCommand, images);
	}
	
	/**
	 * 构造商品的图片
	 * @param itemId
	 * @return List {@link ItemImageViewCommand}
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
