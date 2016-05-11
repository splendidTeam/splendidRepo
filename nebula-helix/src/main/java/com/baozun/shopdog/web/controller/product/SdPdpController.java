/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 *
 */
package com.baozun.shopdog.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.exception.IllegalItemStateException.IllegalItemState;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.product.converter.ItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand;
import com.baozun.shopdog.exception.BusinessException;
import com.baozun.shopdog.web.controller.product.converter.ShopdogItemImageViewCommandConverter;
import com.baozun.shopdog.web.controller.product.converter.ShopdogItemPropertyCommandConverter;
import com.baozun.shopdog.web.controller.product.converter.ShopdogItemViewCommandConverter;
import com.baozun.shopdog.web.controller.product.converter.ShopdogSkuViewCommandConverter;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogItemPropertyViewCommand;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogItemViewCommand;
import com.baozun.shopdog.web.controller.product.viewcommand.ShopdogSkuViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年5月11日 下午4:06:51 
 * @version   
 */
public class SdPdpController implements AbstractSdPdpController {
	
	private static final Logger	LOG										= LoggerFactory.getLogger(SdPdpController.class);
	
	@Autowired
	protected SdkItemManager sdkItemManager;
	
	@Autowired
	protected ItemDetailManager itemDetailManager;
	
	@Autowired
	private ItemColorSwatchViewCommandResolver    colorSwatchViewCommandResolver;
	
	@Autowired
	@Qualifier("shopdogItemViewCommandConverter")
	private ShopdogItemViewCommandConverter    shopdogItemViewCommandConverter;
	
	@Autowired
	@Qualifier("shopdogItemImageViewCommandConverter")
	private ShopdogItemImageViewCommandConverter    shopdogItemImageViewCommandConverter;
	
	@Autowired
	@Qualifier("shopdogSkuViewCommandConverter")
	private ShopdogSkuViewCommandConverter shopdogSkuViewCommandConverter;

	@Autowired
	@Qualifier("shopdogItemPropertyCommandConverter")
	private ShopdogItemPropertyCommandConverter		shopdogItemPropertyCommandConverter;
	
	@Autowired
	@Qualifier("itemImageViewCommandConverter")
	protected ItemImageViewCommandConverter itemImageViewCommandConverter;
	
	@Autowired
	protected ItemPropertyViewCommandResolver itemPropertyViewCommandResolver;
	@Autowired
	private ItemDetailManager detailManager;
	/* 
	 * @see com.baozun.shopdog.web.controller.product.AbstractSdPdpController#getPdpItem(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ShopdogItemViewCommand> getPdpItem(String itemCode,
			String extCode) throws BusinessException {
		// 如果是extCode，先转成itemCode
		if(Validator.isNotNullOrEmpty(extCode)) {
			Item item = detailManager.findItemByExtentionCode(extCode);
			if(Validator.isNotNullOrEmpty(item)) {
				itemCode = item.getCode();
			}
		}
		try {
			return buildPdpViewCommand(itemCode);
		} catch (IllegalItemStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		/*// 没有取到合适的参数
		if(Validator.isNullOrEmpty(itemCode)) {
//					return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.COMMON_PARAMETER_ERROR);
			throw new BusinessException(ShopdogErrorCodes(
					ShopdogErrorType.COMMON_PARAMETER_ERROR.getErrorCode(),
					ShopdogErrorType.COMMON_PARAMETER_ERROR.getMessage()));
		}
		
		try {
			
			List<ShopdogItemViewCommand> items = buildPdpViewCommand(itemCode);
			return items;
			
		} catch (IllegalItemStateException ie) {
			
			LOG.error("[SHOPDOG_GET_PDP_DATA] Item state illegal. itemCode:{}, {}", itemCode, ie.getState().name());
			
			if(ie.getState().equals(IllegalItemState.ITEM_NOT_EXISTS)) {
				return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_NOT_EXISTS);
			}
			
			if(ie.getState().equals(IllegalItemState.ITEM_LIFECYCLE_OFFSALE)) {
				return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_LIFECYCLE_OFFSALE);
			}
			
			if(ie.getState().equals(IllegalItemState.ITEM_LIFECYCLE_LOGICAL_DELETED)) {
				return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_LIFECYCLE_LOGICAL_DELETED);
			}
			
			if(ie.getState().equals(IllegalItemState.ITEM_LIFECYCLE_NEW)) {
				return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_LIFECYCLE_NEW);
			}
			
			if(ie.getState().equals(IllegalItemState.ITEM_BEFORE_ACTIVE_TIME)) {
				return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_BEFORE_ACTIVE_TIME);
			}
			
			return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.ITEM_ILLEGAL_STATE);
			
		} catch (Exception e) {
			
			LOG.error("[SHOPDOG_GET_PDP_DATA] " + e.getMessage(), e);
			return ShopdogResultCommand.getErrorInstance(ShopdogErrorType.COMMON_SYSTEM_ERROR);
		}*/
		
	}
	
    /**
	 * 构造PdpViewCommand
	 * @throws IllegalItemStateException 
	 */
	protected List<ShopdogItemViewCommand> buildPdpViewCommand(String itemCode) throws IllegalItemStateException{
		
		List<ShopdogItemViewCommand>  shopdogItemViewCommands = new ArrayList<ShopdogItemViewCommand>();
		
		//获取商品信息
		ShopdogItemViewCommand shopdogItemViewCommand = buildShopdogItemViewCommand(itemCode);
		shopdogItemViewCommands.add(shopdogItemViewCommand);
		
		//获取同款
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		List<ItemColorSwatchViewCommand> itemColorSwatchViewCommands = buildItemColorSwatchViewCommands(itemBaseInfo);
		if(Validator.isNotNullOrEmpty(itemColorSwatchViewCommands)){
			for(ItemColorSwatchViewCommand itemColorSwatchViewCommand:itemColorSwatchViewCommands){
				ShopdogItemViewCommand res = buildShopdogItemViewCommand(itemColorSwatchViewCommand.getItemCode());
				shopdogItemViewCommands.add(res);
			}
		}
 		
		return shopdogItemViewCommands;
	}
	
	public String getMainPicType(){
		return ItemImage.IMG_TYPE_LIST;
	}
	
	protected ShopdogItemViewCommand buildShopdogItemViewCommand(String itemCode) throws IllegalItemStateException{
		
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		
		//商品基本信息
		ShopdogItemViewCommand shopdogItemViewCommand =  shopdogItemViewCommandConverter.convert(itemBaseInfo);
		
		List<ItemImageViewCommand> images = buildItemImageViewCommand(itemBaseInfo.getId());
		
	    //商品全部图
		List<ShopdogItemImageViewCommand> shopdogItemImageViewCommands = shopdogItemImageViewCommandConverter.convert(buildItemImageViewCommand(itemBaseInfo.getId()));
		shopdogItemViewCommand.setAllPictures(shopdogItemImageViewCommands);
		
		//主图
		shopdogItemViewCommand.setMainPicture(getMainUrls(shopdogItemImageViewCommands));
		
		//设置销售属性
		ItemPropertyViewCommand itemPropertyViewCommand =itemPropertyViewCommandResolver.resolve(itemBaseInfo, images);
		ShopdogItemPropertyViewCommand shopdogItemPropertyViewCommand =shopdogItemPropertyCommandConverter.convert(itemPropertyViewCommand);
		
		if(Validator.isNotNullOrEmpty(shopdogItemPropertyViewCommand)){
			shopdogItemViewCommand.setSalesProperties(shopdogItemPropertyViewCommand.getSalesProperties());
		}
		
		//sku
		shopdogItemViewCommand.setSkus(buildSkuViewCommand(itemBaseInfo.getId()));
	
		return shopdogItemViewCommand;
		
	}

	private List<String> getMainUrls(List<ShopdogItemImageViewCommand> shopdogItemImageViewCommands) {
		return shopdogItemImageViewCommands.get(0).getImages().get(getMainPicType());
	}

	/**
	 * 获取并校验商品基本信息 
	 * @param itemBaseInfo
	 * @return
	 */
	protected ItemBaseInfoViewCommand getAndValidateItemBaseInfo(String itemCode) throws IllegalItemStateException {
		// 取得商品的基本信息
		ItemBaseInfoViewCommand itemBaseInfo = buildItemBaseInfoViewCommand(itemCode);
		
		// 商品不存在
		if (Validator.isNullOrEmpty(itemBaseInfo)) {
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item not exists. itemCode:{}.", itemCode);
            throw new IllegalItemStateException(IllegalItemState.ITEM_NOT_EXISTS);
        }
		
		if(LOG.isInfoEnabled()) {
			LOG.info("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] itemCode:{}, baseInfo:{}", itemCode, JsonUtil.format(itemBaseInfo));
		}
		
		Integer lifecycle = itemBaseInfo.getLifecycle();
		if(Item.LIFECYCLE_DELETED == lifecycle) {
			// 商品逻辑删除
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item logical deleted. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_LOGICAL_DELETED);
		} else if(Item.LIFECYCLE_UNACTIVE == lifecycle) {
			// 商品新建状态
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status new. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_NEW);
		} else if(Item.LIFECYCLE_DISABLE == lifecycle) {
			// 商品未上架
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status offSale. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_OFFSALE);
		}
		
		Date activeBeginTime = itemBaseInfo.getActiveBeginTime();
		if (Validator.isNotNullOrEmpty(activeBeginTime) && !DateUtil.isAfter(new Date(), activeBeginTime)) {
			// 商品未上架
 			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item before active begin time. itemCode:{}, activeBeginTime:{}.", itemCode, activeBeginTime);
            throw new IllegalItemStateException(IllegalItemState.ITEM_BEFORE_ACTIVE_TIME);
        }
		
		if(Constants.ITEM_TYPE_PREMIUMS == itemBaseInfo.getType()) {
			// 商品是赠品
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item is gift. itemCode:{}, type:{}.", itemCode, itemBaseInfo.getType());
            throw new IllegalItemStateException(IllegalItemState.ITEM_ILLEGAL_TYPE_GIFT);
		}
		
		return itemBaseInfo;
	}
	
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
	
	protected List<ItemColorSwatchViewCommand> buildItemColorSwatchViewCommands(ItemBaseInfoViewCommand baseInfoViewCommand){
		return colorSwatchViewCommandResolver.resolve(baseInfoViewCommand, itemImageViewCommandConverter);
	}
	
	/**
	 * 构造sku信息
	 * @param itemId
	 * @return List {@link SkuViewCommand}
	 */
	protected List<ShopdogSkuViewCommand> buildSkuViewCommand(Long itemId) {
		assert itemId != null : "Please Check itemId!";
		
		//获取所有有效的sku信息
		List<SkuCommand> commands = itemDetailManager.findEffectiveSkuInvByItemId(itemId);
		
		if(Validator.isNotNullOrEmpty(commands)){
			//转换
			List<ShopdogSkuViewCommand> skuViewCommands = shopdogSkuViewCommandConverter.convert(commands);
			
			return skuViewCommands;
		}
		
		return null;
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
